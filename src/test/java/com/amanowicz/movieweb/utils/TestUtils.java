package com.amanowicz.movieweb.utils;

import com.amanowicz.movieweb.client.model.OmdbMovie;
import com.amanowicz.movieweb.model.RateRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import liquibase.pro.packaged.T;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtils {

    public static final String AUTH_PREFIX = "Bearer ";
    public static final String CLIENT_SECRET = "movieweb123";

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

    public static OmdbMovie getOmdbMovie() throws IOException {
        return readValue("__files/omdb-api-dune.json", OmdbMovie.class);
    }

    public static String getNewRateRequestAsString() throws IOException {
        RateRequest rateRequest = new RateRequest("Prestige", 4);
        return getStringFromObject(rateRequest);
    }

    public static String getUpdateRateRequestAsString() throws IOException {
        RateRequest rateRequest = new RateRequest("The Shawshank Redemption", 1);
        return getStringFromObject(rateRequest);
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
