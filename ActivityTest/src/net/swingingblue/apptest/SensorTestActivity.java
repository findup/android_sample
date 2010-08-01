package net.swingingblue.apptest;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SensorTestActivity extends Activity {
	
	ListView listview;
	TextView accuracyTextView;
	TextView sensorValuesTextView;
	TextView sensorInfoTextView;
	SensorListenerEx sensorListener = new SensorListenerEx();
    SensorManager sm;
    
    // デバイスのセンサー情報の保持
    List<Sensor> sensorList;

	private final String LOGTAG = SensorTestActivity.class.getName();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor);
        
        listview = (ListView)findViewById(R.id.ListView01);
        accuracyTextView = (TextView)findViewById(R.id.SensorAccuracy);
        sensorValuesTextView = (TextView)findViewById(R.id.SensorValues);
        sensorInfoTextView = (TextView)findViewById(R.id.SensorInfo);
        
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        
        listview.setOnItemClickListener(itemClickListener);
    }
    
	@Override
	protected void onResume() {

		int layout = R.layout.list;
        ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this, layout);
                
        // センサー情報の取得
        SensorManager sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorList = sm.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sl : sensorList) {
        	StringBuffer sb = new StringBuffer();
        	sb.append("Name: " + sl.getName());
        	sb.append("\n");
        	sb.append("Type: " + getSensorTypeName(sl.getType()));
        	sb.append("\n");
        	sb.append("Vendor: " + sl.getVendor());
        	listadapter.add(sb.toString());
        }
        
        // Listviewにセット
        listview.setAdapter(listadapter);
        
		super.onResume();
	}

    @Override
	protected void onPause() {
    	sm.unregisterListener(sensorListener);
		super.onPause();
	}

	@Override
	protected void onStart() {
    	
    	new Thread(new Runnable() {
			public void run() {
		    	handler.sendEmptyMessage(0);
			}
		}).start();
		super.onStart();
	}

	private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_LONG).show();
    	};
    };
    
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			sm.unregisterListener(sensorListener);
			
			Sensor sensor = sensorList.get(arg2);
	        Sensor ds = sm.getDefaultSensor(sensor.getType());
	        sm.registerListener(sensorListener, ds, SensorManager.SENSOR_DELAY_FASTEST);
	        
	        StringBuffer sb = new StringBuffer();
	        sb.append(sensor.getName());
	        sb.append("\n");
	        sb.append(sensor.getVendor());
	        
	        sensorInfoTextView.setText(sb);
	        
			Toast.makeText(getApplicationContext(), ds.getName()+ " selected.", Toast.LENGTH_LONG).show();
		}
	};
    
    private String getSensorTypeName(int sensorType) {
    	
    	String sensorTypeName;
    	
    	switch (sensorType) {
		case Sensor.TYPE_ACCELEROMETER:
			sensorTypeName = "TYPE_ACCELEROMETER";
			break;
		case Sensor.TYPE_GYROSCOPE:
			sensorTypeName = "TYPE_GYROSCOPE";
			break;
		case Sensor.TYPE_LIGHT:
			sensorTypeName = "TYPE_LIGHT";
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			sensorTypeName = "TYPE_MAGNETIC_FIELD";
			break;
		case Sensor.TYPE_ORIENTATION:
			sensorTypeName = "TYPE_ORIENTATION";
			break;
		case Sensor.TYPE_PRESSURE:
			sensorTypeName = "TYPE_PRESSURE";
			break;
		case Sensor.TYPE_PROXIMITY:
			sensorTypeName = "TYPE_PROXIMITY";
			break;
		case Sensor.TYPE_TEMPERATURE:
			sensorTypeName = "TYPE_TEMPERATURE";
			break;
		default:
			sensorTypeName = "unknown";
			break;
		}
    	
    	return sensorTypeName;
    }
    
    class SensorListenerEx implements SensorEventListener {

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
//			accuracyTextView.setText(new Integer(accuracy).toString());
		}

		public void onSensorChanged(SensorEvent event) {
			StringBuffer sb = new StringBuffer();
			
			sb.append("accuracy : ");
			sb.append(event.accuracy);
			sb.append("\n");
			
			for (float values : event.values ) {
				sb.append("values : ");
				sb.append(values);
				sb.append("\n");
			};

			sensorValuesTextView.setText(sb);
		}
    };
}