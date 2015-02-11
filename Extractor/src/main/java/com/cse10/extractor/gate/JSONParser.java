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
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JSONParser {

    // declare logger
    private Logger logger;

    // constructor
    public JSONParser() {
        logger = Logger.getLogger(this.getClass());
    }

    // get JSONObject from JSON response from google map api in order to get detailed location identification
    public JSONObject getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            // replace white spaces
            address = address.replaceAll("[ \\u00A0]", "%20");

            // make request to google map api and get response
            HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");

            HttpClient client;

            // check for proxy settings
            if(GlobalConstants.PROXY_PORT != 0){
                HttpHost proxy = new HttpHost(GlobalConstants.PROXY_ADDRESS, GlobalConstants.PROXY_PORT, "http");
                client = HttpClientBuilder.create().setProxy(proxy).build();
            }else{
                client = HttpClientBuilder.create().build();
            }
            HttpResponse response;
            stringBuilder = new StringBuilder();

            // obtaining response
            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();

            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            logger.info("Client Protocol Exception : ", e);
        } catch (IOException e) {
            logger.info("IO Exception : ", e);
        }

        // forming JSON object from response
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            logger.info("Wrong JSON : "+stringBuilder.toString());
        }

        return jsonObject;
    }
}