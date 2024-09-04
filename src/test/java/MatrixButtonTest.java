import org.evalart.pages.ButtonsTestPage;
import org.evalart.pages.LoginPage;
import org.evalart.pages.SuccessPage;
import static org.testng.Assert.*;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class MatrixButtonTest extends BaseTest {

    @Test
    @Parameters({"username", "password"})
    public void clickMatrixButtonUntilSeeSuccessHashTest(String username, String password) {

        LoginPage loginPage = getLoginPage();
        ButtonsTestPage buttonsTestPage = loginPage.fillFormLogin(username, password);
        SuccessPage successPage = buttonsTestPage.makeClick();
        assertTrue(successPage.isDisplayedSuccessText());
    }
}
