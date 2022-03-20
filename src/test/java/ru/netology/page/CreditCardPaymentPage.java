package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import lombok.Value;

import java.time.Duration;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class CreditCardPaymentPage {
    private SelenideElement cardNumberField = $("[placeholder=\"0000 0000 0000 0000\"]");
    private SelenideElement monthField = $("[placeholder=\"08\"]");
    private SelenideElement yearField = $("[placeholder=\"22\"]");
    private SelenideElement holderNameField = $(byText("Владелец")).parent().$(".input__control");
    private SelenideElement cvvField = $("[placeholder=\"999\"]");
    private SelenideElement continueButton = $(withText("Продолжить"));
    private SelenideElement successNotification = $(withText("Операция одобрена"));
    private SelenideElement failureNotification = $(withText("Ошибка"));
    private SelenideElement incorrectCardExpirationDate = $(withText("Неверно указан срок действия карты"));
    private SelenideElement cardHasExpired = $(withText("Истёк срок действия карты"));
    private SelenideElement incorrectHolderName = $(withText("Неверно указан владелец карты"));
    private SelenideElement incorrectFormat = $(withText("Неверный формат"));
    private SelenideElement needToFillInfo = $(withText("Поле обязательно для заполнения"));

    public void setValueInCardNumberField(String cardNumber) {
        cardNumberField.setValue(cardNumber);
    }

    public void setValueInMonthField(String month) {
        monthField.setValue(month);
    }

    public void setValueInYearField(String year) {
        yearField.setValue(year);
    }

    public void setValueInHolderNameField(String holderName) {
        holderNameField.setValue(holderName);
    }

    public void setValueInCvvField(String cvv) {
        cvvField.setValue(cvv);
    }

    public void clickContinueButton() {
        continueButton.click();
    }

    public void checkSuccessNotification() {
        successNotification.shouldBe(appear, Duration.ofSeconds(20));
    }

    public void checkFailureNotification() {
        failureNotification.shouldBe(appear, Duration.ofSeconds(20));
    }

    public void checkIncorrectCardExpirationDate() {
        incorrectCardExpirationDate.shouldBe(appear);
    }

    public void checkCardExpiration() {
        cardHasExpired.shouldBe(appear);
    }

    public void checkIncorrectHolderName() {
        incorrectHolderName.shouldBe(appear);
    }

    public void checkIncorrectFormat() {
        incorrectFormat.shouldBe(appear);
    }

    public void checkFieldFulfillmentObligation() {
        needToFillInfo.shouldBe(appear);
    }
}
