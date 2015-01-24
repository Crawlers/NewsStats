package com.cse10.extractor.gate;

/**
 * Created with IntelliJ IDEA.
 * User: Isuru Jayaweera
 * Date: 12/20/14
 * Extracting the necessary information from the JSONObject.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DistrictExtractor {

	// extracting the district of the location
	public String getDistrict(String location) {
		String district = "NULL";
		String status = "error";
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return district;
	}
}
