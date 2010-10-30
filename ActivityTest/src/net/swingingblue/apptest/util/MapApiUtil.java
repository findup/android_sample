package net.swingingblue.apptest.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Projection;

import android.text.InputFilter.LengthFilter;
import android.util.Log;

import net.swingingblue.flickruploader.data.RestRequestData;
import net.swingingblue.flickruploader.restful.RestfulLib;

public class MapApiUtil {

	private static final String LOG_TAG = MapApiUtil.class.getSimpleName();
	
	// base parameters
	//	http://maps.google.com/maps/api/directions/output?parameters
	private static final String baseUrl = "maps.google.co.jp";
	private static final String servicePath = "maps/api/directions/json";
	private static final String jsonFormat = "json";
	
	// query parameters
	private static final String PARAM_ORIGIN = "origin";
	private static final String PARAM_DESTINATION = "destination";
	private static final String PARAM_MODE = "mode";
	private static final String PARAM_WAYPOINTS = "waypoints";
	private static final String PARAM_SENSOR = "sensor";
	private static final String PARAM_LANGUAGE = "language";
	
	// additional parameters
	private static final String PARAM_MODE_WALKING = "walking";
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<GeoPoint> requestDirections(String src, String dest) {
		
		TreeMap<String, String> map = new TreeMap<String, String>();
		
		map.put(PARAM_ORIGIN, src);
		map.put(PARAM_DESTINATION, dest);
		map.put(PARAM_SENSOR, "false");
		map.put(PARAM_LANGUAGE, "ja");

		RestRequestData request = new RestRequestData();
		request.setUrl(baseUrl);
		request.setPath(servicePath);
		request.setQueryParam(map);
		
		URL url = RestfulLib.makeUrl(request);
		String response = RestfulLib.httpGet(url.toString());
		
		return parseDiretion(response);
	}

	
	private ArrayList<GeoPoint> parseDiretion(String entity) {
		ArrayList<GeoPoint> result = new ArrayList<GeoPoint>();
		List<GeoPoint> polylineList;
		
        JSONObject jsonEntity;

        try {
			jsonEntity = new JSONObject(entity);
		
	        if (jsonEntity != null) {
	        	Log.d(LOG_TAG, jsonEntity.toString(4));
	        	
//            	if (!jsonEntity.optString("status").equals("ok")) {
//            		Log.d(LOG_TAG, "failed. : " + jsonEntity.getString("message"));
//            	}
            	
	        	JSONObject jsonRoutes = jsonEntity.optJSONArray("routes").getJSONObject(0);
            	JSONArray jsonLegs = jsonRoutes.getJSONArray("legs");
            	JSONArray jsonSteps = jsonLegs.getJSONObject(0).getJSONArray("steps");
            	
            	String lat;
            	String lng;
            	 
            	for (int i = 0; i < jsonSteps.length(); i++) {
                	JSONObject jsonLoc = jsonSteps.optJSONObject(i);
                	lat = jsonLoc.optJSONObject("start_location").getString("lat");
                	lng = jsonLoc.optJSONObject("start_location").getString("lng");
                	                	
                	GeoPoint geo = new GeoPoint((int)(Double.valueOf(lat)*1000000), (int)(Double.valueOf(lng)*1000000));
                	result.add(geo);
                	
                	Log.d(LOG_TAG, "start_location " + geo.toString());
                	
                	String polyline = jsonLoc.getJSONObject("polyline").getString("points");
                	polylineList = decodePoly(polyline);
                	
                	result.addAll(polylineList);
                	
                	lat = jsonLoc.optJSONObject("end_location").getString("lat");
                	lng = jsonLoc.optJSONObject("end_location").getString("lng");
                	
                	geo = new GeoPoint((int)(Double.valueOf(lat)*1000000), (int)(Double.valueOf(lng)*1000000));
                	result.add(geo);
                	
                	Log.d(LOG_TAG, "end_location " + geo.toString());

                }
	        }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	/**
	 *  http://jeffreysambells.com/posts/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java/
	 */
	private List<GeoPoint> decodePoly(String encoded) {
		List<GeoPoint> poly = new ArrayList<GeoPoint>();

		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;
		
		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;
			GeoPoint p = new GeoPoint((int) (((double) lat / 1E5) * 1E6),
					(int) (((double) lng / 1E5) * 1E6));
			poly.add(p);
		}
		return poly;
	}
	
}
