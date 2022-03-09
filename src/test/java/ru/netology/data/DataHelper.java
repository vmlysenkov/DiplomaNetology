package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.util.Date;
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

    public static String getRandomMonth() {
        return faker.number().numberBetween(1, 13) + "";
    }
    public static String getRandomYear() {
        LocalDate localDate = LocalDate.now();
        String year = (localDate.getYear() + "").substring(2);
        return faker.number().numberBetween(Integer.parseInt(year), (Integer.parseInt(year) + 6)) + "";
    }
    public static String getRandomHolderName() {
        return faker.name().fullName();
    }
    public static String getRandomCvc() {
        return faker.number().digits(3);
    }
    public static AuthInfo getApprovedCard() {
        return new AuthInfo("4444 4444 4444 4441", getRandomMonth(), getRandomYear(), getRandomHolderName(), getRandomCvc());
    }
    public static AuthInfo getDeclinedCard() {
        return new AuthInfo("4444 4444 4444 4442", getRandomMonth(), getRandomYear(), getRandomHolderName(), getRandomCvc());
    }
}
