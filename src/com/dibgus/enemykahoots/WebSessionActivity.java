package com.dibgus.enemykahoots;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.dibgus.EnemyKahoots.R;
import com.dibgus.enemykahoots.user.UserType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Ivan on 3/24/2016.
 */
public class WebSessionActivity extends Activity {

    private static final char[] escapeSanitization = {'\\', '\'', '"'};
    static ArrayList<WebSessionActivity> sessions = new ArrayList<WebSessionActivity>();
    private WebView mainView;
    private String userName, gameID;
    private boolean answered;
    private int timeoutStart;
    private static final int TIMEOUT = 10;
    private UserType behaviour;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.onBackPressed(); //comment this out for visual debugging
        setVisible(false); //comment this out too for visual debugging

        //parcel extraction
        WebSessionData data = getIntent().getExtras().getParcelable("info");
        userName = sanitizeInput(data.userName);
        gameID = data.gameID;
        try
        {
            Integer.parseInt(gameID);
        } catch(NumberFormatException ex) { Toast.makeText(getApplicationContext(), "Game ID isn't numeric!", Toast.LENGTH_SHORT).show(); return; }
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
                //System.out.println("URL loaded: " + url);
                if(url.equals("https://kahoot.it/#/") || url.equals("http://kahoot.it/#/"))
                    execJavaScript("" +
                        "var inField = document.getElementById('inputSession'); " +
                        "inField.value = '" + gameID + "';" +
                        "inField.dispatchEvent(new Event('change'));" +
                        "document.getElementsByTagName('button')[0].click();"
                    );
                else if(url.contains("join"))
                    execJavaScript("var nameField = document.getElementById(\"username\");" +
                            "nameField.value = '" + userName + "';" +
                            "nameField.dispatchEvent(new Event('change'));" +
                            "document.getElementsByTagName('button')[0].click()"
                    );
                else if(url.contains("answer") && behaviour == UserType.RANDOM)
                    getAnswer();
                else if(url.contains("ranking") || url.contains("feedback"))
                {
                    //destroy
                    unload();
                }
                else if(url.contains(""))
                    answered = false;
            }
        });
        mainView.loadUrl("https://kahoot.it");
    }

    private String sanitizeInput(String in)
    {
        for(char baddie : escapeSanitization)
            in = in.replace(baddie + "", "\\" + baddie);
        return in;
    }

    void getAnswer() {
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
        answered = true;
    }

    private void execJavaScript(String cmd) {
        mainView.loadUrl("javascript:" + cmd);
    }

    String getUserName()
    {
        return userName;
    }

    private void unload()
    {
        Toast.makeText(getApplicationContext(), "User " + getUserName() + " was unloaded from timeout or game end.", Toast.LENGTH_SHORT).show();
        sessions.remove(this);
        finish();
    }

    WebState getStatus()
        {
            String url = mainView.getUrl();
            if(url.equals("https://kahoot.it/#/") || url.equals("http://kahoot.it/#/")) {
                Calendar c = Calendar.getInstance();
                int currentTime =  c.get(Calendar.HOUR) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND);
                if(timeoutStart == 0)
                    timeoutStart = currentTime;
                else if(currentTime - timeoutStart >= TIMEOUT)
                    unload();
                return WebState.ERROR;
            }
            else
                timeoutStart = 0;
            if(url.contains("answer"))
                if(answered)
                    return WebState.ANSWERED;
                else
                    return WebState.ANSWERING;
        else if(url.contains("join") || url.contains("instructions"))
                return WebState.CONNECTED;
        else
                return WebState.READYING;
    }

}