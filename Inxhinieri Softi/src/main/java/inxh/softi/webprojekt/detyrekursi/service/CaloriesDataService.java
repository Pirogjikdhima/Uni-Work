package inxh.softi.webprojekt.detyrekursi.service;

import inxh.softi.webprojekt.detyrekursi.entity.CaloriesData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CaloriesDataService {
    CaloriesData saveCaloriesData(CaloriesData caloriesData);

    List<CaloriesData> getCaloriesDataByUsername(String username);

    Optional<CaloriesData> getCaloriesDataById(Long id);

    void deleteCaloriesDataById(Long id);

    CaloriesData updateCaloriesData(Long id, CaloriesData caloriesData);

    Map<LocalDate, Integer> getDaysExceeding2500Calories(String username);

    Map<YearMonth, Integer> getSpendingsExceeding1000(String username);

    Map<LocalDate, Integer> getTotalCaloriesPerDayForWeek(String username);

    int countDaysExceedingThresholdTotal(String username);

    int calculateTotalExpenditureForWeek(String username);

    List<CaloriesData> filterCaloriesDataByDateRange(String username, LocalDateTime fromDate, LocalDateTime toDate);

    Map<String, Object> getAdminReport();

    List<CaloriesData> getAllCaloriesData();

    Map<LocalDate, Integer> countEntriesByDateRangeGroupedByDay(LocalDateTime startDate, LocalDateTime endDate);
}
