package com.amanowicz.movieweb.service;

import com.amanowicz.movieweb.model.RateRequest;
import com.amanowicz.movieweb.model.RatedMovieDto;

import java.util.List;

public interface MovieRatingsService {

    RatedMovieDto rateMovie(RateRequest rateRequest);

    List<RatedMovieDto> getRatedMovies(String username);

    List<RatedMovieDto> getTopRatedMovies(String username);

}
