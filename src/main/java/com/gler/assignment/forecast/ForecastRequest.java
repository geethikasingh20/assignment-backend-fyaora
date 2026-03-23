package com.gler.assignment.forecast;

import jakarta.validation.constraints.NotNull;

public class ForecastRequest {

    @NotNull
    private Boolean addTemprature;

    @NotNull
    private Boolean addHumidity;

    @NotNull
    private Boolean addWindSpeed;

    public Boolean getAddTemprature() {
        return addTemprature;
    }

    public void setAddTemprature(Boolean addTemprature) {
        this.addTemprature = addTemprature;
    }

    public Boolean getAddHumidity() {
        return addHumidity;
    }

    public void setAddHumidity(Boolean addHumidity) {
        this.addHumidity = addHumidity;
    }

    public Boolean getAddWindSpeed() {
        return addWindSpeed;
    }

    public void setAddWindSpeed(Boolean addWindSpeed) {
        this.addWindSpeed = addWindSpeed;
    }
}
