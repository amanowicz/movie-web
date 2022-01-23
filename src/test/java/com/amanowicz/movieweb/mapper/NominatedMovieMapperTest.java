package com.amanowicz.movieweb.mapper;

import com.amanowicz.movieweb.model.NominatedMovieDto;
import com.amanowicz.movieweb.model.WonOscar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.amanowicz.movieweb.utils.TestUtils.getNominatedMovieWithOscar;

class NominatedMovieMapperTest {

    private final NominatedMovieMapper nominatedMovieMapper = new NominatedMovieMapperImpl();

    @Test
    public void shouldMapNominatedMovie() {
        NominatedMovieDto expected = new NominatedMovieDto("Dune", WonOscar.YES);

        NominatedMovieDto nominatedMovieDto = nominatedMovieMapper.map(getNominatedMovieWithOscar());

        Assertions.assertEquals(expected, nominatedMovieDto);
    }

}