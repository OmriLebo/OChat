package com.example.omri.ochat;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Struct;

public class ChatActivity extends AppCompatActivity {

    private EditText InputMSG;
    private Button Sendbutton;
    private TextView ChatTextview;
    private String nickname;
    //ok
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ChatTextview = (TextView)findViewById(R.id.ChatScreen);
        ChatTextview.setMovementMethod(new ScrollingMovementMethod());
        InputMSG = (EditText)findViewById(R.id.InputText);
        Sendbutton = (Button)findViewById(R.id.SendButton);
        nickname = getIntent().getExtras().getString("nickname" , "Unknown");
        ConnectToServer.start();
    }



    Thread ConnectToServer = new Thread()
    {
        @Override
        public void run() {
            try
            {
                final Socket s = new Socket("roomserver.dynu.net", 31021);
                final DataOutputStream out = new DataOutputStream(s.getOutputStream());
                Thread Recieving = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final DataInputStream in = new DataInputStream(s.getInputStream());
                            while(true)
                            {
                                final String Recv = in.readUTF();
                                ChatTextview.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ChatTextview.setText(Recv);
                                    }
                                });
                            }
                        }
                        catch (Exception  e){}
                    }
                });

                View.OnClickListener SendListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Thread Sending = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                final String Send = nickname + ": " + InputMSG.getText().toString();
                                try {
                                    out.writeUTF(Send);

                                }
                                catch (final Exception e){
                                    ChatTextview.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            ChatTextview.setText(e.toString());
                                        }

                                    });
                                }
                            }
                        });
                        Sending.start();
                        InputMSG.setText("");
                    }
                };
                Sendbutton.setOnClickListener(SendListener);
                Recieving.start();
            }
            catch (Exception e)
            {

            }
        }
    };


}
