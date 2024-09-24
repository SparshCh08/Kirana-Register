package com.kirana.register.controller;

import com.kirana.register.model.Report;
import com.kirana.register.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/weekly")
    public ResponseEntity<Report> getWeeklyReport(
            @RequestParam String currencyFrom,
            @RequestParam String customerUserName) {
        Report report = reportService.generateWeeklyReport(customerUserName, currencyFrom);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/monthly")
    public ResponseEntity<Report> getMonthlyReport(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam String customerUserName,
            @RequestParam String currencyFrom) {

        Report report = reportService.generateMonthlyReport(customerUserName, year, month, currencyFrom);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/yearly")
    public ResponseEntity<Report> getYearlyReport(
            @RequestParam int year,
            @RequestParam String customerUserName,
            @RequestParam String currencyFrom) {

        Report report = reportService.generateYearlyReport(customerUserName, year, currencyFrom);
        return ResponseEntity.ok(report);
    }
}
