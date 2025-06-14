package inxh.softi.webprojekt.detyrekursi.repository;

import inxh.softi.webprojekt.detyrekursi.entity.CaloriesData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CaloriesDataRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CaloriesDataRepository caloriesDataRepository;

    private CaloriesData sampleData1;
    private CaloriesData sampleData2;
    private CaloriesData sampleData3;

    @BeforeEach
    void setUp() {
        sampleData1 = new CaloriesData();
        sampleData1.setUsername("user1");
        sampleData1.setDateTime(LocalDateTime.now());
        sampleData1.setFoodName("Pizza");
        sampleData1.setCalories(800);
        sampleData1.setPrice(15);

        sampleData2 = new CaloriesData();
        sampleData2.setUsername("user1");
        sampleData2.setDateTime(LocalDateTime.now().minusDays(1));
        sampleData2.setFoodName("Salad");
        sampleData2.setCalories(300);
        sampleData2.setPrice(10);

        sampleData3 = new CaloriesData();
        sampleData3.setUsername("user2");
        sampleData3.setDateTime(LocalDateTime.now());
        sampleData3.setFoodName("Burger");
        sampleData3.setCalories(1000);
        sampleData3.setPrice(20);

        entityManager.persist(sampleData1);
        entityManager.persist(sampleData2);
        entityManager.persist(sampleData3);
        entityManager.flush();
    }

    @Test
    void findByUsername_ShouldReturnCorrectEntries() {

        List<CaloriesData> result = caloriesDataRepository.findByUsername("user1");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(CaloriesData::getUsername)
                .containsOnly("user1");
    }

    @Test
    void findByUsernameAndDateRange_ShouldReturnEntriesWithinRange() {

        LocalDateTime startOfWeek = LocalDateTime.now().minusDays(7);
        LocalDateTime endOfWeek = LocalDateTime.now().plusDays(1);

        List<CaloriesData> result = caloriesDataRepository.findByUsernameAndDateRange(
                "user1", startOfWeek, endOfWeek);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(CaloriesData::getUsername)
                .containsOnly("user1");
    }

    @Test
    void countEntriesByDateRange_ShouldReturnCorrectCount() {

        LocalDateTime startDate = LocalDateTime.now().minusDays(2);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        int count = caloriesDataRepository.countEntriesByDateRange(startDate, endDate);

        assertThat(count).isEqualTo(3);
    }

    @Test
    void findByDateRange_ShouldReturnEntriesWithinRange() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(2);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        List<CaloriesData> result = caloriesDataRepository.findByDateRange(startDate, endDate);

        assertThat(result).hasSize(3);
    }

    @Test
    void findUsersExceedingMonthlyLimit_ShouldReturnUsersAboveLimit() {
        CaloriesData expensiveEntry = new CaloriesData();
        expensiveEntry.setUsername("user2");
        expensiveEntry.setDateTime(LocalDateTime.now());
        expensiveEntry.setFoodName("Expensive Meal");
        expensiveEntry.setCalories(1000);
        expensiveEntry.setPrice(990);

        entityManager.persist(expensiveEntry);
        entityManager.flush();

        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        List<String> result = caloriesDataRepository.findUsersExceedingMonthlyLimit(startDate, endDate);

        assertThat(result).hasSize(1);
        assertThat(result).contains("user2");
    }
}