package com.example.srishansupertramp.draidclientretrofit.Interfaces;

import com.example.srishansupertramp.draidclientretrofit.MainActivity;
import com.example.srishansupertramp.draidclientretrofit.Models.AccelDataRequest;
import com.example.srishansupertramp.draidclientretrofit.Models.AccelDataResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by srishansupertramp on 8/17/16.
 */
public interface DRAIDClientAPI {

    @POST("create.php/")
    Call<AccelDataResponse> getResponse(@Body AccelDataRequest accelTask);
}
