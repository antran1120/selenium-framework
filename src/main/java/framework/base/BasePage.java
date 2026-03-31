package framework.base;

import framework.config.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage is the foundation class for all Page Objects.
 * Contains common WebDriver operations wrapped with Explicit Waits.
 */
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        int timeout = ConfigReader.getInstance().getExplicitWait();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        PageFactory.initElements(driver, this);
    }

    /**
     * Waits until the element is clickable and then clicks it.
     * Prevents ElementNotInteractableException.
     * @param element The WebElement to click.
     */
    protected void waitAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /**
     * Waits until the element is visible, clears its content and types the provided text.
     * Prevents appending to existing data.
     * @param element The WebElement to type in.
     * @param text The text to enter.
     */
    protected void waitAndType(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Waits for the element to be visible and returns its trimmed text.
     * @param element The WebElement to get text from.
     * @return Trimmed text of the element.
     */
    protected String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText().trim();
    }

    /**
     * Checks if an element is visible based on its locator.
     * Does not throw an exception if element is not found or is stale.
     * @param locator The By locator of the element.
     * @return True if visible, false otherwise.
     */
    protected boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Scrolls the page until the element is in view.
     * Useful for elements outside the current viewport.
     * @param element The WebElement to scroll to.
     */
    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Waits for the entire document page load to complete.
     */
    protected void waitForPageLoad() {
        wait.until(driver1 -> ((JavascriptExecutor) driver1)
                .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Waits for the element to be visible and retrieves the value of the specified attribute.
     * @param element The WebElement.
     * @param attr The attribute name.
     * @return The attribute value.
     */
    protected String getAttribute(WebElement element, String attr) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getAttribute(attr);
    }
}
