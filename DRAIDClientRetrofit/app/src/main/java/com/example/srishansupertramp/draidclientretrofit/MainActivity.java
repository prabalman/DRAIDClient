package com.example.srishansupertramp.draidclientretrofit;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.srishansupertramp.draidclientretrofit.Interfaces.DRAIDClientAPI;
import com.example.srishansupertramp.draidclientretrofit.Models.AccelDataRequest;
import com.example.srishansupertramp.draidclientretrofit.Models.AccelDataResponse;

import java.net.URL;
import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    double Xval,Yval,Zval;


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(getString(R.string.SERVER_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    AccelDataRequest accelDataRequest = new AccelDataRequest();

    //Accel Values
    int num=0;
    final double alpha = 0.8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI);


    }


    @Override
    public void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double[] gravity = new double[]{0, 1, 2};

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            Xval = rountToDouble(event.values[0] - gravity[0]);
            Yval = rountToDouble(event.values[1] - gravity[1]);
            Zval = rountToDouble(event.values[2] - gravity[2]);

            TextView textView1 = (TextView) findViewById(R.id.textView);
            TextView textView2 = (TextView) findViewById(R.id.textView2);
            TextView textView3 = (TextView) findViewById(R.id.textView3);

            textView1.setText(String.valueOf(Xval));
            textView2.setText(String.valueOf(Yval));
            textView3.setText(String.valueOf(Zval));



            accelDataRequest.setX(Xval);
            accelDataRequest.setY(Yval);
            accelDataRequest.setZ(Zval);

            DRAIDClientAPI draidClientAPI = retrofit.create(DRAIDClientAPI.class);
            Call<AccelDataResponse> ServerResponse = draidClientAPI.getResponse(accelDataRequest);

            ServerResponse.enqueue(new Callback<AccelDataResponse>() {
                @Override
                public void onResponse(Response<AccelDataResponse> response) {

                    AccelDataResponse RESPONSE = response.body();

                    Log.d("Retrofit","Response: "+RESPONSE.getSuccess() + " " + RESPONSE.getMessage());
                }

                @Override
                public void onFailure(Throwable t) {

                    Log.d("Retrofit","Response Fail:" + t.getMessage());
                }
            });


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    double rountToDouble(double num){
        return Double.valueOf(new DecimalFormat("#.###").format(num));
    }


}
