package com.pmg.peppermintgardensapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class activity_history extends AppCompatActivity implements GestureDetector.OnGestureListener{
    // GESTURE STUFF
    private static final String TAG = "Swipe Position";
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 150;
    private GestureDetector gestureDetector;

    private RequestQueue queue;
    private RequestQueue queue2;
    //

    int num_of_packets;
    PacketsClass p_c;
    Context this_context;
    TextView history_num, packets_text, laptop_text;
    String url_http = "http://192.168.1.8/pmg/packet_data_recieve.pl"; // perl script to send packets to

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // checks if INTERNET has permission
        if (ContextCompat.checkSelfPermission(activity_history.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity_history.this, new String[] {Manifest.permission.INTERNET},100);
        }

        if (ContextCompat.checkSelfPermission(activity_history.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity_history.this, new String[] {Manifest.permission.ACCESS_NETWORK_STATE},100);
        }


        this.gestureDetector = new GestureDetector(activity_history.this,this);

        // SET UP PACKET VARIABLE //
        p_c = new PacketsClass();
        num_of_packets = p_c.find_number_of_packets(this);
        this_context = this;

        history_num = (TextView) findViewById(R.id.history_num);
        history_num.setText(Integer.toString(num_of_packets));

        packets_text = (TextView) findViewById(R.id.packets_num);
        packets_text.setText(Integer.toString(num_of_packets));

        // check if there's a connection or not.
        laptop_text = (TextView) findViewById(R.id.laptop_connect);
        test_connection();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public void go_to_client_page(View view) {
        Intent intent = new Intent(this, activity_client.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public void go_to_journey_page(View view) {
        Intent intent = new Intent(this, activity_journey.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public void go_to_expenditure_page(View view) {
        Intent intent = new Intent(this, activity_expenditure.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public void upload_data(View view) {
        upload_packets(p_c.get_packet_data(this));
    }

    public void clear_packets(View view) {
        p_c.clear_packet_file(this_context);
    }

    public void test_connection() {
        queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_http + "?type=test", new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d(TAG,response.toString());
                if (response.toString().equals("success")) {
                    laptop_text.setText("Laptop Connected!");
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                laptop_text.setText("Laptop Disconnected!");
            }
        });

        // add the request to the request queue
        queue.add(stringRequest);
    }

    public void upload_packets(String data) {
        queue = Volley.newRequestQueue(this);
        // request a string response from the provided url.
        Log.d(TAG,"SENDING A REQUEST NOW: " + data);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_http + "?type=data&data=" + data, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d(TAG,response.toString());

                if (response.toString().equals("success")) {
                    //data_packet_sent = "success";
                    Log.d(TAG,"WINNING!!!!");
                    laptop_text.setText("UPLOAD COMPLETE!!");

                    p_c.clear_packet_file(this_context);
                    num_of_packets = p_c.find_number_of_packets(this_context);

                    history_num.setText(Integer.toString(num_of_packets));
                    packets_text.setText(Integer.toString(num_of_packets));

                }
                if (response.toString().equals("data_file_fail")) {
                    laptop_text.setText("UPLOAD FAIL!!");
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                laptop_text.setText("UPLOAD FAIL!!");

            }
        });

        // add the request to the request queue
        queue.add(stringRequest);
    }




    // GESTURE STUFF
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
                        // do stuff
                        Intent intent = new Intent(this, activity_expenditure.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    }
                    else
                    {
                        // detect right to left swipe
                        // do stuff
                    }
                }
        }
        return super.onTouchEvent(event);
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