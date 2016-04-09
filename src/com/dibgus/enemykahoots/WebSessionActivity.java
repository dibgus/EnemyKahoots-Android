package com.dibgus.enemykahoots;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.dibgus.EnemyKahoots.R;
import com.dibgus.enemykahoots.user.UserType;

import java.util.ArrayList;

/**
 * Created by Ivan on 3/24/2016.
 */
public class WebSessionActivity extends Activity {
    public static ArrayList<WebSessionActivity> sessions = new ArrayList<WebSessionActivity>();
    private WebView mainView;
    private String userName, gameID;
    private UserType behaviour;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //this.onBackPressed(); //comment this out for visual debugging
        //setVisible(false); //comment this out too for visual debugging

        //parcel extraction
        WebSessionData data = getIntent().getExtras().getParcelable("info");
        userName = data.userName;
        gameID = data.gameID;
        behaviour = data.type;
        sessions.add(this);

        //Sets up the WebView
        setContentView(R.layout.view);
        super.onCreate(savedInstanceState);
        mainView = (WebView) findViewById(R.id.mainView);
        mainView.getSettings().setJavaScriptEnabled(true);
        mainView.getSettings().setDomStorageEnabled(true);
        mainView.getSettings().setLoadsImagesAutomatically(false); //resource saving since the user doesn't see flashy graphics

        mainView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                execJavaScript("" +
                        "var inField = document.getElementById('inputSession'); " +
                        "inField.value = '" + gameID + "';" +
                        "inField.dispatchEvent(new Event('change'));" +
                        "document.getElementsByTagName('button')[0].click();" +
                        "setTimeout(function()" +
                        "{var nameField = document.getElementById(\"username\");" +
                        "nameField.value = '" + userName + "';" +
                        "nameField.dispatchEvent(new Event('change'));" +
                        "document.getElementsByTagName('button')[0].click();}, 5000);" //TODO don't hardcode wait
                );
                //javascript:var test = document.getElementById("inputSession").value;window.alert(test);
                //javascript:document.getElementById("inputSession").value = "ID"
                //javascript:document.getElementsByTagName('button')[0].click();
                //INSTANTIATE COMMAND WITH ID
                //.javascript:var inField = document.getElementById("inputSession"); inField.value = '100416'; inField.dispatchEvent(new Event('change'));document.getElementsByTagName('button')[0].click();
                //USERNAME SNIPPET
                //.javascript:var nameField = document.getElementById("username"); nameField.value = 'NAME'; nameField.dispatchEvent(new Event('change'));document.getElementsByTagName('button')[0].click();
                //combined: .javascript:var inField = document.getElementById("inputSession"); inField.value = '100416'; inField.dispatchEvent(new Event('change'));document.getElementsByTagName('button')[0].click();setTimeout(function(){var nameField = document.getElementById("username"); nameField.value = 'NAME'; nameField.dispatchEvent(new Event('change'));document.getElementsByTagName('button')[0].click();}, 5000);
            }
        });
        mainView.loadUrl("https://kahoot.it");
    }

    public void getAnswer() {
        int choice = -1;
        switch (behaviour) {
            case RANDOM:
                choice = (int) (Math.random() * 4);
                break;
            case MIMIC:
                choice = SubmissionActivity.answer;
                break;
        }
        if (choice == -1) return;
        execJavaScript("document.getElementsByTagName('button')[" + (choice) + "].dispatchEvent(new Event('mousedown'));");
    }

    public void execJavaScript(String cmd) {
        mainView.loadUrl("javascript:" + cmd);
    }

}