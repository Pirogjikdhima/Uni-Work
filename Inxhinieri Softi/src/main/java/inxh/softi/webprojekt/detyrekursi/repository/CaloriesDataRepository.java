package inxh.softi.webprojekt.detyrekursi.repository;

import inxh.softi.webprojekt.detyrekursi.entity.CaloriesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CaloriesDataRepository extends JpaRepository<CaloriesData, Long> {
    List<CaloriesData> findByUsername(String username);

    @Query("SELECT c FROM CaloriesData c WHERE c.username = :username AND c.dateTime BETWEEN :startOfWeek AND :endOfWeek")
    List<CaloriesData> findByUsernameAndDateRange(@Param("username") String username, @Param("startOfWeek") LocalDateTime startOfWeek, @Param("endOfWeek") LocalDateTime endOfWeek);

    @Query("SELECT COUNT(c) FROM CaloriesData c WHERE c.dateTime BETWEEN :startDate AND :endDate")
    int countEntriesByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT c FROM CaloriesData c WHERE c.dateTime BETWEEN :startDate AND :endDate")
    List<CaloriesData> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT c.username FROM CaloriesData c " + "WHERE c.dateTime BETWEEN :startDate AND :endDate " + "GROUP BY c.username " + "HAVING SUM(c.price) > 1000")
    List<String> findUsersExceedingMonthlyLimit(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT DATE(c.dateTime), COUNT(c) FROM CaloriesData c WHERE c.dateTime BETWEEN :startDate AND :endDate GROUP BY DATE(c.dateTime)")
    List<Object[]> countEntriesByDateRangeGroupedByDay(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
