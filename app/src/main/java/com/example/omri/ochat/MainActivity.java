package com.example.omri.ochat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
            Intent GoToChat = new Intent(MainActivity.this,ChatActivity.class);
            GoToChat.putExtra("nickname",Nick.getText().toString());
            startActivity(GoToChat);
        }
    };


}
