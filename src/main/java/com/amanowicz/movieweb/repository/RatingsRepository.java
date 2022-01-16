package com.amanowicz.movieweb.repository;

import com.amanowicz.movieweb.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingsRepository extends JpaRepository<Rating, Long> {

    List<Rating> findAllByUsernameId(Long usernameId);

    @Query(value = "select r.title, r.rate, r.id, r.username_id from Ratings r" +
                    " inner join Users u on u.id = r.username_id" +
                    " where u.username = :username" +
                    " order by r.rate desc", nativeQuery = true
    )
    List<Rating> findTop10ByUsernameOrderByRateDesc(@Param("username") String username);

}
