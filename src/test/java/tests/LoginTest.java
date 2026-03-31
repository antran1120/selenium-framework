package tests;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.qameta.allure.*;

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

        Allure.step("Nhập thông tin người dùng", () -> {
            loginPage.login(user, pwd);
        });

        Allure.step("Kiểm tra trang quản lý load thành công", () -> {
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
        
        Allure.step("Nhập tài khoản bị khoá", () -> {
            loginPage.loginExpectingFailure("locked_out_user", pwd);
        });

        Allure.step("Xác nhận thông báo lỗi", () -> {
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
        
        Allure.step("Đăng nhập với mật khẩu sai", () -> {
            loginPage.loginExpectingFailure(user, "wrong_password");
        });
        
        Allure.step("Xác nhận có hiển thị lỗi mâu thuẫn tên", () -> {
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Không hiển thị thông báo lỗi");
            Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"));
        });
    }
}
