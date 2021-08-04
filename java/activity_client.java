package com.pmg.peppermintgardensapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

public class activity_client extends AppCompatActivity implements GestureDetector.OnGestureListener{
    // GESTURE STUFF
    private static final String TAG = "Swipe Position";
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 150;
    private GestureDetector gestureDetector;
    //

    Button dayList_BTN, monthList_BTN, yearList_BTN;
    TextView day_text, month_text, year_text, total_num, history_num;
    String[] year_list = {"2020","2021","2022","2023"};
    String[] month_list = {"01","02","03","04","05","06","07","08","09","10","11","12"};
    String[] day_list = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    ArrayList<Integer> mUserItems = new ArrayList<>();
    RelativeLayout current_extra_layout;
    LinearLayout extra_layout_section;
    EditText hoursBox, costPerHourBox, clientText;
    ArrayList<EditText> extrasBoxes = new ArrayList<>();
    ArrayList<EditText> extrasDescription = new ArrayList<>();
    PacketsClass p_c;
    Context this_context;
    int num_of_packets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        this.gestureDetector = new GestureDetector(activity_client.this,this);

        current_extra_layout = (RelativeLayout) findViewById(R.id.extra_01);
        extra_layout_section = (LinearLayout) findViewById(R.id.extras_section);
        clientText = (EditText) findViewById(R.id.client_text);


        // SET UP PACKET VARIABLE //
        p_c = new PacketsClass();
        num_of_packets = p_c.find_number_of_packets(this);
        this_context = this;

        history_num = (TextView) findViewById(R.id.history_num);
        history_num.setText(Integer.toString(num_of_packets));



        // CHECK WHEN EDIT TEXT IS CHANGED
        total_num = (TextView) findViewById(R.id.total_num);
        hoursBox = (EditText) findViewById(R.id.hours_num);
        costPerHourBox = (EditText) findViewById(R.id.cost_per_hour_num);
        EditText extra_01 = (EditText) findViewById(R.id.extra_01_num);

        costPerHourBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    // calculate new total
                    calculate_new_total();
                }
            }
        });

        hoursBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    // calculate new total
                    calculate_new_total();
                }
            }
        });
        extrasBoxes.add(extra_01);
        extrasDescription.add(findViewById(R.id.extra_01_text));
        extra_01.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    // calculate new total
                    calculate_new_total();
                }
            }
        });
        // END EDIT TEXT BOXES CHANGE CHECK


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

        // ADD DATE BUTTON TRIGGERS
        // FIRST BUTTON
        dayList_BTN = (Button) findViewById(R.id.day_list_btn);
        dayList_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity_client.this);
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity_client.this);
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity_client.this);
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

    public void calculate_new_total() {
        if (hoursBox.getText().length() > 0 && costPerHourBox.getText().length() > 0) {
            double hours_times_rate_total = Double.parseDouble(hoursBox.getText().toString()) * Double.parseDouble(costPerHourBox.getText().toString());
            // add extras on top.
            double extras_total = 0.0;
            for (EditText object : extrasBoxes) {
                if (object.getText().length() > 0) {
                    extras_total += Double.parseDouble(object.getText().toString());
                }
            }
            total_num.setText(Double.toString(hours_times_rate_total + extras_total));
        }
    }

    public void save_client_info(View view) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity_client.this);
        mBuilder.setTitle("Are you sure you want to submit?");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                dialog.dismiss();

                // BUILD THE DATA STRING
                String data_to_send = "{\"date\":\"" + day_text.getText().toString() + "-" + month_text.getText().toString() + "-" + year_text.getText().toString() + "\",\"total_hours\":\"" + hoursBox.getText().toString();
                data_to_send += "\",\"cost_per_hour\":\"" + costPerHourBox.getText().toString() + "\",\"total_cost\":\"" + total_num.getText().toString() + "\",\"client_text\":\"" + clientText.getText().toString() + "\"";

                for (int i = 0; i < extrasBoxes.size(); i++) {
                    data_to_send += ",\"extra_" + Integer.toString(i) + "_amount\":\"" + extrasBoxes.get(i).getText().toString() + "\",\"extra_" + Integer.toString(i);
                    data_to_send += "_description\":\"" + extrasDescription.get(i).getText().toString() + "\"";
                }
                data_to_send += "}";
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

    public void add_new_extra(View view) {
        final float scale = this.getResources().getDisplayMetrics().density;


        RelativeLayout new_l = new RelativeLayout(this);
        RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(0, (int) (40 * scale + 0.5f),0,0);
        new_l.setLayoutParams(lparams);

        extra_layout_section.addView(new_l);

        // TEXT VIEW 01
        TextView text_01 = new TextView(this);
        text_01.setTextColor(Color.parseColor("#7C7C7C"));
        text_01.setTextSize(16);
        text_01.setTypeface(Typeface.create("@font/roboto",Typeface.NORMAL));
        RelativeLayout.LayoutParams new_layout_1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        new_layout_1.setMargins( (int) (20 * scale + 0.5f), (int) (5 * scale + 0.5f),0,0);
        text_01.setLayoutParams(new_layout_1);
        text_01.setText("Extra");

        // TEXT VIEW 02
        TextView text_02 = new TextView(this);
        text_02.setText("£");
        text_02.setTextColor(Color.parseColor("#8E8E8E"));
        text_02.setTextSize(40);
        text_01.setTypeface(Typeface.create("@font/roboto",Typeface.NORMAL));
        RelativeLayout.LayoutParams new_layout_2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        new_layout_2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        new_layout_2.setMargins(0, (int) (20 * scale + 0.5f), (int) (235 * scale + 0.5f),0);
        text_02.setLayoutParams(new_layout_2);

        // EDIT TEXT 01
        EditText text_03 = new EditText(this);
        text_03.setHint("0");
        text_03.setTextColor(Color.parseColor("#000000"));
        text_03.setTextSize(50);
        text_03.setInputType(2);
        text_03.setTypeface(Typeface.create("@font/roboto_bold",Typeface.BOLD));
        RelativeLayout.LayoutParams new_layout_3 = new RelativeLayout.LayoutParams(
                (int) (180 * scale + 0.5f), (int) (90 * scale + 0.5f));
        new_layout_3.setMargins((int) (80 * scale + 0.5f), (int) (8 * scale + 0.5f), 0,0);
        text_03.setLayoutParams(new_layout_3);
        extrasBoxes.add(text_03);
        text_03.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    // calculate new total
                    calculate_new_total();
                }
            }
        });
        
        // EDIT TEXT 02
        EditText text_04 = new EditText(this);
        text_04.setHint("Description goes here...");
        text_04.setTextColor(Color.parseColor("#000000"));
        text_04.setTextSize(17);
        text_04.setTypeface(Typeface.create("@font/roboto_bold",Typeface.BOLD));
        RelativeLayout.LayoutParams new_layout_4 = new RelativeLayout.LayoutParams(
                (int) (220 * scale + 0.5f), (int) (90 * scale + 0.5f));
        new_layout_4.setMargins( (int) (40 * scale + 0.5f), (int) (90 * scale + 0.5f),0,0);
        text_04.setLayoutParams(new_layout_4);
        extrasDescription.add(text_04);

        //Drawable.createFromPath("@drawable/rectangle_large_greenbottom_01")
        // VIEW 01
        View view_01 = new View(this);
        view_01.setBackground(ContextCompat.getDrawable(this, R.drawable.rectangle_large_01));
        RelativeLayout.LayoutParams new_layout_5 = new RelativeLayout.LayoutParams(
                (int) (304 * scale + 0.5f), (int) (181 * scale + 0.5f));
        new_layout_5.setMargins(0,0, (int) (10 * scale + 0.5f),0);
        view_01.setLayoutParams(new_layout_5);

        // VIEW 02
        View view_02 = new View(this);
        view_02.setBackground(ContextCompat.getDrawable(this, R.drawable.rectangle_large_greenbottom_01));
        RelativeLayout.LayoutParams new_layout_6 = new RelativeLayout.LayoutParams(
                (int) (304 * scale + 0.5f), (int) (90 * scale + 0.5f));
        new_layout_6.setMargins(0, (int) (90 * scale + 0.5f), (int) (10 * scale + 0.5f),0);
        view_02.setLayoutParams(new_layout_6);

        new_l.addView(text_01);
        new_l.addView(view_02);
        new_l.addView(view_01);
        new_l.addView(text_02);
        new_l.addView(text_03);
        new_l.addView(text_04);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public void go_to_journey_page(View view) {
        Intent intent = new Intent(this, activity_journey.class);
        //intent.over
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void go_to_expenditure_page(View view) {
        Intent intent = new Intent(this, activity_expenditure.class);
        //intent.over
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void go_to_history_page(View view) {
        Intent intent = new Intent(this, activity_history.class);
        //intent.over
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
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    }
                    else
                    {
                        // detect right to left swipe
                        // do stuff
                        Intent intent = new Intent(this, activity_journey.class);
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