package com.amanowicz.movieweb.service;

import com.amanowicz.movieweb.client.OmdbApiService;
import com.amanowicz.movieweb.client.model.OmdbMovie;
import com.amanowicz.movieweb.exception.MovieNotFoundException;
import com.amanowicz.movieweb.mapper.NominatedMovieMapper;
import com.amanowicz.movieweb.model.NominatedMovie;
import com.amanowicz.movieweb.model.NominatedMovieDto;
import com.amanowicz.movieweb.repository.NominatedMoviesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class NominatedMovieServiceImpl implements NominatedMovieService {

    private final OmdbApiService omdbApiService;
    private final NominatedMoviesRepository nominatedMoviesRepository;
    private final NominatedMovieMapper nominatedMovieMapper;

    @Override
    public NominatedMovieDto getMovieByTitle(String title) {
        Optional<OmdbMovie> omdbMovie = omdbApiService.getMovieInfoByTitle(title);
        if (omdbMovie.isEmpty()) {
            throw new MovieNotFoundException(String.format("Movie with title: %s not found", title));
        }

        Optional<NominatedMovie> nominatedMovie = nominatedMoviesRepository
                .findNominatedMovieByNominee(omdbMovie.get().getTitle());

        return nominatedMovie.map(nominatedMovieMapper::map)
                .orElse(new NominatedMovieDto(omdbMovie.get().getTitle(), "No"));
    }
}
