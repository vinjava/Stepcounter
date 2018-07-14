package com.jay.stepcounter1;



import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  implements SensorEventListener{

    TextView tv_steps;

    SensorManager sensorManager;



    boolean running = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String total_steps_day;
        total_steps_day  = readFromFile();
        Log.wtf("total_steps day before assignment ", "stepcountfile" + total_steps_day);
        total_steps_day = "test";
        writeToFile(total_steps_day);

        tv_steps =findViewById(R.id.tv_steps);
        //tv_steps2 =findViewById(R.id.tv_steps2);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }
    @Override
    protected  void onResume()
    {
        super.onResume();
        running = true;
       // System.out.println("inside  ON RESUME");
       Log.wtf(" inside  ON RESUME", "TSTMP: " + System.currentTimeMillis());
        Sensor countsensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countsensor != null)
        {
            sensorManager.registerListener(this, countsensor,SensorManager.SENSOR_DELAY_UI);
        }
        else
        {
            Toast.makeText(this,"sensor not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected  void onPause()
    {
        super.onPause();
        running = false;
        Log.wtf(" inside ONPAUSE", "TSTMP: " + System.currentTimeMillis());
       // System.out.println("Inside oN PAUSE");
        getDate(System.currentTimeMillis());
        //update DB here

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(running) {
            //store in temp counter  and on pause update db.
            //user,start time ,end time,steps
           Log.wtf("STEPS","VALUE" + String.valueOf(event.values[0]));
          //  System.out.println("STEPS" + String.valueOf(event.values[0]));
            tv_steps.setText(String.valueOf(event.values[0]));
          //  event.sensor.getVendor().
          //  Log.wtf("TVSTEPS","TVSTEPS" + tv_steps.getText());

            //Log.wtf(" INSIDE IF ", "TSTMP: " + System.currentTimeMillis());

            //  tv_steps2.setText(String.valueOf(event.values[1]));
            //Log.wtf(" inside sensor changed", "TSTMP: " + System.currentTimeMillis());
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("stepcount.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput("stepcount.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("yyyy-MM-dd'T'HH:mm'Z'", cal).toString();
        Log.wtf("ISODATE","ISODATE:" + date);
    }



}
