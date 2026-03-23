package com.gler.assignment.forecast;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ForecastRepositoryTest {

    @Autowired
    private ForecastRepository forecastRepository;

    @Test
    void savesAndReadsRecord() {
        ForecastRecord record = new ForecastRecord();
        record.setDate(LocalDate.of(2025, 1, 1));
        record.setMaxTemperature(12.5);
        record.setMaxHumidity(75.0);
        record.setMaxWindSpeed(7.5);

        ForecastRecord saved = forecastRepository.save(record);

        assertThat(saved.getId()).isNotNull();
        assertThat(forecastRepository.findAll()).hasSize(1);
    }
}
