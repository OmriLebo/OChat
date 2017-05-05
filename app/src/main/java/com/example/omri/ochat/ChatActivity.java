package com.example.omri.ochat;

import android.app.Activity;

import android.support.v4.app.Fragment;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Struct;

public class ChatActivity extends FragmentActivity implements Input.OnFragmentInteractionListener{


    private TextView ChatTextview;
    private String nickname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ChatTextview = (TextView)findViewById(R.id.ChatScreen);
        ChatTextview.setMovementMethod(new ScrollingMovementMethod());
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
                        catch (Exception  e){
                            Log.v("debug" , "Something bad at connect thread ");
                        }
                    }
                });

                Recieving.start();
            }
            catch (Exception e)
            {
                Log.v("debug" , "Something bad at sending thread ");
            }
        }
    };


    @Override
    public void onFragmentInteraction(final String text) {
        Thread Sending = new Thread(new Runnable() {
            @Override
            public void run() {

                final String Send = " " + nickname + ": " + text;
                try {
                    final Socket s = new Socket("roomserver.dynu.net", 31021);
                    final DataOutputStream out = new DataOutputStream(s.getOutputStream());

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
    }
}
