package com.amanowicz.movieweb.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "user_seq_gen")
    @SequenceGenerator(name="user_seq_gen", sequenceName = "users_id_seq")
    private Long id;
    private String username;
    private String password;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "username_id")
    private List<Rating> ratings;

}
