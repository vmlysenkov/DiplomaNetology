package ru.netology.data;

import lombok.Value;

public class DataHelper {
    @Value
    public static class AuthInfo {
        private String cardNumber;
        private String month;
        private String year;
        private String owner;
        private String cvv;
    }
}
