package net.swingingblue.apptest;

import java.util.Date;
import java.util.Iterator;

import android.app.Activity;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class GeoTestActivity extends Activity {

	private LocationManager locationManager;
	
	private TextView sateliteNumView;
	private TextView latitudeView;
	private TextView longitudeView;
	private TextView gpsPrn;
	private TextView timeView;
	private TextView gpsStatusView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.geotest);
		
		sateliteNumView = (TextView)findViewById(R.id.SateliteNum);
		latitudeView = (TextView)findViewById(R.id.Latitude);
		longitudeView = (TextView)findViewById(R.id.Longtitude);
		gpsPrn = (TextView)findViewById(R.id.GpsPrn);
		timeView = (TextView)findViewById(R.id.Time);
		gpsStatusView = (TextView)findViewById(R.id.GpsStatus);
		
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		
		locationManager.addGpsStatusListener(gpsListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onPause() {
		locationManager.removeUpdates(listener);
		locationManager.removeGpsStatusListener(gpsListener);
		
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	LocationListener listener = new LocationListener() {
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(),String.valueOf(status), Toast.LENGTH_SHORT).show();
		}
		
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		public void onLocationChanged(Location location) {
			
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
			
			latitudeView.setText(sb);
			
//			longitudeView.setText(String.valueOf(location.getLongitude()));
			
			timeView.setText(new Date(location.getTime()).toString());
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
				
//				Toast.makeText(getApplicationContext(), "GPS_EVENT_SATELLITE_STATUS", Toast.LENGTH_SHORT).show();
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
	
}
