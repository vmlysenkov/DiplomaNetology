package ru.netology.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.PaymentFields;
import ru.netology.page.PaymentMethod;

import java.time.Duration;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.open;

class PaymentMethodTest {
    @BeforeAll
    static void shouldOpenPage() {
        open("http://localhost:8080/");
    }

    @AfterEach
    void shouldCleanData() {
        DataHelper dataHelper = new DataHelper();
        dataHelper.cleanDataFromTable();
    }

    @Test
    void shouldBuyTourUsingDebitCard() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.payUsingDebitCard();
        PaymentFields paymentFields = new PaymentFields();
        paymentFields.getCardNumber().setValue(DataHelper.getApprovedCard());
        paymentFields.getMonth().setValue(DataHelper.getRandomMonth(DataHelper.getRandomValidDate()));
        paymentFields.getYear().setValue(DataHelper.getRandomYear(DataHelper.getRandomValidDate()));
        paymentFields.getHolderName().setValue(DataHelper.getRandomHolderName());
        paymentFields.getCvv().setValue(DataHelper.getRandomCvc());
        paymentFields.getContinueButton().click();
        paymentFields.getSuccessNotification().shouldBe(appear, Duration.ofSeconds(20));
        String actual = DataHelper.getStatusFromDb().getStatus();
        String expected = "APPROVED";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldBuyTourUsingCreditCard() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.payUsingCreditCard();
        PaymentFields paymentFields = new PaymentFields();
        paymentFields.getCardNumber().setValue(DataHelper.getApprovedCard());
        paymentFields.getMonth().setValue(DataHelper.getRandomMonth(DataHelper.getRandomValidDate()));
        paymentFields.getYear().setValue(DataHelper.getRandomYear(DataHelper.getRandomValidDate()));
        paymentFields.getHolderName().setValue(DataHelper.getRandomHolderName());
        paymentFields.getCvv().setValue(DataHelper.getRandomCvc());
        paymentFields.getContinueButton().click();
        paymentFields.getSuccessNotification().shouldBe(appear, Duration.ofSeconds(20));
        String actual = DataHelper.getStatusFromDb().getStatus();
        String expected = "APPROVED";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldNotAcceptDeclinedDebitCard() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.payUsingDebitCard();
        PaymentFields paymentFields = new PaymentFields();
        paymentFields.getCardNumber().setValue(DataHelper.getDeclinedCard());
        paymentFields.getMonth().setValue(DataHelper.getRandomMonth(DataHelper.getRandomValidDate()));
        paymentFields.getYear().setValue(DataHelper.getRandomYear(DataHelper.getRandomValidDate()));
        paymentFields.getHolderName().setValue(DataHelper.getRandomHolderName());
        paymentFields.getCvv().setValue(DataHelper.getRandomCvc());
        paymentFields.getContinueButton().click();
        paymentFields.getFailureNotification().shouldBe(appear, Duration.ofSeconds(20));
        String actual = DataHelper.getStatusFromDb().getStatus();
        String expected = "DECLINED";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldNotAcceptDeclinedCreditCard() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.payUsingCreditCard();
        PaymentFields paymentFields = new PaymentFields();
        paymentFields.getCardNumber().setValue(DataHelper.getDeclinedCard());
        paymentFields.getMonth().setValue(DataHelper.getRandomMonth(DataHelper.getRandomValidDate()));
        paymentFields.getYear().setValue(DataHelper.getRandomYear(DataHelper.getRandomValidDate()));
        paymentFields.getHolderName().setValue(DataHelper.getRandomHolderName());
        paymentFields.getCvv().setValue(DataHelper.getRandomCvc());
        paymentFields.getContinueButton().click();
        paymentFields.getFailureNotification().shouldBe(appear, Duration.ofSeconds(20));
        String actual = DataHelper.getStatusFromDb().getStatus();
        String expected = "DECLINED";
        Assertions.assertEquals(expected, actual);
    }
}