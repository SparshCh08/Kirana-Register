//package com.kirana.register.service;
//
//import com.kirana.register.model.Report;
//import com.kirana.register.model.Transaction;
//import com.kirana.register.model.TransactionType;
//import com.kirana.register.repository.TransactionRepository;
//import com.kirana.register.util.CurrencyConversionUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class ReportService {
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    @Autowired
//    private CurrencyConversionUtil currencyConversionUtil;
//
//    public Report generateReport(LocalDateTime startDate, LocalDateTime endDate, String targetCurrency) {
//        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startDate, endDate);
//
//        BigDecimal totalCredit = BigDecimal.ZERO;
//        BigDecimal totalDebit = BigDecimal.ZERO;
//
//        for (Transaction txn : transactions) {
//            BigDecimal convertedAmount = currencyConversionUtil.convertCurrency(
//                    txn.getCurrency(), targetCurrency, txn.getAmount());
//
//            if (txn.getType() == TransactionType.CREDIT) {
//                totalCredit = totalCredit.add(convertedAmount);
//            } else {
//                totalDebit = totalDebit.add(convertedAmount);
//            }
//        }
//
//        BigDecimal netFlow = totalCredit.subtract(totalDebit);
//        Report report = new Report();
//        report.setTotalCredits(totalCredit);
//        report.setTotalDebits(totalDebit);
//        report.setNetFlow(netFlow);
//        report.setDescription(netFlow.compareTo(BigDecimal.ZERO) > 0 ? "Profit" : "Loss");
//
//        return report;
//    }
//}


package com.kirana.register.service;

import com.kirana.register.model.Report;
import com.kirana.register.model.Transaction;
import com.kirana.register.model.TransactionType;
import com.kirana.register.repository.TransactionRepository;
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

@Service
public class ReportService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CurrencyConversionUtil currencyConversionUtil;

    public Report generateWeeklyReport(String customerUserName, String currencyFrom) {
        LocalDateTime endDate = LocalDateTime.now().with(LocalTime.MAX); // Current date and time
        LocalDateTime startDate = endDate.minusWeeks(1).with(LocalTime.MIN); // One week ago

        return generateReport(customerUserName,startDate, endDate, currencyFrom);
    }

    public Report generateMonthlyReport(String customerUserName, int year, int month, String currencyFrom) {
        YearMonth yearMonth = YearMonth.of(year, month); // Create YearMonth object
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay(); // Start of the month
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(LocalTime.MAX); // End of the month

        return generateReport(customerUserName, startDate, endDate, currencyFrom);
    }

    public Report generateYearlyReport(String customerUserName, int year, String currencyFrom) {
        LocalDateTime startDate = LocalDate.of(year, 1, 1).atStartOfDay(); // Start of the year
        LocalDateTime endDate = LocalDate.of(year, 12, 31).atTime(LocalTime.MAX); // End of the year

        return generateReport(customerUserName, startDate, endDate, currencyFrom);
    }

    public Report generateReport(String customerUserName, LocalDateTime startDate, LocalDateTime endDate, String targetCurrency) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();

        List<Transaction> transactions = transactionRepository.findTransactionsWithOptionalCustomer(email, startDate, endDate, customerUserName);

        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalDebit = BigDecimal.ZERO;

        for (Transaction txn : transactions) {
            BigDecimal convertedAmount = currencyConversionUtil.convertCurrency(
                    txn.getCurrency(), targetCurrency, txn.getAmount());

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

        return report;
    }
}
