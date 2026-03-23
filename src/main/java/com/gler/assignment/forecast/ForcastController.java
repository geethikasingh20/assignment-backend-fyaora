package com.gler.assignment.forecast;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ForcastController {

    private final ForcastService forcastService;

    public ForcastController(ForcastService forcastService) {
        this.forcastService = forcastService;
    }

    @PostMapping("/forcast")
    public ResponseEntity<ForecastResponse> createForecast(@Valid @RequestBody ForecastRequest request) {
        return ResponseEntity.ok(forcastService.createForecast(request));
    }
}
