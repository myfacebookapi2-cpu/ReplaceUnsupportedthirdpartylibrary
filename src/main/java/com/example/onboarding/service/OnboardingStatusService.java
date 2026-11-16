package com.example.onboarding.service;

import com.example.onboarding.dto.OnboardingStatus;
import com.example.onboarding.model.Employee;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Works out how far away an employee's onboarding date is.
 */
@Service
public class OnboardingStatusService {

    public OnboardingStatus statusFor(Employee employee, LocalDate today) {
        LocalDate start = employee.getOnboardingDate();

        int days = Math.toIntExact(ChronoUnit.DAYS.between(today, start));
        String status = days > 0 ? "UPCOMING" : (days == 0 ? "STARTS_TODAY" : "STARTED");

        // Working days (Monday to Friday) after today, up to and including the onboarding date.
        int workingDays = 0;
        for (LocalDate date = today; date.isBefore(start); ) {
            date = date.plusDays(1);
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workingDays++;
            }
        }

        String label = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.ENGLISH).format(start);

        return new OnboardingStatus(employee.getId(), employee.getName(), employee.getOnboardingDate(),
                label, days, workingDays, status);
    }
}
