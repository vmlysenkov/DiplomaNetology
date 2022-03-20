package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.CreditCardPaymentPage;
import ru.netology.page.DebitCardPaymentPage;
import ru.netology.page.MainPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.open;

class MainPageTest {
    @BeforeAll
    static void shouldOpenPage() {
        open("http://localhost:8080/");
    }
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterEach
    void shouldCleanData() {
        DataHelper.cleanDataFromTable();
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldBuyTourUsingDebitCard() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkSuccessNotification();
        var actual = DataHelper.getStatusFromDb().getStatus();
        var expected = "APPROVED";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldBuyTourUsingCreditCard() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkSuccessNotification();
        var actual = DataHelper.getStatusFromDb().getStatus();
        var expected = "APPROVED";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldNotAcceptDeclinedDebitCard() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getDeclinedCard());
        debitCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkFailureNotification();
        String actual = DataHelper.getStatusFromDb().getStatus();
        String expected = "DECLINED";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldNotAcceptDeclinedCreditCard() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getDeclinedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkFailureNotification();
        String actual = DataHelper.getStatusFromDb().getStatus();
        String expected = "DECLINED";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithMonthLessThanMin() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField("00");
        debitCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkIncorrectCardExpirationDate();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithMonthLessThanMin() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField("00");
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectCardExpirationDate();
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithMonthMoreThanMax() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField("13");
        debitCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkIncorrectCardExpirationDate();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithMonthMoreThanMax() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField("13");
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectCardExpirationDate();
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithYearInPast() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.setValueInYearField("21");
        debitCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkCardExpiration();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithYearInPast() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField("21");
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkCardExpiration();
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithYearInFuture() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.setValueInYearField("28");
        debitCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkIncorrectCardExpirationDate();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithYearInFuture() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField("28");
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectCardExpirationDate();
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithNumbersInOwnerField() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.setValueInHolderNameField("123");
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkIncorrectHolderName();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithNumbersInOwnerField() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField("123");
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectHolderName();
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithSpecialCharactersInOwnerField() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.setValueInHolderNameField("!!!");
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkIncorrectHolderName();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithSpecialCharactersInOwnerField() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField("!!!");
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectHolderName();
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithLongNameInOwnerField() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.setValueInHolderNameField("odpzlxasfpzbldwokdcqxwsptfdvmlinyvvymxwoubkocjfzgqgvgrbuslchgfjfiapjwnvytfpfxwcdckbrkatpsfpqzqauadcx");
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkIncorrectHolderName();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithLongNameInOwnerField() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField("odpzlxasfpzbldwokdcqxwsptfdvmlinyvvymxwoubkocjfzgqgvgrbuslchgfjfiapjwnvytfpfxwcdckbrkatpsfpqzqauadcx");
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectHolderName();
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithShortNameInOwnerField() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.setValueInHolderNameField("F");
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkIncorrectHolderName();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithShortNameInOwnerField() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField("F");
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectHolderName();
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithCyrillicCharactersInOwnerField() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.setValueInHolderNameField("Вася");
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkIncorrectHolderName();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithCyrillicCharactersInOwnerField() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField("Вася");
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectHolderName();
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithInvalidCVC() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        debitCardPaymentPage.setValueInCvvField("01");
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkIncorrectFormat();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithInvalidCVC() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField("01");
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectFormat();
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithEmptyCardNumber() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField("");
        debitCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkIncorrectFormat();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithEmptyCardNumber() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField("");
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectFormat();
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithEmptyMonth() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField("");
        debitCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkIncorrectFormat();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithEmptyMonth() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField("");
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectFormat();
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithEmptyYear() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.setValueInYearField("");
        debitCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkIncorrectFormat();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithEmptyYear() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField("");
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectFormat();
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithEmptyOwner() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.setValueInHolderNameField("");
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkFieldFulfillmentObligation();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithEmptyOwner() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField("");
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkFieldFulfillmentObligation();
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithEmptyCvc() {
        var mainPage = new MainPage();
        var debitCardPaymentPage = mainPage.payUsingDebitCard();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        debitCardPaymentPage.setValueInCvvField("");
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkIncorrectFormat();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithEmptyCvc() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField("");
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectFormat();
    }
}