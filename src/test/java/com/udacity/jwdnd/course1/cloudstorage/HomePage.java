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

    //@FindBy(css = "div#noteModal button.btn.btn-primary")
    @FindBy(css = "#noteSubmit")
    private WebElement noteSubmitButton;

    @FindBy(css = "#notes")
    private List<WebElement> notes;

    @FindBy(css = "#noteModal")
    private WebElement noteModalDialog;

    //CREDENTIALS
    @FindBy(css = "#nav-credentials-tab")
    private WebElement navCredentialsTab;

    @FindBy(css = "#credentials")
    private List<WebElement> credentials;

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

    public void editNote (int listIndex, String noteTitle, String noteDescription) {
        WebElement note = this.notes.get(listIndex);
        int iStat = listIndex+1;
        WebElement buttonEditNote = note.findElement(By.cssSelector("#buttonEditNote" + iStat));
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

    public String getNoteTitle(int listIndex) {
        WebElement note = this.notes.get(listIndex);
        int iStat = listIndex+1;
        WebElement notetitle = note.findElement(By.cssSelector("#notetitle" + iStat));
        return notetitle.getText();
    }

    public String getNoteDescription(int listIndex) {
        WebElement note = this.notes.get(listIndex);
        int iStat = listIndex+1;
        WebElement notedescription = note.findElement(By.cssSelector("#notedescription" + iStat));
        return notedescription.getText();
    }

    public void deleteNote(int listIndex) {
        WebElement note = this.notes.get(listIndex);
        int iStat = listIndex+1;
        WebElement buttonDeleteNote = note.findElement(By.cssSelector("#buttonDeleteNote" + iStat));
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

    public WebElement getCredentialFromIndex(int index) {
        return this.credentials.get(index);
    }

    public void onAddNewCredential() {
        this.addCredentialButton.click();
    }

    public void onEditCredential(int listIndex) {
        WebElement note = this.credentials.get(listIndex);
        int iStat = listIndex+1;
        WebElement buttonEditCredential = note.findElement(By.cssSelector("#buttonEditCredential" + iStat));
        buttonEditCredential.click();
    }

    public void editCredential(String credentialUrl, String credentialUsername, String credentialPassword) {
        if(StringUtil.isBlank(credentialUrl)) { credentialUrl = this.credentialUrlInput.getText();  }
        if(StringUtil.isBlank(credentialUsername)) { credentialUsername = this.credentialUsernameInput.getText(); }
        if(StringUtil.isBlank(credentialPassword)) { credentialPassword = this.credentialPasswordInput.getText(); }

        this.credentialUrlInput.clear();
        this.credentialUsernameInput.clear();
        this.credentialPasswordInput.clear();

        this.createCredential(credentialUrl, credentialUsername, credentialPassword);
    }

    public void createCredential(String credentialUrl, String credentialUsername, String credentialPassword) {
        this.credentialUrlInput.sendKeys(credentialUrl);
        this.credentialUsernameInput.sendKeys(credentialUsername);
        this.credentialPasswordInput.sendKeys(credentialPassword);
        exec.executeScript("arguments[0].click();", this.credentialSubmitButton);
    }

    public String getCredentialUrl(int listIndex) {
        WebElement note = this.credentials.get(listIndex);
        int iStat = listIndex+1;
        WebElement credentialurl = note.findElement(By.cssSelector("#credentialurl" + iStat));
        return credentialurl.getText();
    }

    public String getCredentialUsername(int listIndex) {
        WebElement note = this.credentials.get(listIndex);
        int iStat = listIndex+1;
        WebElement credentialusername = note.findElement(By.cssSelector("#credentialusername" + iStat));
        return credentialusername.getText();
    }

    public String getCredentialPassword(int listIndex) {
        WebElement note = this.credentials.get(listIndex);
        int iStat = listIndex+1;
        WebElement credentialpassword = note.findElement(By.cssSelector("#credentialpassword" + iStat));
        return credentialpassword.getText();
    }

    public String getDialogCredentialUrl() {
        return this.credentialUrlInput.getText();
    }

    public String getDialogCredentialUsername() {
        return this.credentialUsernameInput.getText();
    }

    public String getDialogCredentialPassword() {
        return this.credentialPasswordInput.getText();
    }

}
