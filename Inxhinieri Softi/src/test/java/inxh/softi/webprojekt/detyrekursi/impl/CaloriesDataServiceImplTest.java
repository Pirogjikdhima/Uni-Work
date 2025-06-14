package inxh.softi.webprojekt.detyrekursi.impl;

import inxh.softi.webprojekt.detyrekursi.entity.CaloriesData;
import inxh.softi.webprojekt.detyrekursi.exception.CaloriesDataNotFoundException;
import inxh.softi.webprojekt.detyrekursi.repository.CaloriesDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CaloriesDataServiceImplTest {

    @Mock
    private CaloriesDataRepository caloriesDataRepository;

    @InjectMocks
    private CaloriesDataServiceImpl caloriesDataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        caloriesDataService = new CaloriesDataServiceImpl(caloriesDataRepository);
    }

    @Test
    void testSaveCaloriesData() {
        CaloriesData data = new CaloriesData("user", LocalDateTime.now(), "Apple", 100, 10);
        when(caloriesDataRepository.save(data)).thenReturn(data);

        CaloriesData result = caloriesDataService.saveCaloriesData(data);

        assertNotNull(result);
        assertEquals("Apple", result.getFoodName());
        verify(caloriesDataRepository, times(1)).save(data);
    }

    @Test
    void testGetCaloriesDataByUsername() {
        String username = "user";
        List<CaloriesData> dataList = List.of(
                new CaloriesData(username, LocalDateTime.now(), "Apple", 100, 10),
                new CaloriesData(username, LocalDateTime.now(), "Banana", 200, 20)
        );
        when(caloriesDataRepository.findByUsername(username)).thenReturn(dataList);

        List<CaloriesData> result = caloriesDataService.getCaloriesDataByUsername(username);

        assertEquals(2, result.size());
        verify(caloriesDataRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGetCaloriesDataById() {
        Long id = 1L;
        CaloriesData data = new CaloriesData("user", LocalDateTime.now(), "Apple", 100, 10);
        data.setId(id);
        when(caloriesDataRepository.findById(id)).thenReturn(Optional.of(data));

        Optional<CaloriesData> result = caloriesDataService.getCaloriesDataById(id);

        assertTrue(result.isPresent());
        assertEquals("Apple", result.get().getFoodName());
        verify(caloriesDataRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteCaloriesDataById() {
        Long id = 1L;
        doNothing().when(caloriesDataRepository).deleteById(id);

        caloriesDataService.deleteCaloriesDataById(id);

        verify(caloriesDataRepository, times(1)).deleteById(id);
    }

    @Test
    void testCalculateTotalExpenditureForWeek() {
        String username = "user";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusDays(6).withHour(23).withMinute(59).withSecond(59);

        List<CaloriesData> mockData = List.of(
                new CaloriesData(username, now.minusDays(1), "Apple", 100, 50),
                new CaloriesData(username, now.minusDays(2), "Banana", 200, 100),
                new CaloriesData(username, now.minusDays(3), "Orange", 300, 150)
        );

        when(caloriesDataRepository.findByUsernameAndDateRange(username, startOfWeek, endOfWeek)).thenReturn(mockData);

        int result = caloriesDataService.calculateTotalExpenditureForWeek(username);

        assertEquals(300, result); // 50 + 100 + 150 = 300
        verify(caloriesDataRepository, times(1)).findByUsernameAndDateRange(username, startOfWeek, endOfWeek);
    }


    @Test
    void testUpdateCaloriesDataNotFound() {
        Long id = 1L;
        CaloriesData updatedData = new CaloriesData("user", LocalDateTime.now(), "Banana", 150, 15);

        when(caloriesDataRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CaloriesDataNotFoundException.class, () -> caloriesDataService.updateCaloriesData(id, updatedData));
        verify(caloriesDataRepository, times(1)).findById(id);
        verify(caloriesDataRepository, never()).save(any());
    }

    @Test
    void testGetDaysExceeding2500Calories() {
        String username = "user";
        List<CaloriesData> dataList = List.of(
                new CaloriesData(username, LocalDateTime.now().minusDays(1), "Apple", 2000, 10),
                new CaloriesData(username, LocalDateTime.now().minusDays(1), "Banana", 700, 20),
                new CaloriesData(username, LocalDateTime.now(), "Orange", 800, 5)
        );
        when(caloriesDataRepository.findByUsername(username)).thenReturn(dataList);

        Map<LocalDate, Integer> result = caloriesDataService.getDaysExceeding2500Calories(username);

        assertEquals(1, result.size());
        verify(caloriesDataRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGetSpendingsExceeding1000() {
        // Arrange
        String username = "user";
        List<CaloriesData> dataList = List.of(
                new CaloriesData(username, LocalDateTime.now().minusMonths(1), "Apple", 2000, 800),
                new CaloriesData(username, LocalDateTime.now().minusMonths(1), "Banana", 700, 400)
        );
        when(caloriesDataRepository.findByUsername(username)).thenReturn(dataList);

        Map<YearMonth, Integer> result = caloriesDataService.getSpendingsExceeding1000(username);

        assertEquals(1, result.size());
        verify(caloriesDataRepository, times(1)).findByUsername(username);
    }

    @Test
    void testCountDaysExceedingThresholdTotal() {
        String username = "testUser";
        final int threshold = 2500;

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime startOfWeek = currentDateTime.with(TemporalAdjusters.previousOrSame(LocalDate.now().getDayOfWeek().MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusDays(6).withHour(23).withMinute(59).withSecond(59);

        List<CaloriesData> mockData = new ArrayList<>();
        mockData.add(new CaloriesData(username, startOfWeek.plusDays(1), "Food1", 1200, 10));
        mockData.add(new CaloriesData(username, startOfWeek.plusDays(1), "Food2", 1500, 15)); // Day total: 2700 (exceeds threshold)
        mockData.add(new CaloriesData(username, startOfWeek.plusDays(2), "Food3", 2400, 20));
        mockData.add(new CaloriesData(username, startOfWeek.plusDays(2), "Food4", 200, 5)); // Day total: 2600 (exceeds threshold)
        mockData.add(new CaloriesData(username, startOfWeek.plusDays(3), "Food5", 2000, 50)); // Day total: 2000 (does not exceed threshold)

        when(caloriesDataRepository.findByUsernameAndDateRange(username, startOfWeek, endOfWeek)).thenReturn(mockData);

        int result = caloriesDataService.countDaysExceedingThresholdTotal(username);

        assertEquals(2, result); // Two days exceed the threshold
        verify(caloriesDataRepository, times(1)).findByUsernameAndDateRange(username, startOfWeek, endOfWeek);
    }

}
