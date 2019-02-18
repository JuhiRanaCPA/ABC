package Objects_Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Google {

	// Search - Logo - IMAGE - XPATH
	@FindBy(xpath = ".//*[@id='hplogo']")
	public WebElement Search_Logo;

	// Search - TextSearch - TEXTBOX - XPATH
	@FindBy(xpath = ".//*[@id='lst-ib']")
	public WebElement Search_TextSearch;

	// Search - Submit - BUTTON - XPATH
	@FindBy(xpath = ".//*[@value='Google Search']")
	public WebElement Search_Submit;

	// Result - TextSearch - TEXTBOX - XPATH
	@FindBy(xpath = ".//*[@id='gs_htif0']")
	public WebElement Result_TextSearch;

	// Result - Maps - LINK - CSS
	@FindBy(css = "a.q")
	public WebElement Result_Maps;

	// Result - India_WikipediaLink - LINK - XPATH
	@FindBy(xpath = "//a[contains(@href, 'https://en.wikipedia.org/wiki/India')]")
	public WebElement Result_India_WikipediaLink;

	// Wikipedia - Logo - IMAGE - XPATH
	@FindBy(xpath = "//a[contains(@href, 'Visit the main page')]")
	public WebElement Wikipedia_Logo;

	// Wikipedia - SatyamavaJayate_Link - LINK - XPATH
	@FindBy(xpath = "//a[contains(@href, '/wiki/Satyameva_Jayate')]")
	public WebElement Wikipedia_SatyamavaJayate_Link;

	// Wikipedia - Heading - MESSAGE - XPATH
	@FindBy(xpath = "//*[@id='firstHeading']")
	public WebElement Wikipedia_Heading;

	// Search - SignIn - BUTTON - XPATH
	@FindBy(xpath = "//*[@id='gb_70']")
	public WebElement Search_SignIn;

	// Google_SignIn - Email_TextBox - TEXTBOX - XPATH
	@FindBy(xpath = "//*[@id='identifierId']")
	public WebElement Google_SignIn_Email_TextBox;

	// Google_SignIn - Next_Button - BUTTON - XPATH
	@FindBy(xpath = "//*[@id='identifierNext']")
	public WebElement Google_SignIn_Next_Button;

	// Google_SignIn - Password_TextBox - TEXTBOX - XPATH
	@FindBy(xpath = "//input[@class='whsOnd zHQkBf']")
	public WebElement Google_SignIn_Password_TextBox;

	// Google_SignIn - SignIn_Button - BUTTON - XPATH
	@FindBy(xpath = "//*[@class='ZFr60d CeoRYc']")
	public WebElement Google_SignIn_SignIn_Button;

	// Google_SignIn - StaySignedIn_CheckBox - CHKBOX - XPATH
	@FindBy(xpath = "//*[@id='PersistentCookie']")
	public WebElement Google_SignIn_StaySignedIn_CheckBox;

	// Google_SignIn - LogOut_Button - BUTTON - XPATH
	@FindBy(xpath = "//*[@id='gb_71']")
	public WebElement Google_SignIn_LogOut_Button;

	// Google_SignIn - Account_Link - LINK - XPATH
	@FindBy(xpath = "//*[@class='gb_Wa gbii']")
	public WebElement Google_SignIn_Account_Link;

	WebDriver driver;
	public Google(WebDriver driver){
		this.driver = driver;
	}
}
