package framework.utils;

import framework.config.ConfigReader;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    public static String capture(WebDriver driver, String testName) {
        if (driver == null) return "";
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String dest = ConfigReader.getInstance().getScreenshotPath() + testName + "_" + timestamp + ".png";
            File destination = new File(dest);
            FileHandler.copy(source, destination);
            System.out.println("Screenshot captured at: " + destination.getAbsolutePath());
            return destination.getAbsolutePath();
        } catch (IOException e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
            return "";
        }
    }

    public static byte[] captureAsBytes(WebDriver driver) {
        if (driver == null) return new byte[0];
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
