package Supporting;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class EDD {
 
 public static String Send_SOAP_Request(String sTestName, String SOAPUrl, String xmlFile2Send) throws IOException, HeadlessException, AWTException {
  return Send_SOAP_Request(sTestName, SOAPUrl, xmlFile2Send, "");  }
 public static String Send_SOAP_Request(String sTestName, String SOAPUrl, String xmlFile2Send, String tagToFetch) throws IOException, HeadlessException, AWTException {

  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, "Send SOAP Request (" + SOAPUrl + ", " + xmlFile2Send + ")");
  String respVal = "";
  try {
   // Create the connection where we're going to send the file.
         URL url = new URL(SOAPUrl);
         URLConnection connection = url.openConnection();
         HttpURLConnection httpConn = (HttpURLConnection) connection;
 
         File file = new File(xmlFile2Send); BufferedReader reader = new BufferedReader(new FileReader(file));
         String data=""; String line = ""; while((line = reader.readLine()) != null) { data += line + "\r\n"; } reader.close();
         String fPath = IO.sExe_Fldr  + "\\" + sTestName.substring(0, sTestName.indexOf("-")) + "\\SOAP_Request.xml";
         IO.PrintLog(fPath, data);
        
         FileInputStream fin = new FileInputStream(xmlFile2Send);
         ByteArrayOutputStream bout = new ByteArrayOutputStream();
    
         // Copy the SOAP file to the open connection.
         copy(fin,bout); fin.close();
 
         byte[] b = bout.toByteArray();
 
         // Set the appropriate HTTP parameters.
         httpConn.setRequestProperty( "Content-Length", String.valueOf( b.length ) );
         httpConn.setRequestProperty("Content-Type","text/xml; charset=utf-8");
         httpConn.setRequestMethod( "POST" );
         httpConn.setDoOutput(true);
         httpConn.setDoInput(true);
 
         // Everything's set up; send the XML that was read in to b.
         OutputStream out = httpConn.getOutputStream();
         out.write( b ); out.close();
 
         // Read the response and write it to standard out.
         InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
         BufferedReader in = new BufferedReader(isr);
 
         String outData, soapResp = "";
         while ((outData = in.readLine()) != null)
          soapResp = soapResp + outData;
         in.close();
        
         fPath = IO.sExe_Fldr  + "\\" + sTestName.substring(0, sTestName.indexOf("-")) + "\\SOAP_Response.xml";
         IO.PrintLog(fPath, soapResp);
        
         if (tagToFetch.equals("")) { tagToFetch = "deliveryKey"; }
         respVal = Get_XMLTag_Data(sTestName, "SOAP_Response", tagToFetch);

         Report.OperationPoint(sTestName, "SOAP Response stores at '" +fPath+ "'");
  } catch(Exception e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", false);
  }
  IO.PrintLog(sTestName, " ---------------------------------------------------------------------------------------------------\n");
        return respVal;
    }
 
 public static String Get_XMLTag_Data(String sTestName, String xmlFile, String xmlTag) throws HeadlessException, IOException, AWTException {
  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, "Get XML Tag Data (" + xmlFile + ", " + xmlTag + ")");
  String tagData = ""; String data = "";
  try {
   
   String flNm = (xmlFile.equals("SOAP_Response")) ? IO.sExe_Fldr  + "\\" + sTestName.substring(0, sTestName.indexOf("-")) + "\\SOAP_Response.xml" :
    IO.sExe_Fldr  + "\\" + sTestName.substring(0, sTestName.indexOf("-")) + "\\SOAP_Request.xml";
   
         File file = new File(flNm);
            BufferedReader reader = new BufferedReader(new FileReader(file));
      String line = "";
            while((line = reader.readLine()) != null) {
             data += line + "\r\n";
            }
            reader.close();
            tagData = StringUtils.substringBetween(data, xmlTag+">", "</");
        
         Report.OperationPoint(sTestName, "Get XML Tag Data -> Received Value: " + tagData);
  } catch(Exception e) {
   e.printStackTrace();
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", false);
  }
  IO.PrintLog(sTestName, " ---------------------------------------------------------------------------------------------------\n");
  return tagData;
 }

 public static void copy(InputStream in, OutputStream out) throws IOException {
 
     synchronized (in) {
       synchronized (out) {
 
         byte[] buffer = new byte[256];
         while (true) {
           int bytesRead = in.read(buffer);
           if (bytesRead == -1) break;
           out.write(buffer, 0, bytesRead);
         }
       }
     }
 }
 
 public static void Connect_To_SSH(String sTestName, String ip, String id, String pwd) throws IOException, HeadlessException, AWTException  {
  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, "Connect To SSH (" + ip + ", " + id + ", " + pwd + ")");
  try {
   JSch jSch = new JSch();
   Session session = jSch.getSession(id, ip); session.setPassword(pwd);
   Properties config = new Properties(); config.put("StrictHostKeyChecking", "no");
   session.setConfig(config); session.setServerAliveInterval(5000); session.connect();
   Channel channel = session.openChannel("shell");
   channel.connect();
   final ByteArrayOutputStream baos = new ByteArrayOutputStream(); channel.setOutputStream(baos);
   Thread.sleep(1000);
   IO.PrintLog(sTestName, baos.toString().replaceAll("\u0007", ""));
   
   Utilities.Map_SSHName_LASTSSH.put(sTestName, baos.toString().replaceAll("\u0007", ""));
   Utilities.Map_SSHName_To_Channel.put(sTestName, channel);
   Utilities.Map_SSHName_To_TestName.put(sTestName, "FREE");
   Utilities.Map_SSHName_PWD.put(sTestName, pwd);
  } catch(Exception e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", false);
  }
  IO.PrintLog(sTestName, " ---------------------------------------------------------------------------------------------------\n");
 }
 
 public static void Execute_SSH_Cmd(String sTestName, String cmd, String tmOut, String expectCond) throws HeadlessException, IOException, AWTException   {
  Execute_SSH_Cmd(sTestName, cmd, tmOut, expectCond, ""); }
 public static void Execute_SSH_Cmd(String sTestName, String cmd, String tmOut, String expectCond, String srvr) throws IOException, HeadlessException, AWTException  {
  if (!expectCond.contains("assword")) {
   IO.PrintLog(sTestName, " ---------------------------------------------------------------------------------------------------");
   Report.OperationPoint(sTestName, "Execute SSH Command (" + cmd + ", " + tmOut + ", " + expectCond + ")");
   IO.PrintLog(sTestName, " ---------------------------------------------------------------------------------------------------");
    try {
     if (cmd.startsWith("ssh ")) { cmd = cmd.replace("ssh", "ssh -o StrictHostKeyChecking=no"); }
     String outCmd = ExecuteSSHCmd(sTestName, cmd, tmOut, expectCond, srvr);
     IO.PrintLog(sTestName, outCmd);
    } catch(Exception e) { Report.TestPoint(sTestName, e.getMessage(), "True", "False", false); }
   IO.PrintLog(sTestName, "\n---------------------------------------------------------------------------------------------------\n\n");
  }
 }
 
 public static void Execute_Export_SSH_Cmd(String sTestName, String cmd, String tmOut, String expectCond) throws HeadlessException, IOException, AWTException   {
  Execute_Export_SSH_Cmd(sTestName, cmd, tmOut, expectCond, ""); }
 public static void Execute_Export_SSH_Cmd(String sTestName, String cmd, String tmOut, String expectCond, String srvr) throws IOException, HeadlessException, AWTException  {
  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, "Execute_Export_SSH_Cmd (" + cmd + ", " + tmOut + ", " + expectCond + ")");
  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  try {
   if (cmd.contains("LAST_LIST")) {
    String lastSSH = Utilities.Map_TestName_To_SSHName.get(sTestName);
    String last = Utilities.Map_SSHName_LASTSSH.get(lastSSH);
    String textStr[] = last.split("\\r?\\n");
    for( int i = 1; i < textStr.length - 1; i++) {
     
     String newCmd = cmd.replaceAll("LAST_LIST", textStr[i]);
     String outCmd = ExecuteSSHCmd(sTestName, newCmd, tmOut, expectCond, srvr);

     String fPath = IO.sExe_Fldr  + "\\" + sTestName.substring(0, sTestName.indexOf("-")) + "\\" +
       textStr[i] + "_" + Report.newtimeStamp + ".log";
     String bsData = "\n ---------------------------------------------------------------------------------------------------" +
        "\nExecute_Export_SSH_Cmd (" + newCmd + ", " + tmOut + ", " + expectCond + ")" +
         "\n ---------------------------------------------------------------------------------------------------\n";
     IO.appendFile(fPath, bsData);
     IO.appendFile(fPath, outCmd);

     Report.ValidationPoint(sTestName, "Checking for NO ERROR/WARNING in (" + newCmd + ")", "true",
         String.valueOf(((!outCmd.contains("ERROR")) && (!outCmd.contains("WARN")))), false);

     IO.PrintLog(sTestName, newCmd);
     IO.PrintLog(sTestName, "Log stored - " + fPath);
    }
   }
   
  } catch(Exception e) { Report.TestPoint(sTestName, e.getMessage(), "True", "False", false); }
  IO.PrintLog(sTestName, "\n---------------------------------------------------------------------------------------------------\n\n");
 }
 
 public static void Free_SSH(String sTestName) throws IOException, HeadlessException, AWTException  {
  IO.PrintLog(sTestName, " ---------------------------------------------------------------------------------------------------");
  try {
   String oldSSH = (Utilities.Map_TestName_To_SSHName.get(sTestName) == null) ? "" : Utilities.Map_TestName_To_SSHName.get(sTestName);
   if (!oldSSH.equals("")) {
    Utilities.Map_TestName_To_SSHName.put(sTestName, "FREE");
       Utilities.Map_SSHName_To_TestName.put(oldSSH, "FREE");
   }   
   Report.OperationPoint(sTestName, "Free SSH - " + oldSSH);
  } catch(Exception e) { Report.TestPoint(sTestName, e.getMessage(), "True", "False", false); }
  IO.PrintLog(sTestName, " ---------------------------------------------------------------------------------------------------\n");
 }
 
 public static Connection Connect_To_DB(String sTestName, String host, String srvName, String uid, String pwd) throws SQLException, HeadlessException, IOException, AWTException  {
  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, "Connect To DB (" + host + ", " + srvName + ", " + uid + ", " + pwd + ")");
  Connection con = null;
  try {
   Class.forName("oracle.jdbc.OracleDriver");
   con = DriverManager.getConnection("jdbc:oracle:thin:"+uid+"/"+pwd+"@//"+host+":1521/"+srvName);
  } catch (ClassNotFoundException e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", false);
        } catch (SQLException e) {
         Report.TestPoint(sTestName, e.getMessage(), "True", "False", false);
        }
  IO.PrintLog(sTestName, " ---------------------------------------------------------------------------------------------------\n");
  return con;
 }
 
 public static Hashtable<String, String> Execute_DB_Query(String sTestName, Connection conn, String query) throws HeadlessException, IOException, AWTException, SQLException, InterruptedException  {
  return Execute_DB_Query(sTestName, conn, query, "");
 }
 public static Hashtable<String, String> Execute_DB_Query(String sTestName, Connection conn, String query, String moveCond) throws HeadlessException, IOException, AWTException, SQLException, InterruptedException  {
  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, "Execute DB Query (" + query + ")");
  IO.PrintLog(sTestName, " ---------------------------------------------------------------------------------------------------");
  Hashtable<String, String> rcrdSetValue = new Hashtable<String, String>();
  try {
   String colNm=""; String colVl = ""; int tmr=1;
   Statement stmt = conn.createStatement();             
   ResultSet rs = stmt.executeQuery(query);
   
   rcrdSetValue = GetColValue_FrmDB(sTestName, rs);
   if (moveCond.contains("=")) {
    colNm = moveCond.substring(0, moveCond.indexOf("=")-1); colVl = moveCond.substring(moveCond.indexOf("="));
    while (!rcrdSetValue.get(colNm).equals(colVl) && (tmr<=Integer.valueOf(Utilities.StoreCopy_Env_Values.get("ObjTimeOut")))) {
     Thread.sleep(5000L); rcrdSetValue = GetColValue_FrmDB(sTestName, rs); tmr+=5;
    }
   }
   stmt.close();
        } catch (SQLException e) {
         Report.TestPoint(sTestName, e.getMessage(), "True", "False", false);
        }
  IO.PrintLog(sTestName, " ---------------------------------------------------------------------------------------------------\n");
  return rcrdSetValue;
 }
 
 public static Hashtable<String, String> GetColValue_FrmDB(String sTestName, ResultSet res) throws SQLException, HeadlessException, IOException, AWTException  {
  
  Hashtable<String, String> hm = new Hashtable<String, String>();
  hm = TablePrinter.printResultSet(sTestName, res);
     return hm;
 }
 
 public static void Print(String sTestName, String msg) throws HeadlessException, IOException, AWTException  {
  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, msg);
  IO.PrintLog(sTestName, " ---------------------------------------------------------------------------------------------------\n");
 }
 
 private static String ExecuteSSHCmd(String sTestName, String cmd, String tmOut, String expectCond, String srvr) {
  Channel tcChannel = null; String currSSH = null;
  String vtier = (srvr.equals("") && (Utilities.Map_TestName_Params.get(sTestName).get("VTIER_INFO")==null)) ? "" : srvr;
  try {
   int waitOut = 1;
   String oldSSH = (Utilities.Map_TestName_To_SSHName.get(sTestName) == null) ? "" : Utilities.Map_TestName_To_SSHName.get(sTestName);
   if (oldSSH.equals("")) {
    boolean fSSH = false;
    while ((!fSSH) && (waitOut<15)) {
     Set<String> keys = Utilities.Map_SSHName_To_TestName.keySet();
     for(String key: keys) {
      if (vtier.equals("") && Utilities.Map_SSHName_To_TestName.get(key).equals("FREE")) {
             Utilities.Map_TestName_To_SSHName.put(sTestName, key);
          Utilities.Map_SSHName_To_TestName.put(key, sTestName);
          oldSSH = key; fSSH = true; break;
      } else if (key.contains(vtier) && Utilities.Map_SSHName_To_TestName.get(key).equals("FREE")) {
             Utilities.Map_TestName_To_SSHName.put(sTestName, key);
          Utilities.Map_SSHName_To_TestName.put(key, sTestName);
          oldSSH = key; fSSH = true; break;
      } 
     }
     if (!fSSH) { Thread.sleep(45000); } waitOut++;
    }
   }
   
   if (waitOut==16) { throw new Exception ("SSH Server '"+ oldSSH + " not get free in 10 mins"); }
   currSSH = oldSSH; tcChannel = Utilities.Map_SSHName_To_Channel.get(currSSH);
   
   String msg = " --------------SSH SERVER (" + currSSH + ")----------------------------\n";
   Expect expect = new ExpectBuilder().withOutput(tcChannel.getOutputStream()).withInputs(tcChannel.getInputStream(), tcChannel.getExtInputStream())
     .withEchoInput(System.out).withEchoOutput(System.err).withTimeout(30, TimeUnit.SECONDS).withExceptionOnFailure().build();  
   
   final ByteArrayOutputStream baos = new ByteArrayOutputStream(); tcChannel.setOutputStream(baos);
   String lastO = (Utilities.Map_SSHName_LASTSSH.get(currSSH) == null) ? "NA" : Utilities.Map_SSHName_LASTSSH.get(currSSH);
   if (lastO.equals("NA") || (lastO.substring(lastO.lastIndexOf("\n")).contains("assword"))) {
    expect.sendLine(Utilities.Map_SSHName_PWD.get(currSSH));
    Thread.sleep(Long.valueOf(3000));
   }
   
   if (tmOut.isEmpty()) { tmOut = "5000"; }
   if (!expectCond.isEmpty()) {
    if (!lastO.substring(lastO.lastIndexOf("\n")).contains(expectCond)) {
      Thread.sleep(Long.valueOf(tmOut));
     Report.TestPoint(sTestName, "Expect Fails - " + expectCond , "True", "False", false);
    }
   }
   expect.sendLine(cmd); int ctr=1; int timOut = 181;
   String prompt = (Utilities.Map_SSHName_Prompt.get(currSSH) == null) ? "" : Utilities.Map_SSHName_Prompt.get(currSSH);
   if (prompt.equals("")) { Thread.sleep(Long.valueOf(tmOut)); }
   else {
    Thread.sleep(2000);
    while (!baos.toString().substring(baos.toString().lastIndexOf("\n")).contains(prompt) && (ctr<181)) {
     Thread.sleep(1000); ctr++; } }
   Thread.sleep(1000);
   if (ctr>timOut-1) {throw new Exception ("TimeOut " + ctr + "- Command '"+cmd+"' not finished on Server '"+ oldSSH + "'");}
         if (cmd.equals("bash")) { Utilities.Map_SSHName_Prompt.put(currSSH, baos.toString().substring(baos.toString().lastIndexOf("\n")).trim()); }
   Utilities.Map_SSHName_LASTSSH.put(currSSH, baos.toString());    tcChannel.setOutputStream(System.out);
         if (baos.toString().contains("No such file or directory")) { throw new Exception ("Error in Command '"+cmd+"' on Server '"+ oldSSH + "'");}
         String cmdOut = baos.toString(); return msg+cmdOut;
  } catch (Exception e) {
   return e.getMessage();
  }
 }
 
 public static void CheckUI_WorkItem(String sTestName, String transID) throws InterruptedException, HeadlessException, IOException, AWTException {

  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  Report.OperationPoint(sTestName, "CheckUI_WorkItem (" + transID + ")");
  
  DesiredCapabilities oCap = DesiredCapabilities.firefox();
  oCap.setBrowserName("firefox");
  WebDriver driver = new RemoteWebDriver(new URL("http://localhost:5555/wd/hub"), oCap);

  driver.manage().timeouts().implicitlyWait(Long.valueOf(Utilities.StoreCopy_Env_Values.get("ObjTimeOut")), TimeUnit.SECONDS);
  driver.manage().window().maximize(); 
  
  try {
   driver.get(Utilities.StoreCopy_Env_Values.get("WorkItem_URL")); Thread.sleep(10);
   driver.findElement(By.xpath("//*[@name='userid']")).sendKeys(Utilities.StoreCopy_Env_Values.get("WorkItem_UserId"));
   driver.findElement(By.xpath("//*[@name='password']")).sendKeys(Utilities.StoreCopy_Env_Values.get("WorkItem_Pwd"));
   driver.findElement(By.xpath("//*[@name='btnSubmit']")).click();
   
   driver.findElement(By.xpath("//*[@name='successOK']")).click();
   
   if (driver.getTitle().equals("WIP-View Workitem (viewer.jsp)")) {
    driver.findElement(By.xpath("//*[@name='filtervalue']")).sendKeys(transID);
    Select list = new Select (driver.findElement(By.xpath("//*[@name='filterselect']")));
    list.selectByValue("stopvalue");
    driver.findElement(By.xpath("//*[@name='appfilter']")).click();
    
    int rowCount=driver.findElements(By.xpath("//table[@class='table_data']/tbody/tr")).size();
    if (rowCount>1)
     Report.TestPoint(sTestName, "WIP-View Workitem - Failed For 'stopvalue'", "No Records", "Records Found", true); 
    
    list.selectByValue("transid");
    driver.findElement(By.xpath("//*[@name='appfilter']")).click();
    rowCount=driver.findElements(By.xpath("//table[@class='table_data']/tbody/tr")).size();
    if (rowCount>1)
     Report.TestPoint(sTestName, "WIP-View Workitem - Failed For 'transid'", "No Records", "Records Found", true); 
    
    Report.TestPoint(sTestName, "WIP-View Workitem Validation", "No Records", "No Records", true);
   } else
    Report.TestPoint(sTestName, "WIP-View Workitem page not displayed", "True", "False", true);
   
  } catch(Exception e) {
   Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
  }
  IO.PrintLog(sTestName, " ---------------------------------------------------------------------------------------------------");
  driver.close();
 }
 
 public static void CheckUI_UniSyncTool(String sTestName, String searchVal) throws InterruptedException, HeadlessException, IOException, AWTException {

  IO.PrintLog(sTestName, "\n ---------------------------------------------------------------------------------------------------");
  //Report.OperationPoint(sTestName, "CheckUI_UniSyncTool (" + searchVal + ")");
  
  DesiredCapabilities oCap = DesiredCapabilities.firefox();
  oCap.setBrowserName("firefox");
  WebDriver driver = new RemoteWebDriver(new URL("http://localhost:5555/wd/hub"), oCap);

  driver.manage().timeouts().implicitlyWait(Long.valueOf(Utilities.StoreCopy_Env_Values.get("ObjTimeOut")), TimeUnit.SECONDS);
  driver.manage().window().maximize(); 
  
  try {
   driver.get(Utilities.StoreCopy_Env_Values.get("UniSyncTool_URL")); Thread.sleep(10);
   driver.findElement(By.xpath("//*[@name='userid']")).sendKeys(Utilities.StoreCopy_Env_Values.get("UniSyncTool_UserId"));
   driver.findElement(By.xpath("//*[@name='password']")).sendKeys(Utilities.StoreCopy_Env_Values.get("UniSyncTool_Pwd"));
   driver.findElement(By.xpath("//*[@name='btnSubmit']")).click();
   
   driver.findElement(By.xpath("//*[@name='successOK']")).click();
   
   if (driver.getTitle().contains("Universal Sync Tool")) {
    driver.findElement(By.xpath("//*[@value='compareIT']")).click();
    driver.findElement(By.xpath("//*[@id='imageField']")).click();
    
    driver.findElement(By.xpath("//*[@id='dummyban']")).sendKeys(searchVal);
    driver.findElement(By.xpath("//*[src='img/go_button.gif']")).click();
    
    driver.findElement(By.xpath("//*[@value='mpsmpsldapss']")).click();
    driver.findElement(By.xpath("//*[@id='SUBMIT_SYNC']")).click();

    List<WebElement> CHECKBOXlist = driver.findElements(By.xpath("//input[@type='checkbox']"));
    for(WebElement checkbox : CHECKBOXlist) {
        checkbox.click();
    }  
      
    String outMsg = driver.findElement(By.xpath("//*[@class='actionMessage']")).getText();
    //Report.TestPoint(sTestName, "Universal Sync Tool - Validation", "All fields match", outMsg, true);
   } else {
    //Report.TestPoint(sTestName, "Universal Sync Tool page not displayed", "True", "False", true);
   }
   
  } catch(Exception e) {
   //Report.TestPoint(sTestName, e.getMessage(), "True", "False", true);
  }
  IO.PrintLog(sTestName, " ---------------------------------------------------------------------------------------------------");
  driver.close();
 }
 
}