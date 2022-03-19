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
        DataHelper dataHelper = new DataHelper();
        dataHelper.cleanDataFromTable();
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void checkForCorrectness() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        debitCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        debitCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        debitCardPaymentPage.clickContinueButton();
        debitCardPaymentPage.checkSuccessNotification();
        String actual = DataHelper.getStatusFromDb().getStatus();
        String expected = "APPROVED";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void checkForCorrectnessCreditCard() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        CreditCardPaymentPage creditCardPaymentPage = new CreditCardPaymentPage();
        creditCardPaymentPage.setValueInCardNumberField(DataHelper.getApprovedCard());
        creditCardPaymentPage.setValueInMonthField(DataHelper.getRandomValidDate("MM"));
        creditCardPaymentPage.setValueInYearField(DataHelper.getRandomValidDate("yy"));
        creditCardPaymentPage.setValueInHolderNameField(DataHelper.getRandomHolderName());
        creditCardPaymentPage.setValueInCvvField(DataHelper.getRandomCvc());
        creditCardPaymentPage.clickContinueButton();
        creditCardPaymentPage.checkSuccessNotification();
        String actual = DataHelper.getStatusFromDb().getStatus();
        String expected = "APPROVED";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldBuyTourUsingDebitCard() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getSuccessNotification().shouldBe(appear, Duration.ofSeconds(20));
        String actual = DataHelper.getStatusFromDb().getStatus();
        String expected = "APPROVED";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldBuyTourUsingCreditCard() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getSuccessNotification().shouldBe(appear, Duration.ofSeconds(20));
        String actual = DataHelper.getStatusFromDb().getStatus();
        String expected = "APPROVED";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldNotAcceptDeclinedDebitCard() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getDeclinedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getFailureNotification().shouldBe(appear, Duration.ofSeconds(20));
        String actual = DataHelper.getStatusFromDb().getStatus();
        String expected = "DECLINED";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldNotAcceptDeclinedCreditCard() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getDeclinedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getFailureNotification().shouldBe(appear, Duration.ofSeconds(20));
        String actual = DataHelper.getStatusFromDb().getStatus();
        String expected = "DECLINED";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithMonthLessThanMin() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue("00");
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectCardExpirationDate().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithMonthLessThanMin() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue("00");
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectCardExpirationDate().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithMonthMoreThanMax() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue("13");
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectCardExpirationDate().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithMonthMoreThanMax() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue("13");
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectCardExpirationDate().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithYearInPast() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue("21");
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getCardHasExpired().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithYearInPast() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue("21");
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getCardHasExpired().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithYearInFuture() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue("28");
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectCardExpirationDate().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithYearInFuture() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue("28");
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectCardExpirationDate().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithNumbersInOwnerField() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue("123");
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectHolderName().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithNumbersInOwnerField() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue("123");
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectHolderName().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithSpecialCharactersInOwnerField() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue("!!!");
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectHolderName().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithSpecialCharactersInOwnerField() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue("!!!");
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectHolderName().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithLongNameInOwnerField() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue("odpzlxasfpzbldwokdcqxwsptfdvmlinyvvymxwoubkocjfzgqgvgrbuslchgfjfiapjwnvytfpfxwcdckbrkatpsfpqzqauadcx");
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectHolderName().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithLongNameInOwnerField() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue("odpzlxasfpzbldwokdcqxwsptfdvmlinyvvymxwoubkocjfzgqgvgrbuslchgfjfiapjwnvytfpfxwcdckbrkatpsfpqzqauadcx");
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectHolderName().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithShortNameInOwnerField() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue("F");
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectHolderName().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithShortNameInOwnerField() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue("F");
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectHolderName().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithCyrillicCharactersInOwnerField() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue("Вася");
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectHolderName().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithCyrillicCharactersInOwnerField() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue("Вася");
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectHolderName().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithInvalidCVC() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue("01");
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectFormat().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithInvalidCVC() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue("01");
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectFormat().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithEmptyCardNumber() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue("");
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectFormat().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithEmptyCardNumber() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue("");
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectFormat().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithEmptyMonth() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue("");
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectFormat().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithEmptyMonth() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue("");
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectFormat().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithEmptyYear() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue("");
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectFormat().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithEmptyYear() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue("");
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectFormat().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithEmptyOwner() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue("");
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getNeedToFillInfo().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithEmptyOwner() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue("");
        debitCardPaymentPage.getCvvField().setValue(DataHelper.getRandomCvc());
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getNeedToFillInfo().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingDebitCardWithEmptyCvc() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingDebitCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue("");
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectFormat().shouldBe(appear);
    }

    @Test
    void shouldNotBuyTourUsingCreditCardWithEmptyCvc() {
        MainPage mainPage = new MainPage();
        mainPage.payUsingCreditCard();
        DebitCardPaymentPage debitCardPaymentPage = new DebitCardPaymentPage();
        debitCardPaymentPage.getCardNumberField().setValue(DataHelper.getApprovedCard());
        debitCardPaymentPage.getMonthField().setValue(DataHelper.getRandomValidDate("MM"));
        debitCardPaymentPage.getYearField().setValue(DataHelper.getRandomValidDate("yy"));
        debitCardPaymentPage.getHolderNameField().setValue(DataHelper.getRandomHolderName());
        debitCardPaymentPage.getCvvField().setValue("");
        debitCardPaymentPage.getContinueButton().click();
        debitCardPaymentPage.getIncorrectFormat().shouldBe(appear);
    }
}