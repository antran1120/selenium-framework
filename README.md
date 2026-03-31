# Lab 9: Selenium Page Object Model & Data-Driven Testing Framework

Dự án này chứa framework tự động hoá kiểm thử sử dụng Page Object Model (POM) và Data-Driven Testing (DDT), đáp ứng đầy đủ yêu cầu của bài Lab 9.

## Cấu trúc dự án
- `src/main/java/framework/base/`: Chứa các lớp cơ sở `BasePage`, `BaseTest`, `DriverFactory`.
- `src/main/java/framework/config/`: Chứa `ConfigReader` quản lý thông tin đa môi trường.
- `src/main/java/framework/pages/`: Chứa các Page Objects (`LoginPage`, `InventoryPage`, `CartPage`, `CheckoutPage`).
- `src/main/java/framework/utils/`: Chứa các lớp tiện ích (`ExcelReader`, `JsonReader`, `ScreenshotUtil`, `RetryAnalyzer`, `TestDataFactory`).
- `src/main/java/framework/models/`: Chứa các lớp POJO mapping JSON object.
- `src/test/java/tests/`: Chứa các class kiểm thử (Test Class).
- `src/test/resources/`: Chứa config properties, test suites (XML), và test data.

## Hướng dẫn sử dụng

### 1. Chuẩn bị Test Data (Tạo file Excel)
Mã nguồn đã tích hợp sẵn một công cụ tạo dữ liệu test Excel tự động. Hãy chạy file `DataGenerator.java` để sinh file `login_data.xlsx` trong thư mục `src/test/resources/testdata/`.

- Chạy bằng Maven command (chắc chắn bạn di chuyển vào trong folder SeleniumFramework):
```bash
mvn clean compile exec:java -Dexec.mainClass="framework.utils.DataGenerator"
```
- Hoặc chạy trực tiếp `DataGenerator` main method bằng IDE (IntelliJ, Eclipse).

### 2. Chạy Test

Bạn có thể chạy test thông qua Maven hoặc chạy từng Test Suite `.xml` trong IDE.

- Chạy nhóm *Smoke Test* với môi trường `dev` (mặc định):
```bash
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testng-smoke.xml
```

- Chạy nhóm *Regression Test* với môi trường `staging`:
```bash
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testng-regression.xml -Denv=staging
```

### 3. Xem kết quả
- Báo cáo kết quả sẽ được tạo mặc định tại `target/surefire-reports/index.html`.
- Ảnh chụp màn hình khi test thất bại sẽ lưu ở `target/screenshots/`.

## Các yêu cầu đã hoàn thành
- Bài 1: BasePage chứa explicit wait, BaseTest thiết lập cấu hình với ThreadLocal.
- Bài 2: Page Objects cho theo nguyên tắc Fluent Interface của POM, ẩn các element locators.
- Bài 3: Data-Driven dùng Excel (`ExcelReader`), được kết nối qua `@DataProvider`.
- Bài 4: JSON Data-Driven (`JsonReader`) và Sinh dữ liệu Faker ngẫu nhiên (`TestDataFactory`).
- Bài 5: Cấu hình đa môi trường bằng `ConfigReader` lấy properties theo `env`.
- Bài 6: Tự động chạy lại test (`RetryAnalyzer`) và minh hoạ test không ổn định với `FlakySimulationTest`.
