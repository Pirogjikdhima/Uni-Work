package inxh.softi.webprojekt.detyrekursi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "calories_data")
public class CaloriesData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "food_name", nullable = false, length = 100)
    @NotBlank(message = "Food name is required")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Food name must contain only letters and spaces")
    private String foodName;

    @Column(name = "calories", nullable = false)
    @Max(value = 99999999, message = "Calories can be a maximum of 8 digits")
    @Digits(integer = 8, fraction = 0, message = "Calories must be a numeric value with a maximum of 8 digits")
    private int calories;

    @Column(name = "price", nullable = false)
    @Max(value = 99999999, message = "Price can be a maximum of 8 digits")
    @Digits(integer = 8, fraction = 0, message = "Price must be a numeric value with a maximum of 8 digits")
    private int price;

    public CaloriesData() {
    }

    public CaloriesData(String username, LocalDateTime dateTime, String foodName, int calories, int price) {
        this.username = username;
        this.dateTime = dateTime;
        this.foodName = foodName;
        this.calories = calories;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
