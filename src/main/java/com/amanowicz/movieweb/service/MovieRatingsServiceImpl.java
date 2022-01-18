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
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
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
    private final RatedMovieDtoFactory ratedMovieDtoFactory;
    private final TaskExecutor taskExecutor;

    @Override
    public RatedMovieDto rateMovie(RateRequest rateRequest, String username) {
        Optional<OmdbMovie> omdbMovie = omdbApiService.getMovieInfoByTitle(rateRequest.getTitle());
        if (omdbMovie.isEmpty()) {
            throw new MovieNotFoundException(String.format("Movie with title: %s not found", rateRequest.getTitle()));
        }
        User user = userRepository.findByUsername(username);
        Optional<Rating> oldRating = user.getRatings().stream()
                .filter(r -> r.getTitle().equals(omdbMovie.get().getTitle()))
                .findFirst();

        if (oldRating.isPresent()) {
            oldRating.get().setRate(rateRequest.getRate());
            return ratingsMapper.map(oldRating.get());
        } else {
            Rating rating = ratingsRepository.save(createRating(rateRequest, omdbMovie.get(), user));
            return ratingsMapper.map(rating);
        }
    }

    @Override
    public List<RatedMovieDto> getRatedMovies(String username) {
        User user = userRepository.findByUsername(username);

        return ratingsMapper.map(user.getRatings());
    }

    @Override
    public List<RatedMovieDto> getTopRatedMovies(String username) {
        List<Rating> ratings = ratingsRepository.findTop10ByUsernameOrderByRateDesc(username);
        List<CompletableFuture<RatedMovieDto>> futures = new ArrayList<>();
        ratings.forEach(r -> {
            CompletableFuture<RatedMovieDto> future = CompletableFuture.supplyAsync(getSupplier(r), taskExecutor);
            futures.add(future);
        });

        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(r -> getBoxOfficeValue(r.getBoxOffice()),
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());

    }

    private Supplier<RatedMovieDto> getSupplier(Rating rating) {
       return () -> {
           Optional<OmdbMovie> omdbMovie = omdbApiService.getMovieInfoByTitle(rating.getTitle());
           return omdbMovie
                   .map(m -> ratedMovieDtoFactory.createRatedMovieDto(rating, m.getBoxOffice()))
                   .orElse(ratingsMapper.map(rating));
       };
    }

    private Rating createRating(RateRequest rateRequest, OmdbMovie omdbMovie, User user) {
        return ratedMovieDtoFactory.createRating(rateRequest.getRate(),
                user.getId(), omdbMovie.getTitle());
    }

    private Long getBoxOfficeValue(String boxOffice) {
        if (boxOffice == null || boxOffice.isEmpty()) {
            return null;
        }
        String value = boxOffice.replaceAll("[^0-9]", "");

        return Long.valueOf(value);
    }
}
