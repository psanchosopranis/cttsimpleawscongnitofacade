package com.cttexpress.config;

import com.codahale.metrics.health.HealthCheck;

public class AppHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        if (true){
            return Result.healthy();
        } else {
            return Result.unhealthy("Error message");
        }
    }
}