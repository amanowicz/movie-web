package com.amanowicz.movieweb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;


@Entity
@Table(name = "ratings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Rating {

    @Id
    @GeneratedValue(generator = "rating_seq_gen")
    @SequenceGenerator(name="rating_seq_gen", sequenceName = "ratings_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;
    private String title;
    private int rate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return rate == rating.rate && Objects.equals(id, rating.id) && Objects.equals(userId, rating.userId) && Objects.equals(title, rating.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, title, rate);
    }
}
