package com.amanowicz.movieweb.service;

import com.amanowicz.movieweb.client.OmdbApiService;
import com.amanowicz.movieweb.mapper.RatingsMapper;
import com.amanowicz.movieweb.model.RateRequest;
import com.amanowicz.movieweb.model.RatedMovieDto;
import com.amanowicz.movieweb.model.Rating;
import com.amanowicz.movieweb.repository.RatingsRepository;
import com.amanowicz.movieweb.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.amanowicz.movieweb.utils.TestUtils.TEST_USER;
import static com.amanowicz.movieweb.utils.TestUtils.getOmdbMovie;
import static com.amanowicz.movieweb.utils.TestUtils.getRatings;
import static com.amanowicz.movieweb.utils.TestUtils.getTestUser;
import static com.amanowicz.movieweb.utils.TestUtils.getTwoRatingsList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieRatingsServiceTest {

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
    public void shouldRateAMovie() throws IOException {
        RatedMovieDto expected = RatedMovieDto.builder().title("Prestige").rate(3).build();
        when(omdbApiService.getMovieInfoByTitle("Prestige")).thenReturn(Optional.of(getOmdbMovie("Prestige")));
        when(userRepository.findByUsername(TEST_USER)).thenReturn(getTestUser());
        when(ratingsRepository.findAllByUserId(1L)).thenReturn(getRatings());
        when(ratingsMapper.map(Rating.builder().userId(1L).title("Prestige").rate(3).build()))
                .thenReturn(expected);

        RatedMovieDto ratedMovie = movieRatingsService
                .rateMovie(new RateRequest("Prestige", 3), TEST_USER);

        Assertions.assertEquals(expected, ratedMovie);
    }

    @Test
    public void shouldUpdateAMovieRate() throws IOException {
        RatedMovieDto expected = RatedMovieDto.builder().title("Dune").rate(1).build();
        when(omdbApiService.getMovieInfoByTitle("Dune")).thenReturn(Optional.of(getOmdbMovie("Dune")));
        when(userRepository.findByUsername(TEST_USER)).thenReturn(getTestUser());
        when(ratingsRepository.findAllByUserId(1L)).thenReturn(getRatings());
        when(ratingsMapper.map(any(Rating.class))).thenReturn(expected);

        RatedMovieDto ratedMovie = movieRatingsService
                .rateMovie(new RateRequest("Dune", 1), TEST_USER);

        Assertions.assertEquals(expected, ratedMovie);
    }

    @Test
    public void shouldReturnAllRatedMovies() {
        when(userRepository.findByUsername(TEST_USER)).thenReturn(getTestUser());
        when(ratingsRepository.findAllByUserId(1L)).thenReturn(getRatings());
        when(ratingsMapper.map(getRatings())).thenReturn(getRatedMovies());

        Set<RatedMovieDto> ratedMovies = movieRatingsService.getRatedMovies(TEST_USER);

        Assertions.assertEquals(getRatedMovies(), ratedMovies);
    }

    @Test
    public void shouldGetTopRatedMoviesSortedByBoxOffice() throws IOException {
        when(ratingsRepository.findTop10ByUsernameOrderByRateDesc(TEST_USER)).thenReturn(getTwoRatingsList());
        when(omdbApiService.getMovieInfoByTitle("Dune")).thenReturn(Optional.of(getOmdbMovie("Dune")));
        when(omdbApiService.getMovieInfoByTitle("Prestige")).thenReturn(Optional.of(getOmdbMovie("Prestige")));
        when(ratingsMapper.map(getTwoRatingsList().get(0)))
                .thenReturn(RatedMovieDto.builder().title("Dune").rate(5).boxOffice("$107,351,294").build());
        when(ratingsMapper.map(getTwoRatingsList().get(0)))
                .thenReturn(RatedMovieDto.builder().title("Prestige").rate(1).boxOffice("N/A").build());

        List<RatedMovieDto> ratedMovies = movieRatingsService.getTopRatedMovies(TEST_USER);

        Assertions.assertEquals(getTopRatedMovies(), ratedMovies);
    }

    private Set<RatedMovieDto> getRatedMovies() {
        Set<RatedMovieDto> ratedMovieDtos = new HashSet<>();
        ratedMovieDtos.add(RatedMovieDto.builder().title("Dune").rate(5).build());

        return ratedMovieDtos;
    }

    private List<RatedMovieDto> getTopRatedMovies() {
        List<RatedMovieDto> ratedMovieDtos = new ArrayList<>();
        ratedMovieDtos.add(RatedMovieDto.builder().title("Dune").rate(5).boxOffice("$107,351,294").build());
        ratedMovieDtos.add(RatedMovieDto.builder().title("Prestige").rate(1).boxOffice("N/A").build());

        return ratedMovieDtos;
    }

}