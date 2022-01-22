package com.amanowicz.movieweb.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "user_seq_gen")
    @SequenceGenerator(name="user_seq_gen", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;
    private String username;
    private String password;

}
