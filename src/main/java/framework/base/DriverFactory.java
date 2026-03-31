package framework.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverFactory {

    public static WebDriver createDriver(String browser) {
        String gridUrl = System.getProperty("grid.url");
        if (gridUrl != null && !gridUrl.isBlank()) {
            // Chạy trên Selenium Grid
            return createRemoteDriver(browser, gridUrl);
        }
        // Chạy local
        return createLocalDriver(browser);
    }

    private static WebDriver createLocalDriver(String browser) {
        boolean isCI = System.getenv("CI") != null; // GitHub Actions tự set CI=true
        if (browser == null) {
            browser = "chrome";
        }
        switch (browser.toLowerCase()) {
            case "firefox":
                return createFirefoxDriver(isCI);
            case "chrome":
            default:
                return createChromeDriver(isCI);
        }
    }

    private static WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new"); // Chrome 112+ headless mới
            options.addArguments("--no-sandbox"); // BẮT BUỘC trên Linux CI
            options.addArguments("--disable-dev-shm-usage"); // Tránh OOM trên container
            options.addArguments("--window-size=1920,1080"); // Set resolution cố định
            System.out.println("[Driver] Chạy Chrome HEADLESS (CI mode)");
        } else {
            options.addArguments("--start-maximized");
            System.out.println("[Driver] Chạy Chrome bình thường (Local mode)");
        }
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
            System.out.println("[Driver] Chạy Firefox HEADLESS (CI mode)");
        }
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver(options);
    }

    private static WebDriver createRemoteDriver(String browser, String gridUrl) {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName(browser.toLowerCase());

        // Grid 4: Hỗ trợ Chrome Options qua W3C
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
            caps.merge(options);
        }

        try {
            URL gridEndpoint = new URL(gridUrl + "/wd/hub");
            RemoteWebDriver driver = new RemoteWebDriver(gridEndpoint, caps);
            // Tăng timeout khi dùng Grid (mạng có latency)
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            System.out.println("[Grid] Session tạo thành công trên: " + gridUrl
                    + " | Browser: " + browser
                    + " | SessionID: " + driver.getSessionId());
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Grid URL không hợp lệ: " + gridUrl);
        }
    }
}
