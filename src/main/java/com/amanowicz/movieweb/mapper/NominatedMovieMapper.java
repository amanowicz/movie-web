package com.amanowicz.movieweb.mapper;

import com.amanowicz.movieweb.model.NominatedMovie;
import com.amanowicz.movieweb.model.NominatedMovieDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NominatedMovieMapper {

    NominatedMovieDto map(NominatedMovie nominatedMovie);

}
