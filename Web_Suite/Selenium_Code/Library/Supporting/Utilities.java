package Supporting;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;




import Supporting.Report;

public class Utilities {
 public static  PrintStream console = null;
 
 public static Hashtable<String, String> StoreCopy_Env_Values = new Hashtable<String,String>();
 public static Hashtable<String, Channel> Map_SSHName_To_Channel = new Hashtable<String, Channel>();
 public static Hashtable<String, String> Map_SSHName_To_TestName = new Hashtable<String, String>();
 public static Hashtable<String, String> Map_TestName_To_SSHName = new Hashtable<String, String>();

 public static Hashtable<String, String> Map_SSHName_PWD = new Hashtable<String,String>();
 public static Hashtable<String, String> Map_SSHName_LASTSSH = new Hashtable<String,String>();
 public static Hashtable<String, String> Map_SSHName_Prompt = new Hashtable<String,String>();
 public static Hashtable<String, String> Map_TestName_Log = new Hashtable<String,String>();
 public static Hashtable<String, Hashtable<String, String>> Map_TestName_Params = new Hashtable<String,Hashtable<String, String>>();

 public static String getSysDate(String dtFormat)  {
  String retVal = "";
  try {
   DateFormat dateFormat = new SimpleDateFormat(dtFormat);
   Date date = new Date();
   IO.PrintLog("", dateFormat.format(date)); //2014/08/06 15:59:48
   return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
  return retVal;
 }

 @SuppressWarnings("deprecation")
public static void LaunchApplication(final ITestContext testContext, String className, String envName, 
                     String iCurrIter, String sBrowser) throws Exception {

  Hashtable<String, String> sTestParams = new Hashtable<String, String>();
  WebDriver driver=null;
  
  try {
   
   String sTestName = testContext.getName();
   String sTestScriptName = className.substring(className.lastIndexOf(".")+1);
   
   IO.Setup();
   sTestParams = IO.Read_Iter_Data(envName, sTestScriptName, Integer.valueOf(iCurrIter));
   IO.putTestData(sTestName, sTestParams);
   
   // retrieving executed by
   Report.exeBy = StoreCopy_Env_Values.get("Executed_By");                
 
   Report.Start(sTestScriptName);
   Report.PrintIteration(sTestName, "Iteration", "1");
   
   if (Utilities.StoreCopy_Env_Values.get("DEBUG").equals("1")) {
    
    
    	System.setProperty("webdriver.gecko.driver","E:\\Automation\\Web_Suite\\Selenium_Code\\IO\\Utilities\\geckodriver.exe");

		File pathToBinary = new File("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
		FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
		FirefoxProfile firefoxProfile = new FirefoxProfile();       
		driver = new FirefoxDriver(ffBinary,firefoxProfile);    	
    	
    	
    	//driver = new FirefoxDriver();
        // System.out.println("Executed on New FireFox driver");
     
     System.out.println("Executed on remote driver");
   }
    
       
    else {
    if (sBrowser.equalsIgnoreCase("IE")) {
     DesiredCapabilities oCap = DesiredCapabilities.internetExplorer();
     driver = new RemoteWebDriver(new URL("http://localhost:5558/wd/hub"), oCap);
     
     
    } else if (sBrowser.equalsIgnoreCase("Firefox")) {
    
    	System.setProperty("webdriver.gecko.driver","E:\\Automation\\Web_Suite\\Selenium_Code\\IO\\Utilities\\geckodriver.exe");

 	driver = new FirefoxDriver();
    	
   	
/* DesiredCapabilities oCap = DesiredCapabilities.firefox();
 oCap.setBrowserName("firefox");
 driver = new RemoteWebDriver(new URL(
   "http://localhost:5555/wd/hub"), oCap);
*/
    }    
    
    else if (sBrowser.equalsIgnoreCase("Chrome")) {
     DesiredCapabilities oCap = DesiredCapabilities.chrome();
     oCap.setBrowserName("chrome");
     driver = new RemoteWebDriver(new URL("http://localhost:5559/wd/hub"), oCap);
     
     
    } else if (sBrowser.equalsIgnoreCase("Safari")) {
     DesiredCapabilities oCap = DesiredCapabilities.safari();
     oCap.setBrowserName("safari");
     driver = new RemoteWebDriver(new URL(
       "http://localhost:5556/wd/hub"), oCap);
    }
   }
   
   driver.manage().timeouts().implicitlyWait(Long.valueOf(Utilities.StoreCopy_Env_Values.get("ObjTimeOut")), TimeUnit.SECONDS);
   driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
   driver.manage().window().maximize();  
   UI.putDriver(sTestName, driver);
   
  } catch (Exception e) {
   e.printStackTrace();
  }
 }
 
 public static void SetLogStream(String logfile) throws FileNotFoundException {
  
        if (logfile.equals("Console")) {
         System.setOut(Utilities.console);
        } else {
         PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(logfile, true)), true);
         System.setOut(ps);
        }
 }
 
 public static void CloseExecution(final ITestContext testContext) throws HeadlessException,
 IOException, AWTException, InterruptedException {
  try {
   Report.OperationPoint(testContext.getName(), "\n\nDone with " + testContext.getName());
   Report.CloseIteration(testContext.getName());
   Report.Consolidation(testContext.getName());
   IO.PrintLog("",  "Execution Completed for Test - " + testContext.getName());
  } catch (Exception e) {
   e.printStackTrace();
   String sErrMsg = e.getMessage();
   Report.OperationPoint(testContext.getName(), sErrMsg);
  } 
 }

 public static Hashtable<String,String> readEnvironment() throws IOException {
  
  Properties oProp = new Properties(); Hashtable<String,String> hdata = new Hashtable<String,String>();
  
  File file = new File("Config", "Env.Properties");
  FileInputStream fileInput = new FileInputStream(file);
  try {
   oProp.load(fileInput);
   for (String key : oProp.stringPropertyNames()) {
    hdata.put(key, oProp.getProperty(key));
   }
  } catch (Exception e) {
   throw new FileNotFoundException("property file 'Env.Properties' not found in the classpath");
  }  
  return hdata;
 }
 

 public static void PreRequisite() throws IOException, InterruptedException {
  
  if (Utilities.StoreCopy_Env_Values.get("MODE").equals("OPENSSH")) {

   int x = 180; // wait secs
      System.out.println("\nPlease check 'PreRequisite Status' in above HTML file for any error, to proceeed with execution type 'GO' here");
      boolean stopFlg = false; while (!stopFlg) {
    ExecuteCmd_AllSSH("bash");
       BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 
       long startTime = System.currentTimeMillis(); while ((System.currentTimeMillis() - startTime) < x * 1000 && !in.ready()) {}
       if (in.ready() && in.readLine().equals("GO")) { stopFlg = true; }
      }
  }
 }
 
 public static void ExecuteCmd_AllSSH(String cmd) throws IOException, InterruptedException {
  Set <String> keys = Map_SSHName_To_Channel.keySet();
        for(String key: keys){
            Channel tcChannel = Utilities.Map_SSHName_To_Channel.get(key);
            OutputStream out = tcChannel.getOutputStream();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream(); tcChannel.setOutputStream(baos);
            out.write((cmd + "\n").getBytes()); out.flush(); Thread.sleep(Long.valueOf(2000));
            IO.PrintLog(IO.sExe_Fldr + "\\PreRequisite.log", "-------------------------------- Keep Alive - " + 
               new SimpleDateFormat("MM-dd-yyyy hh.mm.ss").format(Calendar.getInstance().getTime()) + " - Server " + key + " ---------------------- ");
            IO.PrintLog(IO.sExe_Fldr + "\\PreRequisite.log", baos.toString());
            tcChannel.setOutputStream(System.out);
        }
 }
 

 
 
 
 
}