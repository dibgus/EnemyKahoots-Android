package com.dibgus.enemykahoots;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.*;
import com.dibgus.EnemyKahoots.R;
import com.dibgus.enemykahoots.user.UserType;

import java.util.ArrayList;

public class SubmissionActivity extends Activity {

    private static SubmissionActivity main;
    static int answer; //0-3 a-d; -1 none
    private ArrayList<UserType> users = new ArrayList<UserType>();
    private KeyListener txtIDListener;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        main = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //region ADD USER BUTTON
        Button submit = (Button) findViewById(R.id.btnAddUser);
        View.OnClickListener submit_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtName = (EditText) findViewById(R.id.txtName);
                EditText txtID = (EditText) findViewById(R.id.txtID);
                if (txtID.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please specify a game ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txtName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please specify a Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserType type;
                if (((RadioButton) findViewById(R.id.rbtnMimic)).isChecked())
                    type = UserType.MIMIC;
                else if (((RadioButton) findViewById(R.id.rbtnRandom)).isChecked())
                    type = UserType.RANDOM;
                else
                    type = UserType.DEAD;
                Intent i = new Intent(getApplicationContext(), WebSessionActivity.class);
                WebSessionData data = new WebSessionData(txtName.getText().toString(), txtID.getText().toString(), type);
                i.putExtra("info", data);
                users.add(type);
                startActivity(i);
                startActivity(main.getIntent());
                TextView userLabel = (TextView) findViewById(R.id.lblUsers);
                if (userLabel.getText().toString().equals(getString(R.string.userliststart)))
                    userLabel.setText(Html.fromHtml(userLabel.getText().toString() + "<font color=\"red\">" + txtName.getText().toString() + "</font>"));
                else
                    userLabel.setText(Html.fromHtml(userLabel.getText().toString() + ", <font color=\"red\">" + txtName.getText().toString() + "</font>"));
                Toast.makeText(getApplicationContext(), "Added User: " + txtName.getText().toString(), Toast.LENGTH_SHORT).show();
                txtName.setText("");
            }
        };
        submit.setOnClickListener(submit_click);
        //endregion

        //region lock checkbox listener
        View.OnClickListener checkbox_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText idtextbox = (EditText) findViewById(R.id.txtID);
                if (((CheckBox) findViewById(R.id.ckbxLockID)).isChecked()) {
                    txtIDListener = idtextbox.getKeyListener();
                    idtextbox.setKeyListener(null);
                } else {
                    idtextbox.setKeyListener(txtIDListener);
                }
            }
        };
        findViewById(R.id.ckbxLockID).setOnClickListener(checkbox_click);
        //endregion

        //region UPDATE ANSWER
        Button ansA = (Button) findViewById(R.id.btnA);
        Button ansB = (Button) findViewById(R.id.btnB);
        Button ansC = (Button) findViewById(R.id.btnC);
        Button ansD = (Button) findViewById(R.id.btnD);
        View.OnClickListener answer_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnA)
                    SubmissionActivity.answer = 0;
                else if (v.getId() == R.id.btnB)
                    SubmissionActivity.answer = 1;
                else if (v.getId() == R.id.btnC)
                    SubmissionActivity.answer = 2;
                else if (v.getId() == R.id.btnD)
                    SubmissionActivity.answer = 3;
                else
                    SubmissionActivity.answer = -1;
                for (WebSessionActivity activity : WebSessionActivity.sessions)
                    activity.getAnswer();
                //Toast.makeText(getApplicationContext(), SubmissionActivity.answer + "", Toast.LENGTH_SHORT).show();
            }
        };
        ansA.setOnClickListener(answer_click);
        ansB.setOnClickListener(answer_click);
        ansC.setOnClickListener(answer_click);
        ansD.setOnClickListener(answer_click);
        //endregion

        //username text update
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    //region sleeping
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    } //endregion
                    //region ui update
                    runOnUiThread((new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            TextView userLabel = (TextView) findViewById(R.id.lblUsers);
                            String userText = "";
                            ArrayList<WebSessionActivity> sessions = (ArrayList<WebSessionActivity>)WebSessionActivity.sessions.clone();
                            for (WebSessionActivity activity : sessions) {
                                String color = "";

                                switch (activity.getStatus()) {
                                    case CONNECTED:
                                    case ANSWERED:
                                        color = "green";
                                        break;

                                    case ANSWERING:
                                        color = "#FF9900";
                                        break;
                                    case READYING:
                                        color = "yellow";
                                        break;
                                    case ERROR:
                                        color = "red";
                                        break;
                                }
                                if (userText.equals(""))
                                    userText = "<font color=\"" + color + "\">" + activity.getUserName() + "</font>";
                                else
                                    userText += ", " + "<font color=\"" + color + "\">" + activity.getUserName() + "</font>";
                            }
                            if(sessions.size() > 0)
                                userLabel.setText(Html.fromHtml(getResources().getText(R.string.userliststart) + userText));
                            else
                                userLabel.setText(getResources().getText(R.string.userliststart));
                        }
                    })); //endregion
                }
            }
        }).start();

    }
}
