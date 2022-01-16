package com.amanowicz.movieweb.repository;

import com.amanowicz.movieweb.model.NominatedMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NominatedMoviesRepository extends JpaRepository<NominatedMovie, Long> {

    Optional<NominatedMovie> findNominatedMovieByNominee(String title);

}
