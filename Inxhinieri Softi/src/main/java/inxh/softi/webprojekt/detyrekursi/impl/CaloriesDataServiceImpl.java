package inxh.softi.webprojekt.detyrekursi.impl;

import inxh.softi.webprojekt.detyrekursi.entity.CaloriesData;
import inxh.softi.webprojekt.detyrekursi.exception.CaloriesDataNotFoundException;
import inxh.softi.webprojekt.detyrekursi.repository.CaloriesDataRepository;
import inxh.softi.webprojekt.detyrekursi.service.CaloriesDataService;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class CaloriesDataServiceImpl implements CaloriesDataService {

    private final CaloriesDataRepository caloriesDataRepository;

    public CaloriesDataServiceImpl(CaloriesDataRepository caloriesDataRepository) {
        this.caloriesDataRepository = caloriesDataRepository;
    }

    @Override
    public CaloriesData saveCaloriesData(CaloriesData caloriesData) {
        return caloriesDataRepository.save(caloriesData);
    }

    @Override
    public List<CaloriesData> getCaloriesDataByUsername(String username) {
        return caloriesDataRepository.findByUsername(username);
    }

    @Override
    public Optional<CaloriesData> getCaloriesDataById(Long id) {

        return caloriesDataRepository.findById(id);
    }

    @Override
    public void deleteCaloriesDataById(Long id) {

        caloriesDataRepository.deleteById(id);
    }

    @Override
    public CaloriesData updateCaloriesData(Long id, CaloriesData caloriesData) {
        Optional<CaloriesData> existingCaloriesData = caloriesDataRepository.findById(id);

        if (existingCaloriesData.isEmpty()) {
            throw new CaloriesDataNotFoundException(id);
        }

        caloriesData.setId(id);
        return caloriesDataRepository.save(caloriesData);
    }

    @Override
    public Map<LocalDate, Integer> getDaysExceeding2500Calories(String username) {
        List<CaloriesData> allData = caloriesDataRepository.findByUsername(username);

        Map<LocalDate, Integer> dailyCalories = new HashMap<>();
        for (CaloriesData data : allData) {
            LocalDate date = data.getDateTime().toLocalDate();
            int calories = data.getCalories();
            if (dailyCalories.containsKey(date)) {
                int currentCalories = dailyCalories.get(date);
                dailyCalories.put(date, currentCalories + calories);
            } else {
                dailyCalories.put(date, calories);
            }
        }
        Map<LocalDate, Integer> exceedingDays = new HashMap<>();
        for (Map.Entry<LocalDate, Integer> entry : dailyCalories.entrySet()) {
            if (entry.getValue() > 2500) {
                exceedingDays.put(entry.getKey(), entry.getValue());
            }
        }
        return exceedingDays;
    }

    @Override
    public Map<YearMonth, Integer> getSpendingsExceeding1000(String username) {
        List<CaloriesData> allData = caloriesDataRepository.findByUsername(username);

        Map<YearMonth, Integer> monthlySpendings = new HashMap<>();
        for (CaloriesData data : allData) {
            YearMonth yearMonth = YearMonth.from(data.getDateTime());
            int price = data.getPrice();
            if (monthlySpendings.containsKey(yearMonth)) {
                int currentSpendings = monthlySpendings.get(yearMonth);
                monthlySpendings.put(yearMonth, currentSpendings + price);
            } else {
                monthlySpendings.put(yearMonth, price);
            }
        }
        Map<YearMonth, Integer> exceedingMonths = new HashMap<>();
        for (Map.Entry<YearMonth, Integer> entry : monthlySpendings.entrySet()) {
            if (entry.getValue() > 1000) {
                exceedingMonths.put(entry.getKey(), entry.getValue());
            }
        }

        return exceedingMonths;
    }

    @Override
    public Map<LocalDate, Integer> getTotalCaloriesPerDayForWeek(String username) {
        LocalDate currentDate = LocalDate.now();
        // Fillimi i javes - E hene, fundi i javes e diele
        LocalDate startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // I bejme konvert LocalDate ne LocalDateTime
        LocalDateTime startOfWeekDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endOfWeekDateTime = endOfWeek.atTime(LocalTime.MAX);

        List<CaloriesData> allData = caloriesDataRepository.findByUsernameAndDateRange(username, startOfWeekDateTime, endOfWeekDateTime);
        Map<LocalDate, Integer> dailyCalories = new HashMap<>();

        for (CaloriesData data : allData) {
            LocalDate date = data.getDateTime().toLocalDate();
            dailyCalories.put(date, dailyCalories.getOrDefault(date, 0) + data.getCalories());
        }

        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            dailyCalories.putIfAbsent(date, 0);
        }
        return dailyCalories;
    }

    public int countDaysExceedingThresholdTotal(String username) {
        final int threshold = 2500;
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime startOfWeek = currentDateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusDays(6).withHour(23).withMinute(59).withSecond(59);
        List<CaloriesData> allData = caloriesDataRepository.findByUsernameAndDateRange(username, startOfWeek, endOfWeek);
        Map<LocalDate, Integer> dailyCalories = new HashMap<>();

        for (CaloriesData data : allData) {
            LocalDate date = data.getDateTime().toLocalDate();

            if (!dailyCalories.containsKey(date)) {
                dailyCalories.put(date, 0);
            }
            int totalCaloriesForDay = dailyCalories.get(date) + data.getCalories();
            dailyCalories.put(date, totalCaloriesForDay);
        }
        int count = 0;
        for (Integer calories : dailyCalories.values()) {
            if (calories > threshold) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int calculateTotalExpenditureForWeek(String username) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime startOfWeek = currentDateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusDays(6).withHour(23).withMinute(59).withSecond(59);
        List<CaloriesData> allData = caloriesDataRepository.findByUsernameAndDateRange(username, startOfWeek, endOfWeek);

        int totalExpenditure = 0;

        for (CaloriesData data : allData) {
            totalExpenditure += data.getPrice();
        }
        return totalExpenditure;
    }

    @Override
    public List<CaloriesData> filterCaloriesDataByDateRange(String username, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("fromDate must be before or equal to toDate.");
        }
        return caloriesDataRepository.findByUsernameAndDateRange(username, fromDate, toDate);
    }

    @Override
    public Map<String, Object> getAdminReport() {
        LocalDate today = LocalDate.now();

        LocalDate startOfThisWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate startOfLastWeek = startOfThisWeek.minusWeeks(1);
        LocalDate endOfLastWeek = startOfThisWeek.minusDays(1);

        LocalDateTime startOfThisWeekDateTime = startOfThisWeek.atStartOfDay();
        LocalDateTime startOfLastWeekDateTime = startOfLastWeek.atStartOfDay();
        LocalDateTime endOfLastWeekDateTime = endOfLastWeek.atTime(LocalTime.MAX);

        LocalDateTime startOfLastMonth = YearMonth.now().minusMonths(1).atDay(1).atStartOfDay();
        LocalDateTime endOfLastMonth = YearMonth.now().minusMonths(1).atEndOfMonth().atTime(LocalTime.MAX);

        // te dhenat e javes
        Map<LocalDate, Integer> entriesThisWeek = countEntriesByDateRangeGroupedByDay(startOfThisWeekDateTime, LocalDateTime.now());

        // te dhenat e javes se kaluar
        Map<LocalDate, Integer> entriesLastWeek = countEntriesByDateRangeGroupedByDay(startOfLastWeekDateTime, endOfLastWeekDateTime);

        // mesatarja e kalorive per 7 ditet e fundit
        List<CaloriesData> last7DaysData = caloriesDataRepository.findByDateRange(startOfThisWeekDateTime, LocalDateTime.now());
        Map<String, Integer> userCaloriesSum = new HashMap<>();
        for (CaloriesData data : last7DaysData) {
            userCaloriesSum.merge(data.getUsername(), data.getCalories(), Integer::sum);
        }
        userCaloriesSum.values().stream().mapToInt(Integer::intValue).average().orElse(0.0);

        // userat qe tejkalojne limitin e kalorive
        List<String> usersExceedingLimit = caloriesDataRepository.findUsersExceedingMonthlyLimit(startOfLastMonth, endOfLastMonth);

        Map<String, Object> report = new HashMap<>();
        report.put("entriesThisWeek", entriesThisWeek);
        report.put("entriesLastWeek", entriesLastWeek);
        report.put("averageCaloriesPerUser", userCaloriesSum);
        report.put("usersExceedingMonthlyLimit", usersExceedingLimit);

        return report;
    }

    @Override
    public List<CaloriesData> getAllCaloriesData() {
        return caloriesDataRepository.findAll();
    }
    @Override
    public Map<LocalDate, Integer> countEntriesByDateRangeGroupedByDay(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = caloriesDataRepository.countEntriesByDateRangeGroupedByDay(startDate, endDate);
        Map<LocalDate, Integer> entriesByDay = new HashMap<>();
        for (Object[] result : results) {
            java.sql.Date sqlDate = (java.sql.Date) result[0];
            LocalDate date = sqlDate.toLocalDate();
            Integer count = ((Number) result[1]).intValue();
            entriesByDay.put(date, count);
        }
        return entriesByDay;
    }

}
