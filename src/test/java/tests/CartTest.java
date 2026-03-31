package tests;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.CartPage;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.qameta.allure.*;

public class CartTest extends BaseTest {

    @Test(description = "Verify that user can add an item to the cart", groups = {"smoke", "regression"})
    @Feature("Giỏ hàng (Cart)")
    @Story("UC-004: Thêm sản phẩm vào giỏ hàng")
    @Severity(SeverityLevel.NORMAL)
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
        
        Allure.step("Bấm nút Add to Cart ở sản phẩm đầu tiên", () -> {
            finalInventoryPage.addFirstItemToCart();
            Assert.assertEquals(finalInventoryPage.getCartItemCount(), 1, "Số lượng sản phẩm trong giỏ hàng không đúng");
        });
        
        Allure.step("Chuyển sang trang Cart và kiểm tra số lượng hiển thị", () -> {
            CartPage cartPage = finalInventoryPage.goToCart();
            Assert.assertEquals(cartPage.getItemCount(), 1, "Số lượng item hiển thị trong trang Cart không đúng");
        });
    }

    @Test(description = "Verify that user can remove an item from the cart", groups = {"regression"})
    @Feature("Giỏ hàng (Cart)")
    @Story("UC-005: Xóa sản phẩm khỏi giỏ hàng")
    @Severity(SeverityLevel.NORMAL)
    public void testRemoveItemFromCart() {
        LoginPage loginPage = new LoginPage(getDriver());
        String user = ConfigReader.getInstance().getUsername();
        String pwd = ConfigReader.getInstance().getPassword();

        InventoryPage inventoryPage = loginPage.login(user, pwd);
        
        Allure.step("Thêm sản phẩm để chuẩn bị xóa", () -> {
            inventoryPage.addFirstItemToCart();
            CartPage cartPage = inventoryPage.goToCart();
            Assert.assertEquals(cartPage.getItemCount(), 1);
        });
        
        Allure.step("Thực hiện xóa và kiểm chứng giỏ hàng trống", () -> {
            CartPage cartPage = new CartPage(getDriver());
            cartPage.removeFirstItem();
            Assert.assertEquals(cartPage.getItemCount(), 0, "Item chưa được xóa khỏi giỏ hàng");
        });
    }
}
