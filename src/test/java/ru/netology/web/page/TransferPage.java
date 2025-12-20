package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private SelenideElement amountField = $("[data-test-id='amount'] input");
    private SelenideElement fromField = $("[data-test-id='from'] input");
    private SelenideElement toField = $("[data-test-id='to'] input");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public TransferPage() {
        heading.shouldBe(visible);
        amountField.shouldBe(visible);
        fromField.shouldBe(visible);
        toField.shouldBe(visible);
        cancelButton.shouldBe(visible);
    }

    // для целей тестирования я разбил операцию перевода денег на несколько методов,
    // чтобы сохранить инкапсуляцию и иметь возможность использовать в тесте
    // приватные поля этого класса через его методы

//    public void setAmount(int amount) {
//        amountField.setValue(String.valueOf(amount));
//    }
//
//    public void setFromCard(DataHelper.CardInfo fromCard) {
//        fromField.setValue(fromCard.getNumber());
//    }
//
//    public DashboardPage transfer() {
//        transferButton.click();
//        return new DashboardPage();
//    }
//
//    public DashboardPage cancelTransfer() {
//        cancelButton.click();
//        return new DashboardPage();
//    }

    public DashboardPage transfer(int amount, DataHelper.CardInfo fromCard) {
        amountField.setValue(String.valueOf(amount));
        fromField.setValue(fromCard.getNumber());
        transferButton.click();
        return new DashboardPage();
    }

    public DashboardPage emptyFromFieldError (int amount, String fromCard) {
        amountField.setValue(String.valueOf(amount));
        fromField.setValue((String) null);
        transferButton.click();
        return null;
    }

    public DashboardPage emptyAmountFieldError (String amount, DataHelper.CardInfo fromCard) {
        amountField.setValue((String) null);
        fromField.setValue(fromCard.getNumber());
        transferButton.click();
        errorNotification.shouldBe(visible);
        return null;
    }

    public boolean emptyFieldError () {
        return errorNotification.is(visible);
    }
}
