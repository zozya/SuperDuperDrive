package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private LoginPage loginPage;
	private HomePage homePage;
	private ResultPage resultPage;
	WebDriverWait wait20s;
	WebDriverWait wait50s;

	private String baseURL;
	private final static String URL_LOGIN = "/login";
	private final static String URL_SIGNUP = "/signup";
	private final static String URL_HOME = "/home";
	private final static String URL_RESULT = "/result";

	private final static String USERNAME = "someone";
	private final static String PASSWORD = "Abc123";
	private final static String SIGNUP_SUCCESS_MESSAGE = "You successfully signed up!";
	private final static String LOGOUT_SUCCESS_MESSAGE = "You are logged out";

	private final static String NOTE_TITLE = "Test note";
	private final static String NOTE_DESCRIPTION = "You should test it";
	private final static String NOTE_EDIT = "!!!";

	private final static String CREDENTIAL_URL = "http://localhost:8080/login";
	private final static String CREDENTIAL_USERNAME = "admin";
	private final static String CREDENTIAL_PASSWORD = "pass";
	private final static String CREDENTIAL_EDIT = "change";

	private final static int START_ID = 1;
	private final static int CREDENTIALS_SET_SIZE = 3;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		this.baseURL = "http://localhost:" + port;
		this.loginPage = new LoginPage(driver);
		this.homePage = new HomePage(driver);
		this.resultPage = new ResultPage(driver);
		wait20s = new WebDriverWait(driver,20);
		wait50s = new WebDriverWait(driver,50);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get(baseURL + URL_LOGIN);
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignupPage() {
		driver.get(baseURL + URL_SIGNUP);
		assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void testUnauthorizedUserAccess() {
		getLoginPage();
		getSignupPage();
		//verifies that an unauthorized user can only access the login and signup pages.
		driver.get(baseURL + URL_HOME);
		assertEquals("Login", driver.getTitle());
		driver.get(baseURL + URL_RESULT);
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testUserSignupAndLoginAndLogout() throws InterruptedException {
		signup();
		login();
		logout();
	}

	private void signup() {
		driver.get(baseURL + URL_SIGNUP);

		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Maria", "Test", USERNAME, PASSWORD);

		this.checkActualUrl(URL_LOGIN);
		assertEquals(SIGNUP_SUCCESS_MESSAGE, loginPage.getSuccessMessage());
	}

	private void login() {
		driver.get(baseURL + URL_LOGIN);
		loginPage.login(USERNAME, PASSWORD);
		this.checkActualUrl(URL_HOME);

	}

	private void logout() {
		driver.get(baseURL + URL_HOME);
		homePage.logout();

		this.checkActualUrl(URL_LOGIN);
		assertEquals(LOGOUT_SUCCESS_MESSAGE, loginPage.getLogoutMessage());
		assertEquals(true, loginPage.isLogoutSuccessfull());
	}

	// Test NOTES
	@Test
	public void testCreateDisplayEditDeleteNote() {
		signup();
		login();

		assertTrue(homePage.isNavNotesTabDisplay());
		homePage.onNavNotesTab();
		wait20s.until(ExpectedConditions.visibilityOf(homePage.getNavNotes()));
		assertTrue(homePage.getNotesSize() <= 0);

		this.createNote();
		this.editNote();
		this.deleteNote();
	}

	private void createNote() {
		homePage.onAddNewNote();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		homePage.createOrEditNote(NOTE_TITLE, NOTE_DESCRIPTION);
		wait20s.until(ExpectedConditions.urlContains(URL_RESULT));
		this.checkActualUrl(URL_RESULT);

		resultPage.onLinkToHome();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		this.checkActualUrl(URL_HOME);

		homePage.onNavNotesTab();
		wait20s.until(ExpectedConditions.visibilityOf(homePage.getFirstNote()));

		assertTrue(homePage.getNotesSize() == 1);
		assertEquals(NOTE_TITLE, homePage.getNoteTitle(START_ID));
		assertEquals(NOTE_DESCRIPTION, homePage.getNoteDescription(START_ID));
	}

	private void editNote() {
		homePage.editNote(START_ID,NOTE_TITLE + NOTE_EDIT, NOTE_DESCRIPTION + NOTE_EDIT);
		wait20s.until(ExpectedConditions.urlContains(URL_RESULT));
		this.checkActualUrl(URL_RESULT);

		resultPage.onLinkToHome();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		this.checkActualUrl(URL_HOME);

		homePage.onNavNotesTab();
		wait50s.until(ExpectedConditions.visibilityOf(homePage.getFirstNote()));

		assertEquals( NOTE_TITLE + NOTE_EDIT, homePage.getNoteTitle(1));
		assertEquals(NOTE_DESCRIPTION + NOTE_EDIT, homePage.getNoteDescription(1));
	}

	private void deleteNote() {
		homePage.deleteNote(START_ID);
		wait20s.until(ExpectedConditions.urlContains(URL_RESULT));
		checkActualUrl(URL_RESULT);

		resultPage.onLinkToHome();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		checkActualUrl(URL_HOME);

		homePage.onNavNotesTab();

		assertTrue(homePage.getNotesSize() <= 0);
	}

	// Test CREDENTIALS
	@Test
	public void testCreateDisplayEditDeleteCredential() {
		signup();
		login();

		assertTrue(homePage.isNavCredentialsTabDisplay());
		homePage.onNavCredentialsTab();
		wait20s.until(ExpectedConditions.visibilityOf(homePage.getNavCredentials()));
		assertTrue(homePage.getCredentialsSize() <= 0);

		this.createCredential(START_ID);
		this.editCredential(START_ID);
		this.deleteCredential(START_ID);

		assertTrue(homePage.getCredentialsSize() <= 0);
	}

	@Test
	public void testCreateDisplayEditDeleteCredentialsSet() {
		signup();
		login();

		assertTrue(homePage.isNavCredentialsTabDisplay());
		homePage.onNavCredentialsTab();
		wait20s.until(ExpectedConditions.visibilityOf(homePage.getNavCredentials()));
		assertTrue(homePage.getCredentialsSize() <= 0);

		for(int i=START_ID; i<=CREDENTIALS_SET_SIZE; i++) {
			this.createCredential(i);
		}
		assertTrue(homePage.getCredentialsSize() == CREDENTIALS_SET_SIZE);

		for(int i=START_ID; i<=CREDENTIALS_SET_SIZE; i++) {
			this.editCredential(i);
		}

		for(int i=START_ID; i<=CREDENTIALS_SET_SIZE; i++) {
			this.deleteCredential(i);
			assertTrue(homePage.getCredentialsSize() == CREDENTIALS_SET_SIZE - i);
		}
	}

	private void createCredential(int index) {
		homePage.onAddNewCredential();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		homePage.createCredential(CREDENTIAL_URL + index, CREDENTIAL_USERNAME + index, CREDENTIAL_PASSWORD + index);

		wait20s.until(ExpectedConditions.urlContains(URL_RESULT));
		this.checkActualUrl(URL_RESULT);

		resultPage.onLinkToHome();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		this.checkActualUrl(URL_HOME);

		homePage.onNavCredentialsTab();
		wait20s.until(ExpectedConditions.visibilityOf(homePage.getNavCredentials()));

		assertTrue(homePage.getCredentialsSize() == index);
		assertEquals(CREDENTIAL_URL + index, homePage.getCredentialUrl(index));
		assertEquals(CREDENTIAL_USERNAME + index, homePage.getCredentialUsername(index));
		assertFalse((CREDENTIAL_PASSWORD + index).equals(homePage.getCredentialPassword(index)));
	}

	private void editCredential(int index) {
		String passwordBeforeChange = homePage.getCredentialPassword(index);

		homePage.onEditCredential(index);
		wait20s.until(ExpectedConditions.visibilityOf(homePage.getCredentialModalDialog()));

		String passwordDecryptedBeforeChange = homePage.getCredentialPasswordDecrypted();
		assertFalse(passwordDecryptedBeforeChange.equals(passwordBeforeChange));

		homePage.editCredential(CREDENTIAL_URL + index + CREDENTIAL_EDIT,
				CREDENTIAL_USERNAME + index + CREDENTIAL_EDIT,
				CREDENTIAL_PASSWORD + index + CREDENTIAL_EDIT);
		wait20s.until(ExpectedConditions.urlContains(URL_RESULT));
		this.checkActualUrl(URL_RESULT);

		resultPage.onLinkToHome();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		this.checkActualUrl(URL_HOME);

		homePage.onNavCredentialsTab();
		wait50s.until(ExpectedConditions.visibilityOf(homePage.getNavCredentials()));

		assertEquals( CREDENTIAL_URL + index + CREDENTIAL_EDIT, homePage.getCredentialUrl(index));
		assertEquals(CREDENTIAL_USERNAME + index + CREDENTIAL_EDIT, homePage.getCredentialUsername(index));
		assertFalse((CREDENTIAL_PASSWORD + index + CREDENTIAL_EDIT).equals(homePage.getCredentialPassword(index)));
		assertFalse(passwordBeforeChange.equals(homePage.getCredentialPassword(index)));
	}

	private void deleteCredential(int index) {
		homePage.deleteCredential(index);
		wait20s.until(ExpectedConditions.urlContains(URL_RESULT));
		checkActualUrl(URL_RESULT);

		resultPage.onLinkToHome();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		checkActualUrl(URL_HOME);

		homePage.onNavCredentialsTab();
	}

	//verifies that the page is accessible
	private void checkActualUrl(String url) {
		String expectedUrl = driver.getCurrentUrl();
		String actualUrl = baseURL + url;
		assertEquals(expectedUrl, actualUrl);
	}
}
