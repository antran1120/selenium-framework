package tests;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginDDTTest extends BaseTest {

    @DataProvider(name = "excelSmokeData")
    public Object[][] getSmokeData() {
        String path = "src/test/resources/testdata/login_data.xlsx";
        return ExcelReader.getData(path, "SmokeCases");
    }

    @DataProvider(name = "excelNegativeData")
    public Object[][] getNegativeData() {
        String path = "src/test/resources/testdata/login_data.xlsx";
        return ExcelReader.getData(path, "NegativeCases");
    }

    @DataProvider(name = "excelBoundaryData")
    public Object[][] getBoundaryData() {
        String path = "src/test/resources/testdata/login_data.xlsx";
        return ExcelReader.getData(path, "BoundaryCases");
    }

    @Test(dataProvider = "excelSmokeData", groups = {"smoke"}, description = "Test valid login from Excel")
    public void testSmokeLoginFromExcel(String username, String password, String expectedUrl, String description) {
        // Cần truyền description vào log hoặc console nếu cần
        System.out.println("Running: " + description);
        LoginPage login = new LoginPage(getDriver());
        InventoryPage inventory = login.login(username, password);
        Assert.assertTrue(inventory.isLoaded(), "Trang inventory không load thành công cho: " + description);
    }

    @Test(dataProvider = "excelNegativeData", groups = {"regression"}, description = "Test negative login from Excel")
    public void testNegativeLoginFromExcel(String username, String password, String expectedError, String description) {
        LoginPage login = new LoginPage(getDriver());
        login.loginExpectingFailure(username, password);
        Assert.assertTrue(login.isErrorDisplayed(), "Không hiển thị lỗi cho: " + description);
        Assert.assertEquals(login.getErrorMessage(), expectedError, "Thông báo lỗi không khớp cho: " + description);
    }
    
    @Test(dataProvider = "excelBoundaryData", groups = {"regression"}, description = "Test boundary login from Excel")
    public void testBoundaryLoginFromExcel(String username, String password, String expectedError, String description) {
        LoginPage login = new LoginPage(getDriver());
        login.loginExpectingFailure(username, password);
        Assert.assertTrue(login.isErrorDisplayed(), "Không hiển thị lỗi cho: " + description);
        Assert.assertEquals(login.getErrorMessage(), expectedError, "Thông báo lỗi không khớp cho: " + description);
    }
}
