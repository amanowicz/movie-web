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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Data
@Transactional
public class MovieRatingsServiceImpl implements MovieRatingsService {

    private final OmdbApiService omdbApiService;
    private final UserRepository userRepository;
    private final RatingsRepository ratingsRepository;
    private final RatingsMapper ratingsMapper;

    @Override
    public RatedMovieDto rateMovie(RateRequest rateRequest) {
        Optional<OmdbMovie> omdbMovie = omdbApiService.getMovieInfoByTitle(rateRequest.getTitle());
        if (omdbMovie.isEmpty()) {
            throw new MovieNotFoundException(String.format("Movie with title: %s not found", rateRequest.getTitle()));
        }
        User user = userRepository.findByUsername(rateRequest.getUsername());

        Rating rating = ratingsRepository.save(new Rating(
                null, user.getId(), omdbMovie.get().getTitle(), rateRequest.getRate()));

        return ratingsMapper.map(rating);
    }

    @Override
    public List<RatedMovieDto> getRatedMovies(String username) {
        User user = userRepository.findByUsername(username);

        return ratingsMapper.map(user.getRatings());
    }
}
