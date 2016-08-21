package com.example.srishansupertramp.draidclientretrofit.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srishansupertramp on 8/17/16.
 */
public class AccelDataResponse {

    @SerializedName("success")
    @Expose
    public Boolean success;

    @SerializedName("message")
    @Expose
    public String message;


    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }



}
