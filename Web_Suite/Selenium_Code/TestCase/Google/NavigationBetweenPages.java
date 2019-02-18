package Google;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.testng.ITestContext;
import org.testng.annotations.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;



public class NavigationBetweenPages {
	WebDriver driver;
	String baseUrl;

	 @BeforeTest
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://letskodeit.teachable.com/";
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	@Test
	public void test() throws InterruptedException {
		driver.get(baseUrl);
		String title = driver.getTitle();
		System.out.println("Title of the page is: " + title);

		
		String currentUrl = driver.getCurrentUrl();
		System.out.println("Current URL is: " + currentUrl);
		
		String urlToNavigate = "https://sso.teachable.com/secure/42299/users/sign_in?reset_purchase_session=1";
		driver.navigate().to(urlToNavigate);
		System.out.println("Navigating To Login");
		currentUrl = driver.getCurrentUrl();
		System.out.println("Current URL is: " + currentUrl);
		
		
		Thread.sleep(2000);
		
		driver.navigate().back();
		System.out.println("Navigate Back");
		currentUrl = driver.getCurrentUrl();
		System.out.println("Current URL is: " + currentUrl);
		
		Thread.sleep(2000);
		
		driver.navigate().forward();
		System.out.println("Navigate Forward");
		currentUrl = driver.getCurrentUrl();
		System.out.println("Current URL is: " + currentUrl);
		
		Thread.sleep(2000);
		
		driver.navigate().back();
		System.out.println("Navigate Back");
		currentUrl = driver.getCurrentUrl();
		System.out.println("Current URL is: " + currentUrl);
		
		driver.navigate().refresh();
		System.out.println("Navigate Refresh");
		driver.get(currentUrl);
		System.out.println("Navigate Refresh");
		
		String pageSource = driver.getPageSource();
		System.out.println(pageSource);
		
		boolean isChecked = false;
		List<WebElement> radioButtons = driver.findElements(
				By.xpath("//input[contains(@type,'radio') and contains(@name,'cars')]"));
		int size = radioButtons.size();
		System.out.println("Size of the list: " + size);
		for (WebElement Button: radioButtons) {
			isChecked = Button.isSelected();
			
			if (!isChecked) {
				Button.click();
				Thread.sleep(2000);
			}
		}

		
		
		
		
		////select example
		
		driver.get(baseUrl);
		WebElement element = driver.findElement(By.id("carselect"));
		Select sel = new Select(element);
		
		Thread.sleep(2000);
		System.out.println("Select Benz by value");
		sel.selectByValue("benz");

		Thread.sleep(2000);
		System.out.println("Select Honda by index");
		sel.selectByIndex(2);
		
		Thread.sleep(2000);
		System.out.println("Select BMW by visible text");
		sel.selectByVisibleText("BMW");
		
		Thread.sleep(2000);
		System.out.println("Print the list of all options");
		List<WebElement> options = sel.getOptions();
		int size1 = options.size();
		
		for (int i=0; i<size1; i++) {
			String optionName = options.get(i).getText();
			System.out.println(optionName);
		}
	///////////////Example multiselect
		
		driver.get(baseUrl);
		 element = driver.findElement(By.id("multiple-select-example"));
		 sel = new Select(element);
		
		Thread.sleep(2000);
		System.out.println("Select orange by value");
		sel.selectByValue("orange");
		
		Thread.sleep(2000);
		System.out.println("De-select orange by value");
		sel.deselectByValue("orange");
		
		Thread.sleep(2000);
		System.out.println("Select peach by index");
		sel.selectByIndex(2);
		
		Thread.sleep(2000);
		System.out.println("Select Apple by visible text");
		sel.selectByVisibleText("Apple");
		
		Thread.sleep(2000);
		System.out.println("Print all selected options");
		List<WebElement> selectedOptions = sel.getAllSelectedOptions();
		for (WebElement option : selectedOptions) {
			System.out.println(option.getText());
		}
		
		Thread.sleep(2000);
		System.out.println("De-select all selected options");
		sel.deselectAll();
	


//		
		
		
		// Get the handle
				String parentHandle = driver.getWindowHandle();
				System.out.println("Parent Handle: " + parentHandle);
				
				// Find Open Window button
				WebElement openWindow = driver.findElement(By.id("openwindow"));
				openWindow.click();

				// Get all handles
				Set<String> handles = driver.getWindowHandles();

				// Switching between handles
				for (String handle: handles) {
					System.out.println(handle);
					if (!handle.equals(parentHandle)) {
						driver.switchTo().window(handle);
						Thread.sleep(2000);
						WebElement searchBox = driver.findElement(By.id("search-courses"));
						searchBox.sendKeys("python");
						Thread.sleep(2000);
						driver.close();
						break;
					}
				}
				// Switch back to the parent window
				
				driver.switchTo().window(parentHandle);
				driver.findElement(By.id("name")).sendKeys("Test Successful");

	}
	
	@AfterTest
	public void tearDown() throws Exception {
		// driver.quit();
	}}