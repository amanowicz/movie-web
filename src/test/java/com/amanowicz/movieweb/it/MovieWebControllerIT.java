package com.amanowicz.movieweb.it;

import com.amanowicz.movieweb.config.MovieWebPostgresContainer;
import com.amanowicz.movieweb.config.WebSecurityConfig;
import com.amanowicz.movieweb.model.NominatedMovieDto;
import com.amanowicz.movieweb.model.RatedMovieDto;
import com.amanowicz.movieweb.model.WonOscar;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Set;

import static com.amanowicz.movieweb.config.WireMockStubs.initStubs;
import static com.amanowicz.movieweb.utils.TestUtils.getJWTToken;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@Import({WebSecurityConfig.class})
@WireMockTest(httpPort = 8089)
public class MovieWebControllerIT {

    private static final String TOKEN = getJWTToken();

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
        Assertions.assertEquals(WonOscar.YES, result.getWon());
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

        Assertions.assertEquals(2, result.size());
        assertThat(result, containsInAnyOrder(
                RatedMovieDto.builder().title("The Shawshank Redemption").rate(5).build(),
                RatedMovieDto.builder().title("The Green Mile").rate(2).build()
        ));
    }

    @Test
    void shouldGetTopRatedMoviesSortedByBoxOffice() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/movies/ratings/top-rated")
                        .header("Authorization", TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        List<RatedMovieDto> result = objectMapper.readValue(content, new TypeReference<List<RatedMovieDto>>() {});

        Assertions.assertEquals(2, result.size());
        assertThat(result, contains(
                RatedMovieDto.builder().title("The Green Mile").rate(2).boxOffice("$136,801,374").build(),
                RatedMovieDto.builder().title("The Shawshank Redemption").rate(5).boxOffice("$28,699,976").build()
        ));
    }
}
