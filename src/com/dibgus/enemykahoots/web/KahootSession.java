package com.dibgus.enemykahoots.web;

import android.content.Context;
import android.content.Intent;
import android.renderscript.RenderScript;
import android.webkit.WebView;
import com.dibgus.enemykahoots.MyActivity;
import com.dibgus.enemykahoots.WebSessionActivity;
import com.dibgus.enemykahoots.user.UserType;

import java.util.List;

/**
 * Created by Ivan on 3/17/2016.
 */
public class KahootSession implements Runnable {
    //private HtmlUnitDriver headlessSession = new SilentHtmlUnitDriver();
    private WebSessionActivity headlessSession;
    private UserType userType;
    private String gameID;
    private boolean answered = false;
    public KahootSession(UserType answerType, String gameID)
    {
        userType = answerType;
        //settings.setPreference("startup.homepage_welcome_url.additional", "http://kahoot.it");
        //headlessSession.setJavascriptEnabled(true);
        this.gameID = gameID;
        Thread me = new Thread(this);
        me.start();
    }

    public void run() {
        //headlessSession = new AndroidDriver<RemoteWebElement>(DesiredCapabilities.android());

        enterGame();
        waitForStart();
        answerMode();
    }
//inst to start to loop(getready to answer) gameover<asldfjaslf;j>
    public void enterGame()
    {

    }

    public void waitForStart()
    {

    }

    public void waitForAnswerTime()
    {
    }

    public boolean hasEnded()
    {
        return false;
    }

    public void answerMode() {
        /*
        while (!hasEnded()) {
            try {
                waitForAnswerTime();
                int choice;
                List<WebElement> elements = headlessSession.findElements(By.xpath("//button"));
                WebElement selected;
                if(!answered)
                switch (userType) {
                    case RANDOM:
                        choice = (int) (Math.random() * 4);
                        if(choice == -1)
                        {
                            System.out.println("nochoice");
                            break;
                        }
                        selected = elements.size() > 0 ? elements.get(choice) : null;
                        if (selected != null && selected.isDisplayed() && selected.isEnabled())
                            selected.click();
                        answered = true;
                        break;
                    case MIMIC:
                        choice = MyActivity.answer;
                        selected = elements.size() > 0 ? elements.get(choice) : null;
                        if (selected != null && selected.isDisplayed() && selected.isEnabled()) {
                            selected.click();
                            answered = true;
                        }
                        break;
                }
            } catch(Exception e)
            {
            }
        }
        */
    }
}
