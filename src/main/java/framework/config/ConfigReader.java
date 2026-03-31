package framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = new Properties();
    private static ConfigReader instance;

    private ConfigReader() {
        String env = System.getProperty("env", "dev");
        String file = "src/test/resources/config-" + env + ".properties";
        try (FileInputStream fis = new FileInputStream(file)) {
            props.load(fis);
            System.out.println("[ConfigReader] Đang dùng môi trường: " + env);
        } catch (IOException e) {
            throw new RuntimeException("Không tìm thấy config: " + file);
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) instance = new ConfigReader();
        return instance;
    }

    public String getBaseUrl() { 
        return props.getProperty("base.url"); 
    }
    
    public String getBrowser() { 
        return props.getProperty("browser", "chrome"); 
    }
    
    public int getExplicitWait() { 
        return Integer.parseInt(props.getProperty("explicit.wait","15")); 
    }
    
    public int getImplicitWait() { 
        return Integer.parseInt(props.getProperty("implicit.wait","5")); 
    }
    
    public int getRetryCount() { 
        return Integer.parseInt(props.getProperty("retry.count","1")); 
    }
    
    public String getScreenshotPath() { 
        return props.getProperty("screenshot.path", "target/screenshots/"); 
    }

    public String getUsername() {
        String username = System.getenv("APP_USERNAME");
        if (username == null || username.isBlank()) {
            username = props.getProperty("app.username", "standard_user");
        }
        return username;
    }

    public String getPassword() {
        String password = System.getenv("APP_PASSWORD");
        if (password == null || password.isBlank()) {
            password = props.getProperty("app.password", "secret_sauce");
        }
        return password;
    }
}
