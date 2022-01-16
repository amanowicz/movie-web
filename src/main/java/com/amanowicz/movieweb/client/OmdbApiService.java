package com.amanowicz.movieweb.client;

import com.amanowicz.movieweb.client.model.OmdbMovie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class OmdbApiService {

    private final RestTemplate restTemplate;

    @Value("${omdb.api.url}")
    private String apiUrl;

    @Value("${omdb.api.key}")
    private String apiKey;

    public OmdbApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<OmdbMovie> getMovieInfoByTitle(String title) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("apikey", apiKey)
                .queryParam("t", title);
        String uriBuilder = builder.build().toUriString();

        OmdbMovie omdbMovie = restTemplate.getForObject(uriBuilder, OmdbMovie.class);
        if (omdbMovie.getTitle() == null) {
            return Optional.empty();
        }

        return Optional.of(omdbMovie);
    }
}
