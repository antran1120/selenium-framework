package tests;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    
    @Test(description = "Verify successful login with valid credentials", groups = {"smoke", "regression"})
    public void testLoginSuccess() {
        LoginPage loginPage = new LoginPage(getDriver());
        String user = ConfigReader.getInstance().getUsername();
        String pwd = ConfigReader.getInstance().getPassword();

        loginPage.login(user, pwd);

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isLoaded(), "Trang inventory chưa load");
    }

    @Test(description = "Verify login failure with locked out user", groups = {"regression"})
    public void testLoginLockedOutUser() {
        LoginPage loginPage = new LoginPage(getDriver());
        String pwd = ConfigReader.getInstance().getPassword();
        
        loginPage.loginExpectingFailure("locked_out_user", pwd);

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Không hiển thị thông báo lỗi");
        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Sorry, this user has been locked out.");
    }

    @Test(description = "Verify login failure with invalid password", groups = {"regression"})
    public void testLoginInvalidPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        String user = ConfigReader.getInstance().getUsername();
        
        loginPage.loginExpectingFailure(user, "wrong_password");
        
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Không hiển thị thông báo lỗi");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"));
    }
}
