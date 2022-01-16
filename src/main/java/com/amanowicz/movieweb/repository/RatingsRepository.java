package com.amanowicz.movieweb.repository;

import com.amanowicz.movieweb.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingsRepository extends JpaRepository<Rating, Long> {

    List<Rating> findAllByUsernameId(Long usernameId);

    List<Rating> findTop10ByUsernameIdOrderByRateDesc(Long usernameId);

}
