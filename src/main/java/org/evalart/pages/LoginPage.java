package org.evalart.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(css = "input[name='username']")
    private WebElement usernameInput;

    @FindBy(css = "input[name='password']")
    private WebElement passwordInput;

    @FindBy(css = "button.bg-white")
    private WebElement sendLoginButton;

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
    }

    public ButtonsTestPage fillFormLogin(String username, String password) {

        try {
            type(usernameInput, username);
            type(passwordInput, password);
            clickJS(sendLoginButton);
            waitForElementInvisibility(sendLoginButton);
            return new ButtonsTestPage(getDriver());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
