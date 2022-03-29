package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
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
    public static DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yy");

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

    public static String getZeroMonth() {
        return "00";
    }

    public static String getZeroYear() {
        return "00";
    }

    public static String getZeroCardValue() {
        return "0000 0000 0000 0000";
    }

    public static String getZeroCvc() {
        return "000";
    }

    public static String get13Month() {
        return "13";
    }

    public static String getYearBeforeCurrent() {
        int year = faker.number().numberBetween(2000, LocalDate.now().getYear());
        LocalDate randomYear = LocalDate.now().withYear(year);
        String yearInFormat = randomYear.format(yearFormatter);
        System.out.println(yearInFormat);
        return yearInFormat;
    }

    public static String getYearInFuture() {
        int year = faker.number().numberBetween(LocalDate.now().getYear() + 6, 2100);
        LocalDate randomYear = LocalDate.now().withYear(year);
        String yearInFormat = randomYear.format(yearFormatter);
        System.out.println(yearInFormat);
        return yearInFormat;
    }

    public static String getNumber() {
        return faker.number().digits(3);
    }

    public static String getSpecCharacter() {
        return faker.regexify("[^A-Za-z0-9]{3}");
    }

    public static String getLongWord() {
        return faker.regexify("[A-Za-z]{100}");
    }

    public static String getOneLetter() {
        return faker.regexify("[A-Z]");
    }

    public static String getCyrillicName() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.name().firstName();
    }

    public static String getInvalidCvc() {
        return String.valueOf(faker.number().numberBetween(0, 99));
    }


    @Value
    public static class StatusFromDb {
        private String status;
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(System.getProperty("db.url"), "app", "pass");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StatusFromDb getStatusOfDebitCardFromDb() {
        String statusSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1;";
        var runner = new QueryRunner();
        try (var conn = getConnection()) {
            String status = runner.query(conn, statusSQL, new ScalarHandler<String>());
            return new StatusFromDb(status);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static StatusFromDb getStatusOfCreditCardFromDb() {
        String statusSQL = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
        var runner = new QueryRunner();
        try (var conn = getConnection()) {
            String status = runner.query(conn, statusSQL, new ScalarHandler<String>());
            return new StatusFromDb(status);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static void cleanDataFromTable() {
        String clearCodesFromPaymentEntity = "DELETE FROM payment_entity;";
        String clearCodesFromCreditEntity = "DELETE FROM credit_request_entity;";
        String clearCodes = "DELETE FROM order_entity;";
        var runner = new QueryRunner();
        try (var conn = getConnection()) {
            runner.update(conn, clearCodesFromPaymentEntity);
            runner.update(conn, clearCodesFromCreditEntity);
            runner.update(conn, clearCodes);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
