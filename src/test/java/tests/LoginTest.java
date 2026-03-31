package tests;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    
    @Test(description = "Verify successful login with valid credentials", groups = {"smoke", "regression"})
    @Feature("Đăng nhập")
    @Story("UC-001: Đăng nhập hợp lệ")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Kiểm thử đăng nhập thành công với tài khoản đúng")
    public void testLoginSuccess() {
        LoginPage loginPage = new LoginPage(getDriver());
        String user = ConfigReader.getInstance().getUsername();
        String pwd = ConfigReader.getInstance().getPassword();

        Allure.step("Nhập thông tin đăng nhập", () -> {
            loginPage.login(user, pwd);
        });

        Allure.step("Kiểm tra trang Inventory được load", () -> {
            InventoryPage inventoryPage = new InventoryPage(getDriver());
            Assert.assertTrue(inventoryPage.isLoaded(), "Trang inventory chưa load");
        });
    }

    @Test(description = "Verify login failure with locked out user", groups = {"regression"})
    @Feature("Đăng nhập")
    @Story("UC-002: Đăng nhập bị khóa")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginLockedOutUser() {
        LoginPage loginPage = new LoginPage(getDriver());
        String pwd = ConfigReader.getInstance().getPassword();
        
        Allure.step("Nhập thông tin khóa", () -> {
            loginPage.loginExpectingFailure("locked_out_user", pwd);
        });

        Allure.step("Kiểm tra lỗi", () -> {
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Không hiển thị thông báo lỗi");
            Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Sorry, this user has been locked out.");
        });
    }

    @Test(description = "Verify login failure with invalid password", groups = {"regression"})
    @Feature("Đăng nhập")
    @Story("UC-003: Đăng nhập sai mật khẩu")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginInvalidPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        String user = ConfigReader.getInstance().getUsername();
        
        Allure.step("Nhập sai mật khẩu", () -> {
            loginPage.loginExpectingFailure(user, "wrong_password");
        });
        
        Allure.step("Kiểm tra thông báo lỗi", () -> {
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Không hiển thị thông báo lỗi");
            Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"));
        });
    }
}
