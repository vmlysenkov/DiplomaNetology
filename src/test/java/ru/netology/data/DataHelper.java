package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    @Value
    public static class AuthInfo {
        private String cardNumber;
        private String month;
        private String year;
        private String owner;
        private String cvv;
    }

    private static final Faker faker = new Faker(new Locale("en"));
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");

    public static LocalDate getRandomValidDate() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = Integer.parseInt(currentDate.format(formatter).substring(3, 5));
        int currentYear = Integer.parseInt(currentDate.format(formatter).substring(6));
        int randomMonth, randomYear;
        do {
            randomMonth = faker.number().numberBetween(1, 13);
            randomYear = faker.number().numberBetween(currentYear, (currentYear + 5));
        } while (randomMonth <= currentMonth && randomYear <= currentYear);
        LocalDate randomDate = LocalDate.of(randomYear, randomMonth, 1);
        return randomDate;
    }

    public static String getRandomMonth(LocalDate randomDate) {
        return randomDate.format(formatter).substring(3, 5);
    }

    public static String getRandomYear(LocalDate randomDate) {
        return randomDate.format(formatter).substring(6);
    }

    public static String getRandomHolderName() {
        return faker.name().fullName();
    }

    public static String getRandomCvc() {
        return faker.number().digits(3);
    }

    public static String getApprovedCard() {
        return "4444 4444 4444 4441";
    }

    public static String getDeclinedCard() {
        return "4444 4444 4444 4442";
    }

    @Value
    public static class StatusFromDb {
        private String status;
    }

    public static StatusFromDb getStatusFromDb() {
        String statusSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1;";
        var runner = new QueryRunner();
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app"
                , "pass")) {
            String status = runner.query(conn, statusSQL, new ScalarHandler<String>());
            return new StatusFromDb(status);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void cleanDataFromTable() {
        String clearCodes = "DELETE FROM payment_entity;";
        var runner = new QueryRunner();
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app"
                , "pass")) {
            var status = runner.update(conn, clearCodes);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
