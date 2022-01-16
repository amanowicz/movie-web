package com.amanowicz.movieweb.repository;

import com.amanowicz.movieweb.model.NominatedMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NominatedMoviesRepository extends JpaRepository<NominatedMovie, Long> {

    Optional<NominatedMovie> findNominatedMovieByNomineeAndCategory(String nominee, String category);

}
