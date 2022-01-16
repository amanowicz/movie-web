package com.amanowicz.movieweb.controller;

import com.amanowicz.movieweb.exception.MovieNotFoundException;
import com.amanowicz.movieweb.model.NominatedMovieDto;
import com.amanowicz.movieweb.model.RateRequest;
import com.amanowicz.movieweb.service.NominatedMovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/movies")
public class MovieWebController {

    private final NominatedMovieService nominatedMovieService;

    @GetMapping("/with-oscar")
    public ResponseEntity<NominatedMovieDto> isOscarMovie(@RequestParam @NonNull String title) {

        return new ResponseEntity<>(nominatedMovieService.getMovieByTitle(title), HttpStatus.OK);
    }

    @PostMapping("/ratings")
    public String rateMovie(@RequestBody RateRequest rateRequest) {

        return null;
    }

    @GetMapping("/ratings/{username}")
    public String getRatedMovies(@PathVariable String username) {

        return null;
    }

    @ExceptionHandler({MovieNotFoundException.class})
    public ResponseEntity<String> handleNotFound(MovieNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}

