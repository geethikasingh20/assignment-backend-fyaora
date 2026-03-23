package com.gler.assignment.forecast;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.gler.assignment.common.exception.UpstreamApiException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ForcastServiceTest {

    @Mock
    private OpenMeteoClient openMeteoClient;

    @Mock
    private ForecastRepository forecastRepository;

    @InjectMocks
    private ForcastService forcastService;

    @Test
    void savesMaxValuesWhenFlagsAreTrue() {
        ForecastRequest request = new ForecastRequest();
        request.setAddTemprature(true);
        request.setAddHumidity(true);
        request.setAddWindSpeed(true);

        OpenMeteoResponse response = new OpenMeteoResponse();
        OpenMeteoResponse.Hourly hourly = new OpenMeteoResponse.Hourly();
        hourly.setTime(List.of("2025-01-01T00:00"));
        hourly.setTemperature2m(List.of(10.0, 12.5, 9.0));
        hourly.setRelativeHumidity2m(List.of(60.0, 75.0, 65.0));
        hourly.setWindSpeed10m(List.of(5.0, 7.5, 6.0));
        response.setHourly(hourly);

        given(openMeteoClient.fetchForecast()).willReturn(response);

        ForecastRecord savedRecord = new ForecastRecord();
        savedRecord.setDate(LocalDate.of(2025, 1, 1));
        savedRecord.setMaxTemperature(12.5);
        savedRecord.setMaxHumidity(75.0);
        savedRecord.setMaxWindSpeed(7.5);
        given(forecastRepository.save(org.mockito.ArgumentMatchers.any(ForecastRecord.class)))
            .willReturn(savedRecord);

        ForecastResponse result = forcastService.createForecast(request);

        assertThat(result.getMaxTemperature()).isEqualTo(12.5);
        assertThat(result.getMaxHumidity()).isEqualTo(75.0);
        assertThat(result.getMaxWindSpeed()).isEqualTo(7.5);

        ArgumentCaptor<ForecastRecord> captor = ArgumentCaptor.forClass(ForecastRecord.class);
        verify(forecastRepository).save(captor.capture());
        ForecastRecord captured = captor.getValue();
        assertThat(captured.getDate()).isEqualTo(LocalDate.of(2025, 1, 1));
    }

    @Test
    void storesNullsWhenFlagsAreFalse() {
        ForecastRequest request = new ForecastRequest();
        request.setAddTemprature(false);
        request.setAddHumidity(false);
        request.setAddWindSpeed(false);

        OpenMeteoResponse response = new OpenMeteoResponse();
        OpenMeteoResponse.Hourly hourly = new OpenMeteoResponse.Hourly();
        hourly.setTime(List.of("2025-01-01T00:00"));
        hourly.setTemperature2m(List.of(10.0));
        hourly.setRelativeHumidity2m(List.of(60.0));
        hourly.setWindSpeed10m(List.of(5.0));
        response.setHourly(hourly);

        given(openMeteoClient.fetchForecast()).willReturn(response);

        ForecastRecord savedRecord = new ForecastRecord();
        savedRecord.setDate(LocalDate.of(2025, 1, 1));
        savedRecord.setMaxTemperature(null);
        savedRecord.setMaxHumidity(null);
        savedRecord.setMaxWindSpeed(null);
        given(forecastRepository.save(org.mockito.ArgumentMatchers.any(ForecastRecord.class)))
            .willReturn(savedRecord);

        ForecastResponse result = forcastService.createForecast(request);

        assertThat(result.getMaxTemperature()).isNull();
        assertThat(result.getMaxHumidity()).isNull();
        assertThat(result.getMaxWindSpeed()).isNull();
    }

    @Test
    void propagatesUpstreamException() {
        ForecastRequest request = new ForecastRequest();
        request.setAddTemprature(true);
        request.setAddHumidity(true);
        request.setAddWindSpeed(true);

        given(openMeteoClient.fetchForecast())
            .willThrow(new UpstreamApiException("Connection to the upstream is unreachable", new RuntimeException("boom")));

        assertThatThrownBy(() -> forcastService.createForecast(request))
            .isInstanceOf(UpstreamApiException.class);
    }
}
