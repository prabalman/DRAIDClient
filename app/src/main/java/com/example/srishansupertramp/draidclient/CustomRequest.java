package com.example.srishansupertramp.draidclient;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by srishansupertramp on 8/16/16.
 */
public class CustomRequest extends Request<JSONObject>{

    private Response.Listener<JSONObject> listener;
    private Map<String,String> params;

    @Override
    protected Response<JSONObject> parseNetworkResponse (NetworkResponse response){
        try{
            String JSONstring = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JSONObject jobj = new JSONObject(JSONstring);
            return Response.success(jobj, HttpHeaderParser.parseCacheHeaders(response));
        }catch (UnsupportedEncodingException e){
            return Response.error(new ParseError(e));
        }catch (JSONException e){
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response){
        listener.onResponse(response);
    }

    public CustomRequest(String url, Map<String, String> params, Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        super(Method.GET,url,errorListener);

        this.listener = responseListener;
        this.params =  params;
    }

    public CustomRequest(int method, String url, Map<String,String> params, Response.Listener responseListener, Response.ErrorListener errorListener){
        super(method, url, errorListener);
        this.listener = responseListener;
        this.params= params;
    }

    protected Map<String,String> getParams() throws AuthFailureError{
        return params;
    }

}
