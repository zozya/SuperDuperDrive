package com.udacity.jwdnd.course1.cloudstorage;

import org.jsoup.helper.StringUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Driver;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class HomePage {

    @FindBy(css = "#logout-submit-button")
    private WebElement logoutSubmitButton;

    // NOTES
    @FindBy(css = "#nav-notes-tab")
    private WebElement navNotesTab;

    @FindBy(css = "#nav-notes")
    private WebElement navNotes;

    @FindBy(css = "#add-note-button")
    private WebElement addNoteButton;

    @FindBy(css = "#note-id")
    private WebElement noteIdInput;

    @FindBy(css = "#note-title")
    private WebElement noteTitleInput;

    @FindBy(css = "#note-description")
    private WebElement noteDescriptionTextarea;

    @FindBy(css = "#noteSubmit")
    private WebElement noteSubmitButton;

    @FindBy(css = "#noteTable")
    private WebElement noteTable;

    @FindBy(css = "#notes")
    private List<WebElement> notes;

    @FindBy(css = "#noteModal")
    private WebElement noteModalDialog;

    //CREDENTIALS
    @FindBy(css = "#nav-credentials-tab")
    private WebElement navCredentialsTab;

    @FindBy(css = "#credentials")
    private List<WebElement> credentials;

    @FindBy(css = "#credentialTable")
    private WebElement credentialTable;

    @FindBy(css = "#nav-credentials")
    private WebElement navCredentials;

    @FindBy(css = "#add-credential-button")
    private WebElement addCredentialButton;

    @FindBy(css = "#credentialModal")
    private WebElement credentialDialog;

    @FindBy(css = "#credential-id")
    private WebElement credentialIdInput;

    @FindBy(css = "#credential-url")
    private WebElement credentialUrlInput;

    @FindBy(css = "#credential-username")
    private WebElement credentialUsernameInput;

    @FindBy(css = "#credential-password")
    private WebElement credentialPasswordInput;

    @FindBy(css = "#credentialSubmit")
    private WebElement credentialSubmitButton;

    private JavascriptExecutor exec;
    private WebDriver driver;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.exec = (JavascriptExecutor) driver;
        this.driver= driver;
    }

    public void logout() {
        this.logoutSubmitButton.click();
    }

    // NOTES
    public void onNavNotesTab() {
        exec.executeScript("arguments[0].click();", this.navNotesTab);
    }

    public boolean isNavNotesTabDisplay() {
        return this.navNotesTab.isDisplayed();
    }

    public WebElement getNavNotes() {
        return this.navNotes;
    }

    public void onAddNewNote() {
        this.addNoteButton.click();
    }

    public void createOrEditNote(String noteTitle, String noteDescription) {
        this.noteTitleInput.sendKeys(noteTitle);
        this.noteDescriptionTextarea.click();
        this.noteDescriptionTextarea.sendKeys(noteDescription);
        this.noteDescriptionTextarea.sendKeys(Keys.ENTER);
        exec.executeScript("arguments[0].click();", this.noteSubmitButton);
    }

    public void editNote (int id, String noteTitle, String noteDescription) {
        WebElement buttonEditNote = noteTable.findElement(By.cssSelector("#buttonEditNote" + id));
        buttonEditNote.click();
        this.noteTitleInput.clear();
        this.noteDescriptionTextarea.clear();

        this.createOrEditNote(noteTitle, noteDescription);
    }

    public int getNotesSize() {
        return this.notes.size();
    }

    public WebElement getFirstNote() {
        return this.notes.get(0);
    }

    public String getNoteTitle(int id) {
        WebElement notetitle = noteTable.findElement(By.cssSelector("#notetitle" + id));
        return notetitle.getText();
    }

    public String getNoteDescription(int id) {
        WebElement notedescription = noteTable.findElement(By.cssSelector("#notedescription" + id));
        return notedescription.getText();
    }

    public void deleteNote(int id) {
        WebElement buttonDeleteNote = noteTable.findElement(By.cssSelector("#buttonDeleteNote" + id));
        buttonDeleteNote.click();
   }

   // CREDENTIALS
   public boolean isNavCredentialsTabDisplay() {
       return this.navCredentialsTab.isDisplayed();
   }

    public void onNavCredentialsTab() {
        exec.executeScript("arguments[0].click();", this.navCredentialsTab);
    }

    public int getCredentialsSize() {
        return this.credentials.size();
    }

    public WebElement getNavCredentials() {
        return this.navCredentials;
    }

    public WebElement getCredentialModalDialog() {
        return this.credentialDialog;
    }

    public void onAddNewCredential() {
        this.addCredentialButton.click();
    }

    public void onEditCredential(int id) {
        WebElement buttonEditCredential = this.credentialTable.findElement(By.cssSelector("#buttonEditCredential" + id));
        buttonEditCredential.click();
    }

    public void editCredential(String credentialUrl, String credentialUsername, String credentialPassword) {
        this.credentialUrlInput.clear();
        this.credentialUsernameInput.clear();
        this.credentialPasswordInput.clear();

        this.createCredential(credentialUrl, credentialUsername, credentialPassword);
    }

    public void createCredential(String credentialUrl, String credentialUsername, String credentialPassword) {
        this.credentialUrlInput.sendKeys(credentialUrl);
        this.credentialUsernameInput.sendKeys(credentialUsername);
        this.credentialPasswordInput.click();
        this.credentialPasswordInput.sendKeys(credentialPassword);
        exec.executeScript("arguments[0].click();", this.credentialSubmitButton);
    }

    public String getCredentialPasswordDecrypted() {
        return this.credentialPasswordInput.getText();
    }

    public String getCredentialUrl(int id) {
        WebElement credentialurl = this.credentialTable.findElement(By.cssSelector("#credentialurl" + id));
        return credentialurl.getText();
    }

    public String getCredentialUsername(int id) {
        WebElement credentialusername = this.credentialTable.findElement(By.cssSelector("#credentialusername" + id));
        return credentialusername.getText();
    }

    public String getCredentialPassword(int id) {
        WebElement credentialpassword = this.credentialTable.findElement(By.cssSelector("#credentialpassword" + id));
        return credentialpassword.getText();
    }

    public void deleteCredential(int id) {
        WebElement buttonDeleteCredential = this.credentialTable.findElement(By.cssSelector("#buttonDeleteCredential" + id));
        buttonDeleteCredential.click();
    }

}
