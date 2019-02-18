Set oShell = WScript.CreateObject ("WScript.Shell")
curDir = Replace(Wscript.ScriptFullName, Wscript.ScriptName, "")
oShell.run "cmd.exe /K java -jar " & curDir  & "selenium-server-standalone-2.48.2.jar -port 4444 -role hub -nodeTimeout 1000"
WScript.Sleep 10000

oShell.run "cmd.exe /K java -jar " & curDir  & "selenium-server-standalone-2.48.2.jar -role node -hub http://localhost:4444/grid/register -browser browserName=firefox,maxInstances=5 -port 5555"
WScript.Sleep 10000

oShell.run "cmd.exe /K java -Dwebdriver.ie.driver=" & curDir  & "IEDriverServer.exe -jar " & curDir  & "selenium-server-standalone-2.48.2.jar -role webdriver -hub http://localhost:4444/grid/register -browser browserName=ie,platform=WINDOWS,maxInstances=5 -port 5558"
WScript.Sleep 10000

oShell.run "cmd.exe /K java -Dwebdriver.chrome.driver=" & curDir  & "chromedriver.exe -jar " & curDir & "\selenium-server-standalone-2.48.2.jar -role webdriver -hub http://localhost:4444/grid/register -browser browserName=chrome,platform=WINDOWS,maxInstances=5 -port 5559"
WScript.Sleep 10000

Set oShell = Nothing