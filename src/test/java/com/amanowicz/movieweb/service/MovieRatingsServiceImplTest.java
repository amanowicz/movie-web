package com.amanowicz.movieweb.service;

import com.amanowicz.movieweb.client.OmdbApiService;
import com.amanowicz.movieweb.mapper.RatingsMapper;
import com.amanowicz.movieweb.model.RatedMovieDto;
import com.amanowicz.movieweb.model.Rating;
import com.amanowicz.movieweb.model.User;
import com.amanowicz.movieweb.repository.RatingsRepository;
import com.amanowicz.movieweb.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieRatingsServiceImplTest {

    private static final String TEST_USER = "test_user";

    @Mock
    private OmdbApiService omdbApiService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RatingsRepository ratingsRepository;
    @Mock
    private RatingsMapper ratingsMapper;

    @InjectMocks
    private MovieRatingsServiceImpl movieRatingsService;

    @Test
    public void shouldReturnAllRatedMovies() {
        when(userRepository.findByUsername(TEST_USER)).thenReturn(getTestUser());
        when(ratingsRepository.findAllByUserId(1L)).thenReturn(getRatings());
        when(ratingsMapper.map(getRatings())).thenReturn(getRatedMovies());

        Set<RatedMovieDto> ratedMovies = movieRatingsService.getRatedMovies(TEST_USER);

        Assertions.assertEquals(getRatedMovies(), ratedMovies);
    }

    private Set<RatedMovieDto> getRatedMovies() {
        Set<RatedMovieDto> ratedMovieDtos = new HashSet<>();
        ratedMovieDtos.add(RatedMovieDto.builder().title("Dune").rate(5).build());

        return ratedMovieDtos;
    }

    private Set<Rating> getRatings() {
        Set<Rating> ratings = new HashSet<>();
        Rating rating = new Rating();
        rating.setId(1L);
        rating.setTitle("Dune");
        rating.setRate(5);
        rating.setUserId(1L);
        ratings.add(rating);

        return ratings;
    }

    private User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername(TEST_USER);
        user.setPassword("123");

        return user;
    }

}