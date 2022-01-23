package com.amanowicz.movieweb.config;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class WireMockStubs {

    public static void initStubs() {

        stubFor(get(urlEqualTo("/omdb/?apikey=123&t=Dune")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBodyFile("omdb-api-dune.json")));

        stubFor(get(urlEqualTo("/omdb/?apikey=123&t=The%20Green%20Mile")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBodyFile("omdb-api-green-mile.json")));

        stubFor(get(urlEqualTo("/omdb/?apikey=123&t=The%20Shawshank%20Redemption")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBodyFile("omdb-api-shawshank.json")));

        stubFor(get(urlEqualTo("/omdb/?apikey=123&t=Prestige")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBodyFile("omdb-api-prestige.json")));
    }
}
