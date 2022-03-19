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
import java.util.Random;

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

    public static String getRandomValidDate(String formatPattern) {
        return LocalDate.now().plusMonths(new Random().nextInt(60)).format(DateTimeFormatter.ofPattern(formatPattern));
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
