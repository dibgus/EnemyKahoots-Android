package com.dibgus.enemykahoots;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.text.method.KeyListener;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;
import com.dibgus.EnemyKahoots.R;
import com.dibgus.enemykahoots.user.UserType;

import java.io.IOException;
import java.util.ArrayList;

public class MyActivity extends Activity {

    public static ArrayList<WebView> drivers = new ArrayList<WebView>();
    public static MyActivity main;
    public static int answer; //0-4 a-d; -1 none
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
        //ADD USER BUTTON
        Button submit = (Button)findViewById(R.id.btnAddUser);
        View.OnClickListener submit_click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText txtName = (EditText)findViewById(R.id.txtName);
                EditText txtID = (EditText)findViewById(R.id.txtID);
                UserType type;
                if(((RadioButton)findViewById(R.id.rbtnMimic)).isChecked())
                    type = UserType.MIMIC;
                else if(((RadioButton)findViewById(R.id.rbtnRandom)).isChecked())
                    type = UserType.RANDOM;
                else
                    type = UserType.DEAD;
                Intent i = new Intent(getApplicationContext(), WebSessionActivity.class);
                System.out.println(txtName.getText().toString() + " " + txtID.getText().toString());
                WebSessionData data = new WebSessionData(txtName.getText().toString(), txtID.getText().toString(), type);
                i.putExtra("info", data);
                users.add(type);
                startActivity(i);
                startActivity(main.getIntent());
                TextView userLabel = (TextView)findViewById(R.id.lblUsers);
                if(userLabel.getText().toString().equals("Users: "))
                    userLabel.setText(userLabel.getText().toString() + txtName.getText().toString());
                else
                    userLabel.setText(userLabel.getText().toString() + ", " + txtName.getText().toString());
                txtName.setText("");
            }
        };
        submit.setOnClickListener(submit_click);

        View.OnClickListener checkbox_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText idtextbox = (EditText)findViewById(R.id.txtID);
                if(((CheckBox)findViewById(R.id.ckbxLockID)).isChecked())
                {
                    txtIDListener = idtextbox.getKeyListener();
                    idtextbox.setKeyListener(null);
                }
                else
                {
                    idtextbox.setKeyListener(txtIDListener);
                }
            }
        };
        findViewById(R.id.ckbxLockID).setOnClickListener(checkbox_click);

        //UPDATE ANSWER
        Button ansA = (Button)findViewById(R.id.btnA);
        Button ansB = (Button)findViewById(R.id.btnB);
        Button ansC = (Button)findViewById(R.id.btnC);
        Button ansD = (Button)findViewById(R.id.btnD);
        View.OnClickListener answer_click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v.getId() == R.id.btnA)
                    MyActivity.answer = 0;
                else if(v.getId() == R.id.btnB)
                    MyActivity.answer = 1;
                else if(v.getId() == R.id.btnC)
                    MyActivity.answer = 2;
                else if(v.getId() == R.id.btnD)
                    MyActivity.answer = 3;
                else
                    MyActivity.answer = -1;
                for(WebSessionActivity activity : WebSessionActivity.sessions)
                    activity.getAnswer();
                System.out.println(MyActivity.answer);
            }
        };
        ansA.setOnClickListener(answer_click);
        ansB.setOnClickListener(answer_click);
        ansC.setOnClickListener(answer_click);
        ansD.setOnClickListener(answer_click);
    }

    public String getID()
    {
        EditText txtID = (EditText)findViewById(R.id.txtID);
        return txtID.getText().toString();
    }
}
