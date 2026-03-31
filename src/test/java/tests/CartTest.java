package tests;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.CartPage;
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

public class CartTest extends BaseTest {

    @Test(description = "Verify that user can add an item to the cart", groups = {"smoke", "regression"})
    @Feature("Giỏ hàng")
    @Story("UC-004: Thêm sản phẩm vào giỏ hàng")
    @Severity(SeverityLevel.NORMAL)
    @Description("Kiểm thử thêm 1 sản phẩm vào trang Cart")
    public void testAddItemToCart() {
        LoginPage loginPage = new LoginPage(getDriver());
        String user = ConfigReader.getInstance().getUsername();
        String pwd = ConfigReader.getInstance().getPassword();

        InventoryPage inventoryPage = null;
        try {
            inventoryPage = loginPage.login(user, pwd);
        } catch(Exception e) {
            Assert.fail("Login failed: " + e.getMessage());
        }
        
        InventoryPage finalInventoryPage = inventoryPage;
        Allure.step("Thêm sản phẩm đầu tiên vào giỏ", () -> {
            finalInventoryPage.addFirstItemToCart();
            Assert.assertEquals(finalInventoryPage.getCartItemCount(), 1, "Số lượng sản phẩm trong giỏ hàng không đúng");
        });
        
        Allure.step("Vào trang Cart và kiểm tra số lượng", () -> {
            CartPage cartPage = finalInventoryPage.goToCart();
            Assert.assertEquals(cartPage.getItemCount(), 1, "Số lượng item hiển thị trong trang Cart không đúng");
        });
    }

    @Test(description = "Verify that user can remove an item from the cart", groups = {"regression"})
    @Feature("Giỏ hàng")
    @Story("UC-005: Xoá sản phẩm khỏi giỏ hàng")
    @Severity(SeverityLevel.NORMAL)
    public void testRemoveItemFromCart() {
        LoginPage loginPage = new LoginPage(getDriver());
        String user = ConfigReader.getInstance().getUsername();
        String pwd = ConfigReader.getInstance().getPassword();

        InventoryPage inventoryPage = loginPage.login(user, pwd);
        
        Allure.step("Thêm sản phẩm đầu tiên", () -> {
            inventoryPage.addFirstItemToCart();
        });
        
        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertEquals(cartPage.getItemCount(), 1);
        
        Allure.step("Bỏ sản phẩm đầu tiên khỏi giỏ", () -> {
            cartPage.removeFirstItem();
            Assert.assertEquals(cartPage.getItemCount(), 0, "Item chưa được xóa khỏi giỏ hàng");
        });
    }
}
