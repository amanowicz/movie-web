package com.amanowicz.movieweb.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "nominated_movies")
public class NominatedMovie {

    @Id
    @GeneratedValue(generator = "movie_seq_gen")
    @SequenceGenerator(name="movie_seq_gen", sequenceName = "nominated_movies_id_seq")
    private Long id;
    private String year;
    private String category;
    private String nominee;
    private String won;


}