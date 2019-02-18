package Supporting;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List; 

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.james.mime4j.field.datetime.DateTime;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.Assert;

@SuppressWarnings("all")
public class Report {
 
 //Global variable declaration
 public static String reportFile, iterStatus, testStatus, screenshotFile, exeBy, fmtRepData, imgSize;
 private static Integer iter, itrPassPts, itrFailPts, testReportCtr=1;
 static Hashtable<String, String> tcReport_TC_Map = new Hashtable<String, String>();
 static Hashtable<String, String> openReport = new Hashtable<String, String>();
 static Hashtable<String, String> closeReport = new Hashtable<String, String>();
 static Hashtable<String, String> tcChildReport_TC_Map = new Hashtable<String, String>();
 static Hashtable<String, Integer> stepCount = new Hashtable<String, Integer>();
 static Hashtable<String, Long> stepTimer = new Hashtable<String, Long>();
 static DecimalFormat twoDecimalDigits = new DecimalFormat ("0.00");
 public static Hashtable<String, WebDriver> sTestName_Driver = new Hashtable<String, WebDriver>();
 static Hashtable<String, String> sTestName_Status = new Hashtable<String, String>();
 
 public static String newtimeStamp = new SimpleDateFormat(
   "MM-dd-yyyy hh.mm.ss").format(Calendar.getInstance().getTime());
 
 private static void overwriteFile(String fileName, String data, String methodName) throws IOException {

  File f = null; 
  String folderPath = IO.sExe_Fldr + "\\" + fileName;
  
  if (!data.substring(0, 17).equals("<font color=blue>")) {  // For TC Report 
   
   //if (Utilities.StoreCopy_Env_Values.get("ReportsInFolders").equals("No")) {
   // folderPath = Utilities.StoreCopy_Env_Values.get("ReportsFolder") + "\\Execution_" + newtimeStamp;
   //}
   f = new File(folderPath);
   if (!(f.exists() && f.isDirectory())) {  f.mkdir();  }
   
   reportFile = folderPath + "\\" + fileName + "_" + newtimeStamp + ".html";
   tcReport_TC_Map.put(fileName, reportFile);
   String logFile = reportFile.replace(".html", ".log");
   Utilities.Map_TestName_Log.put(fileName, logFile);
   
  } else { // For Iteration Report
   folderPath = Utilities.StoreCopy_Env_Values.get("ReportsFolder") + "\\Temp";
   f = new File(folderPath); if (!(f.exists() && f.isDirectory())) {  f.mkdir();  }
   reportFile = folderPath + "\\" + fileName + ".html";
  }
  
  File file = new File(reportFile);
  FileWriter FW = new FileWriter(file, false);
  FW.write(data);
  FW.close();
 }
 
 private static void appendFile(String fileName, String data) {
  if (!fileName.contains("-")) { fileName = fileName + "-1-Iteration"; }
  String reportFile = Utilities.StoreCopy_Env_Values.get("ReportsFolder") + "\\Temp\\" + fileName + ".html";
  try {
   File file = new File(reportFile);
            BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
   output.write("\n" + data);
   output.close();
  } catch (IOException e) {
   e.printStackTrace();
  }
 }

 private static String readFile(String fileName, Boolean withHead) {
  String reportFile = fileName;
  String data = "";
        try {
         File file = new File(reportFile);
            BufferedReader reader = new BufferedReader(new FileReader(file));
      String line = "";
            while((line = reader.readLine()) != null) {
                data += line + "\r\n";
            }
            reader.close();
          } catch ( IOException e ) {
             e.printStackTrace();
          }
  if (withHead) { data = StringUtils.substringAfter(data, "</script></head>"); }
        return data;
 }

 public static void CloseIteration(String file) throws IOException, ParseException {
  if (!file.contains("-")) { file = file + "-1-Iteration"; }
  String data = readFile(Utilities.StoreCopy_Env_Values.get("ReportsFolder") + "\\Temp\\" + file + ".html", false);
  
  String time = StringUtils.substringBetween(data, "<font color=blue>", "</font>"); Long stTime = Long.valueOf(time);
  Long entime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - stTime);
  data = StringUtils.replace(data, "<font color=blue>"+time+"</font>", "<font color=blue>"+entime+"</font>");
  Integer passPts = StringUtils.countMatches(data, "<Font COLOR=Green>Pass</Font>");
  Integer failPts = StringUtils.countMatches(data, "<Font COLOR=Red>Fail</Font>");
  iterStatus = (failPts>0) ? iterStatus = "<Font COLOR=Red>Fail</Font>" : "<Font COLOR=Green>Pass</Font>";
  data = data.replaceAll("ITR_STATUS", iterStatus);
  data = data.replace("ITR_PASSPTS", passPts.toString());
  data = data.replace("ITR_FAILPTS", failPts.toString());
  data = data.replace("ITR_EXE_TIME", DurationFormatUtils.formatDuration(entime*1000, "mm:ss") + " Mins");
  data = data + "</table><br/></div>";
  overwriteFile(file, data, "");
 } 
    
 public static void Consolidation(String file) throws IOException, ParseException {
  
  String tcData, itrData, tcName, itrInfo, testStatus, tcReportFile;
     Integer exeTime = 0;

  tcName = tcChildReport_TC_Map.get(file); tcReportFile = tcReport_TC_Map.get(tcName);
     testStatus = "Pass"; tcData = readFile(tcReportFile, false);
  
  itrData = readFile(Utilities.StoreCopy_Env_Values.get("ReportsFolder") + "\\Temp\\" + file + ".html", false);
  String time = StringUtils.substringBetween(itrData, "<font color=blue>", "</font>"); exeTime = exeTime + Integer.valueOf(time);
  itrData = StringUtils.substringAfter(itrData, "</style></head>");
  
  Integer failIdx = StringUtils.indexOf(itrData, "<td width=\"19%\"><Font COLOR=Red>Fail</Font></td>");
  if ((failIdx > 0) && (testStatus.equals("Pass"))) { testStatus = "Fail"; }
  tcData = tcData + itrData;
  
  String sTCStatus = StringUtils.substringBetween(tcData, "</td><td width=\"12%\">Status: ", "</td><td width=\"25%\">");
  String sTCStatus_Replace = "<Font COLOR=Red></Font><Font COLOR=Green>Pass</Font>";
  if (testStatus.equals("Fail")) { sTCStatus_Replace = "<Font COLOR=Red>Fail</Font><Font COLOR=Green></Font>"; }
  tcData = StringUtils.replace(tcData, sTCStatus, sTCStatus_Replace);
  sTestName_Status.put(file, testStatus);

  String sTCExeTime = StringUtils.substringBetween(tcData, "<td width=\"22%\">Total Execution Time: ", "</td><td width=\"auto\">Executed By:");
  String sOldTime = sTCExeTime.replace(" Mins", ""); sOldTime = sOldTime.replace("EXE_TIME", "");
  
  String sComTime = DurationFormatUtils.formatDuration(exeTime*1000, "HH:mm:ss");
  SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss"); 
  
  if (!sOldTime.isEmpty()) {
   Date date2 = timeFormat.parse(sOldTime); 
   Calendar instance = GregorianCalendar.getInstance(); instance.setTime(date2);
   instance.add(GregorianCalendar.MILLISECOND, exeTime*1000);
   sComTime = timeFormat.format(instance.getTime());
  }
  
  tcData = StringUtils.replace(tcData, sTCExeTime, sComTime + " Mins");
  overwriteFile(tcName, tcData, "Consolidation");
 }

 private static String imgToBase64() {

  File file = new File(screenshotFile); String imgData = null;
     try {          
      FileInputStream imageInFile = new FileInputStream(file);
      byte imageData[] = new byte[(int) file.length()];
      imageInFile.read(imageData); imgData = Base64.encodeBase64String(imageData); imageInFile.close();
     } catch (FileNotFoundException e) {
      IO.PrintLog("", "Image not found" + e);
     } catch (IOException ioe) {
      IO.PrintLog("", "Exception while reading the Image " + ioe);
     }
     return imgData;
    }
 
 private static String TakeScreenshot(WebDriver driver) throws IOException, HeadlessException, AWTException  { 
     //String path;
     String screenshotBase64 = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
  //File source = ((TakesScreenshot)UI.driver).getScreenshotAs(OutputType.FILE);
  //FileUtils.copyFile(source, new File(screenshotFile));
         
  //return imgToBase64();
  return screenshotBase64;
 }
 
 public static void Start(String tcName) throws IOException {
  
  if (!openReport.containsKey(tcName)) {
   
   String strHTML = ""; String dt = new SimpleDateFormat("MM-dd-yyyy hh:mm").format(new Date());
   strHTML = strHTML + "<html><head><Title>" + tcName + "</Title>"
     + "<script type=\"text/javascript\">function toggle2(id, link) {var e = document.getElementById(id);"
     + "if (e.style.display == '') {e.style.display = 'none';link.innerHTML = '[ + ]';} else {e.style.display = ''; "
     + "link.innerHTML = '[ - ]'; }  }var PopupImageContainer = new Image();var PopupImageCaption = new String();"
     + "var PopupImageSRC = new String();function PopImage(caption, imagesrc) {if( length.imagesrc < 1 ) { return; }"
     + "var loadDelay = PopupImageSRC.length ? 1 : 750;PopupImageSRC = imagesrc;PopupImageCaption = caption;"
     + "PopupImageContainer.src = PopupImageSRC;setTimeout(\"PopupImageDisplay()\",loadDelay);} function PopupImageDisplay() "
     + "{var iw = parseInt(PopupImageContainer.width);var ih = parseInt(PopupImageContainer.height);"
     + "var properties = 'height=' + 600 + ',width=' + 900 + ',resizable=yes,location=no,scrollbars=yes,left=500,top=100';"
     + "var picture = window.open('','',properties);picture.document.writeln('<html><head><title>' + PopupImageCaption + "
     + "'</title>');picture.document.write('<img src=\"' + PopupImageSRC + '\" width=\"' + iw + '\" height=\"' + ih + '\" "
     + "border=\"0\">');picture.document.writeln('<'+'/center><'+'/body><'+'/html>');}</script><style>#itrdata, "
     + "#itrhead {    font-family: \"Trebuchet MS\";    width: 100%;    border-collapse: collapse;}"
     + "#itrhead td {    font-size: 0.80em; font-weight: bold;    text-align: left; "
     + "padding: 1px 5px 1px 5px; border: 1px solid #E8782B;    background-color: #FFA740;    color: #ffffff;}#top td "
     + "{font-size: 0.95em; font-weight: bold; text-align: left; padding: 1px 5px 1px 5px; background-color: #ffffff; "
     + "color: #005B95;}#itrdata td, #itrdata th {    font-size: 0.75em;    border: 1px solid #E8782B;    "
     + "padding: 1px 5px 1px 5px;}#itrdata th {    font-size: 0.70em;    text-align: left;    padding-top: 1px;    "
     + "padding-bottom: 1px;    background-color: #FFBC64;}#itrdata tr.alt td {    color: #000000;    background-color: "
     + "#FFE5BE;}</style></head>"
     + "<table id=\"top\"><tr><td width=\"25%\">Test Name: "+ tcName +"</td><td width=\"12%\">"
     + "Status: <Font COLOR=Red>Fail</Font><Font COLOR=Green></Font></td><td width=\"25%\"><b>Execution Date: "+ dt
     + "</td><td width=\"22%\">Total Execution Time: EXE_TIME</td><td width=\"auto\">Executed By: "+ exeBy
     + "</td></tr></table>&nbsp;";
   
   overwriteFile(tcName, strHTML, "start");
   testStatus = "Pass";
   openReport.put(tcName, "Y");
  }
 }
 
 public static void PrintIteration(String file, String brwsrName, String iterNum) throws IOException {
  
  stepCount.put(file,  1); stepTimer.put(file, System.currentTimeMillis());
  String strHTML = "<font color=blue>" + System.currentTimeMillis() + "</font>"; 
   strHTML = strHTML + "<html><head><Title>" + file + "</Title>"
     + "<script type=\"text/javascript\">function toggle2(id, link) {var e = document.getElementById(id);"
     + "if (e.style.display == '') {e.style.display = 'none';link.innerHTML = '[ + ]';} else {e.style.display = ''; "
     + "link.innerHTML = '[ - ]'; }  }var PopupImageContainer = new Image();var PopupImageCaption = new String();"
     + "var PopupImageSRC = new String();function PopImage(caption, imagesrc) {if( length.imagesrc < 1 ) { return; }"
     + "var loadDelay = PopupImageSRC.length ? 1 : 750;PopupImageSRC = imagesrc;PopupImageCaption = caption;"
     + "PopupImageContainer.src = PopupImageSRC;setTimeout(\"PopupImageDisplay()\",loadDelay);} function PopupImageDisplay() "
     + "{var iw = parseInt(PopupImageContainer.width);var ih = parseInt(PopupImageContainer.height);"
     + "var properties = 'height=' + 600 + ',width=' + 900 + ',resizable=yes,location=no,scrollbars=yes,left=500,top=100';"
     + "var picture = window.open('','',properties);picture.document.writeln('<html><head><title>' + PopupImageCaption + "
     + "'</title>');picture.document.write('<img src=\"' + PopupImageSRC + '\" width=\"' + iw + '\" height=\"' + ih + '\" "
     + "border=\"0\">');picture.document.writeln('<'+'/center><'+'/body><'+'/html>');}</script><style>#itrdata, "
     + "#itrhead {    font-family: \"Trebuchet MS\";    width: 100%;    border-collapse: collapse;}"
     + "#itrhead td {    font-size: 0.80em; font-weight: bold;    text-align: left; "
     + "padding: 1px 5px 1px 5px; border: 1px solid #E8782B;    background-color: #FFA740;    color: #ffffff;}#top td "
     + "{font-size: 0.95em; font-weight: bold; text-align: left; padding: 1px 5px 1px 5px; background-color: #ffffff; "
     + "color: #005B95;}#itrdata td, #itrdata th {    font-size: 0.75em;    border: 1px solid #E8782B;    "
     + "padding: 1px 5px 1px 5px;}#itrdata th {    font-size: 0.70em;    text-align: left;    padding-top: 1px;    "
     + "padding-bottom: 1px;    background-color: #FFBC64;}#itrdata tr.alt td {    color: #000000;    background-color: "
     + "#FFE5BE;}</style></head>"
     + "<table id=\"itrhead\"><tr><td width=\"19%\">Iteration "+iterNum+"</td><td width=\"19%\">"+ brwsrName + "</td><td width=\"19%\">"
     + "ITR_STATUS</td><td width=\"19%\">Execution Time: ITR_EXE_TIME</td><td width=\"19%\">Validation Points "
     + "(Pass - <Font COLOR=Green>ITR_PASSPTS</Font> / Fail- <Font COLOR=Red>ITR_FAILPTS</Font> )</td><td>&nbsp;&nbsp;&nbsp;<a href=\"#\" "
     + "onclick=\"toggle2('content"  + file + "', this)\">[ + ]</a></td></tr></table>&nbsp;"
     + "<div id=\"content"  + file + "\" style=\"display:none\"><table id=\"itrdata\"><tr><th width=\"2.5%\">"
     + "#</th><th width=\"4%\">Timer</th><th width=\"30%\">Step Details</th><th width=\"30%\">Expected</th><th width=\"30%\">Actual</th>"
     + "<th width=\"3.5%\">Status</th></tr>";

  overwriteFile(file, strHTML, "");
  String tcName = file.substring(0, file.indexOf("-"));
  
  tcChildReport_TC_Map.put(file, tcName);
  /*if (closeReport.containsKey(tcName)) {
   String oldVal = closeReport.get(tcName); 
   closeReport.remove(tcName); 
   closeReport.put(tcName, oldVal+"|"+file); 
  } else {
   closeReport.put(tcName, file);
  }*/
  iterStatus = "Pass";
 }
 
 public static boolean ValidationPoint(String iterFile, String validDtls, String validEXP, String validACT, Boolean takeScrnShot) throws HeadlessException, IOException, AWTException  {
  
  String stepStatus = "Fail"; if (validEXP.equals(validACT)) { stepStatus = "Pass";  }
  String stepHTML = "<Font COLOR=Red>"+stepStatus+"</Font>"; String baseSTR = ""; String strHTML="";
  String repMsg = validDtls.replace("'",  ""); repMsg = validDtls.replace("\\r",  ""); repMsg = validDtls.replace("\\n",  "");
  repMsg = validDtls.replace("\"",  "");

  IO.PrintLog(iterFile, "\n ---------------------------------------------------------------------------------------------------");
  IO.PrintLog(iterFile, "ValidationPoint (" + validDtls + ", " + validEXP + ", " + validACT + ")");
  
  IO.PrintLog(iterFile, "\n Validation Point: " + stepStatus + "\n");
  IO.PrintLog(iterFile, "Desc: " + validDtls + " - Expected - [" + validEXP + "] & Actual - [" + validACT + "]");

  if (stepStatus.equals("Pass")) { stepHTML = "<Font COLOR=Green>"+stepStatus+"</Font>";  }
  if (takeScrnShot) { 
   WebDriver driverSS = UI.getDriver(iterFile);  
   baseSTR = TakeScreenshot(driverSS); 
   validDtls = validDtls.replaceAll("\"", "");
   stepHTML = "<a href=\"#\" alt=\"Click For Screenshot\""
   + " onclick=\"PopImage('"+ validDtls.replace("'",  "") + " [" + stepStatus + "]', 'data:image/png;base64," + baseSTR + "')\" />"
   + stepHTML + "</a>";
  }  

  Integer stepCtr = ((Integer)stepCount.get(iterFile));
  long stepTime = System.currentTimeMillis()-(Long) stepTimer.get(iterFile);
  String stpTmr = DurationFormatUtils.formatDuration(stepTime, "mm:ss:SSS");
  strHTML = (stepCtr%2 == 0) ? "<tr>" : "<tr class=\"alt\">"; 
  strHTML = strHTML + "<td>"+stepCtr+"</td><td>"+stpTmr+"</td>"
    + "<td>Validation Point - "+validDtls+"</td><td>"+validEXP+"</td><td>"+validACT+"</td><td>"+stepHTML+"</td></tr>";
  appendFile(iterFile, strHTML); stepCtr++;
  stepCount.put(iterFile, stepCtr);
  IO.PrintLog(iterFile, "\n ---------------------------------------------------------------------------------------------------");
  return validEXP.equals(validACT);
  //stepTimer.put(iterFile, System.currentTimeMillis());
 }
 
 
 
public static boolean ValidationPoint(String iterFile, String validDtls, Boolean validACT, Boolean takeScrnShot) throws HeadlessException, IOException, AWTException  {
  
     String validEXP ="true";
 
  String stepStatus = "Fail";
  
  if (validEXP.equals(validACT)) 
  { stepStatus = "Pass";  }
  String stepHTML = "<Font COLOR=Red>"+stepStatus+"</Font>"; String baseSTR = ""; String strHTML="";
  String repMsg = validDtls.replace("'",  ""); repMsg = validDtls.replace("\\r",  ""); repMsg = validDtls.replace("\\n",  "");
  repMsg = validDtls.replace("\"",  "");

  IO.PrintLog(iterFile, "\n ---------------------------------------------------------------------------------------------------");
  IO.PrintLog(iterFile, "ValidationPoint (" + validDtls + ", " + validEXP + ", " + validACT + ")");
  
  IO.PrintLog(iterFile, "\n Validation Point: " + stepStatus + "\n");
  IO.PrintLog(iterFile, "Desc: " + validDtls + " - Expected - [" + validEXP + "] & Actual - [" + validACT + "]");

  if (stepStatus.equals("Pass")) { stepHTML = "<Font COLOR=Green>"+stepStatus+"</Font>";  }
  if (takeScrnShot) { 
   WebDriver driverSS = UI.getDriver(iterFile);  
   baseSTR = TakeScreenshot(driverSS); 
   validDtls = validDtls.replaceAll("\"", "");
   stepHTML = "<a href=\"#\" alt=\"Click For Screenshot\""
   + " onclick=\"PopImage('"+ validDtls.replace("'",  "") + " [" + stepStatus + "]', 'data:image/png;base64," + baseSTR + "')\" />"
   + stepHTML + "</a>";
  }  

  Integer stepCtr = ((Integer)stepCount.get(iterFile));
  long stepTime = System.currentTimeMillis()-(Long) stepTimer.get(iterFile);
  String stpTmr = DurationFormatUtils.formatDuration(stepTime, "mm:ss:SSS");
  strHTML = (stepCtr%2 == 0) ? "<tr>" : "<tr class=\"alt\">"; 
  strHTML = strHTML + "<td>"+stepCtr+"</td><td>"+stpTmr+"</td>"
    + "<td>Validation Point - "+validDtls+"</td><td>"+validEXP+"</td><td>"+validACT+"</td><td>"+stepHTML+"</td></tr>";
  appendFile(iterFile, strHTML); stepCtr++;
  stepCount.put(iterFile, stepCtr);
  IO.PrintLog(iterFile, "\n ---------------------------------------------------------------------------------------------------");
  return validEXP.equals(validACT);
  //stepTimer.put(iterFile, System.currentTimeMillis());
 }
 
 
 
 public static boolean TestPoint(String iterFile, String validDtls, String validEXP, String validACT, Boolean takeScrnShot) throws HeadlessException, IOException, AWTException  {

  String stepStatus = "Fail"; if (validEXP.equals(validACT)) { stepStatus = "Pass";  }
  String stepHTML = "<Font COLOR=Red>"+stepStatus+"</Font>"; String baseSTR = ""; String strHTML="";
  String repMsg = validDtls.replace("'",  ""); repMsg = validDtls.replace("\\r",  ""); repMsg = validDtls.replace("\\n",  "");
  repMsg = validDtls.replace("\"",  "");
  
  if (stepStatus.equals("Pass")) { stepHTML = "<Font COLOR=Green>"+stepStatus+"</Font>";  }
  if (takeScrnShot) { 
   WebDriver driverSS = UI.getDriver(iterFile);  
   baseSTR = TakeScreenshot(driverSS); 
   validDtls = validDtls.replaceAll("\"", "");
   stepHTML = "<a href=\"#\" alt=\"Click For Screenshot\""
   + " onclick=\"PopImage('"+ validDtls.replace("'",  "") + " [" + stepStatus + "]', 'data:image/png;base64," + baseSTR + "')\" />"
   + stepHTML + "</a>";
  }  

  
  IO.PrintLog(iterFile, "\n ---------------------------------------------------------------------------------------------------");
  IO.PrintLog(iterFile, "TestPoint (" + validDtls + ", " + validEXP + ", " + validACT + ")");
  IO.PrintLog(iterFile, "\n Test Point: " + stepStatus + "\n");
  IO.PrintLog(iterFile, "Desc: " + validDtls + " - Expected - [" + validEXP + "] & Actual - [" + validACT + "]");
  
  Integer stepCtr = ((Integer)stepCount.get(iterFile));
  long stepTime = System.currentTimeMillis()-(Long) stepTimer.get(iterFile);
  String stpTmr = DurationFormatUtils.formatDuration(stepTime, "mm:ss:SSS");
  strHTML = (stepCtr%2 == 0) ? "<tr>" : "<tr class=\"alt\">"; 
  strHTML = strHTML + "<td>"+stepCtr+"</td><td>"+stpTmr+"</td>"
    + "<td>Test Point - "+validDtls+"</td><td>"+validEXP+"</td><td>"+validACT+"</td><td>"+stepHTML+"</td></tr>";
  appendFile(iterFile, strHTML); stepCtr++;
  stepCount.put(iterFile, stepCtr);
  IO.PrintLog(iterFile, "\n ---------------------------------------------------------------------------------------------------");
  if (stepStatus.equals("Fail")) { 
   Assert.fail(validDtls);
  }
  return validEXP.equals(validACT);
 }

 
 public static boolean TestPoint(String iterFile, String validDtls, Boolean validACT, Boolean takeScrnShot) throws HeadlessException, IOException, AWTException  {
  String validEXP = "true";
  String stepStatus = "Fail"; if (validEXP.equals(validACT)) { stepStatus = "Pass";  }
  String stepHTML = "<Font COLOR=Red>"+stepStatus+"</Font>"; String baseSTR = ""; String strHTML="";
  String repMsg = validDtls.replace("'",  ""); repMsg = validDtls.replace("\\r",  ""); repMsg = validDtls.replace("\\n",  "");
  repMsg = validDtls.replace("\"",  "");
  
  if (stepStatus.equals("Pass")) { stepHTML = "<Font COLOR=Green>"+stepStatus+"</Font>";  }
  if (takeScrnShot) { 
   WebDriver driverSS = UI.getDriver(iterFile);  
   baseSTR = TakeScreenshot(driverSS); 
   validDtls = validDtls.replaceAll("\"", "");
   stepHTML = "<a href=\"#\" alt=\"Click For Screenshot\""
   + " onclick=\"PopImage('"+ validDtls.replace("'",  "") + " [" + stepStatus + "]', 'data:image/png;base64," + baseSTR + "')\" />"
   + stepHTML + "</a>";
  }  

  
  IO.PrintLog(iterFile, "\n ---------------------------------------------------------------------------------------------------");
  IO.PrintLog(iterFile, "TestPoint (" + validDtls + ", " + validEXP + ", " + validACT + ")");
  IO.PrintLog(iterFile, "\n Test Point: " + stepStatus + "\n");
  IO.PrintLog(iterFile, "Desc: " + validDtls + " - Expected - [" + validEXP + "] & Actual - [" + validACT + "]");
  
  Integer stepCtr = ((Integer)stepCount.get(iterFile));
  long stepTime = System.currentTimeMillis()-(Long) stepTimer.get(iterFile);
  String stpTmr = DurationFormatUtils.formatDuration(stepTime, "mm:ss:SSS");
  strHTML = (stepCtr%2 == 0) ? "<tr>" : "<tr class=\"alt\">"; 
  strHTML = strHTML + "<td>"+stepCtr+"</td><td>"+stpTmr+"</td>"
    + "<td>Test Point - "+validDtls+"</td><td>"+validEXP+"</td><td>"+validACT+"</td><td>"+stepHTML+"</td></tr>";
  appendFile(iterFile, strHTML); stepCtr++;
  stepCount.put(iterFile, stepCtr);
  IO.PrintLog(iterFile, "\n ---------------------------------------------------------------------------------------------------");
  if (stepStatus.equals("Fail")) { 
   Assert.fail(validDtls);
  }
  return validEXP.equals(validACT);
 }
 
 
 public static void OperationPoint(String iterFile, String opnDtls) throws HeadlessException, IOException, AWTException  {

  opnDtls = opnDtls.replaceAll("\"", "");
  Integer stepCtr = ((Integer)stepCount.get(iterFile));
  long stepTime = System.currentTimeMillis()-(Long) stepTimer.get(iterFile);
  String stpTmr = DurationFormatUtils.formatDuration(stepTime, "mm:ss:SSS");
  String strHTML = ((stepCtr % 2)==0) ? "<tr class=\"alt\">" : "<tr>";
  strHTML = strHTML + "<td><Font>"+ stepCtr +"</font></td><td><Font>"+stpTmr+"</font></td>"
    + "<td colspan=4><Font>"+ opnDtls +"</font></td></tr>";
  appendFile(iterFile, strHTML); stepCtr++;
  stepCount.put(iterFile, stepCtr);
  
  IO.PrintLog(iterFile, opnDtls);
  //stepTimer.put(iterFile, System.currentTimeMillis());
 }
 
 public static String StartSuite() {
  String html="<html><head><Title>EXE_NAME</Title><style>#itrdata, #itrhead {    font-family: \"Trebuchet MS\";    width: 100%;    "
    + "border-collapse: collapse;}#itrhead td {    font-size: 0.80em; font-weight: bold;    text-align: left; padding: 1px 5px 1px 5px; "
    + "border: 1px solid #E8782B;    background-color: #FFA740;    color: #ffffff;}#top td {font-size: 0.95em; font-weight: bold; text-align: left; "
    + "padding: 1px 5px 1px 5px; background-color: #ffffff; color: #005B95;}#itrdata td, #itrdata th {    font-size: 0.75em;    "
    + "border: 1px solid #E8782B;    padding: 1px 5px 1px 5px;}#itrdata th {    font-size: 0.70em;    text-align: left;    "
    + "padding-top: 1px;    padding-bottom: 1px;    background-color: #FFBC64;}#itrdata tr.alt td {    color: #000000;    "
    + "background-color: #FFE5BE;}</style></head><table id=\"top\"><tr><td width=\"70%\">EXE_NAME</td><td width=\"20%\">"
    + "Executed By: VB0053992</td></tr></table><br/>"
    + "<table id=\"itrhead\"><tr><td width=\"33%\"><a href=ENV_FILE target=\"_blank\">Environment Info</a></td><td width=\"33%\"><a href=SUITE_FILE target=\"_blank\">"
    + "Suite Info</a></td><td width=\"33%\"><a href=PREQ_FILE target=\"_blank\">PreRequisite Status</a></td></tr></table><br/>"
    + "<table id=\"itrdata\"><tr><th width=\"2.5%\">#</th><th width=\"4%\">Timer</th><th width=\"50%\">Test Name</th><th width=\"10%\">"
    + "</th><th width=\"10%\"></th><th width=\"10%\"></th><th width=\"3.5%\">Status</th></tr>";
  String fldrNm = "Execution_" + newtimeStamp;
  File f = new File(Utilities.StoreCopy_Env_Values.get("ReportsFolder") + "\\" + fldrNm); String stat = "Skip"; 
  if (!f.exists()) { 
   IO.sReportFolder = Utilities.StoreCopy_Env_Values.get("ReportsFolder") + "\\" + fldrNm;
   
   String tmp = Utilities.StoreCopy_Env_Values.get("TestPlanFolder").replace("\\", "/");
   String tmp1 = System.getProperty("user.dir").replace("\\",  "/"); tmp1 = tmp1.substring(0, tmp1.lastIndexOf("/"));
   IO.TestAssets_Fldr_ForHTML = tmp.replace(tmp1, "..");
   tmp = Utilities.StoreCopy_Env_Values.get("ReportsFolder").replace("\\", "/");
   IO.Reports_Fldr_ForHTML = tmp.replace(tmp1, "..")+"/"+fldrNm;
 
   html = html.replaceAll("EXE_NAME", fldrNm);
   html = html.replaceAll("SUITE_FILE", "'" + IO.TestAssets_Fldr_ForHTML + "/" + Utilities.StoreCopy_Env_Values.get("TestSuiteFile") + ".xlsm'");
   html = html.replaceAll("ENV_FILE", "'" + IO.TestAssets_Fldr_ForHTML + "/" + Utilities.StoreCopy_Env_Values.get("TestSuiteFile") + ".xlsm'");
   IO.sExe_File = Utilities.StoreCopy_Env_Values.get("ReportsFolder") + "\\" + fldrNm + ".html";
   IO.sExe_Fldr = Utilities.StoreCopy_Env_Values.get("ReportsFolder") + "\\" + fldrNm;
   IO.appendFile(IO.sExe_Fldr + "\\PreRequisite.log", "");
   html = html.replaceAll("PREQ_FILE", "'" + IO.Reports_Fldr_ForHTML + "/" + "PreRequisite.log'");
   IO.appendFile(IO.sExe_File, html);
   System.out.println("Started\nExecution File - " + IO.sExe_File); stat = "Created";
  }
  return stat;
 }
 
 public static void cleanTemp() throws IOException {
  File f = new File(Utilities.StoreCopy_Env_Values.get("ReportsFolder") + "\\Temp"); FileUtils.cleanDirectory(f); 
 }
 
 public static void PrintTestReport(String sTestName) throws NumberFormatException, IOException {
  
  long stepTime = System.currentTimeMillis()-(Long) stepTimer.get(sTestName);
  String stpTmr = DurationFormatUtils.formatDuration(stepTime, "mm:ss:SSS");
  
  String tcHTML = tcReport_TC_Map.get(sTestName.substring(0, sTestName.indexOf("-"))).replace("\\", "/"); String rpFldr = Utilities.StoreCopy_Env_Values.get("ReportsFolder").replace("\\", "/");
  tcHTML = tcHTML.replace(rpFldr, IO.Reports_Fldr_ForHTML.substring(0, IO.Reports_Fldr_ForHTML.lastIndexOf("/")));
  
  String tcLog = Utilities.Map_TestName_Log.get(sTestName.substring(0, sTestName.indexOf("-"))).replace("\\", "/");
  tcLog = tcLog.replace(rpFldr, IO.Reports_Fldr_ForHTML.substring(0, IO.Reports_Fldr_ForHTML.lastIndexOf("/")));
  
  String sHTML = "<script>document.write(\"<tr\");var ctr = document.getElementById(\"itrdata\").getElementsByTagName(\"tr\").length;"
    + "var h = ((ctr%2 == 0) ? \" class='alt'>\" : \" >\");document.write(h+\"<td width='2.5%'>\"+ctr);</script>";
  sHTML = sHTML + "</td><td width=\"4%\">"+ stpTmr + "</td><td width=\"50%\">"+ sTestName.substring(0, sTestName.indexOf("-")) + "</td>"
    +"<td width=\"10%\"><a href='"+ tcLog +"' target=\"_blank\">Logs</a></td>"
    +"<td width=\"10%\"><a href='"+ tcHTML +"' target=\"_blank\">HTML</a></td>"
    +"<td width=\"10%\"><a href='"+ tcHTML.substring(0, tcHTML.lastIndexOf("/")) +"' target=\"_blank\">Folder</a></td>"
    +"<td width=\"8%\">Running</td></tr>";
  IO.appendFile(IO.sExe_File, sHTML);
 }
 
 public static void CloseTestReport(String sTestName) {

  String tcHTML = tcReport_TC_Map.get(sTestName.substring(0, sTestName.indexOf("-"))).replace("\\", "/"); String rpFldr = Utilities.StoreCopy_Env_Values.get("ReportsFolder").replace("\\", "/");
  tcHTML = tcHTML.replace(rpFldr, IO.Reports_Fldr_ForHTML.substring(0, IO.Reports_Fldr_ForHTML.lastIndexOf("/")));
  String tcStatus = (!sTestName_Status.get(sTestName).equals("Pass")) ? "<Font COLOR=Red>Fail</Font>" : "<Font COLOR=Green>Pass</Font>";
  
  String HTML1 = "<td width=\"10%\"><a href='"+ tcHTML.substring(0, tcHTML.lastIndexOf("/")) +"' target=\"_blank\">Folder</a></td>"
    +"<td width=\"8%\">Running</td></tr>";
  String HTML2 = "<td width=\"10%\"><a href='"+ tcHTML.substring(0, tcHTML.lastIndexOf("/")) +"' target=\"_blank\">Folder</a></td>"
    +"<td width=\"8%\">"+tcStatus+"</td></tr>";
  IO.replaceInFile(IO.sExe_File, HTML1, HTML2);
 }

 public static class TableBuilder
 {
     List<String[]> rows = new LinkedList<String[]>();
  
     public void addRow(String... cols)
     {
         rows.add(cols);
     }
  
     private int[] colWidths()
     {
         int cols = -1;
  
         for(String[] row : rows)
             cols = Math.max(cols, row.length);
  
         int[] widths = new int[cols];
  
         for(String[] row : rows) {
             for(int colNum = 0; colNum < row.length; colNum++) {
                 widths[colNum] =
                     Math.max(
                         widths[colNum],
                         StringUtils.length(row[colNum]));
             }
         }
         return widths;
     }
  
     @Override
     public String toString()
     {
         StringBuilder buf = new StringBuilder();
         int[] colWidths = colWidths();
  
         for(String[] row : rows) {
             for(int colNum = 0; colNum < row.length; colNum++) {
                 buf.append(
                     StringUtils.rightPad(
                         StringUtils.defaultString(
                             row[colNum]), colWidths[colNum]));
                 buf.append(' ');
             }
             buf.append('\n');
         }
         return buf.toString();
     }
 }
}