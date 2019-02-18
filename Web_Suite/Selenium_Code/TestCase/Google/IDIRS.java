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
import Objects_Map.IDIRS_ObjectMap;
public class IDIRS {
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
	// VDS_APP.LaunchVDS_APP(testContext.getName());
  Utilities.LaunchApplication(testContext, this.getClass().getName(), Store_Env_Values.get("Environment"), iteration, browser);
  Report.PrintTestReport(testContext.getName());
 }
 @Test
 public void test(final ITestContext testContext) throws Exception{
  
  String sTestName = testContext.getName(); String tmpVar = null; Connection conn = null;
  Store_Param_Values = IO.getTestData(testContext.getName()); Utilities.Map_TestName_Params.put(sTestName, Store_Param_Values);
  WebDriver lDriver = UI.getDriver(sTestName); 
  IDIRS_ObjectMap IDIRS_ObjectMap = PageFactory.initElements(lDriver, IDIRS_ObjectMap.class);

  
  try {
			// Step 1 - Open URL: CONFIG{AppURL}
			Web.OpenURL(sTestName, "http://127.0.0.1:8091/");
			
			Web.MiddleButton(sTestName);
			Web.Wait(sTestName, "5");
			Web.Click(sTestName, IDIRS_ObjectMap.Driver, "IDIRS_ObjectMap.Driver");
			Web.PageSource(sTestName);
			Web.Click(sTestName, IDIRS_ObjectMap.Two, "IDIRS_ObjectMap.Two");
			Web.Click(sTestName, IDIRS_ObjectMap.Two, "IDIRS_ObjectMap.Two");
			Web.Click(sTestName, IDIRS_ObjectMap.Two, "IDIRS_ObjectMap.Two");
			Web.Click(sTestName, IDIRS_ObjectMap.Two, "IDIRS_ObjectMap.Two");
			Web.Click(sTestName, IDIRS_ObjectMap.Two, "IDIRS_ObjectMap.Two");
			Web.Click(sTestName, IDIRS_ObjectMap.Four, "IDIRS_ObjectMap.Four");
			Web.Click(sTestName, IDIRS_ObjectMap.Enter, "IDIRS_ObjectMap.Enter");
			
			Web.Click(sTestName, IDIRS_ObjectMap.Block, "IDIRS_ObjectMap.Block");
			Web.Wait(sTestName, "3");
			Web.PageSource(sTestName);
			Web.HighLightElement(sTestName, IDIRS_ObjectMap.BlockDiv, "IDIRS_ObjectMap.BlockDiv");
			
			Web.Click1(sTestName, IDIRS_ObjectMap.One, "IDIRS_ObjectMap.One");
			Web.Click1(sTestName, IDIRS_ObjectMap.Three, "IDIRS_ObjectMap.Three");
			Web.Click(sTestName, IDIRS_ObjectMap.Three, "IDIRS_ObjectMap.Three");
			Web.Click(sTestName, IDIRS_ObjectMap.Three, "IDIRS_ObjectMap.Three");
			Web.Click(sTestName, IDIRS_ObjectMap.Two, "IDIRS_ObjectMap.Two");			
			Web.Click(sTestName, IDIRS_ObjectMap.Four, "IDIRS_ObjectMap.Four");
			Web.Click(sTestName, IDIRS_ObjectMap.Zero, "IDIRS_ObjectMap.Zero");
			Web.Click(sTestName, IDIRS_ObjectMap.One, "IDIRS_ObjectMap.One");
			Web.Click(sTestName, IDIRS_ObjectMap.Enter, "IDIRS_ObjectMap.Enter");
			
			

			
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