package com.amanowicz.movieweb.service;

import com.amanowicz.movieweb.client.OmdbApiService;
import com.amanowicz.movieweb.exception.MovieNotFoundException;
import com.amanowicz.movieweb.mapper.NominatedMovieMapper;
import com.amanowicz.movieweb.model.NominatedMovie;
import com.amanowicz.movieweb.model.NominatedMovieDto;
import com.amanowicz.movieweb.model.WonOscar;
import com.amanowicz.movieweb.repository.NominatedMoviesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static com.amanowicz.movieweb.utils.TestUtils.getOmdbMovie;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NominatedMovieServiceImplTest {

    @Mock
    private NominatedMoviesRepository nominatedMoviesRepository;
    @Mock
    private NominatedMovieMapper nominatedMovieMapper;
    @Mock
    private OmdbApiService omdbApiService;

    @InjectMocks
    private NominatedMovieServiceImpl service;

    @Test
    public void shouldConfirmThatMovieWonOscarInBestPictureCategory() throws IOException {
        NominatedMovieDto expected = new NominatedMovieDto("Dune", WonOscar.YES);
        NominatedMovie nominatedMovie = getNominatedMovie();

        when(omdbApiService.getMovieInfoByTitle("Dune")).thenReturn(Optional.of(getOmdbMovie()));
        when(nominatedMoviesRepository.findNominatedMovieByNomineeAndCategory("Dune", "Best Picture"))
                .thenReturn(Optional.of(nominatedMovie));
        when(nominatedMovieMapper.map(nominatedMovie)).thenReturn(expected);

        NominatedMovieDto movieDto = service.getNominatedMovieInBestPictureCategory("Dune");

        assertEquals(expected, movieDto);
    }

    @Test
    public void shouldThrowErrorWhenWrongMovieTitleInserted() {
        when(omdbApiService.getMovieInfoByTitle(any())).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> service.getNominatedMovieInBestPictureCategory("xyz"));
    }

    private NominatedMovie getNominatedMovie() {
        NominatedMovie nominatedMovie = new NominatedMovie();
        nominatedMovie.setId(1L);
        nominatedMovie.setCategory("Best Picture");
        nominatedMovie.setNominee("Dune");
        nominatedMovie.setYear("2021");
        nominatedMovie.setWon(WonOscar.YES);
        return nominatedMovie;
    }

}