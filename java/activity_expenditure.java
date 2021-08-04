package com.pmg.peppermintgardensapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.util.Calendar;

public class activity_expenditure extends AppCompatActivity implements GestureDetector.OnGestureListener{
    // GESTURE STUFF
    private static final String TAG = "Swipe Position";
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 150;
    private GestureDetector gestureDetector;
    Button dayList_BTN, monthList_BTN, yearList_BTN;
    TextView day_text, month_text, year_text, total_num, source_text, item_text, cost_num, client_text,history_num;
    String[] year_list = {"2020","2021","2022","2023"};
    String[] month_list = {"01","02","03","04","05","06","07","08","09","10","11","12"};
    String[] day_list = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    PacketsClass p_c;
    Context this_context;
    int num_of_packets;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure);
        this.gestureDetector = new GestureDetector(activity_expenditure.this,this);

        // SET UP PACKET VARIABLE //
        p_c = new PacketsClass();
        num_of_packets = p_c.find_number_of_packets(this);
        this_context = this;

        history_num = (TextView) findViewById(R.id.history_num);
        history_num.setText(Integer.toString(num_of_packets));

        source_text = (TextView) findViewById(R.id.source_text);
        item_text = (TextView) findViewById(R.id.item_text);
        cost_num = (TextView) findViewById(R.id.cost_num);
        client_text = (TextView) findViewById(R.id.client_text);

        // GET CURRENT DATE
        Calendar current_date = Calendar.getInstance();
        Integer m_day = current_date.get(Calendar.MONTH) + 1;
        String current_month = "";
        if (m_day < 10) {
            current_month = "0" + String.valueOf(m_day);
        } else {
            current_month = String.valueOf(m_day);
        }

        Integer d_day = current_date.get(Calendar.DAY_OF_MONTH);
        String current_day = "";
        if (d_day < 10) {
            current_day = "0" + String.valueOf(d_day);
        } else {
            current_day = String.valueOf(d_day);
        }

        String current_year = String.valueOf(current_date.get(Calendar.YEAR));
        // END CURRENT DATE


        // SET DATE STRINGS
        day_text = (TextView) findViewById(R.id.day_text);
        day_text.setText(current_day);

        month_text = (TextView) findViewById(R.id.month_text);
        month_text.setText(current_month);

        year_text = (TextView) findViewById(R.id.year_text);
        year_text.setText(current_year);

        // FIRST BUTTON
        dayList_BTN = (Button) findViewById(R.id.day_list_btn);
        //listItems = getResources().getStringArray(R.array.year_select);
        dayList_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity_expenditure.this);
                mBuilder.setTitle("Select Day");
                mBuilder.setItems(day_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        dialog.dismiss();
                        day_text.setText(day_list[which]);
                    }
                });
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        dialog.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        // SECOND BUTTON
        monthList_BTN = (Button) findViewById(R.id.month_list_btn);
        monthList_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity_expenditure.this);
                mBuilder.setTitle("Select Month");
                mBuilder.setItems(month_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        dialog.dismiss();
                        month_text.setText(month_list[which]);
                    }
                });
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        dialog.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        // THIRD BUTTON
        yearList_BTN = (Button) findViewById(R.id.year_list_btn);
        yearList_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity_expenditure.this);
                mBuilder.setTitle("Select Year");
                mBuilder.setItems(year_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        dialog.dismiss();
                        year_text.setText(year_list[which]);
                    }
                });
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        dialog.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        // ADD DATE BUTTON TRIGGERS - END
    }

    public void save_client_info(View view) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity_expenditure.this);
        mBuilder.setTitle("Are you sure you want to submit?");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                dialog.dismiss();

                // BUILD THE DATA STRING
                String data_to_send = "{\"date\":\"" + day_text.getText().toString() + "-" + month_text.getText().toString() + "-" + year_text.getText().toString() + "\"," + "\"source\":\"" + source_text.getText().toString();
                data_to_send += "\",\"item\":\"" + item_text.getText().toString() + "\",\"cost\":\"" + cost_num.getText().toString() + "\",\"client\":\"" + client_text.getText().toString() + "\"}";

                Log.d(TAG,data_to_send);

                try {
                    p_c.add_packet_to_que(data_to_send,this_context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                num_of_packets = p_c.find_number_of_packets(this_context);
                history_num.setText(Integer.toString(num_of_packets));
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                dialog.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
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

    public void go_to_history_page(View view) {
        Intent intent = new Intent(this, activity_history.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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
                        Intent intent = new Intent(this, activity_journey.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    }
                    else
                    {
                        // detect right to left swipe
                        // do stuff
                        Intent intent = new Intent(this, activity_history.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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