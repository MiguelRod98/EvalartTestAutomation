package org.evalart.pages;

import lombok.Data;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Data
public class BasePage {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor javascriptExecutor;

    public BasePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        wait = new WebDriverWait(webDriver, 15, 3000);
        javascriptExecutor = (JavascriptExecutor) webDriver;
        driver = webDriver;
    }

    public void waitForElementInvisibility(WebElement element) {
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public void waitForElementVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void type(WebElement element, String text) {
        waitForElementVisible(element);
        element.sendKeys(text);
    }

    public void scrollToElement(WebElement element) {
        javascriptExecutor.executeScript("arguments[0].scrollIntoView()", element);
    }

    public void clickJS(WebElement element) {
        waitForElementClickable(element);
        javascriptExecutor.executeScript("arguments[0].click();", element);
    }
}
