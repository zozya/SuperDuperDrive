package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {

    @FindBy(css="#linkOnSuccess")
    WebElement linkToHomePage;

    private JavascriptExecutor exec;

    public ResultPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        this.exec = (JavascriptExecutor) webDriver;
    }

    public void onLinkToHome(){
       exec.executeScript("arguments[0].click();", this.linkToHomePage);
    }
}
