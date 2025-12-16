package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {

    @Test
    void shouldTransferFromSecondToFirst() { // должен перевести со второй на первую карту
        var toCard = DataHelper.getFirstCardInfo(); // карта для пополнения
        var fromCard = DataHelper.getSecondCardInfo(); // карта для списания
        int amount = 200; // сумма перевода

        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        var verificationPage = loginPage.validLogin(authInfo);
        var dashBoardPage = verificationPage.validVerify(verificationCode);

        int initialFirstCardBalance = dashBoardPage.getCardBalance(toCard); // проверяем балансы перед операцией
        int initialSecondCardBalance = dashBoardPage.getCardBalance(fromCard);

        var transferPage = dashBoardPage.selectCard(toCard);
        transferPage.setAmount(amount);
        transferPage.setFromCard(fromCard);
        transferPage.transfer();

        int finalFirstCardBalance = dashBoardPage.getCardBalance(toCard); // проверяем балансы после операции
        int finalSecondCardBalance = dashBoardPage.getCardBalance(fromCard);

        // проверяем, отражают ли балансы после операции переведённую сумму
        assertEquals(initialFirstCardBalance + amount, finalFirstCardBalance);
        assertEquals(initialSecondCardBalance - amount, finalSecondCardBalance);
    }

    @Test
    void shouldTransferFromFirstToSecond() { // должен перевести с первой на вторую карту
        var toCard = DataHelper.getSecondCardInfo(); // карта для пополнения
        var fromCard = DataHelper.getFirstCardInfo(); // карта для списания
        int amount = 200; // сумма перевода

        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        var verificationPage = loginPage.validLogin(authInfo);
        var dashBoardPage = verificationPage.validVerify(verificationCode);

        int initialSecondCardBalance = dashBoardPage.getCardBalance(toCard);
        int initialFirstCardBalance = dashBoardPage.getCardBalance(fromCard);

        var transferPage = dashBoardPage.selectCard(toCard);
        transferPage.setAmount(amount);
        transferPage.setFromCard(fromCard);
        transferPage.transfer();

        int finalSecondCardBalance = dashBoardPage.getCardBalance(toCard);
        int finalFirstCardBalance = dashBoardPage.getCardBalance(fromCard);

        assertEquals(initialFirstCardBalance - amount, finalFirstCardBalance);
        assertEquals(initialSecondCardBalance + amount, finalSecondCardBalance);
    }

    @Test
    void shouldDisplayAnErrorWithEmptyFromField() { //должен показать ошибку при пустом поле "Откуда"
        var toCard = DataHelper.getFirstCardInfo(); // карта для пополнения
        int amount = 200; // сумма перевода

        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        var verificationPage = loginPage.validLogin(authInfo);
        var dashBoardPage = verificationPage.validVerify(verificationCode);

        var transferPage = dashBoardPage.selectCard(toCard);
        transferPage.setAmount(amount);
        transferPage.transfer();
        $("[data-test-id='error-notification'] .notification__title")
                .shouldHave(text("Ошибка"))
                .shouldBe(visible);
    }

    @Test
    void shouldDisplayAnErrorWithEmptyAmount() { // должен показать ошибку при пустом поле "Сумма"
        var toCard = DataHelper.getFirstCardInfo(); // карта для пополнения
        var fromCard = DataHelper.getFirstCardInfo(); // карта для списания

        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        var verificationPage = loginPage.validLogin(authInfo);
        var dashBoardPage = verificationPage.validVerify(verificationCode);

        var transferPage = dashBoardPage.selectCard(toCard);
        transferPage.setFromCard(fromCard);
        transferPage.transfer();
        $("[data-test-id='error-notification'] .notification__title")
                .shouldHave(text("Ошибка"))
                .shouldBe(visible);
    }
}
