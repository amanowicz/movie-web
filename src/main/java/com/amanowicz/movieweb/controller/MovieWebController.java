package com.amanowicz.movieweb.controller;

import com.amanowicz.movieweb.exception.MovieNotFoundException;
import com.amanowicz.movieweb.model.NominatedMovieDto;
import com.amanowicz.movieweb.model.RateRequest;
import com.amanowicz.movieweb.model.RatedMovieDto;
import com.amanowicz.movieweb.service.MovieRatingsService;
import com.amanowicz.movieweb.service.NominatedMovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/movies")
@AllArgsConstructor
@Validated
public class MovieWebController {

    private final NominatedMovieService nominatedMovieService;
    private final MovieRatingsService movieRatingsService;

    @GetMapping("/with-oscar")
    public NominatedMovieDto isOscarMovie(@RequestParam @NotBlank String title) {
        return nominatedMovieService.getNominatedMovieInBestPictureCategory(title);
    }

    @PutMapping("/ratings")
    public RatedMovieDto rateMovie(@Valid @RequestBody RateRequest rateRequest, Authentication auth) {
        return movieRatingsService.rateMovie(rateRequest, auth.getName());
    }

    @GetMapping("/ratings/top-rated")
    public List<RatedMovieDto> getTopRatedMovies(Authentication auth) {
        return movieRatingsService.getTopRatedMovies(auth.getName());
    }

    @GetMapping("/ratings")
    public Set<RatedMovieDto> getRatedMovies(Authentication auth) {
        return movieRatingsService.getRatedMovies(auth.getName());
    }

    @ExceptionHandler({MovieNotFoundException.class})
    public ResponseEntity<String> handleNotFound(MovieNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}

