package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import lombok.Value;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

@Value
public class PaymentFields {
    private SelenideElement cardNumber = $("[placeholder=\"0000 0000 0000 0000\"]");
    private SelenideElement month = $("[placeholder=\"08\"]");
    private SelenideElement year = $("[placeholder=\"22\"]");
    private SelenideElement holderName = $(byText("Владелец")).parent().$(".input__control");
    private SelenideElement cvv = $("[placeholder=\"999\"]");
    private SelenideElement continueButton = $(withText("Продолжить"));
    private SelenideElement successNotification = $(withText("Операция одобрена"));
    private SelenideElement failureNotification = $(withText("Ошибка"));
    private SelenideElement incorrectCardExpirationDate = $(withText("Неверно указан срок действия карты"));
    private SelenideElement cardHasExpired = $(withText("Истёк срок действия карты"));
    private SelenideElement incorrectOwnerName = $(withText("Неверно указан владелец карты"));
    private SelenideElement incorrectFormat = $(withText("Неверный формат"));
    private SelenideElement needToFillInfo = $(withText("Поле обязательно для заполнения"));
}
