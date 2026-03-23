package com.gler.assignment.forecast;

import com.gler.assignment.common.exception.UpstreamApiException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenMeteoClient {

    public static final String OPEN_METEO_URL =
        "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41"
            + "&current=temperature_2m,wind_speed_10m"
            + "&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m";

    private final RestTemplate restTemplate;

    public OpenMeteoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OpenMeteoResponse fetchForecast() {
        try {
            return restTemplate.getForObject(OPEN_METEO_URL, OpenMeteoResponse.class);
        } catch (RestClientException ex) {
            throw new UpstreamApiException("Connection to the upstream is unreachable", ex);
        }
    }
}
