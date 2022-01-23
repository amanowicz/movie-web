package com.amanowicz.movieweb.utils;

import com.amanowicz.movieweb.client.model.OmdbMovie;
import com.amanowicz.movieweb.model.NominatedMovie;
import com.amanowicz.movieweb.model.RateRequest;
import com.amanowicz.movieweb.model.Rating;
import com.amanowicz.movieweb.model.User;
import com.amanowicz.movieweb.model.WonOscar;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestUtils {

    public static final String AUTH_PREFIX = "Bearer ";
    public static final String CLIENT_SECRET = "movieweb123";
    public static final String TEST_USER = "test_user";

    public static String getJWTToken(){
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts.builder()
                .setId("moviewebJWT")
                .setSubject("admin")
                .claim("authorities", grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60*60*1000))
                .signWith(SignatureAlgorithm.HS512, CLIENT_SECRET.getBytes()).compact();

        return AUTH_PREFIX + token;
    }

    public static OmdbMovie getOmdbMovie(String title) throws IOException {
        switch (title) {
            case "Dune":
                return readValue("__files/omdb-api-dune.json", OmdbMovie.class);
            case "Prestige":
            default:
                return readValue("__files/omdb-api-prestige.json", OmdbMovie.class);
        }
    }

    public static String getNewRateRequestAsString() throws IOException {
        RateRequest rateRequest = new RateRequest("Prestige", 4);
        return getStringFromObject(rateRequest);
    }

    public static String getUpdateRateRequestAsString() throws IOException {
        RateRequest rateRequest = new RateRequest("The Shawshank Redemption", 1);
        return getStringFromObject(rateRequest);
    }

    public static User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername(TEST_USER);
        user.setPassword("123");

        return user;
    }

    public static NominatedMovie getNominatedMovieWithOscar() {
        NominatedMovie nominatedMovie = new NominatedMovie();
        nominatedMovie.setId(1L);
        nominatedMovie.setCategory("Best Picture");
        nominatedMovie.setNominee("Dune");
        nominatedMovie.setYear("2021");
        nominatedMovie.setWon(WonOscar.YES);
        return nominatedMovie;
    }

    public static Set<Rating> getRatings() {
        Set<Rating> ratings = new HashSet<>();
        ratings.add(getRating("Dune", 5, 1L));

        return ratings;
    }

    public static Set<Rating> getTwoRatingsSet() {
        Set<Rating> ratings = new HashSet<>();
        ratings.add(getRating("Dune", 5, 1L));
        ratings.add(getRating("Prestige", 1, 2L));

        return ratings;
    }

    public static List<Rating> getTwoRatingsList() {
        List<Rating> ratings = new ArrayList<>();
        ratings.add(getRating("Dune", 5, 1L));
        ratings.add(getRating("Prestige", 1, 2L));

        return ratings;
    }

    public static Rating getRating(String title, int rate, long id) {
        Rating rating = new Rating();
        rating.setId(id);
        rating.setTitle(title);
        rating.setRate(rate);
        rating.setUserId(1L);

        return rating;
    }

    private static <T> String getStringFromObject(T object) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper.writeValueAsString(object);
    }

    private static <T> T readValue(String filename, Class<T> tClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        InputStream inJson = new ClassPathResource(filename).getInputStream();
        return objectMapper.readValue(inJson, tClass);
    }
}
