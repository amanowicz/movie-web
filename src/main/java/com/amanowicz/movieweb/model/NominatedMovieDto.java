package com.amanowicz.movieweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NominatedMovieDto {

    private final String nominee;
    private final WonOscar won;

}
