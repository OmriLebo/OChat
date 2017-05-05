package com.example.omri.ochat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private Button JoinChatbutton;
    private ImageButton Settingsbutton;
    private EditText Nick;
    //Git Commennt


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Nick = (EditText)findViewById(R.id.NickNameInput);
        JoinChatbutton = (Button)findViewById(R.id.JoinChatButton);
        Settingsbutton = (ImageButton) findViewById(R.id.SettingsButton);
        JoinChatbutton.setOnClickListener(Join);
    }



    View.OnClickListener Join = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String nickName;
            Nick.setHint("");
            if(!(nickName = Nick.getText().toString()).equals("")) {
                Intent GoToChat = new Intent(MainActivity.this,ChatActivity.class);
                GoToChat.putExtra("nickname", nickName);
                startActivity(GoToChat);
            }
            else{
                Log.v("Debug","Nickname missing");
                Nick.setHint("This field is mandatory");
                Nick.setHintTextColor(Color.RED);
            }
        }
    };


}
