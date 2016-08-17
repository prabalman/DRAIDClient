package com.example.srishansupertramp.draidclient;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements SensorEventListener {

    final String URL = "http://192.168.0.101/create.php";


    private SensorManager sensorManager;
    double xxval, yyval, zzval;

    //Accelerometer Calculation values
    int num = 0;
    final double alpha = 0.8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);

//        VideoView video  = (VideoView) findViewById(R.id.videoView);
//        MediaController ctrl;
//        video.setVideoURI(Uri.parse("http://192.168.0.101:8080/test.ts"));
//        ctrl = new MediaController(this);
//        ctrl.setMediaPlayer(video);
//        video.setMediaController(ctrl);
//        video.requestFocus();
//        video.start();
        }


    @Override
    public void onSensorChanged(SensorEvent event) {



        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double xval, yval, zval;
            double[] gravity = new double[]{0, 1, 2};

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            xval = event.values[0] - gravity[0];
            yval = event.values[1] - gravity[1];
            zval = event.values[2] - gravity[2];

            xxval = rountToDouble(xval);
            yyval = rountToDouble(yval);
            zzval = rountToDouble(zval);

            new AccelTask(xxval, yyval, zzval).execute();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    double rountToDouble(double num) {
        return Double.valueOf(new DecimalFormat("#.###").format(num));
    }


    public class AccelTask extends AsyncTask<Void, Void, Void> {

        public Double x;
        public Double y;
        public Double z;


        public AccelTask(Double z, Double x, Double y){
            this.z = z;
            this.x = x;
            this.y = y;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            String xx = String.valueOf(x);
            String yy = String.valueOf(y);
            String zz = String.valueOf(z);

            final Map<String, String> parameters = new HashMap<>();
            try {
                parameters.put("x", xx);
                parameters.put("y", yy);
                parameters.put("z", zz);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SystemClock.sleep(1000);
            CustomRequest jsonObject = new CustomRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Boolean success = response.getBoolean("success");
                        String message = response.getString("message");
                        Log.e("JSONsend",success + message );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("JSONsend", "Error: " + error.toString());

                }
            });

            requestQueue.add(jsonObject);
            return null;
        }
    }
}