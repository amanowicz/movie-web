package com.amanowicz.movieweb.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OmdbRating {

    @JsonProperty("Source")
    private String source;
    @JsonProperty("Value")
    private String value;

}
