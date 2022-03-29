package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.open;

class CreditCardPaymentPageTest {

    @BeforeEach
    void shouldOpenPage() {
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
        var actual = DataHelper.getStatusOfCreditCardFromDb().getStatus();
        var expected = "APPROVED";
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
        String actual = DataHelper.getStatusOfCreditCardFromDb().getStatus();
        String expected = "DECLINED";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithMonthLessThanMin() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getZeroMonth());
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectCardExpirationDate();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithMonthMoreThanMax() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.get13Month());
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectCardExpirationDate();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithYearInPast() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getYearBeforeCurrent());
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkCardExpiration();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithYearInFuture() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getYearInFuture());
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectCardExpirationDate();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithNumbersInOwnerField() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getNumber());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectHolderName();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithSpecialCharactersInOwnerField() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getSpecCharacter());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectHolderName();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithLongNameInOwnerField() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getLongWord());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectHolderName();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithShortNameInOwnerField() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getOneLetter());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectHolderName();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithCyrillicCharactersInOwnerField() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getCyrillicName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectHolderName();
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithInvalidCVC() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getInvalidCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkIncorrectFormat();
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

    @Test
    void shouldNotBuyTourUsingCreditCardWithZeroYear() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getZeroYear());
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkCardExpiration();
    }

    @Test
    void shouldNotAcceptCreditCardWithZeroValues() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getZeroCardValue());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkFailureNotification();
    }

    @Test
    void shouldNotAcceptCreditCardWithZeroCvc() {
        var mainPage = new MainPage();
        var creditCardPaymentPage = mainPage.payUsingCreditCard();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getZeroCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkFailureNotification();
    }
}