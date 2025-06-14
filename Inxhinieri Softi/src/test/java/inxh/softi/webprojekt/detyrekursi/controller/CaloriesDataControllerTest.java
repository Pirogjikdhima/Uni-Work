package inxh.softi.webprojekt.detyrekursi.controller;

import inxh.softi.webprojekt.detyrekursi.controllers.CaloriesDataController;
import inxh.softi.webprojekt.detyrekursi.entity.CaloriesData;
import inxh.softi.webprojekt.detyrekursi.service.CaloriesDataService;
import inxh.softi.webprojekt.detyrekursi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CaloriesDataControllerTest {

    @Mock
    private CaloriesDataService caloriesDataService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CaloriesDataController caloriesDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testAddCaloriesData_Successful() {
        CaloriesData caloriesData = new CaloriesData();
        caloriesData.setId(1L);
        caloriesData.setCalories(500);
        String username = "testusername";

        when(userService.doesUserExists(username)).thenReturn(true);
        when(caloriesDataService.saveCaloriesData(any(CaloriesData.class))).thenReturn(caloriesData);

        ResponseEntity<?> response = caloriesDataController.addCaloriesData(caloriesData, username);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(caloriesDataService).saveCaloriesData(any(CaloriesData.class));
    }

    @Test
    void testAddCaloriesData_UserDoesNotExist() {
        CaloriesData caloriesData = new CaloriesData();
        String username = "testUnknownUsername";

        when(userService.doesUserExists(username)).thenReturn(false);

        ResponseEntity<?> response = caloriesDataController.addCaloriesData(caloriesData, username);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User with username testUnknownUsername does not exist.", response.getBody());
        verifyNoInteractions(caloriesDataService);
    }

    @Test
    void testGetCaloriesDataByUsername_Successful() {
        String username = "testusername";
        CaloriesData data1 = new CaloriesData();
        CaloriesData data2 = new CaloriesData();

        when(userService.doesUserExists(username)).thenReturn(true);
        when(caloriesDataService.getCaloriesDataByUsername(username)).thenReturn(List.of(data1, data2));

        ResponseEntity<?> response = caloriesDataController.getCaloriesDataByUsername(username);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, ((List<?>) response.getBody()).size());
    }

    @Test
    void testGetCaloriesDataByUsername_UserDoesNotExist() {
        String username = "testUnknownUsername";

        when(userService.doesUserExists(username)).thenReturn(false);

        ResponseEntity<?> response = caloriesDataController.getCaloriesDataByUsername(username);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User with username testUnknownUsername does not exist.", response.getBody());
    }

    @Test
    void testGetCaloriesDataById_Found() {
        Long id = 1L;
        CaloriesData data = new CaloriesData();
        data.setId(id);

        when(caloriesDataService.getCaloriesDataById(id)).thenReturn(Optional.of(data));

        ResponseEntity<?> response = caloriesDataController.getCaloriesDataById(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(data, response.getBody());
    }

    @Test
    void testGetCaloriesDataById_NotFound() {
        Long id = 999L;

        when(caloriesDataService.getCaloriesDataById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = caloriesDataController.getCaloriesDataById(id);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals(Map.of("message", "Food entry not found."), response.getBody());
    }

    @Test
    void testUpdateCaloriesData_UserDoesNotExist() {
        Long id = 1L;
        CaloriesData caloriesData = new CaloriesData();
        String username = "nonExistentUser";

        when(userService.doesUserExists(username)).thenReturn(false);

        ResponseEntity<?> response = caloriesDataController.updateCaloriesData(id, caloriesData, username);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User with username " + username + " does not exist.", response.getBody());
    }

    @Test
    void testUpdateCaloriesData_FoodEntryNotFound() {
        Long id = 1L;
        CaloriesData caloriesData = new CaloriesData();
        String username = "validUser";

        when(userService.doesUserExists(username)).thenReturn(true);
        when(caloriesDataService.getCaloriesDataById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = caloriesDataController.updateCaloriesData(id, caloriesData, username);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Food entry not found.", response.getBody());
    }

    @Test
    void testUpdateCaloriesData_Successful() {
        Long id = 1L;
        CaloriesData caloriesData = new CaloriesData();
        caloriesData.setCalories(600);
        String username = "validUser";

        when(userService.doesUserExists(username)).thenReturn(true);
        when(caloriesDataService.getCaloriesDataById(id)).thenReturn(Optional.of(new CaloriesData()));
        when(caloriesDataService.updateCaloriesData(eq(id), any(CaloriesData.class))).thenReturn(caloriesData);

        ResponseEntity<?> response = caloriesDataController.updateCaloriesData(id, caloriesData, username);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(caloriesData, response.getBody());
    }


    @Test
    void testDeleteCaloriesData_Successful() {
        Long id = 1L;
        String username = "testusername";

        when(userService.doesUserExists(username)).thenReturn(true);
        when(caloriesDataService.getCaloriesDataById(id)).thenReturn(Optional.of(new CaloriesData()));

        ResponseEntity<?> response = caloriesDataController.deleteCaloriesData(id, username);

        assertEquals(204, response.getStatusCodeValue());
        assertEquals("Food entry deleted successfully.", response.getBody());
        verify(caloriesDataService).deleteCaloriesDataById(id);
    }

    @Test
    void testDeleteCaloriesData_UserDoesNotExist() {
        Long id = 1L;
        String username = "testUnknownUsername";

        when(userService.doesUserExists(username)).thenReturn(false);

        ResponseEntity<?> response = caloriesDataController.deleteCaloriesData(id, username);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User with username testUnknownUsername does not exist.", response.getBody());
    }

    @Test
    void testDeleteCaloriesData_NotFound() {
        Long id = 999L;
        String username = "testUsername";

        when(userService.doesUserExists(username)).thenReturn(true);
        when(caloriesDataService.getCaloriesDataById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = caloriesDataController.deleteCaloriesData(id, username);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Food entry not found.", response.getBody());
    }

    @Test
    void testGetDaysExceeding2500Calories_Successful() {
        String username = "testUsername";
        Map<LocalDate, Integer> exceedingDays = Map.of(LocalDate.now(), 3500);

        when(userService.doesUserExists(username)).thenReturn(true);
        when(caloriesDataService.getDaysExceeding2500Calories(username)).thenReturn(exceedingDays);

        ResponseEntity<?> response = caloriesDataController.getDaysExceeding2500(username);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(exceedingDays, response.getBody());
    }

    @Test
    void testGetDaysExceeding2500Calories_UserDoesNotExist() {
        String username = "testUnknownUsername";

        when(userService.doesUserExists(username)).thenReturn(false);

        ResponseEntity<?> response = caloriesDataController.getDaysExceeding2500(username);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User with username testUnknownUsername does not exist.", response.getBody());
    }

    @Test
    void testGetDaysExceeding2500Calories_NoExceedingDays() {
        String username = "testUsername";
        Map<LocalDate, Integer> exceedingDays = Map.of(); // Empty map

        when(userService.doesUserExists(username)).thenReturn(true);
        when(caloriesDataService.getDaysExceeding2500Calories(username)).thenReturn(exceedingDays);

        ResponseEntity<?> response = caloriesDataController.getDaysExceeding2500(username);

        assertEquals(200, response.getStatusCodeValue(), "Expected HTTP status 200 for no exceeding days case.");
        assertEquals(Map.of("status", HttpStatus.NOT_FOUND.value(), "message", "No days exceeding 2500 calories found for user: " + username), response.getBody(), "Response body does not match the expected message for no exceeding days.");
    }

    @Test
    void testGetSpendingsExceeding1000_Successful() {
        String username = "testUsername";
        Map<YearMonth, Integer> exceedingSpendings = Map.of(YearMonth.now(), 1500);

        when(userService.doesUserExists(username)).thenReturn(true);
        when(caloriesDataService.getSpendingsExceeding1000(username)).thenReturn(exceedingSpendings);

        ResponseEntity<?> response = caloriesDataController.getSpendingsExceeding1000(username);

        assertEquals(200, response.getStatusCodeValue(), "Expected HTTP status 200 for successful case.");
        assertEquals(exceedingSpendings, response.getBody(), "Response body does not match the expected data.");
    }

    @Test
    void testGetSpendingsExceeding1000_UserDoesNotExist() {
        String username = "testUnknownUsername";

        when(userService.doesUserExists(username)).thenReturn(false);

        ResponseEntity<?> response = caloriesDataController.getSpendingsExceeding1000(username);

        assertEquals(400, response.getStatusCodeValue(), "Expected HTTP status 400 for user not found.");
        assertEquals("User with username " + username + " does not exist.", response.getBody(), "Response body does not match the expected error message.");
    }

    @Test
    void testGetTotalCaloriesPerDayForWeek_UserDoesNotExist() {
        String username = "nonExistentUser";

        when(userService.doesUserExists(username)).thenReturn(false);

        ResponseEntity<?> response = caloriesDataController.getTotalCaloriesPerDayForWeek(username);

        assertEquals(400, response.getStatusCodeValue(), "Expected HTTP status 400 for user not found.");
        assertEquals("User with username " + username + " does not exist.", response.getBody(), "Response body does not match the expected error message.");
    }

    @Test
    void testGetTotalCaloriesPerDayForWeek_ValidUser() {
        String username = "testUser";
        Map<LocalDate, Integer> mockData = Map.of(
                LocalDate.now().minusDays(1), 2000,
                LocalDate.now(), 2500
        );

        when(userService.doesUserExists(username)).thenReturn(true);
        when(caloriesDataService.getTotalCaloriesPerDayForWeek(username)).thenReturn(mockData);

        ResponseEntity<?> response = caloriesDataController.getTotalCaloriesPerDayForWeek(username);

        assertEquals(200, response.getStatusCodeValue(), "Expected HTTP status 200 for a valid user.");
        assertEquals(mockData, response.getBody(), "Response body does not match the expected calorie data.");
    }

    @Test
    void testGetTotalCaloriesPerDayForWeek_UserHasNoData() {
        String username = "testUserWithNoData";
        Map<LocalDate, Integer> emptyData = Collections.emptyMap();

        when(userService.doesUserExists(username)).thenReturn(true);
        when(caloriesDataService.getTotalCaloriesPerDayForWeek(username)).thenReturn(emptyData);

        ResponseEntity<?> response = caloriesDataController.getTotalCaloriesPerDayForWeek(username);

        assertEquals(200, response.getStatusCodeValue(), "Expected HTTP status 200 for a valid user with no calorie data.");
        assertEquals(emptyData, response.getBody(), "Response body should be an empty map.");
    }

    @Test
    void testGetCaloriesDataByDate_UserDoesNotExist() {
        String username = "nonExistentUser";
        LocalDateTime fromDate = LocalDateTime.now().minusDays(7);
        LocalDateTime toDate = LocalDateTime.now();

        when(userService.doesUserExists(username)).thenReturn(false);

        ResponseEntity<?> response = caloriesDataController.getCaloriesDataByDate(username, fromDate, toDate);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User with username " + username + " does not exist.", response.getBody());
    }

    @Test
    void testGetCaloriesDataByDate_Successful() {
        String username = "validUser";
        LocalDateTime fromDate = LocalDateTime.now().minusDays(7);
        LocalDateTime toDate = LocalDateTime.now();
        List<CaloriesData> mockData = List.of(new CaloriesData(), new CaloriesData());

        when(userService.doesUserExists(username)).thenReturn(true);
        when(caloriesDataService.filterCaloriesDataByDateRange(username, fromDate, toDate)).thenReturn(mockData);

        ResponseEntity<?> response = caloriesDataController.getCaloriesDataByDate(username, fromDate, toDate);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockData, response.getBody());
    }

    @Test
    void testGetCaloriesDataByDate_InvalidRange() {
        String username = "validUser";
        LocalDateTime fromDate = LocalDateTime.now();
        LocalDateTime toDate = LocalDateTime.now().minusDays(7); // Invalid range

        when(userService.doesUserExists(username)).thenReturn(true);
        when(caloriesDataService.filterCaloriesDataByDateRange(username, fromDate, toDate)).thenThrow(new IllegalArgumentException("Invalid date range"));

        ResponseEntity<?> response = caloriesDataController.getCaloriesDataByDate(username, fromDate, toDate);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid date range", response.getBody());
    }



}
