package com.vanopt.vanopt.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class OptimizationControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    private static final String VALID_REQUEST = """
            {
              "maxVolume": 15,
              "availableShipments": [
                { "name": "Parcel A", "volume": 5, "revenue": 120 },
                { "name": "Parcel B", "volume": 10, "revenue": 200 },
                { "name": "Parcel C", "volume": 3, "revenue": 80 },
                { "name": "Parcel D", "volume": 8, "revenue": 160 }
              ]
            }
            """;

    @Test
    void optimizeShouldReturnOptimalSelection() throws Exception {
        mockMvc.perform(post("/van-optimizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").exists())
                .andExpect(jsonPath("$.totalRevenue").value(320))
                .andExpect(jsonPath("$.totalVolume").value(15))
                .andExpect(jsonPath("$.selectedShipments", hasSize(2)))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void optimizeShouldPersistAndBeRetrievable() throws Exception {
        MvcResult result = mockMvc.perform(post("/van-optimizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_REQUEST))
                .andExpect(status().isOk())
                .andReturn();

        String requestId = com.jayway.jsonpath.JsonPath
                .read(result.getResponse().getContentAsString(), "$.requestId");

        mockMvc.perform(get("/van-optimizations/" + requestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(requestId))
                .andExpect(jsonPath("$.totalRevenue").value(320));
    }

    @Test
    void getByIdShouldReturn404ForUnknownId() throws Exception {
        mockMvc.perform(get("/van-optimizations/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllShouldReturnList() throws Exception {
        mockMvc.perform(post("/van-optimizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST));

        mockMvc.perform(get("/van-optimizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void shouldReturn400ForMissingMaxVolume() throws Exception {
        String invalidRequest = """
                {
                  "availableShipments": [
                    { "name": "Parcel A", "volume": 5, "revenue": 120 }
                  ]
                }
                """;

        mockMvc.perform(post("/van-optimizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForEmptyShipments() throws Exception {
        String invalidRequest = """
                {
                  "maxVolume": 10,
                  "availableShipments": []
                }
                """;

        mockMvc.perform(post("/van-optimizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }
}
