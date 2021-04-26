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
	private final static String SIGNUP_SUCCESS_MESSAGE = "You successfully signed up! Please continue to the login page.";
	private final static String LOGOUT_SUCCESS_MESSAGE = "You have been logged out";

	private final static String NOTE_TITLE = "Test note";
	private final static String NOTE_DESCRIPTION = "You should test it";
	private final static String NOTE_EDIT = "!!!";

	private final static String CREDENTIAL_URL = "http://localhost:8080/login";
	private final static String CREDENTIAL_USERNAME = "admin";
	private final static String CREDENTIAL_PASSWORD = "pass";
	private final static String CREDENTIAL_EDIT = "change";

	private final static String KEY_CREDENTIAL_URL = "credential_url";
	private final static String KEY_CREDENTIAL_USERNAME = "credential_username";
	private final static String KEY_CREDENTIAL_PASSWORD = "credential_password";

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

		assertEquals(SIGNUP_SUCCESS_MESSAGE, signupPage.getSuccessMessage());
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
		this.checkActualUrl(URL_HOME);
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
		assertEquals(NOTE_TITLE, homePage.getNoteTitle(0));
		assertEquals(NOTE_DESCRIPTION, homePage.getNoteDescription(0));
	}

	private void editNote() {
		homePage.editNote(0,NOTE_TITLE + NOTE_EDIT, NOTE_DESCRIPTION + NOTE_EDIT);
		wait20s.until(ExpectedConditions.urlContains(URL_RESULT));
		this.checkActualUrl(URL_RESULT);

		resultPage.onLinkToHome();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		this.checkActualUrl(URL_HOME);

		homePage.onNavNotesTab();
		wait50s.until(ExpectedConditions.visibilityOf(homePage.getFirstNote()));

		assertEquals( NOTE_TITLE + NOTE_EDIT, homePage.getNoteTitle(0));
		assertEquals(NOTE_DESCRIPTION + NOTE_EDIT, homePage.getNoteDescription(0));
	}

	private void deleteNote() {
		homePage.deleteNote(0);
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

		Map<String, String> mapCredential = this.createCredential();
		this.editCredential(mapCredential);
//		this.deleteCredential();
	}

	public Map<String, String> createCredential() {
		homePage.onAddNewCredential();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		homePage.createCredential(CREDENTIAL_URL, CREDENTIAL_USERNAME, CREDENTIAL_PASSWORD);

		wait20s.until(ExpectedConditions.urlContains(URL_RESULT));
		this.checkActualUrl(URL_RESULT);

		resultPage.onLinkToHome();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		this.checkActualUrl(URL_HOME);

		homePage.onNavCredentialsTab();
		wait20s.until(ExpectedConditions.visibilityOf(homePage.getNavCredentials()));

		assertTrue(homePage.getCredentialsSize() == 1);
		assertEquals(CREDENTIAL_URL, homePage.getCredentialUrl(0));
		assertEquals(CREDENTIAL_USERNAME, homePage.getCredentialUsername(0));
		assertFalse(CREDENTIAL_PASSWORD.equals(homePage.getCredentialPassword(0)));

		Map<String, String> mapCredential = new HashMap<>();
		mapCredential.put(KEY_CREDENTIAL_URL, homePage.getCredentialUrl(0));
		mapCredential.put(KEY_CREDENTIAL_USERNAME, homePage.getCredentialUsername(0));
		mapCredential.put(KEY_CREDENTIAL_PASSWORD, homePage.getCredentialPassword(0));
		return mapCredential;
	}

	private void editCredential(Map<String, String> mapCredential) {
		homePage.onEditCredential(0);
		wait20s.until(ExpectedConditions.visibilityOf(homePage.getCredentialModalDialog()));

		assertEquals(mapCredential.get(KEY_CREDENTIAL_URL), homePage.getDialogCredentialUrl());
		assertEquals(mapCredential.get(KEY_CREDENTIAL_USERNAME), homePage.getDialogCredentialUsername());
		//assertEquals(mapCredential.get(KEY_CREDENTIAL_PASSWORD), homePage.getDialogCredentialPassword());

		homePage.editCredential(CREDENTIAL_URL + CREDENTIAL_EDIT, CREDENTIAL_USERNAME + CREDENTIAL_EDIT, "");
		wait20s.until(ExpectedConditions.urlContains(URL_RESULT));
		this.checkActualUrl(URL_RESULT);

		resultPage.onLinkToHome();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		this.checkActualUrl(URL_HOME);

		homePage.onNavCredentialsTab();
		wait50s.until(ExpectedConditions.visibilityOf(homePage.getNavCredentials()));

		assertEquals( CREDENTIAL_URL + CREDENTIAL_EDIT, homePage.getCredentialUrl(0));
		assertEquals(CREDENTIAL_USERNAME + CREDENTIAL_EDIT, homePage.getCredentialUsername(0));
		assertEquals(mapCredential.get(KEY_CREDENTIAL_PASSWORD), homePage.getCredentialPassword(0));
	}

	//verifies that the home page is accessible
	private void checkActualUrl(String url) {
		String expectedUrl = driver.getCurrentUrl();
		String actualUrl = baseURL + url;
		assertEquals(expectedUrl, actualUrl);
	}
}
