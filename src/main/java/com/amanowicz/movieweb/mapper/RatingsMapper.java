package com.amanowicz.movieweb.mapper;

import com.amanowicz.movieweb.model.RatedMovieDto;
import com.amanowicz.movieweb.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RatingsMapper {

    List<RatedMovieDto> map(List<Rating> ratings);

    @Mapping(target = "boxOffice", ignore = true)
    RatedMovieDto map(Rating rating);

}
