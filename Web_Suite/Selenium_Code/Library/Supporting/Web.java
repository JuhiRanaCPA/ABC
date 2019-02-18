package Supporting;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.seleniumhq.jetty9.server.HttpChannelState.Action;
/*import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;*/


@SuppressWarnings("unused")
public class Web {
 
 public static void OpenURL(String sTestName, String URLTo_Open) throws HeadlessException, IOException, AWTException {

  IO.PrintLog(sTestName,"\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, "OpenURL(" + URLTo_Open + ")");
  try {
	  
   WebDriver lDriver = UI.getDriver(sTestName); 
   //lDriver.navigate().to(URLTo_Open);
   lDriver.getWindowHandle();
   lDriver.get(URLTo_Open);
 
   
   Thread.sleep(20);
  } catch(Exception e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
  }
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
    }

 public static boolean CheckExist(String sTestName, WebElement Obj, String ObjStr, String existence, String toStop) throws HeadlessException, IOException, AWTException {

  IO.PrintLog(sTestName,"\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, "CheckExist(" + ObjStr + ", " + existence + ", " + toStop + ")");
  boolean retVal=false;
  try {
   toStop = WordUtils.capitalizeFully(toStop); existence = WordUtils.capitalizeFully(existence);
   String outCome = String.valueOf(UI.CheckExist(Obj));
   
   
   if (toStop.equals("True"))
    retVal = Report.TestPoint(sTestName, "Check the existence of :" + ObjStr, existence, outCome, true);
   else if (toStop.equals("False"))
    retVal = Report.ValidationPoint(sTestName, "Check the existence of :" + ObjStr, existence, outCome, true);
   
   else  if(toStop.equals("Checkif"))
    retVal= outCome.equals(existence);
   
   
  } catch(Exception e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
  }
    
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
  return retVal;
    }
 public static void Wait(String sTestName, String secs) throws NumberFormatException, InterruptedException, HeadlessException, IOException, AWTException {
  
  IO.PrintLog(sTestName,"\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, "Wait(" + secs + ")");
  Thread.sleep(Long.valueOf(secs)*1000);
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
 }
 public static void EnterText(String sTestName, WebElement Obj, String ObjStr, String Text_To_Enter, String Optional) throws HeadlessException, IOException, AWTException {

 IO.PrintLog(sTestName,"\n ---------------------------------------------------------------------------------------------------");
   
 Report.OperationPoint(sTestName, "EnterText(" + ObjStr + ", " + Text_To_Enter + ")");
  try {
   WebDriver lDriver = UI.getDriver(sTestName); 
   // for printing sourcecode
  // String sourceCode =lDriver.getPageSource();
   //Report.OperationPoint(sTestName, sourceCode);
   //*************
   if (ObjStr.contains("PopUp") && lDriver.findElement(By.tagName("iframe")).getTagName()!=null ) 
    lDriver.switchTo().frame(1);
   
   if (UI.CheckExist(Obj).contains("True")) {
    
    if (!Obj.getAttribute("value").equals(""))
    { Obj.clear(); 
    Thread.sleep(1000L);} 
    Obj.sendKeys(Text_To_Enter); 
    lDriver.switchTo().defaultContent();
   } else
    if (!Optional.equals("True")) 
     Report.TestPoint(sTestName, "Object Not Found :" + ObjStr, "True", "False", true);
    
  
 } catch(Exception e) {
  Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
 }
 IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
}
 
 
 public static void ListSelect(String sTestName, WebElement Obj, String ObjStr, int textToSelect) throws HeadlessException, IOException, AWTException {

  IO.PrintLog(sTestName,"\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, "ListSelect(" + ObjStr + ", " + textToSelect + ")");
  try {
   WebDriver lDriver = UI.getDriver(sTestName); 
   
   if (ObjStr.contains("PopUp") && lDriver.findElement(By.tagName("iframe")).getTagName()!=null ) 
    lDriver.switchTo().frame(1);
   
   if (UI.CheckExist(Obj).contains("True")) {
    Select list = new Select(Obj);
    list.selectByIndex(textToSelect-1);
    UI.ForceClick(lDriver, Obj); lDriver.switchTo().defaultContent();
   } else 
    Report.TestPoint(sTestName, "Object Not Found :" + ObjStr, "True", "False", true);
   
  } catch(Exception e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
  }
  
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
    }
 public static void ListSelect(String sTestName, WebElement Obj, String ObjStr, String textToSelect) throws HeadlessException, IOException, AWTException {

  IO.PrintLog(sTestName,"\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, "ListSelect(" + ObjStr + ", " + textToSelect + ")");
  try {
   WebDriver lDriver = UI.getDriver(sTestName); 

   if (ObjStr.contains("PopUp") && lDriver.findElement(By.tagName("iframe")).getTagName()!=null ) 
    lDriver.switchTo().frame(1);

   if (UI.CheckExist(Obj).contains("True")) {
    Select list = new Select(Obj);
    list.selectByVisibleText(textToSelect);
    UI.ForceClick(lDriver, Obj); lDriver.switchTo().defaultContent();
   } else
    Report.TestPoint(sTestName, "Object Not Found :" + ObjStr, "True", "False", true);
   
  } catch(Exception e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
  }
   
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
    }
 public static boolean ValidateSelect(String sTestName, WebElement Obj, String ObjStr, String validn) throws HeadlessException, IOException, AWTException {

  IO.PrintLog(sTestName,"\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, "ValidateSelect(" + ObjStr + ", " + validn + ")");
  boolean retVal=false;
  try {
   WebDriver lDriver = UI.getDriver(sTestName); 

   if (UI.CheckExist(Obj).contains("True")) {
    Select list = new Select(Obj); List oSize = list.getOptions(); String opVal="";
    int iListSize = oSize.size();
    for(int i =0; i>iListSize ; i++){ opVal = opVal + "|" + list.getOptions().get(i).getText(); }
    
    retVal = Report.ValidationPoint(sTestName, "Validate the list '" + ObjStr + "' content(s)" , opVal, validn, true);
   } else
    retVal= Report.TestPoint(sTestName, "Object Not Found :" + ObjStr, "True", "False", true);
   
  } catch(Exception e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
  }
   
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
  return retVal;
 }
 
 public static void MiddleButton(String sTestName) throws HeadlessException, IOException, AWTException{
	 Report.OperationPoint(sTestName, "highLightElement: ");
		WebDriver lDriver = UI.getDriver(sTestName);
	try {	
		 Robot robot=new Robot();
		 robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
		 robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
		Report.OperationPoint(sTestName,"Element Higlighted");}
	catch (Exception e) {Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
		}}
 

 
 public static void HighLightElement(String sTestName, WebElement Obj, String ObjStr) throws HeadlessException, IOException, AWTException{
	 Report.OperationPoint(sTestName, "highLightElement: ("+ObjStr+")");
		WebDriver lDriver = UI.getDriver(sTestName);
	try {JavascriptExecutor js=(JavascriptExecutor)lDriver; 
		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", Obj);
		Report.OperationPoint(sTestName,"Element Higlighted");}
	catch (Exception e) {Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
		}}
 
 
 public static void ClickJS(String sTestName, WebElement Obj, String ObjStr) throws HeadlessException, IOException, AWTException {
	 String Optional ="True";
	  IO.PrintLog(sTestName,"\n ---------------------------------------------------------------------------------------------------");
	  Report.OperationPoint(sTestName, "Clicka(" + ObjStr + ")");
	  try {
	   WebDriver lDriver = UI.getDriver(sTestName); 
	   
	   //System.out.print(lDriver.getPageSource());

	   if (ObjStr.contains("PopUp") && lDriver.findElement(By.tagName("iframe")).getTagName()!=null ) 
	    lDriver.switchTo().frame(1);
	   
	   if (UI.CheckExist(Obj).contains("True")) {
		   
		   JavascriptExecutor js = (JavascriptExecutor)lDriver;
		   
		   
		   js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", Obj);
		   
		   
		   js.executeScript("arguments[0].click();", Obj);
		   
		   
	   // UI.ForceClick(lDriver, Obj);
	   // lDriver.switchTo().defaultContent();
	   } else
	    if (!Optional.equals("True")) 
	     Report.TestPoint(sTestName, "Object Not Found :" + ObjStr, "True", "False", true);
	   
	  } catch(Exception e) {
	   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
	  }
	   
	  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
	    }

 public static void Click(String sTestName, WebElement Obj, String ObjStr) throws HeadlessException, IOException, AWTException {
  Click(sTestName, Obj, ObjStr, "False1");
  
  
 }
 
 public static void PageSource(String sTestName) throws HeadlessException, IOException, AWTException {
	 WebDriver lDriver = UI.getDriver(sTestName); 
	 System.out.print(lDriver.getPageSource());
	 }
 
 
 public static void Click1(String sTestName, WebElement Obj, String ObjStr) throws HeadlessException, IOException, AWTException {
String Optional="True";
	  IO.PrintLog(sTestName,"\n ---------------------------------------------------------------------------------------------------");
	  Report.OperationPoint(sTestName, "Click(" + ObjStr + ")");
	  try {
	   WebDriver lDriver = UI.getDriver(sTestName); 
	   
	   System.out.print(lDriver.getPageSource());

	   if (ObjStr.contains("PopUp") && lDriver.findElement(By.tagName("iframe")).getTagName()!=null ) 
	    lDriver.switchTo().frame(1);
	   
	   if (UI.CheckExist(Obj).contains("True")) {
		   Obj.isDisplayed();
		   Obj.isEnabled();

		   Actions action =new Actions(lDriver);
		   
		   action.click();
		  // JavascriptExecutor executor = (JavascriptExecutor)lDriver;  
		  // executor.executeScript("arguments[0].click();", Obj);  
		   
		  // Obj.isDisplayed();
		 // Obj.click();
	     //UI.ForceClick(lDriver, Obj);
		   // lDriver.switchTo().defaultContent();
	   } else
	    if (!Optional.equals("True")) 
	     Report.TestPoint(sTestName, "Object Not Found :" + ObjStr, "True", "False", true);
	   
	  } catch(Exception e) {
	   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
	  }
	   
	  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
	    }
	 
 
 public static void Click(String sTestName, WebElement Obj, String ObjStr, String Optional) throws HeadlessException, IOException, AWTException {

  IO.PrintLog(sTestName,"\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, "Click(" + ObjStr + ")");
  try {
   WebDriver lDriver = UI.getDriver(sTestName); 
   
  // System.out.print(lDriver.getPageSource());

   if (ObjStr.contains("PopUp") && lDriver.findElement(By.tagName("iframe")).getTagName()!=null ) 
    lDriver.switchTo().frame(1);
   
   if (UI.CheckExist(Obj).contains("True")) {
	   Obj.isDisplayed();
	   Obj.isEnabled();
	   
	   
	  Obj.click();
     //UI.ForceClick(lDriver, Obj);
	   // lDriver.switchTo().defaultContent();
   } else
    if (!Optional.equals("True")) 
     Report.TestPoint(sTestName, "Object Not Found :" + ObjStr, "True", "False", true);
   
  } catch(Exception e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
  }
   
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
    }
 
 
 
 
 public static void SetValue(String sTestName, WebElement Obj, String ObjStr, String ValueEnter) throws HeadlessException, IOException, AWTException { 
  
  SetValue( sTestName,  Obj,  ObjStr,  ValueEnter, "False");
 }
 public static void SetValue(String sTestName, WebElement Obj, String ObjStr, String ValueEnter, String Optional) throws HeadlessException, IOException, AWTException { 
  
  IO.PrintLog(sTestName,"\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, "SetValue(" + ObjStr + ", " + ValueEnter + ")");
  try {
   WebDriver lDriver = UI.getDriver(sTestName); 
  
            
   if (ObjStr.contains("PopUp") && lDriver.findElement(By.tagName("iframe")).getTagName()!=null ) 
    lDriver.switchTo().frame(1);
   
   if (UI.CheckExist(Obj).contains("True")) {
    UI.ForceEnter(lDriver, Obj, ValueEnter); 
    
     lDriver.switchTo().defaultContent();
   } else
    if (!Optional.equals("True")) 
     Report.TestPoint(sTestName, "Object Not Found :" + ObjStr, "True", "False", true);
   
  } catch(Exception e) {
   
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
  }
   
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
    }
 public static boolean ValidateTitle(String sTestName, String expVal, String toContinue) throws HeadlessException, IOException, AWTException {  boolean retVal=false;
  if (toContinue.equals("True")) { retVal = Report.ValidationPoint(sTestName, "Validate Title", expVal, GetTitle(sTestName), true); }
  else { 
   
   retVal = Report.TestPoint(sTestName, "Validate Title", expVal, GetTitle(sTestName), true); }
  return retVal;
 }

 public static boolean ValidateBrwsr_URL(String sTestName, String expBrowserURL) throws HeadlessException, IOException, AWTException {  
  boolean retVal=false;
   return retVal;
 }
 
 public static String GetTitle(String sTestName) throws HeadlessException, IOException, AWTException {

  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  IO.PrintLog(sTestName, "GetTitle - Current Browser");
  String title="";
  try {
   WebDriver lDriver = UI.getDriver(sTestName); 
   title = lDriver.getTitle(); 
   
   Report.OperationPoint(sTestName, "GetTitle =" + title + ")");
  } catch(Exception e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
   
   
  }    
  
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
  return title;
    }
 public static boolean ValidateContent(String sTestName, WebElement Obj, String ObjStr, String expVal, String toContinue) throws HeadlessException, IOException, AWTException {
  boolean retVal = false;
  if (toContinue.equals("True")) { retVal = Report.ValidationPoint(sTestName, "Validate Content", expVal, GetContent(sTestName, Obj, ObjStr), true); }
  else { retVal = Report.TestPoint(sTestName, "Validate Content", expVal, GetContent(sTestName, Obj, ObjStr), true); }
  return retVal;
 }
 public static boolean ValidateTextInContent(String sTestName, WebElement Obj, String ObjStr, String expVal, String toContinue) throws HeadlessException, IOException, AWTException {
  boolean retVal = false;
  
  String ActualValue = GetContent(sTestName, Obj, ObjStr);   
  if (toContinue.equals("True")) {       
   
   retVal = Report.ValidationPoint(sTestName, "Validate Text In Content - "+expVal, String.valueOf(ActualValue).contains(expVal), true); }
  else { retVal = Report.TestPoint(sTestName, "Validate Text In Content - "+expVal, String.valueOf(ActualValue).contains(expVal), true); }
  return retVal;
 }
 public static String GetContent(String sTestName, WebElement Obj, String ObjStr) throws HeadlessException, IOException, AWTException {

  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  IO.PrintLog(sTestName, "GetContent(" + ObjStr + ")");
  String text="";
  try {
   WebDriver lDriver = UI.getDriver(sTestName); 
   
   text = Obj.getText(); text = text.replace("\n", " "); Report.OperationPoint(sTestName, "GetContent(" + ObjStr + ") = " + text);
  } catch(Exception e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
  }
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
  return text;
    }
 public static void Print(String sTestName, String msg) throws HeadlessException, IOException, AWTException  {
  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, msg);
  IO.PrintLog(sTestName, " ---------------------------------------------------------------------------------------------------\n");
 }
 public static void Action_MouseOver(String sTestName, WebElement Obj, String ObjStr) throws HeadlessException, IOException, AWTException {

  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  IO.PrintLog(sTestName, "Action_MouseOver(" + ObjStr + ")");
  try {
   WebDriver lDriver = UI.getDriver(sTestName); 
   
   Actions action = new Actions(lDriver);
   action.moveToElement(Obj); action.perform(); Thread.sleep(2000L);
   
   Report.ValidationPoint(sTestName, "Check the existance of Elements on Mouse over", UI.CheckExist(Obj), "True", true);
   //Report.OperationPoint(sTestName, "Action_MouseOver(" + ObjStr + ")");
  } catch(Exception e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
  }
   
  
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
    }
 
 public static void Action_MouseClick(String sTestName, WebElement Obj, String ObjStr) throws HeadlessException, IOException, AWTException {

  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  IO.PrintLog(sTestName, "Action_MouseClick(" + ObjStr + ")");
  try {WebDriver lDriver = UI.getDriver(sTestName);Actions action = new Actions(lDriver);
   action.moveToElement(Obj); 
   
   Obj.isDisplayed();
   action.click(); 
   action.perform(); 
   Thread.sleep(1000L);
   Report.ValidationPoint(sTestName, "Check the existance of Elements on Mouse click", UI.CheckExist(Obj), "True", true);
   
   //Report.OperationPoint(sTestName, "Action_MouseClick(" + ObjStr + ")");
   } 
  catch(Exception e) 
  
  
  {Report.TestPoint(sTestName, e.getMessage(), "True", "False", true); }
  
  
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
    }
 //***************************************
 public static boolean Action_ValidateLink(String sTestName, WebElement Obj, String ObjStr, String toContinue) throws HeadlessException, IOException, AWTException {
  
  boolean retVal = false;    
  String expVal = GetAttribute(sTestName, Obj, ObjStr, "href"); 
  try {WebDriver lDriver = UI.getDriver(sTestName);Actions action = new Actions(lDriver);
  action.moveToElement(Obj); action.click(); action.perform(); Thread.sleep(2000L);}
  catch(Exception e) {Report.TestPoint(sTestName, e.getMessage(), "True", "False", true); }
  
   
  WebDriver driver = UI.getDriver(sTestName);  
  String actualurl = driver.getCurrentUrl();  
  if (toContinue.equals("True")) { retVal = Report.ValidationPoint(sTestName, "Validate Link", expVal, actualurl, true);}
  else { retVal = Report.TestPoint(sTestName, "Validate Link", expVal, actualurl, true); } 
  
        driver.navigate().back();
  return retVal;
 }
 public static String GetAttribute(String sTestName, WebElement Obj, String ObjStr, String AttribName) throws HeadlessException, IOException, AWTException {

  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  IO.PrintLog(sTestName, "GetLink(" + ObjStr + ")");
  String AttribVal="";
  try {WebDriver lDriver = UI.getDriver(sTestName); 
   AttribVal = Obj.getAttribute(AttribName);} 
  catch(Exception e) {Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);}
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
  return AttribVal;
    }
 //***************************************
 public static void SwitchToFrame(String sTestName) throws HeadlessException, IOException, AWTException {

  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  IO.PrintLog(sTestName, "SwitchToFrame()");
  try {
   WebDriver lDriver = UI.getDriver(sTestName); 
   
   lDriver.switchTo().frame(1);
   Report.OperationPoint(sTestName, "SwitchToFrame()");
  } catch(Exception e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
  }
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
    }
 public static void SwitchToBrowser(String sTestName) throws HeadlessException, IOException, AWTException { 
  
  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  IO.PrintLog(sTestName, "SwitchToBrowser()");
  try {
   WebDriver lDriver = UI.getDriver(sTestName); 
   //String parentWindow = lDriver.getWindowHandle(); 
   
   for(String winHandle : lDriver.getWindowHandles()){
       lDriver.switchTo().window(winHandle);
   } 
   
   UI.putDriver(sTestName, lDriver);
   Report.OperationPoint(sTestName, "SwitchToBrowser()");
  } catch(Exception e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
  }
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
  
 }
 public static void CloseBrowser(String sTestName) throws HeadlessException, IOException, AWTException { 
  
  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  IO.PrintLog(sTestName, "CloseChildBrowser()");
  try {
   WebDriver lDriver = UI.getDriver(sTestName); 
   
      lDriver.close();   
       
   Report.OperationPoint(sTestName, "CloseChildBrowser()");
  } catch(Exception e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
  }
  IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
  
 }
 
 
 public static void HandleBrowserPopupOK(String sTestName)throws HeadlessException, IOException, AWTException {
  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  IO.PrintLog(sTestName, "Click OK On PopUp");
  
  try{
   WebDriver lDriver = UI.getDriver(sTestName);
  Alert alert = lDriver.switchTo().alert();     
  alert.accept();
  Report.OperationPoint(sTestName, "Clicked On OK of PopUp");
                                  
  }                                 
    catch(Exception e) {
    Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);}
  
   IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
 }
 public static void HandleBrowserPopupCancel(String sTestName)throws HeadlessException, IOException, AWTException {
  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  IO.PrintLog(sTestName, "Click Cancel On PopUp");
  
  try{
   WebDriver lDriver = UI.getDriver(sTestName);
  Alert alert = lDriver.switchTo().alert();     
   alert.dismiss();
    Report.OperationPoint(sTestName, "Clicked on Cancel of PopUp");
                                  
  }                                 
    catch(Exception e) {
    Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);}
  
   IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
 }
 
 
 
 public static void RightClick(String sTestName, WebElement Obj, String ObjStr)throws HeadlessException, IOException, AWTException {
	  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
	  IO.PrintLog(sTestName, "window.scrollBy(0,50)");
	  
	  try{
	   WebDriver lDriver = UI.getDriver(sTestName);
	   Actions builder = new Actions(lDriver);
	   builder.moveToElement(Obj);
	   builder.contextClick(Obj).build().perform(); 
	
	      
	   
	  
	  }                                 
	    catch(Exception e) {
	    Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);}
	  
	   IO.PrintLog(sTestName," ---------------------------------------------------------------------------------------------------\n");
	 }


}