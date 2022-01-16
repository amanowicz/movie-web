package com.amanowicz.movieweb.service;

import com.amanowicz.movieweb.model.NominatedMovieDto;

public interface NominatedMovieService {

    NominatedMovieDto getNominatedMovieInBestPictureCategory(String title);
}
