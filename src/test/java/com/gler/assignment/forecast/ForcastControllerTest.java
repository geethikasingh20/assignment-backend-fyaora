package com.gler.assignment.forecast;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gler.assignment.common.exception.GlobalExceptionHandler;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ForcastController.class)
@Import(GlobalExceptionHandler.class)
class ForcastControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForcastService forcastService;

    @Test
    void returnsBadRequestWhenMissingFields() throws Exception {
        String body = "{\"addTemprature\": true, \"addHumidity\": true}";

        mockMvc.perform(post("/api/v1/forcast")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.path").value("/api/v1/forcast"));
    }

    @Test
    void returnsForecastResponse() throws Exception {
        ForecastResponse response = new ForecastResponse(LocalDate.of(2025, 1, 1), 12.5, 75.0, 7.5);
        given(forcastService.createForecast(org.mockito.ArgumentMatchers.any(ForecastRequest.class)))
            .willReturn(response);

        String body = "{\"addTemprature\": true, \"addHumidity\": true, \"addWindSpeed\": true}";

        mockMvc.perform(post("/api/v1/forcast")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.date").value("2025-01-01"))
            .andExpect(jsonPath("$.maxTemperature").value(12.5))
            .andExpect(jsonPath("$.maxHumidity").value(75.0))
            .andExpect(jsonPath("$.maxWindSpeed").value(7.5));
    }
}
