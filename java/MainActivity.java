package com.pmg.peppermintgardensapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{
    private static final String TAG = "Swipe Position";
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 150;
    private boolean timer_stopped;
    private float timer_length;
    private GestureDetector gestureDetector;
    String json_in;
    JSONObject data_packets;
    JSONObject settings_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize gesture
        this.gestureDetector = new GestureDetector(MainActivity.this,this);

        // checks if WRITE storage has permission
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
        }
        // checks if READ storage has permission
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }
        // checks if INTERNET has permission
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.INTERNET},100);
        }
    }

    // override on touch event
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch(event.getAction()) {
            // start to swipe time gesture
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
                // ending time swipe gesture
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                // getting value for horizontal swipe
                float valueX = x2 - x1;
                // getting value for vertical swipe
                float valueY = y2 - y1;

                if (Math.abs(valueX) > MIN_DISTANCE)
                {

                    if (x2>x1)
                    {
                        // detect left to right swipe
                        // Extra implementation planned.
                    }
                    else
                    {
                        // detect right to left swipe
                        Intent intent = new Intent(this, activity_client.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }
                }
        }
        return super.onTouchEvent(event);
    }

    public void test_call_function() {
        Log.d(TAG,convert_json_obj(settings_data));
    }

    public String convert_json_obj(JSONObject objIn) {
        return objIn.toString();
    }

    public String load_packets_data() {
        File file = new File("settings_file.json");
        if (file.exists()) {
            try {
                FileInputStream fileIn = openFileInput("packet_data.json");
                InputStreamReader inputRead = new InputStreamReader(fileIn);
                int c;
                String temp = "";
                while ((c = fileIn.read()) != -1) {
                    temp = temp + Character.toString((char) c);
                }
                fileIn.close();
                Log.d(TAG, "File Loaded!");
                return temp;
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"packets\":[],\"loaded\":\"null\"}";
            }
        } else {
            // save
            save_packets_data("{\"packets\":[],\"loaded\":\"null\"}");
            return "{\"packets\":[],\"loaded\":\"null\"}";
        }
    }

    public void save_packets_data(String input) {
        // json format
        try {
            FileOutputStream fileOut = openFileOutput("packet_data.json", Context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
            outputWriter.write(input);
            outputWriter.close();
            Log.d(TAG, "File Saved!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "File Save PROBLEM!");
        }
    }

    public void clear_packets_data() {
        // json format
        try {
            FileOutputStream fileOut = openFileOutput("packet_data.json", Context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
            outputWriter.write("{\"packets\":[],\"loaded\":\"yes\"}");
            outputWriter.close();
            Log.d(TAG, "File Cleared!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String check_settings_file_exists() {
        File file = new File("settings_file.json");
        if (file.exists()) {
            return load_settings_file();
        } else {
            return create_new_settings();
        }
    }

    public String load_settings_file() {
        try {
            FileInputStream fileIn = openFileInput("settings_file.json");
            InputStreamReader inputRead = new InputStreamReader(fileIn);
            int c;
            String temp = "";
            while((c = fileIn.read()) != -1) {
                temp = temp + Character.toString((char)c);
            }
            fileIn.close();
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"time\":\"null\"}";
        }
    }

    public String create_new_settings() {
        // file is json format
        try {
            String data = "{\"time\":\"30000\"}";
            FileOutputStream fileOut = openFileOutput("settings_file.json", Context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
            outputWriter.write(data);
            outputWriter.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"time\":\"null\"}";
        }
    }

    public void edit_settings(String input) {
        // input is json format
        try {
            String data = input;
            FileOutputStream fileOut = openFileOutput("settings_file.json", Context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
            outputWriter.write(data);
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start_settings_timer() throws JSONException {
        JSONArray packet_arr = data_packets.getJSONArray("packets");
        String d = settings_data.getString("time");

        if (timer_stopped == true) {
            if (d != "null" && packet_arr.length() > 0) {
                timer_stopped = false;
                new CountDownTimer(Long.parseLong(d), 1000) {
                    public void onFinish() {
                        Log.d(TAG,"TIMER DONE!!");
                        timer_stopped = true;
                        attempt_to_connect();
                    }
                }.start();
            } else {
                timer_stopped = true;
            }
        }
    }

    public void go_to_client_page(View view) {
        Intent intent = new Intent(this, activity_client.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

}

