package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class PaymentMethod {
    private SelenideElement debitCardPayment = $(withText("Купить"));
    private SelenideElement visibleDebitCardPaymentText = $(withText("Оплата по карте"));
    private SelenideElement creditCardPayment = $(withText("Купить в кредит"));
    private SelenideElement visibleCreditCardPaymentText = $(withText("Кредит по данным карты"));

    public PaymentFields payUsingDebitCard() {
        debitCardPayment.click();
        visibleDebitCardPaymentText.shouldBe(visible);
        return new PaymentFields();
    }

    public PaymentFields payUsingCreditCard() {
        creditCardPayment.click();
        visibleCreditCardPaymentText.shouldBe(visible);
        return new PaymentFields();
    }
}
