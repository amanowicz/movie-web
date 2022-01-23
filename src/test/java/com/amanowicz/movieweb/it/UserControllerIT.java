package com.amanowicz.movieweb.it;

import com.amanowicz.movieweb.config.MovieWebPostgresContainer;
import com.amanowicz.movieweb.config.WebSecurityConfig;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@Import({WebSecurityConfig.class})
public class UserControllerIT {

    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @ClassRule
    public static PostgreSQLContainer<MovieWebPostgresContainer> postgreSQLContainer =
            MovieWebPostgresContainer.getInstance();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webAppContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void shouldReturnTokenWhenLogInWithValidUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .queryParam("username", "admin")
                        .queryParam("password", "admin"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String tokenHeader = mvcResult.getResponse().getHeader("token");
        Assertions.assertNotNull(tokenHeader);
    }

    @Test
    void shouldReturnUnauthorizedWhenLoginWithWrongPassword() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .queryParam("username", "admin")
                        .queryParam("password", "xyz"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("User not authenticated!", content);
    }

    @Test
    @Transactional
    void shouldAllowToRegisterNewUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .queryParam("username", "test-user")
                        .queryParam("password", "passs123"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String tokenHeader = mvcResult.getResponse().getHeader("token");
        Assertions.assertNotNull(tokenHeader);
    }
}
