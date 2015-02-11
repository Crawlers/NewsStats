package com.cse10.extractor.gate;

/**
 * Created with IntelliJ IDEA.
 * User: Isuru Jayaweera
 * Date: 12/20/14
 * Extracting the necessary information from the JSONObject.
 */

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class DistrictExtractor {

	private ArrayList<String> localDistricts;

	// declare logger
	private Logger logger;

	DistrictExtractor(){
		logger = Logger.getLogger(this.getClass());
	}

	// extracting the district of the location
	public String getDistrict(String location) {
		String district = "NULL";
		String status = "error";

		localDistricts = getDistricts();

		try {

			// using JSONParser to make a request and receive the response as a JSONObject
			JSONParser details = new JSONParser();
			JSONObject jsonObj = details.getLocationInfo(location+",+Sri+Lanka");
			status = jsonObj.getString("status");

			// extracting the district out from the received information
			if (status.equalsIgnoreCase("OK")) {
				JSONArray results = jsonObj.getJSONArray("results");
				JSONObject addressComps = results.getJSONObject(0);
				JSONArray addressCompsArray = addressComps.getJSONArray("address_components");
				int addressSize = addressCompsArray.length();
				int countryPosition;
				int districtPosition;
				String country;

				// obtaining position of country and district details within response
				if (addressCompsArray.getJSONObject(addressSize-1).getJSONArray("types").getString(0).equalsIgnoreCase("country")){
					countryPosition = addressSize-1;
				} else if (addressCompsArray.getJSONObject(addressSize-2).getJSONArray("types").getString(0).equalsIgnoreCase("country")){
					countryPosition = addressSize-2;
				} else{
					countryPosition = 0;
				}

				// if the response is sufficiently informative carry on
				if (countryPosition >= 2){
					districtPosition = countryPosition-2;

					JSONObject countryComp = addressCompsArray.getJSONObject(countryPosition);
					country = countryComp.getString("long_name");

					// check whether location belongs to Sri Lanka or not
					if (country.equalsIgnoreCase("Sri Lanka")){
						JSONObject distComp = addressCompsArray.getJSONObject(districtPosition);
						district = distComp.getString("long_name");
					}
				}
			}

		} catch (JSONException e) {
			logger.info("JSON Exception : ", e);
		}

		if(district.equals("Monaragala")){
			district = "Moneragala";
		}

		if(!localDistricts.contains(district)){
			district = "NULL";
		}

		return district;
	}

	private ArrayList<String> getDistricts(){
		String[] districtArray = {"Ampara", "Anuradhapura", "Badulla", "Batticaloa", "Colombo", "Galle", "Gampaha", "Hambantota", "Jaffna", "Kalutara", "Kandy", "Kegalle", "Kilinochchi", "Kurunegala", "Mannar", "Matale", "Matara", "Moneragala", "Mullaitivu", "Nuwara Eliya", "Polonnaruwa", "Puttalam", "Ratnapura", "Trincomalee"};
		return new ArrayList<String>(Arrays.asList(districtArray));
	}
}