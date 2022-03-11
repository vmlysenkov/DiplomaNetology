package ru.netology.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.PaymentFields;
import ru.netology.page.PaymentMethod;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodTest {
    @BeforeAll
    static void shouldOpenPage() {
        open("http://localhost:8080/");
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
    }
}