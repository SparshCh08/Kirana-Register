package com.kirana.register.controller;

import com.kirana.register.exception.CustomException;
import com.kirana.register.model.Report;
import com.kirana.register.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @GetMapping("/weekly")
    public ResponseEntity<Report> getWeeklyReport(
            @RequestParam String currencyFrom,
            @RequestParam String customerUserName) {
        try {
            logger.info("Generating weekly report for user: {}, currency: {}", customerUserName, currencyFrom);
            Report report = reportService.generateWeeklyReport(customerUserName, currencyFrom);
            logger.info("Weekly report generated successfully for user: {}", customerUserName);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            logger.error("Failed to generate weekly report for user: {}", customerUserName, e);
            throw new CustomException("Error generating weekly report", "REPORT_GENERATION_FAILED");
        }
    }

    @GetMapping("/monthly")
    public ResponseEntity<Report> getMonthlyReport(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam String customerUserName,
            @RequestParam String currencyFrom) {
        try {
            logger.info("Generating monthly report for user: {}, year: {}, month: {}, currency: {}",
                    customerUserName, year, month, currencyFrom);
            Report report = reportService.generateMonthlyReport(customerUserName, year, month, currencyFrom);
            logger.info("Monthly report generated successfully for user: {}", customerUserName);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            logger.error("Failed to generate monthly report for user: {}", customerUserName, e);
            throw new CustomException("Error generating monthly report", "REPORT_GENERATION_FAILED");
        }
    }

    @GetMapping("/yearly")
    public ResponseEntity<Report> getYearlyReport(
            @RequestParam int year,
            @RequestParam String customerUserName,
            @RequestParam String currencyFrom) {
        try {
            logger.info("Generating yearly report for user: {}, year: {}, currency: {}",
                    customerUserName, year, currencyFrom);
            Report report = reportService.generateYearlyReport(customerUserName, year, currencyFrom);
            logger.info("Yearly report generated successfully for user: {}", customerUserName);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            logger.error("Failed to generate yearly report for user: {}", customerUserName, e);
            throw new CustomException("Error generating yearly report", "REPORT_GENERATION_FAILED");
        }
    }
}
