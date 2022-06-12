package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPageV2;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.getFirstCardNumber;
import static ru.netology.web.data.DataHelper.getSecondCardNumber;
import static ru.netology.web.page.DashboardPage.clickFirstCardButton;
import static ru.netology.web.page.DashboardPage.clickSecondCardButton;

class MoneyTransferTest {

    @BeforeAll
    static void openPage() {
        Configuration.holdBrowserOpen=true;
        open("http://localhost:9999");
        val loginPage = new LoginPageV2();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyFromFirstCardToSecondCard() {
        val dashboardPage = new DashboardPage();
        int amount = 200;
        val secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        val firstCardBalanceStart = dashboardPage.getFirstCardBalance();


        val moneyTransfer = clickSecondCardButton();
        moneyTransfer.moneyTransfer(amount, getFirstCardNumber());
        val secondCardBalanceResult = secondCardBalanceStart + amount;
        val firstCardBalanceResult = firstCardBalanceStart - amount;


        assertEquals(firstCardBalanceResult, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceResult, dashboardPage.getSecondCardBalance());
    }

    @Test
    void shouldTransferMoneyFromSecondCardToFirstCard() {
        val dashboardPage = new DashboardPage();
        int amount = 50;
        val firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        val secondCardBalanceStart = dashboardPage.getSecondCardBalance();

        val moneyTransfer = clickFirstCardButton();
        moneyTransfer.moneyTransfer(amount, getSecondCardNumber());
        val firstCardBalanceResult = firstCardBalanceStart + amount;
        val secondCardBalanceResult = secondCardBalanceStart - amount;

        assertEquals(firstCardBalanceResult, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceResult, dashboardPage.getSecondCardBalance());
    }

    @Test
    void shouldBeErrorWhenTransferMoneyMoreThanBalance() {
        int amount = 15300;
        val moneyTransfer = clickSecondCardButton();
        moneyTransfer.moneyTransfer(amount, getFirstCardNumber());
        moneyTransfer.getErrorLimit();

    }
}
