package com.amanowicz.movieweb.service;

import com.amanowicz.movieweb.model.RateRequest;
import com.amanowicz.movieweb.model.RatedMovieDto;

import java.util.List;
import java.util.Set;

public interface MovieRatingsService {

    RatedMovieDto rateMovie(RateRequest rateRequest, String username);

    Set<RatedMovieDto> getRatedMovies(String username);

    List<RatedMovieDto> getTopRatedMovies(String username);

}
