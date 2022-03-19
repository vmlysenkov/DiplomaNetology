package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private SelenideElement debitCardPayment = $(withText("Купить"));
    private SelenideElement visibleDebitCardPaymentText = $(withText("Оплата по карте"));
    private SelenideElement creditCardPayment = $(withText("Купить в кредит"));
    private SelenideElement visibleCreditCardPaymentText = $(withText("Кредит по данным карты"));

    public DebitCardPaymentPage payUsingDebitCard() {
        debitCardPayment.click();
        visibleDebitCardPaymentText.shouldBe(visible);
        return new DebitCardPaymentPage();
    }

    public DebitCardPaymentPage payUsingCreditCard() {
        creditCardPayment.click();
        visibleCreditCardPaymentText.shouldBe(visible);
        return new DebitCardPaymentPage();
    }
}
