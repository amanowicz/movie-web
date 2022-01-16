package com.amanowicz.movieweb.mapper;

import com.amanowicz.movieweb.model.RatedMovieDto;
import com.amanowicz.movieweb.model.Rating;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RatingsMapper {

    List<RatedMovieDto> map(List<Rating> ratings);

    RatedMovieDto map(Rating rating);

}
