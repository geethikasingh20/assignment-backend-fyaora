package com.gler.assignment.forecast;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ForecastRepository extends JpaRepository<ForecastRecord, Long> {
}
