package Google;
import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;

import Supporting.Report;
import Supporting.UI;
import winium.elements.desktop.ComboBox;
import winium.elements.desktop.extensions.ByExtensions;
import winium.elements.desktop.extensions.WebElementExtensions;

public class WiniumTest {
	
	WiniumDriver driverWinium =null;
	
 @BeforeClass
 public void SetUpEnv() throws IOException, AWTException
 {
	 Robot robot = new Robot();
	    robot.keyPress(KeyEvent.VK_WINDOWS);
	    robot.keyPress(KeyEvent.VK_D);
	    robot.keyRelease(KeyEvent.VK_D);
	    robot.keyRelease(KeyEvent.VK_WINDOWS);
	    
	 DesktopOptions options = new DesktopOptions();
	 options.setApplicationPath("C:\\Program Files (x86)\\Trapeze\\VDS\\VDS3.exe");
	 
	 try{
		 driverWinium =new WiniumDriver(new URL("http://localhost:9999"), options);
		 
		 
	 }
	 catch(MalformedURLException e){
		 e.printStackTrace();
	 }
 }
 
 @Test
 public void TestNotePadApp() throws NumberFormatException, InterruptedException
 {
	Thread.sleep(3000);
	driverWinium.findElementByName("Maximize").click();
	driverWinium.findElementByName("File").click();
	driverWinium.findElementByName("Load Database...").click();
	driverWinium.findElementByClassName("WindowsForms10.EDIT.app.0.1ed9395_r9_ad1").sendKeys("C:\\Users\\juhirana\\Downloads\\Data_Supply\\database\\20160729_0200_dat_20160729_01");
	driverWinium.findElementByName("OK").click();
	//Thread.sleep(15000);
	driverWinium.findElement(By.xpath("//*[@AutomationId='TB_Main']")).click();
	//driverWinium.findElementByName("Select VehPos").click();
	//driverWinium.findElementByName("Select VehPos").click();
	//System.out.println("Title Name: "+driverWinium.findElement(By.xpath("//*[@AutomationId='TitleBar']")).getAttribute("Name"));
	//driverWinium.findElement(By.xpath("//*[@AutomationId='TitleBar']")).click();
	//driverWinium.findElement(By.xpath("//*[@AutomationId='Item 3' and Name='Tools']")).click();
	
	driverWinium.findElementByName("Tools").click();
	driverWinium.findElementByName("Options").click();
	driverWinium.findElementByName("GPS settings").click();
	driverWinium.findElementByName("[m/s]").clear();
	driverWinium.findElementByName("[m/s]").sendKeys("1000");
	driverWinium.findElementByName("Port No.:").clear();
	driverWinium.findElementByName("Port No.:").sendKeys("192.168.10.2");
	driverWinium.findElementByName("Navigation").click();
	driverWinium.findElementByName("Automatically download corridor points").click();
	driverWinium.findElementByName("OK").click();
	//driverWinium.findElementByName("OK").click();
	driverWinium.findElementByName("Select VehPos").click();	
	driverWinium.findElementByName("Block").click();
	
	driverWinium.findElement(By.xpath("//*[@AutomationId='CB_Vehicle']")).click();
    driverWinium.findElementByName("1    1    1").click();
	driverWinium.findElement(By.xpath("(//*[@Name='40'])[2]")).click();
	
	    for (int j=1;j<=76;j++){
			 driverWinium.findElementByName("Forward by small amount").click();	
	    }
	    driverWinium.findElementByName("13332401  40  05:11:00  25:46:00").click();
	     driverWinium.findElementByName("View Current Trip").click();
	    
	    if(driverWinium.findElement(By.xpath("//*[@AutomationId='TitleBar']")).getAttribute("Name").equalsIgnoreCase("PathVisualisation")){
	    	driverWinium.findElement(By.xpath("//*[@AutomationId='Minimize']")).click();
	    }
	 /*if(driverWinium.findElementByName("You haven't set the vehicle. This will disable ability to get corridor. Proceed ?").isDisplayed()){
    driverWinium.findElementByName("Yes").click();
    }
	 
	 
    
    if(driverWinium.findElementByName("Continue").isDisplayed()){
    	driverWinium.findElementByName("Continue").click();
    }*/
	
    
    driverWinium.findElementByName("Send GPS").click();
    driverWinium.findElementByName("Automatically go to next trip").click();
    driverWinium.findElementByName("Run with plan").click();
    driverWinium.findElement(By.xpath("//*[@AutomationId='BT_Start']")).click();
    //driverWinium.findElementByName("Start").click();
    
    
  
 }
 
 @AfterTest
 public void TearDown()
 {
	 //driver.close();
 }
 
 
 
}