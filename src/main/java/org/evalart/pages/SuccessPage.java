package org.evalart.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SuccessPage extends BasePage {

    @FindBy(css = "p.text-white")
    private WebElement successText;

    public SuccessPage(WebDriver webDriver) {
        super(webDriver);
    }

    public boolean isDisplayedSuccessText() {
        return successText.isDisplayed();
    }
}
