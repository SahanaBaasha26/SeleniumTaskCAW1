package initialScreening;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class TableAutomation {

    private WebDriver driver;

    @BeforeTest
    public void setup() {
        // open my browser
        driver = new ChromeDriver();

        // Window max
        driver.manage().window().maximize();

        // Allow some time for the table to be refreshed
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

        // Navigate to the URL
        driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
    }

    @Test
    public void testTableAutomation() throws InterruptedException {
        // Step 2: Click on the Table Data button
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement tableData = driver.findElement(By.xpath("//summary[text()='Table Data']"));
        js.executeScript("arguments[0].click();", tableData);

        // Step 3: Insert data and click on Refresh Table button
        String jsonData1 = "[{\"name\":\"Bob\",\"age\":20,\"gender\":\"male\"},"
                + "{\"name\":\"George\",\"age\":42,\"gender\":\"male\"},"
                + "{\"name\":\"Sara\",\"age\":42,\"gender\":\"female\"},"
                + "{\"name\":\"Conor\",\"age\":40,\"gender\":\"male\"},"
                + "{\"name\":\"Jennifer\",\"age\":42,\"gender\":\"female\"}]";

        WebElement inputTextBox = driver.findElement(By.id("jsondata"));
        inputTextBox.clear();
        inputTextBox.sendKeys(jsonData1);

        WebElement refreshButton = driver.findElement(By.xpath("//button[text()='Refresh Table']"));
        refreshButton.click();

        // Wait for the table to be refreshed
       // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='dynamictable']/tbody/tr")));

        // Assert the data in the table
        List<WebElement> tableRows = driver.findElements(By.xpath("//table[@id='dynamictable']/tbody/tr"));

        // Parse JSON data
        JSONArray jsonDataArray = new JSONArray(jsonData1);

        for (int i = 0; i < tableRows.size(); i++) {
            List<WebElement> cells = tableRows.get(i).findElements(By.tagName("td"));

            JSONObject row = jsonDataArray.getJSONObject(i);

            Assert.assertEquals(cells.get(0).getText(), row.getString("name"));
            Assert.assertEquals(Integer.parseInt(cells.get(1).getText()), row.getInt("age"));
            Assert.assertEquals(cells.get(2).getText(), row.getString("gender"));
        }
    }

    @AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}