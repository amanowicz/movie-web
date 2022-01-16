package com.amanowicz.movieweb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RatedMovieDto {

    @NotNull
    private final String title;

    @Min(value = 1, message = "Rate should be in range 1-5")
    @Max(value = 5, message = "Rate should be in range 1-5")
    private final int rate;

    private final String boxOffice;
}
