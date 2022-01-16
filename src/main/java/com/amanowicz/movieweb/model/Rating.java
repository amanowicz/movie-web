package com.amanowicz.movieweb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ratings")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {

    @Id
    @GeneratedValue(generator = "rating_seq_gen")
    @SequenceGenerator(name="rating_seq_gen", sequenceName = "ratings_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "user_id")
    private Long userId;
    private String title;
    private int rate;

}
