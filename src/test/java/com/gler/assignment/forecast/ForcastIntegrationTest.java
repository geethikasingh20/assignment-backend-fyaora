package com.gler.assignment.forecast;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

@SpringBootTest
@AutoConfigureMockMvc
class ForcastIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ForecastRepository forecastRepository;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        forecastRepository.deleteAll();
    }

    @Test
    void endToEndFlowStoresForecast() throws Exception {
        String payload = "{\"addTemprature\": true, \"addHumidity\": true, \"addWindSpeed\": true}";
        String responseBody = "{\"hourly\":{\"time\":[\"2025-01-01T00:00\"]," +
            "\"temperature_2m\":[10.0,12.5,9.0]," +
            "\"relative_humidity_2m\":[60.0,75.0,65.0]," +
            "\"wind_speed_10m\":[5.0,7.5,6.0]}}";

        mockServer.expect(MockRestRequestMatchers.requestTo(OpenMeteoClient.OPEN_METEO_URL))
            .andRespond(MockRestResponseCreators.withSuccess(responseBody, MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/api/v1/forcast")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.date").value("2025-01-01"))
            .andExpect(jsonPath("$.maxTemperature").value(12.5))
            .andExpect(jsonPath("$.maxHumidity").value(75.0))
            .andExpect(jsonPath("$.maxWindSpeed").value(7.5));

        assertThat(forecastRepository.findAll()).hasSize(1);
        mockServer.verify();
    }
}
