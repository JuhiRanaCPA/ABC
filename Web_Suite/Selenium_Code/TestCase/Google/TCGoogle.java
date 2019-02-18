package Google;
import java.awt.AWTException;
import java.awt.HeadlessException;
import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.util.Hashtable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestContext;
import org.testng.annotations.*;
import Supporting.*;
import Objects_Map.Google;
public class TCGoogle {
 public String tcName;
 Hashtable<String, String> Store_DB_Values = new Hashtable<String,String>();
 Hashtable<String, String> Store_Step_Values = new Hashtable<String,String>();
 Hashtable<String, String> Store_Env_Values = new Hashtable<String, String>();
 Hashtable<String, String> Store_Param_Values = new Hashtable<String,String>();
 
 @BeforeSuite
 public void beforeSuite(final ITestContext testContext) throws Exception{
  Store_Env_Values = Utilities.readEnvironment();
  Utilities.StoreCopy_Env_Values = Store_Env_Values;
  Report.StartSuite().equals("Created");
 }
 
 @Parameters({"browser", "iteration"})
 @BeforeTest
 public void beforeTest(final ITestContext testContext, String browser, String iteration) throws Exception{
  Utilities.LaunchApplication(testContext, this.getClass().getName(), Store_Env_Values.get("Environment"), iteration, browser);
  Report.PrintTestReport(testContext.getName());
 }
 @Test
 public void test(final ITestContext testContext) throws Exception{
  
  String sTestName = testContext.getName(); String tmpVar = null; Connection conn = null;
  Store_Param_Values = IO.getTestData(testContext.getName()); Utilities.Map_TestName_Params.put(sTestName, Store_Param_Values);
  WebDriver lDriver = UI.getDriver(sTestName); 
  Google Google = PageFactory.initElements(lDriver, Google.class);

  
  try {
			// Step 1 - Open URL: CONFIG{AppURL}
			Web.OpenURL(sTestName, Store_Env_Values.get("AppURL"));

			// Step 2 -  Click object 'SignIn' of page 'Search'
			Web.Click(sTestName, Google.Search_SignIn, "Google.Search_SignIn");

			// Step 3 - Enter Text 'juhi.rana2007' in object 'Email_TextBox' of page 'Google_SignIn'
			Web.EnterText(sTestName, Google.Google_SignIn_Email_TextBox, "Google.Google_SignIn_Email_TextBox", "juhi.rana2007", "False");

			// Step 4 -  Click object 'Next_Button' of page 'Google_SignIn'
			Web.Click(sTestName, Google.Google_SignIn_Next_Button, "Google.Google_SignIn_Next_Button");

			// Step 5 - Enter Text 'new@0987' in object 'Password_TextBox' of page 'Google_SignIn'
			Web.EnterText(sTestName, Google.Google_SignIn_Password_TextBox, "Google.Google_SignIn_Password_TextBox", "new@0987", "False");

			// Step 6 -  Click object 'SignIn_Button' of page 'Google_SignIn'
			Web.Click(sTestName, Google.Google_SignIn_SignIn_Button, "Google.Google_SignIn_SignIn_Button");
} catch (Exception e){
     
     Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
  }    
 }  
 
 @AfterTest public void afterTest(final ITestContext testContext) throws IOException, ParseException, HeadlessException, AWTException, InterruptedException {
  Utilities.CloseExecution(testContext);
  Report.CloseTestReport(testContext.getName());
 }
 
 @AfterSuite public void endSuite() throws IOException {
  Report.cleanTemp();
 }
 
}