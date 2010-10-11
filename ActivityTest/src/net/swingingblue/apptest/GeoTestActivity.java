package net.swingingblue.apptest;

import java.util.Date;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GeoTestActivity extends Activity {

	private LocationManager locationManager;
	
	private TextView positionView;
	private TextView gpsPrn;
	private TextView timeView;
	private TextView gpsStatusView;
	private Button toMapButton;
	
	private double latitude;
	private double longtitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.geotest);
		
		positionView = (TextView)findViewById(R.id.position);
		gpsPrn = (TextView)findViewById(R.id.GpsPrn);
		timeView = (TextView)findViewById(R.id.Time);
		gpsStatusView = (TextView)findViewById(R.id.GpsStatus);
		toMapButton = (Button)findViewById(R.id.ToMapButton);
		
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		
		toMapButton.setOnClickListener(toMapButtonClick);
		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		stopPositioning();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		startPositionig();
	}

	LocationListener listener = new LocationListener() {
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Toast.makeText(getApplicationContext(),String.valueOf(status), Toast.LENGTH_SHORT).show();
		}
		
		public void onProviderEnabled(String provider) {
			
		}
		
		public void onProviderDisabled(String provider) {
			
		}
		
		public void onLocationChanged(Location location) {
			dispLocation(location);
		}
	};
	
	GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
		
		/**
		 * GPS 情報が更新されたら
		 */
		public void onGpsStatusChanged(int event) {
			
			GpsStatus gpsStatus = locationManager.getGpsStatus(null);
			Iterable<GpsSatellite> i = gpsStatus.getSatellites();
						
			switch (event) {
			case GpsStatus.GPS_EVENT_STARTED:
				Toast.makeText(getApplicationContext(), "GPS_EVENT_STARTED", Toast.LENGTH_SHORT).show();
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				
				StringBuffer sb = new StringBuffer();
				sb.append("PRN:");
				Iterator<GpsSatellite> it = i.iterator();
				
				while (it.hasNext()) {
					sb.append(it.next().getPrn());
					sb.append(" ");
				}
				
				gpsPrn.setText(sb);
				
				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				Toast.makeText(getApplicationContext(), "GPS_EVENT_STOPPED", Toast.LENGTH_SHORT).show();
				break;
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				gpsStatusView.setText("GPS_EVENT_FIRST_FIX");
				Toast.makeText(getApplicationContext(), "GPS_EVENT_FIRST_FIX", Toast.LENGTH_LONG).show();
				break;
			}
			
		}
	};
	
	private OnClickListener toMapButtonClick = new OnClickListener() {
		
		public void onClick(View v) {
			String geo = "geo:" + String.valueOf(latitude) + "," + String.valueOf(longtitude);
			Intent mi = new Intent(Intent.ACTION_VIEW, Uri.parse(geo));
			startActivity(mi);
		}
	};
	
	/**
	 * 測位開始
	 */
	private void startPositionig() {
		if (locationManager != null) {
			locationManager.addGpsStatusListener(gpsListener);
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			dispLocation(location);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, listener);
		}
	}
	
	/**
	 * 測位停止
	 */
	private void stopPositioning() {
		if (locationManager != null) {
			locationManager.removeUpdates(listener);
			locationManager.removeGpsStatusListener(gpsListener);
		}
	}
	
	private void dispLocation(Location location) {
		
		if (location != null) {
				
			StringBuffer sb = new StringBuffer();
			sb.append("lat:");
			sb.append(Location.convert(location.getLatitude(), Location.FORMAT_SECONDS) + "\n");
			sb.append("lon:");
			sb.append(Location.convert(location.getLongitude(), Location.FORMAT_SECONDS) + "\n");
			sb.append("alt:");
			sb.append(location.getAltitude() + "\n");
			sb.append("acc:");
			sb.append(location.getAccuracy() + "\n");
			sb.append("prv:");
			sb.append(location.getProvider() + "\n");
			sb.append("ber:");
			sb.append(location.getBearing() + "\n");
			sb.append("spd:");
			sb.append(location.getSpeed() + "\n");
			
			positionView.setText(sb);
			
			latitude = location.getLatitude();
			longtitude = location.getLongitude();
			
			timeView.setText(new Date(location.getTime()).toString());
		}

	}
	
}
