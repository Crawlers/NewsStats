package com.cse10.extractor.gate;

/**
 * Created with IntelliJ IDEA.
 * User: Isuru Jayaweera
 * Date: 12/19/14
 * Making request to google map api for detailed description on location and parsing the response.
 */

import com.cse10.util.GlobalConstants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JSONParser {

    static InputStream is = null;
    static String json = "";

    // constructor
    public JSONParser() {
    }

    // get JSONObject from JSON response from google map api in order to get detailed location identification
    public JSONObject getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            // replace white spaces
            address = address.replaceAll("[ \\u00A0]", "%20");

            // make request to google map api and get response
            HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
            HttpHost proxy = new HttpHost(GlobalConstants.PROXY_ADDRESS, GlobalConstants.PROXY_PORT, "http");
            HttpClient client = HttpClientBuilder.create().setProxy(proxy).build();
            HttpResponse response;
            stringBuilder = new StringBuilder();

            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();

            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            e.getStackTrace();
        } catch (IOException e) {
            e.getStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }
}