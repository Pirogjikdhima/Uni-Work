package inxh.softi.webprojekt.detyrekursi.exception;

public class CaloriesDataNotFoundException extends RuntimeException {
    public CaloriesDataNotFoundException(Long id) {
        super("CaloriesData entry with ID " + id + " not found.");
    }
}
