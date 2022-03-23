package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.open;

class DebitCardPaymentPageTest {
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
}