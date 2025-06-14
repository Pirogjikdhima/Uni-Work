package inxh.softi.webprojekt.detyrekursi.controllers;

import inxh.softi.webprojekt.detyrekursi.entity.CaloriesData;
import inxh.softi.webprojekt.detyrekursi.service.CaloriesDataService;
import inxh.softi.webprojekt.detyrekursi.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/calories")
@CrossOrigin(value = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class CaloriesDataController {

    private final CaloriesDataService caloriesDataService;

    private final UserService userService;

    @Autowired
    public CaloriesDataController(CaloriesDataService caloriesDataService, UserService userService) {
        this.caloriesDataService = caloriesDataService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCaloriesData(@RequestBody CaloriesData caloriesData, @RequestParam String username) {
        if (!userService.doesUserExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with username " + username + " does not exist.");
        }
        caloriesData.setUsername(username);
        if (caloriesData.getDateTime() == null) {
            caloriesData.setDateTime(LocalDateTime.now());
        }
        CaloriesData savedData = caloriesDataService.saveCaloriesData(caloriesData);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedData);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getCaloriesDataByUsername(@PathVariable String username) {
        if (!userService.doesUserExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with username " + username + " does not exist.");
        }
        List<CaloriesData> data = caloriesDataService.getCaloriesDataByUsername(username);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/data")
    public ResponseEntity<?> getAllCaloriesData() {
        List<CaloriesData> data = caloriesDataService.getAllCaloriesData();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCaloriesDataById(@PathVariable Long id) {
        Optional<CaloriesData> data = caloriesDataService.getCaloriesDataById(id);

        if (data.isPresent()) {
            return ResponseEntity.ok(data.get());
        }

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Food entry not found.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCaloriesData(@PathVariable Long id, @RequestBody CaloriesData caloriesData, @RequestParam String username) {
        if (!userService.doesUserExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with username " + username + " does not exist.");
        }

        CaloriesData existingData = caloriesDataService.getCaloriesDataById(id).orElse(null);
        if (existingData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food entry not found.");
        }

        caloriesData.setId(id);
        caloriesData.setUsername(username);
        if (caloriesData.getDateTime() == null) {
            caloriesData.setDateTime(existingData.getDateTime());
        }

        CaloriesData updatedData = caloriesDataService.updateCaloriesData(id, caloriesData);
        return ResponseEntity.ok(updatedData);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCaloriesData(@PathVariable Long id, @RequestParam String username) {
        if (!userService.doesUserExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with username " + username + " does not exist.");
        }
        CaloriesData existingData = caloriesDataService.getCaloriesDataById(id).orElse(null);
        if (existingData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food entry not found.");
        }

        caloriesDataService.deleteCaloriesDataById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Food entry deleted successfully.");
    }

    @GetMapping("/user/{username}/exceeding-2500")
    public ResponseEntity<?> getDaysExceeding2500(@PathVariable String username) {
        if (!userService.doesUserExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with username " + username + " does not exist.");
        }
        Map<LocalDate, Integer> exceedingDays = caloriesDataService.getDaysExceeding2500Calories(username);
        if (exceedingDays.isEmpty()) {
            return ResponseEntity.ok(Map.of("status", HttpStatus.NOT_FOUND.value(), "message", "No days exceeding 2500 calories found for user: " + username));
        }
        return ResponseEntity.ok(exceedingDays);
    }

    @GetMapping("/user/{username}/spendings-exceeding-1000")
    public ResponseEntity<?> getSpendingsExceeding1000(@PathVariable String username) {
        if (!userService.doesUserExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with username " + username + " does not exist.");
        }
        Map<YearMonth, Integer> exceedingSpendings = caloriesDataService.getSpendingsExceeding1000(username);
        if (exceedingSpendings.isEmpty()) {
            return ResponseEntity.ok().body(Map.of("status", HttpStatus.NOT_FOUND.value(), "message", "No months exceeding 1000 spendings found for user: " + username));
        }
        return ResponseEntity.ok(exceedingSpendings);
    }

    @GetMapping("/user/{username}/total-calories-week")
    public ResponseEntity<?> getTotalCaloriesPerDayForWeek(@PathVariable String username) {
        if (!userService.doesUserExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with username " + username + " does not exist.");
        }

        Map<LocalDate, Integer> totalCaloriesForWeek = caloriesDataService.getTotalCaloriesPerDayForWeek(username);

        return ResponseEntity.ok(totalCaloriesForWeek);
    }

    @GetMapping("/user/{username}/exceed-calorie-threshold-total")
    public ResponseEntity<?> getExceedingDaysCountTotal(@PathVariable String username) {
        if (!userService.doesUserExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with username " + username + " does not exist.");
        }
        int exceedingDaysCount = caloriesDataService.countDaysExceedingThresholdTotal(username);
        return ResponseEntity.ok(exceedingDaysCount);
    }

    @GetMapping("/user/{username}/total-expenditure-week")
    public ResponseEntity<?> getTotalExpenditureForWeek(@PathVariable String username) {
        if (!userService.doesUserExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with username " + username + " does not exist.");
        }
        int totalExpenditure = caloriesDataService.calculateTotalExpenditureForWeek(username);

        return ResponseEntity.ok(totalExpenditure);
    }

    @GetMapping("/user/{username}/filter-calories-data")
    public ResponseEntity<?> getCaloriesDataByDate(@PathVariable String username, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        if (!userService.doesUserExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with username " + username + " does not exist.");
        }
        try {
            List<CaloriesData> caloriesData = caloriesDataService.filterCaloriesDataByDateRange(username, fromDate, toDate);
            return ResponseEntity.ok(caloriesData);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/admin/report")
    public ResponseEntity<?> getAdminReport() {
        try {
            Map<String, Object> report = caloriesDataService.getAdminReport();
            return ResponseEntity.ok(report);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while generating the report: " + ex.getMessage());
        }
    }
}
