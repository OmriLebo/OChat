package com.example.omri.ochat;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;

import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;


import layout.InputFragment;



public class ChatActivity extends AppCompatActivity implements InputFragment.OnInputListener {

    private TextView ChatTextview;
    private String nickname;
    private Calendar cal = Calendar.getInstance();
    private InputFragment inputFragment;
    private String chatRoomName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ChatTextview = (TextView)findViewById(R.id.ChatScreen);
        inputFragment =  (InputFragment) getSupportFragmentManager().findFragmentById(R.id.InputFrag2);
        ChatTextview.setMovementMethod(new ScrollingMovementMethod());
        nickname = getIntent().getExtras().getString("nickname" , "Unknown");
        chatRoomName = getIntent().getExtras().getString("chatRoomName" , "General");
        Recieving.start();
        ChatTextview.setMovementMethod(new ScrollingMovementMethod());
        inputFragment.setFragTexts("<<Send","Your" +
                " message...");
        String ENTER_MESSAGE = nickname + "ENTER_CODE" + chatRoomName;// switch to enum
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String EXIT_MESSAGE = nickname + "EXIT_CODE";// switch to enum
        new SendMessage().execute(EXIT_MESSAGE);
    }

    Thread Recieving = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                final Socket s = new Socket("gate.romcarmel.com", 8822);//("roomserver.dynu.net", 31021);
                final DataInputStream in = new DataInputStream(s.getInputStream());
                while(s.isConnected())
                {
                    Log.v("debug" , "waiting for string");
                    final String Recv = in.readUTF();
                    Log.v("debug" , Recv);
                    ChatTextview.post(new Runnable() {
                        @Override
                        public void run() {
                            ChatTextview.setText(Recv);
                        }
                    });
                }
                in.close();
                s.close();

                // TODO - need to notify somewhere that socket has closed and handle it!
            }
            catch (Exception  e){
                Log.v("debug" , "Something bad at connect thread ");
                Log.v("debug" , "Something bad at recive thread ");
            }
        }
    });


    @Override
    public void onInput(String text) {
        new SendMessage().execute(text);
    }

    //============================================================================================

    private class SendMessage extends AsyncTask<String , Void , Void>{

        @Override
        protected Void doInBackground(String... InputMSG) {
            try {
                final Socket s = new Socket("gate.romcarmel.com", 8822);//("roomserver.dynu.net", 31021);
                final DataOutputStream out = new DataOutputStream(s.getOutputStream());
                int hours = cal.get(Calendar.HOUR_OF_DAY);
                int minutes = cal.get(Calendar.MINUTE);
                String totaltime = hours + ":" + minutes;
                final String Send = InputMSG[0];//" [" + totaltime + "] " + nickname + ": " + InputMSG[0];
                try {
                    out.writeUTF(Send);

                } catch (final Exception e) {
                    Log.v("DEBUG","OutputStream problem");
                }
            }catch (IOException e){
                Log.v("DEBUG","No Connection");
                Toast.makeText(getApplicationContext(), "Turn ON Internet Dumb Dumb" , Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Sent..." , Toast.LENGTH_SHORT).show();
        }
    }

    public enum Code{
        Exit , ENTER

    }
}


