
package Supporting;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;

@SuppressWarnings

("all")

public class UI {

public static int iObjTimeOut;
static Report Report;
static IO IO;
public static int iCurrIter;
public static WebDriver driver;
public static WebDriverWait waitVar;
public static String sTemp = "";
public static int iTemp;
public static ITestContext testContext;

public UI(ITestContext testContext){
this.testContext=testContext;

}

public static void putDriver(String testName, WebDriver driver1) {
Report.sTestName_Driver.put(testName, driver1);

}

public static WebDriver getDriver(String testName) {

return Report.sTestName_Driver.get(testName);

}

public static String CheckExist(WebElement objDesc) throws Exception {

String sRes=

"False";

try{

if (objDesc.getTagName() != null)

{ sRes ="True"; }

}

catch(Exception e) {

return sRes;

}

return sRes;

}

public static void HighlightElement(WebDriver driver, WebElement elmElement) {

for (int i = 0; i < 2; i++) {

JavascriptExecutor oJs = (JavascriptExecutor) driver;

oJs.executeScript(

"arguments[0].setAttribute('style', arguments[1]);",

elmElement,

"color: yellow; border: 2px solid yellow;");

oJs.executeScript(

"arguments[0].setAttribute('style', arguments[1]);",

elmElement,

"");

}

}

public static void printMsg(String sMsg) {

System.

out.println(sMsg);

}

public static Boolean WaitForObject(WebElement elmEle, int iObjTimeOut) {

try {

WebDriverWait oWait =

new WebDriverWait(driver, iObjTimeOut);

oWait.until(ExpectedConditions.visibilityOf(elmEle));

return true;

}

catch (Exception e) {

return false;

}

}

public static void ForceClick(WebDriver driver, WebElement elmEle) {

JavascriptExecutor executor = (JavascriptExecutor) driver;

executor.executeScript(

"arguments[0].click();", elmEle);

}

public static void ForceEnter(WebDriver driver, WebElement ele, String val) {

JavascriptExecutor executor = (JavascriptExecutor) driver;

executor.executeScript(

"arguments[0].value="+val+";", ele);

}

public static WebElement FindObj_WithAttribute(List <WebElement> ele, String atribName, String atribValue, boolean exct) {

WebElement eR =

null;

for(WebElement e1 : ele) {

eR = e1;

if (exct) {

if (e1.getAttribute(atribName).equals(atribValue)) {break;}

}

else {

if (e1.getAttribute(atribName).contains(atribValue)) {break;}

}

}

return eR;

}

public static String NoOther_InList(List <WebElement> ele, String srchVal) {

String srchStatus =

"True";

for(WebElement e1 : ele) {

if (!e1.getText().contains(srchVal)) {

UI.printMsg(e1.getText() +

"--" + srchVal);

srchStatus =

"False"; break;}

}

return srchStatus;

}

public static String getAlertText(WebDriver driver) {

try {

return driver.switchTo().alert().getText();

}

catch (NoAlertPresentException Ex) {

return "False";

}

}

public static String VerifyNotInTable(String sSrchVal, WebElement elmEle, int iRw, int iCl) {

String sFlag =

"True";

if (iRw!=0) {

WebElement row = elmEle.findElements(By.tagName(

"tr")).get(iRw-1);

String sSVal2 = row.findElements(By.tagName(

"td")).get(iCl).getText();

if (!sSVal2.contains(sSrchVal)) { sFlag = "False"; }

}

else {

List<WebElement> rows = elmEle.findElements(By.tagName(

"tr"));

for(WebElement row : rows) {

String sSVal2 = row.findElements(By.tagName(

"td")).get(iCl-1).getText().trim();

if (!sSVal2.contains(sSrchVal)) { sFlag = "False"; break;}

}

}

return sFlag;

}

public static String VerifyInMultipleTables(String sSrchVal, List<WebElement> elmEle, int iCl) {

String sFlag =

"False";

for(WebElement tbl : elmEle) {

List<WebElement> rows = tbl.findElements(By.tagName(

"tr"));

for(WebElement row : rows) {

List<WebElement> cols = row.findElements(By.tagName(

"td"));

String sSVal2 = cols.get(iCl-1).getText().trim();

if (sSrchVal.equalsIgnoreCase(sSVal2)) { sFlag = "True"; break;}

}

}

return sFlag;

}

public static String VerifyInTable(String sSrchVal, WebElement elmEle, int iRw, int iCl) {

String sFlag =

"False";

if (iRw!=0) {

WebElement row = elmEle.findElements(By.tagName(

"tr")).get(iRw-1);

String sSVal2 = row.findElements(By.tagName(

"td")).get(iCl).getText();

if (sSrchVal.equalsIgnoreCase(sSVal2)) { sFlag = "True"; }

}

else {

List<WebElement> rows = elmEle.findElements(By.tagName(

"tr"));

for(WebElement row : rows) {

List<WebElement> cols = row.findElements(By.tagName(

"td"));

String sSVal2 = cols.get(iCl-1).getText().trim();

if (sSrchVal.equalsIgnoreCase(sSVal2)) { sFlag = "True"; break;}

}

}

return sFlag;

}

public static String GetColumn_Value(WebElement elmEle, int iRw, int iCl) {

WebElement row = elmEle.findElements(By.tagName(

"tr")).get(iRw-1);

String sFlag = row.findElements(By.tagName(

"td")).get(iCl).getText();

return sFlag;

}

public static int GetRow_WithText(String sSrchVal, WebElement elmEle, int iCl) {

int iRW = 0;

List<WebElement> rows = elmEle.findElements(By.tagName(

"tr"));

for (int iItr=0; iItr<rows.size(); iItr++) {

String sSVal2 = rows.get(iItr).getText().trim();

if (sSVal2.contains(sSrchVal)) { iRW = iItr+1; break;}

}

return iRW;

}

public static String VerifyText_WithString_Tables(String sSrchVal, String sMtchVal, List<WebElement> elmEle, int iSrchCl, int iMtchCl) {

String sFlag =

"Not Found";

for(WebElement tbl : elmEle) {

List<WebElement> rows = tbl.findElements(By.tagName(

"tr"));

for(WebElement row : rows) {

List<WebElement> cols = row.findElements(By.tagName(

"td"));

String sSVal2 = cols.get(iSrchCl-1).getText().trim();

if (sSrchVal.equalsIgnoreCase(sSVal2)) {

sFlag =

"True";

String val3 = cols.get(iMtchCl-1).getText().trim();

if (!sMtchVal.equalsIgnoreCase(val3)) { sFlag = "False"; break;}

}

}

}

return sFlag;

}

public static String VerifyNotInDivList(String sSrchVal, String sSubLocator, WebElement elmEle, int iRw, int iCl) {

String sFlag =

"True";

if (iRw!=0) {

WebElement row = elmEle.findElements(By.cssSelector(sSubLocator)).get(iRw-1);

String sSVal2 = row.findElements(By.tagName(

"div")).get(iCl).getText();

if (!sSVal2.contains(sSrchVal)) { sFlag = "False"; }

}

else {

List<WebElement> rows = elmEle.findElements(By.cssSelector(sSubLocator));

for(WebElement row : rows) {

String sSVal2 = row.findElements(By.tagName(

"div")).get(iCl-1).getText().trim();

if (!sSVal2.contains(sSrchVal)) { sFlag = "False"; break;}

}

}

return sFlag;

}

public static String VerifyInDivList(String sSrchVal, String sSubLocator, WebElement elmEle, int iRw, int iCl) {

String sFlag =

"False";

if (iRw!=0) {

WebElement row = elmEle.findElements(By.cssSelector(sSubLocator)).get(iRw-1);

String sSVal2 = row.findElements(By.tagName(

"div")).get(iCl).getText();

if (!sSVal2.contains(sSrchVal)) { sFlag = "True"; }

}

else {

List<WebElement> rows = elmEle.findElements(By.cssSelector(sSubLocator));

for(WebElement row : rows) {

String sSVal2 = row.findElements(By.tagName(

"div")).get(iCl-1).getText().trim();

if (sSVal2.contains(sSrchVal)) { sFlag = "True"; break;}

}

}

return sFlag;

}

public static String VerifyText_WithString_Div(String sSrchVal, String sMtchVal, String sSubLocator, WebElement elmEle, int iSrchCl, int iMtchCl) {

String sFlag =

"Not Found";

List<WebElement> rows = elmEle.findElements(By.cssSelector(sSubLocator));

for(WebElement row : rows) {

String sSVal2 = row.findElements(By.tagName(

"div")).get(iSrchCl-1).getText().trim();

if (sSrchVal.equalsIgnoreCase(sSVal2)) {

sFlag =

"True";

String val3 = row.findElements(By.tagName(

"div")).get(iMtchCl-1).getText().trim();

if (!sMtchVal.equalsIgnoreCase(val3)) { sFlag = "False"; break;}

}

}

return sFlag;

}

public static String Verify_NoOther_InDivList(String sSrchVal, String sSubLocator, WebElement elmEle, int iRw, int iCl) {

String sFlag =

"True";

if (iRw!=0) {

WebElement row = elmEle.findElements(By.cssSelector(sSubLocator)).get(iRw-1);

String sSVal2 = row.findElements(By.tagName(

"div")).get(iCl).getText();

if (!sSVal2.contains(sSrchVal)) { sFlag = "False"; }

}

else {

List<WebElement> rows = elmEle.findElements(By.cssSelector(sSubLocator));

Boolean bRFlag;

for(WebElement row : rows) {

bRFlag =

true;

String sSVal2 = row.findElements(By.tagName(

"div")).get(iCl-1).getText().trim();

for (String retval: sSrchVal.split("|")){

if (!sSVal2.contains(retval)) { bRFlag = false;}

}

if (!bRFlag) { sFlag = "False"; break;}

}

}

return sFlag;

}

public static String GetColumnTotal(WebElement elmEle, int iCl) {

float sumC = 0;

List<WebElement> rows = elmEle.findElements(By.tagName(

"tr"));

for(WebElement row : rows) {

String sSVal2 = row.findElements(By.tagName(

"td")).get(iCl-1).getText().trim();

sSVal2 = sSVal2.replace(

"$", "");

sumC = sumC + Float.valueOf(sSVal2);

}

return String.valueOf(sumC);

}

public static void SelectMenuOption(WebDriver driver, WebElement ele1, WebElement ele2) throws Exception {

Actions action =

new Actions(driver);

//action.moveToElement(ele1).perform();

//Thread.sleep(5000L);

//ele2.Click;

UI.ForceClick(driver, ele2);

Thread.sleep(5000L);

}

public static boolean acceptAlert(WebDriver driver) {

try

{

driver.switchTo().alert().accept();

return true;

}

catch (NoAlertPresentException Ex)

{

return false;

}

}

public static String hasValue(String sVal) {

String sFlag =

"False";

if (!sVal.equals("")) {

sFlag =

"True";

}

return sFlag;

}

public static String getFontColor(WebElement elmEle) {

String sColor = elmEle.getCssValue(

"color");

String[] numbers = sColor.replace(

"rgba(", "").replace(")", "").split(",");

int iR = Integer.parseInt(numbers[0].trim()); int iG = Integer.parseInt(numbers[1].trim()); int iB = Integer.parseInt(numbers[2].trim());

String hex = String.format(

"#%02x%02x%02x", iR, iG, iB).toUpperCase();

return hex;

}

public static int randInt(int min, int max) {

Random rand =

new Random();

int randomNum = rand.nextInt((max - min) + 1) + min;

return randomNum;

}

}