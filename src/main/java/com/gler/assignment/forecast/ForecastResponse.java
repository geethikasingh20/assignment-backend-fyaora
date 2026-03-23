package com.gler.assignment.forecast;

import java.time.LocalDate;

public class ForecastResponse {
    private LocalDate date;
    private Double maxTemperature;
    private Double maxHumidity;
    private Double maxWindSpeed;

    public ForecastResponse() {
    }

    public ForecastResponse(LocalDate date, Double maxTemperature, Double maxHumidity, Double maxWindSpeed) {
        this.date = date;
        this.maxTemperature = maxTemperature;
        this.maxHumidity = maxHumidity;
        this.maxWindSpeed = maxWindSpeed;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public Double getMaxHumidity() {
        return maxHumidity;
    }

    public void setMaxHumidity(Double maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public Double getMaxWindSpeed() {
        return maxWindSpeed;
    }

    public void setMaxWindSpeed(Double maxWindSpeed) {
        this.maxWindSpeed = maxWindSpeed;
    }
}
