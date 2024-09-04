import lombok.Data;
import org.evalart.diverConfig.MyDriver;
import org.evalart.pages.LoginPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

@Data
public class BaseTest {

    private MyDriver myDriver;
    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(String browser) {
        myDriver = new MyDriver(browser);
        getMyDriver().getDriver().get("https://tasks.evalartapp.com/automatization/");
        getMyDriver().getDriver().manage().window().maximize();
        loginPage = new LoginPage(getMyDriver().getDriver());
    }

    @AfterMethod
    public void tearDown() {
        if (myDriver != null) {
            myDriver.getDriver().quit();
        }
    }
}
