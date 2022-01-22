package com.amanowicz.movieweb.it;

import com.amanowicz.movieweb.config.MovieWebPostgresContainer;
import com.amanowicz.movieweb.config.WebSecurityConfig;
import com.amanowicz.movieweb.model.NominatedMovieDto;
import com.amanowicz.movieweb.model.RatedMovieDto;
import com.amanowicz.movieweb.service.MovieRatingsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static com.amanowicz.movieweb.config.WireMockStubs.initStubs;
import static com.amanowicz.movieweb.utils.TestUtils.TOKEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@Import({WebSecurityConfig.class})
@WireMockTest(httpPort = 8089)
public class MovieWebControllerIT {

    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @ClassRule
    public static PostgreSQLContainer<MovieWebPostgresContainer> postgreSQLContainer =
            MovieWebPostgresContainer.getInstance();

    @BeforeEach
    public void setup() {
        initStubs();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webAppContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void shouldConfirmThatMovieHasOscar() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/movies/with-oscar")
                        .queryParam("title", "Dune")
                        .header("Authorization", TOKEN)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        NominatedMovieDto result = objectMapper.readValue(content, NominatedMovieDto.class);

        Assertions.assertEquals("Dune", result.getNominee());
        Assertions.assertEquals("YES", result.getWon());
    }

    @Test
    void shouldGetAllRatedMovies() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/movies/ratings")
                        .header("Authorization", TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Set<RatedMovieDto> result = objectMapper.readValue(content, new TypeReference<Set<RatedMovieDto>>() {});

        assertThat(result, hasSize(2));
        assertThat(result, hasItems(allOf(
                hasProperty("title", is("The Shawshank Redemption")),
                        hasProperty("rate", is(5))),
                allOf(hasProperty("title", is("The Green Mile")),
                        hasProperty("rate", is(2)))
        ));
    }
}
