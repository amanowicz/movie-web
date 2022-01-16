package com.amanowicz.movieweb.service;

import com.amanowicz.movieweb.model.RatedMovieDto;
import com.amanowicz.movieweb.model.Rating;
import org.springframework.stereotype.Service;

@Service
public class RatedMovieDtoFactory {

    public RatedMovieDto createRatedMovieDto(Rating rating, String boxOffice) {
        return RatedMovieDto.builder()
                .rate(rating.getRate())
                .title(rating.getTitle())
                .boxOffice(boxOffice)
                .build();
    }
}
