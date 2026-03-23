package com.gler.assignment.forecast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class ForcastService {

    private final OpenMeteoClient openMeteoClient;
    private final ForecastRepository forecastRepository;

    public ForcastService(OpenMeteoClient openMeteoClient, ForecastRepository forecastRepository) {
        this.openMeteoClient = openMeteoClient;
        this.forecastRepository = forecastRepository;
    }

    public ForecastResponse createForecast(ForecastRequest request) {
        OpenMeteoResponse response = openMeteoClient.fetchForecast();
        if (response == null || response.getHourly() == null) {
            throw new IllegalStateException("Upstream response missing hourly data");
        }

        OpenMeteoResponse.Hourly hourly = response.getHourly();
        LocalDate date = extractDate(hourly.getTime());

        Double maxTemperature = request.getAddTemprature() ? maxValue(hourly.getTemperature2m()) : null;
        Double maxHumidity = request.getAddHumidity() ? maxValue(hourly.getRelativeHumidity2m()) : null;
        Double maxWindSpeed = request.getAddWindSpeed() ? maxValue(hourly.getWindSpeed10m()) : null;

        ForecastRecord record = new ForecastRecord();
        record.setDate(date);
        record.setMaxTemperature(maxTemperature);
        record.setMaxHumidity(maxHumidity);
        record.setMaxWindSpeed(maxWindSpeed);

        ForecastRecord saved = forecastRepository.save(record);

        return new ForecastResponse(saved.getDate(), saved.getMaxTemperature(), saved.getMaxHumidity(), saved.getMaxWindSpeed());
    }

    private Double maxValue(List<Double> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.stream().filter(Objects::nonNull).max(Double::compareTo).orElse(null);
    }

    private LocalDate extractDate(List<String> times) {
        if (times != null && !times.isEmpty()) {
            try {
                return LocalDateTime.parse(times.get(0)).toLocalDate();
            } catch (DateTimeParseException ignored) {
                return LocalDate.now();
            }
        }
        return LocalDate.now();
    }
}
