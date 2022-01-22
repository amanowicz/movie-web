package com.amanowicz.movieweb.mapper;

import com.amanowicz.movieweb.model.RatedMovieDto;
import com.amanowicz.movieweb.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RatingsMapper {

    Set<RatedMovieDto> map(Set<Rating> ratings);

    @Mapping(target = "boxOffice", ignore = true)
    RatedMovieDto map(Rating rating);

}
