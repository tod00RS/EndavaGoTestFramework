package tests;

import com.endava.appium.framework.screens.AccountSelectorScreen;
import com.endava.appium.framework.screens.SignInNewAccountMicrosoft;
import com.endava.appium.framework.screens.WelcomeScreen;
import com.endava.appium.framework.util.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class EndavaGoAccountSelectorTest extends BaseTest {

    AccountSelectorScreen accountSelectorScreen;

    @Parameters({"email"})
    @Test (priority = 1)
    public void addingNewAccount(String emailAddress) {
        accountSelectorScreen = new WelcomeScreen().signInWithMicrosoft();
        if (!accountSelectorScreen.isUserAccAvailableInList(emailAddress)) {
            SignInNewAccountMicrosoft signInNewAccountMicrosoft = accountSelectorScreen.addNewAccount(emailAddress);
            Assert.assertTrue(signInNewAccountMicrosoft.getEmailAddress(emailAddress).equalsIgnoreCase(emailAddress), "Failed to add new account!");
            accountSelectorScreen.pressBackButton();
        } else {
            log.info("User already exists in list.");
        }
    }

    @Parameters({"email"})
    @Test (priority = 2)
    public void signOutFromAccount(String emailAddress) {
//        accountSelectorScreen = new WelcomeScreen().signInWithMicrosoft();
        if (accountSelectorScreen.isUserAccAvailableInList(emailAddress)) {
            accountSelectorScreen.openAccOptionTab(emailAddress);
            Assert.assertTrue(accountSelectorScreen.isAccTabOpened(), "Account tab is not opened!");
        } else {
            log.info("User does not exist in list!");
        }
    }





}
