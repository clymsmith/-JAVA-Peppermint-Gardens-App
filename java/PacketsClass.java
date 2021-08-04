package com.pmg.peppermintgardensapp;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.ContentValues.TAG;

public class PacketsClass {



    public String data_packet_sent = "no_success";

    public void test_method() {
        Log.d(TAG,"hello there");
    }

    public void add_packet_to_queue(String packet, Context ctx) throws JSONException {
        // check if file exists
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

        File file = new File(ctx.getFilesDir().getAbsolutePath() + "/packet_data.json");

        if (file.exists()) {
            // if does exist, add to already existing file
            Log.d(TAG,"ONE!!!!");
            overwrite_existing_packet_file(packet, ctx);
        } else {
            // if not, create new file and add details
            Log.d(TAG,"TWO!!!!");
            create_new_packet_file(packet, ctx);
        }
    }

    public String get_packet_data(Context ctx) {
        try {
            FileInputStream fileIn = ctx.openFileInput("packet_data.json");
            InputStreamReader inputRead = new InputStreamReader(fileIn);
            int c;
            String temp = "";
            while ((c = fileIn.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            fileIn.close();
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public int find_number_of_packets(Context ctx) {
        File file = new File(ctx.getFilesDir().getAbsolutePath() + "/packet_data.json");

        if (file.exists()) {
            // get number of packets
            return open_packets(ctx);
        } else {
            // if not, create new file and add details
            return 0;
        }

    }

    public int open_packets(Context ctx) {
        try {
            FileInputStream fileIn = ctx.openFileInput("packet_data.json");
            InputStreamReader inputRead = new InputStreamReader(fileIn);
            int c;
            String temp = "";
            while ((c = fileIn.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            fileIn.close();
            JSONObject json_obj = new JSONObject(temp);
            JSONArray json_array = json_obj.getJSONArray("packets");
            Log.d(TAG, "LENGTH OF ARRAY " + json_array.length());
            return json_array.length();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void create_new_packet_file(String packet, Context ctx) {
        try {
            String data = "{\"packets\":["+ packet +"]}";
            Log.d(TAG,data);

            FileOutputStream fileOut = ctx.openFileOutput("packet_data.json", ctx.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
            outputWriter.write(data);
            outputWriter.close();

            Log.d(TAG,"FILE CREATED!! " + ctx.getFilesDir().getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void overwrite_existing_packet_file(String packet, Context ctx) throws JSONException {
        JSONObject obj = load_packet_json(ctx);
        JSONObject obj_from_data = new JSONObject(packet);

        JSONArray obj_array = obj.getJSONArray("packets");

        obj_array.put(obj_from_data);

        JSONObject final_obj = new JSONObject("{\"packets\":"+ obj_array.toString() +"}");

        // save
        save_packet_file_json(ctx, final_obj.toString());
        Log.d(TAG,"OVERWRITTEN!!!! " + final_obj.toString());

    }

    public JSONObject load_packet_json(Context ctx) throws JSONException {
        JSONObject adc;
            try {
                FileInputStream fileIn = ctx.openFileInput("packet_data.json");
                InputStreamReader inputRead = new InputStreamReader(fileIn);
                int c;
                String temp = "";
                while ((c = fileIn.read()) != -1) {
                    temp = temp + Character.toString((char) c);
                }
                fileIn.close();
                adc = new JSONObject(temp);
                Log.d(TAG, "File Loaded! ++ " + temp);
            } catch (Exception e) {
                e.printStackTrace();
                adc = new JSONObject("");
                Log.d(TAG, "File NOT LOADED!");
            }

            return adc;
    }

    public void save_packet_file_json(Context ctx, String jsonString) {
        try {
            FileOutputStream fileOut = ctx.openFileOutput("packet_data.json", ctx.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
            outputWriter.write(jsonString);
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear_packet_file(Context ctx) {
        try {
            FileOutputStream fileOut = ctx.openFileOutput("packet_data.json", ctx.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
            outputWriter.write("{\"packets\":[]}");
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
