// Thêm 2 dòng import này ở đầu file
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public static WebDriver createDriver(String browser) {
    if (browser == null) {
        browser = "chrome";
    }
    
    switch (browser.toLowerCase()) {
        case "firefox":
            WebDriverManager.firefoxdriver().setup();
            return new FirefoxDriver();
        case "edge":
            WebDriverManager.edgedriver().setup();
            return new EdgeDriver();
        case "chrome":
        default:
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver();
    }
}
