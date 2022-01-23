package com.amanowicz.movieweb.mapper;

import com.amanowicz.movieweb.model.RatedMovieDto;
import com.amanowicz.movieweb.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class RatingsMapperTest {

    private final RatingsMapper ratingsMapper = new RatingsMapperImpl();

    @Test
    public void shouldMapSingleRating() {
        RatedMovieDto expected = RatedMovieDto.builder().title("Dune").rate(5).build();

        RatedMovieDto ratedMovieDto = ratingsMapper.map(TestUtils.getRatings().iterator().next());

        Assertions.assertEquals(expected, ratedMovieDto);
    }

    @Test
    public void shouldMapSetOfRatings() {
        RatedMovieDto expected1 = RatedMovieDto.builder().title("Dune").rate(5).build();
        RatedMovieDto expected2 = RatedMovieDto.builder().title("Prestige").rate(1).build();

        Set<RatedMovieDto> ratedMovieDtos = ratingsMapper.map(TestUtils.getTwoRatingsSet());

        Assertions.assertEquals(Set.of(expected1, expected2), ratedMovieDtos);
    }

}