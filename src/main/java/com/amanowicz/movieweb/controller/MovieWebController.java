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
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/movies")
public class MovieWebController {

    private final NominatedMovieService nominatedMovieService;
    private final MovieRatingsService movieRatingsService;

    @GetMapping("/with-oscar")
    public NominatedMovieDto isOscarMovie(@RequestParam @NonNull String title) {

        return nominatedMovieService.getNominatedMovieInBestPictureCategory(title);
    }

    @PostMapping("/ratings")
    public RatedMovieDto rateMovie(@Valid @RequestBody RateRequest rateRequest) {

        return movieRatingsService.rateMovie(rateRequest);
    }

    @GetMapping("/ratings/{username}")
    public List<RatedMovieDto> getRatedMovies(@PathVariable String username) {

        return movieRatingsService.getRatedMovies(username);
    }

    @GetMapping("/ratings/{username}/top-rated")
    public List<RatedMovieDto> getTopRatedMovies(@PathVariable String username) {

        return movieRatingsService.getTopRatedMovies(username);
    }

    @ExceptionHandler({MovieNotFoundException.class})
    public ResponseEntity<String> handleNotFound(MovieNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

