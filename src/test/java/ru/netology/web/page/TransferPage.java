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

    public DashboardPage validTransfer(String amount, DataHelper.CardInfo fromCard) {
        transfer(amount, fromCard.getNumber());
        return new DashboardPage();
    }

    public void transfer(String amount, String fromCard) {
        amountField.setValue(amount);
        fromField.setValue(fromCard);
        transferButton.click();
    }

    public void emptyFieldError(String expectedText) {
        errorNotification.should(Condition.visible).shouldHave(Condition.text(expectedText));
    }
}
