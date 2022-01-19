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

    private static final String RATE_SHOULD_BE_IN_RANGE_1_5 = "Rate should be in range 1-5";

    @NotNull
    private final String title;

    @Min(value = 1, message = RATE_SHOULD_BE_IN_RANGE_1_5)
    @Max(value = 5, message = RATE_SHOULD_BE_IN_RANGE_1_5)
    private final int rate;

    private final String boxOffice;
}
