package Objects_Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class IDIRS_ObjectMap {


		@FindBy(xpath = "//*[@id='L_menuItem0']")
		public WebElement AppDiv;
	
		@FindBy(xpath = ".//*[@id='L_menuItem0']/div[1]/h1")
		public WebElement Driver;
	
		@FindBy(xpath = "//*[@id='KN_key0']")
		public WebElement Zero;
		
		@FindBy(xpath = "//*[@id='KN_key1']")
		public WebElement One;
		
		@FindBy(xpath = "//*[@id='KN_key2']")
		public WebElement Two;
		
		@FindBy(xpath = "//*[@id='KN_key3']")
		public WebElement Three;
		
		@FindBy(xpath = "//*[@id='KN_key4']")
		public WebElement Four;
		
		@FindBy(xpath = "//*[@id='KN_key5']")
		public WebElement Five;
					
		@FindBy(xpath = "//*[@id='KN_key6']")
		public WebElement Six;
					
		@FindBy(xpath = "//*[@id='KN_key7']")
		public WebElement Seven;
					
		@FindBy(xpath = "//*[@id='KN_key8']	")				
		public WebElement Eight;
					
		@FindBy(xpath = "//*[@id='KN_key9']")
		public WebElement Nine;
				
		@FindBy(xpath = "//*[@id='KN_keyEnter']")
		public WebElement Enter;
				
		@FindBy(xpath = "//h3[contains(text(),'Block')]")
		public WebElement Block;
		
		@FindBy(xpath = "//*[@id='BlockLogonSlide']")
		public WebElement BlockDiv;
		
		


	WebDriver driver;
	public IDIRS_ObjectMap(WebDriver driver){
		this.driver = driver;
	}
}
