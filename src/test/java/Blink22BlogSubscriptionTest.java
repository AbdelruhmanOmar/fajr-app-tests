import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;


public class Blink22BlogSubscriptionTest {
    WebDriver driver;
    WebDriverWait wait;

    public void setup(){
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.manage().window().maximize();
    }

    public void reloadPage() {
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form")));
    }

    private WebElement getForm() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form")));
    }

    @BeforeClass
    public void navigate() {
        setup();
        driver.get("https://www.blink22.com/");
        driver.findElement(By.linkText("Blog")).click();
    }

    @Test(priority = 1)
    public void extractPlaceholders() {
        WebElement form =getForm();
        List<WebElement> inputs = form.findElements(By.tagName("input"));
        for (WebElement field : inputs) {
            String placeholder = field.getAttribute("placeholder");
            System.out.println("Field placeholder: " + placeholder);
            Assert.assertNotNull(placeholder, "Placeholder should not be null");
        }

    }

    @Test(priority = 2)
    public void validateMissingFieldsSubmission() {

        WebElement form =getForm();
        WebElement submitBtn = form.findElement(By.className("_submit"));
        submitBtn.click();
        WebElement errorMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("_error-inner"))
        );
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message should appear when fields are missing");
        System.out.println("Missing field error: " + errorMsg.getText());
        reloadPage();
    }


    @Test(priority = 3)
    public void validateInvalidEmail() {
        WebElement form =getForm();
        WebElement nameField = form.findElement(By.id("fullname"));
        WebElement emailField = form.findElement(By.id("email"));
        WebElement submitBtn = form.findElement(By.className("_submit"));
        nameField.sendKeys("Test User");
        emailField.sendKeys("testuserom");
        submitBtn.click();
        WebElement emailError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("_error-inner"))
        );
        Assert.assertTrue(emailError.isDisplayed(), "Error message should appear for invalid email format");
        System.out.println("Invalid email error: " + emailError.getText());
    }
    @Test(priority = 4)
    public void validateSuccessfulSubmission() {
        WebElement form =getForm();
        WebElement nameField = form.findElement(By.id("fullname"));
        WebElement emailField = form.findElement(By.id("email"));
        WebElement submitBtn = form.findElement(By.id("_form_5_submit"));
        nameField.clear();
        nameField.sendKeys("Test User");
        emailField.clear();
        emailField.sendKeys("testuser@example.com");
        submitBtn.click();
        WebElement thanksMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Thanks')]"))
        );
        Assert.assertTrue(thanksMsg.isDisplayed(), "Thanks message should appear after valid submission");
        System.out.println("Success Message: " + thanksMsg.getText());
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
