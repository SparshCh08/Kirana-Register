package com.kirana.register.service;

import com.kirana.register.model.Report;
import com.kirana.register.model.Transaction;
import com.kirana.register.model.TransactionType;
import com.kirana.register.repository.TransactionRepository;
import io.jsonwebtoken.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.kirana.register.util.CurrencyConversionUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CurrencyConversionUtil currencyConversionUtil;

    public Report generateWeeklyReport(String customerUserName, String currencyFrom) {
        logger.info("Generating weekly report for user: {} with currency: {}", customerUserName, currencyFrom);
        LocalDateTime endDate = LocalDateTime.now().with(LocalTime.MAX);
        LocalDateTime startDate = endDate.minusWeeks(1).with(LocalTime.MIN);

        return generateReport(customerUserName, startDate, endDate, currencyFrom);
    }

    public Report generateMonthlyReport(String customerUserName, int year, int month, String currencyFrom) {
        logger.info("Generating monthly report for user: {} for year: {}, month: {} with currency: {}", customerUserName, year, month, currencyFrom);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);

        return generateReport(customerUserName, startDate, endDate, currencyFrom);
    }

    public Report generateYearlyReport(String customerUserName, int year, String currencyFrom) {
        logger.info("Generating yearly report for user: {} for year: {} with currency: {}", customerUserName, year, currencyFrom);
        LocalDateTime startDate = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(year, 12, 31).atTime(LocalTime.MAX);

        return generateReport(customerUserName, startDate, endDate, currencyFrom);
    }

    public Report generateReport(String customerUserName, LocalDateTime startDate, LocalDateTime endDate, String targetCurrency) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();

        List<Transaction> transactions = transactionRepository.findTransactionsWithOptionalCustomer(email, startDate, endDate, customerUserName);

        if (transactions.isEmpty()) {
            logger.warn("No transactions found for user: {} between {} and {}", email, startDate, endDate);
            throw new RuntimeException("No transactions found for the specified date range.");
        }

        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalDebit = BigDecimal.ZERO;

        for (Transaction txn : transactions) {
            BigDecimal convertedAmount = currencyConversionUtil.convertCurrency(
                    targetCurrency, txn.getCurrency(), txn.getAmount());

            if (txn.getType() == TransactionType.CREDIT) {
                totalCredit = totalCredit.add(convertedAmount);
            } else {
                totalDebit = totalDebit.add(convertedAmount);
            }
        }

        BigDecimal netFlow = totalCredit.subtract(totalDebit);
        Report report = new Report();
        report.setUserName(email);
        report.setTotalCredits(totalCredit);
        report.setTotalDebits(totalDebit);
        report.setNetFlow(netFlow);
        report.setDescription(netFlow.compareTo(BigDecimal.ZERO) > 0 ? "Profit" : "Loss");

        logger.info("Report generated successfully for user: {}", email);
        return report;
    }
}
