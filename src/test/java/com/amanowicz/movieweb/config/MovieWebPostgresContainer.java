package com.amanowicz.movieweb.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class MovieWebPostgresContainer extends PostgreSQLContainer<MovieWebPostgresContainer> {

    private static final String IMAGE_VERSION = "postgres:11.1";
    private static MovieWebPostgresContainer container;

    private MovieWebPostgresContainer(){
        super(IMAGE_VERSION);
    }

    public static MovieWebPostgresContainer getInstance() {
        if (container == null) {
            container = new MovieWebPostgresContainer();
        }
        return container;
    }

    @Override
    public void stop() {

    }
}
