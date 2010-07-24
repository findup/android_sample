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
	TextView sensorNameView;
	TextView textview;
	SensorListenerEx sensorListener = new SensorListenerEx();
    SensorManager sm;

	private final String LOGTAG = SensorTestActivity.class.getName();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor);
        
        listview = (ListView)findViewById(R.id.ListView01);
        textview = (TextView)findViewById(R.id.TextView01);
        sensorNameView = (TextView)findViewById(R.id.TextView00);
        
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "listview Onclick()", Toast.LENGTH_SHORT).show();
			}
		});
        
    }
    
	@Override
	protected void onResume() {
        // ArrayAdaptorを使ってListViewに項目を追加する
		int layout = R.layout.list;
        ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this, layout);
                
        // センサーの種類を一覧表示
        SensorManager sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sm.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sl : sensorList) {
        	StringBuffer sb = new StringBuffer();
        	sb.append("Name: " + sl.getName());
        	sb.append("\n");
        	sb.append("Type: " + getSensorTypeName(sl.getType()));
        	sb.append("\n");
        	sb.append("Vendor: " + sl.getVendor());
        	sb.append("\n");
        	sb.append("MaximumRange: " + sl.getMaximumRange());
        	sb.append("\n");
        	sb.append("Power: " + sl.getPower());
        	sb.append("\n");
        	sb.append("Resolution: " + sl.getResolution());
//        	sb.append("\n");
//        	sb.append("Version: " + sl.getVersion());
        	listadapter.add(sb.toString());
        }
        
        // Listviewにセット
        listview.setAdapter(listadapter);
        
        Sensor ds = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
//        sensorNameView.setText(ds.getName());
        sm.registerListener(sensorListener, ds, SensorManager.SENSOR_DELAY_FASTEST);
        
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
				// Handlerへ空のメッセージ送信
		    	handler.sendEmptyMessage(0);
			}
		}).start();
		super.onStart();
	}

    // Handlerの生成。Looperはここに接続される
	private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_LONG).show();
    	};
    };
    
    class SensorListenerEx implements SensorEventListener {

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			textview.setText(new Integer(accuracy).toString());
			
		}

		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			StringBuffer sb = new StringBuffer();
			
			sb.append(event.accuracy);
			
			for (float values : event.values ) {
				sb.append("values : ");
				sb.append(values);
				sb.append("\n");
			};

			textview.setText(sb);
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
    
}