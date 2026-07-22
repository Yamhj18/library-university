package com.unamba.apilibrary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unamba.apilibrary.business.BusinessDashboard;
import com.unamba.apilibrary.dto.response.ResponseDashboard;
import com.unamba.apilibrary.dto.response.ResponseDashboardCharts;

@RestController
@RequestMapping(path = "dashboard")
public class DashboardController {

    private final BusinessDashboard businessDashboard;

    public DashboardController(BusinessDashboard businessDashboard) {
        this.businessDashboard = businessDashboard;
    }

    @GetMapping(path = "stats")
    public ResponseEntity<ResponseDashboard> getStats() {
        try {
            return ResponseEntity.ok(businessDashboard.getStats());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "charts")
    public ResponseEntity<ResponseDashboardCharts> getCharts() {
        try {
            return ResponseEntity.ok(businessDashboard.getCharts());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
