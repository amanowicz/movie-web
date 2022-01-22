package com.amanowicz.movieweb.service;

import com.amanowicz.movieweb.client.OmdbApiService;
import com.amanowicz.movieweb.client.model.OmdbMovie;
import com.amanowicz.movieweb.exception.MovieNotFoundException;
import com.amanowicz.movieweb.mapper.RatingsMapper;
import com.amanowicz.movieweb.model.RateRequest;
import com.amanowicz.movieweb.model.RatedMovieDto;
import com.amanowicz.movieweb.model.Rating;
import com.amanowicz.movieweb.model.User;
import com.amanowicz.movieweb.repository.RatingsRepository;
import com.amanowicz.movieweb.repository.UserRepository;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Data
@Transactional
public class MovieRatingsServiceImpl implements MovieRatingsService {

    private final OmdbApiService omdbApiService;
    private final UserRepository userRepository;
    private final RatingsRepository ratingsRepository;
    private final RatingsMapper ratingsMapper;
    private final TaskExecutor taskExecutor;

    @Override
    public RatedMovieDto rateMovie(RateRequest rateRequest, String username) {
        OmdbMovie omdbMovie = getOmdbMovie(rateRequest);
        User user = userRepository.findByUsername(username);

        Optional<Rating> oldRating = user.getRatings().stream()
                .filter(r -> r.getTitle().equals(omdbMovie.getTitle()))
                .findFirst();

        if (oldRating.isPresent()) {
            oldRating.get().setRate(rateRequest.getRate());
            return ratingsMapper.map(oldRating.get());
        } else {
            Rating newRating = createRating(rateRequest, omdbMovie, user);
            user.getRatings().add(newRating);
            return ratingsMapper.map(newRating);
        }
    }

    @Override
    public Set<RatedMovieDto> getRatedMovies(String username) {
        User user = userRepository.findByUsername(username);

        return ratingsMapper.map(user.getRatings());
    }

    @Override
    public List<RatedMovieDto> getTopRatedMovies(String username) {
        List<Rating> ratings = ratingsRepository.findTop10ByUsernameOrderByRateDesc(username);
        List<CompletableFuture<RatedMovieDto>> futures = new ArrayList<>();
        ratings.forEach(r -> {
            CompletableFuture<RatedMovieDto> future = CompletableFuture.supplyAsync(getEnrichedRatedMovie(r), taskExecutor);
            futures.add(future);
        });

        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(r -> parseBoxOfficeValue(r.getBoxOffice()),
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());

    }

    private OmdbMovie getOmdbMovie(RateRequest rateRequest) {
        Optional<OmdbMovie> omdbMovie = omdbApiService.getMovieInfoByTitle(rateRequest.getTitle());
        if (omdbMovie.isEmpty()) {
            throw new MovieNotFoundException(String.format("Movie with title: %s not found", rateRequest.getTitle()));
        }
        return omdbMovie.get();
    }

    private Supplier<RatedMovieDto> getEnrichedRatedMovie(Rating rating) {
       return () -> {
           Optional<OmdbMovie> omdbMovie = omdbApiService.getMovieInfoByTitle(rating.getTitle());
           return omdbMovie.map(m -> RatedMovieDto.builder()
                           .rate(rating.getRate())
                           .title(rating.getTitle())
                           .boxOffice(m.getBoxOffice())
                           .build())
                   .orElse(ratingsMapper.map(rating));
       };
    }

    private Rating createRating(RateRequest rateRequest, OmdbMovie omdbMovie, User user) {
        return Rating.builder()
                .rate(rateRequest.getRate())
                .userId(user.getId())
                .title(omdbMovie.getTitle())
                .build();
    }

    private Long parseBoxOfficeValue(String boxOffice) {
        if (StringUtils.isEmpty(boxOffice) || boxOffice.equals("N/A")) {
            return null;
        }
        String value = boxOffice.replaceAll("[^0-9]", "");

        return Long.valueOf(value);
    }
}
