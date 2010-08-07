package net.swingingblue.apptest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * メインメニュー
 * @author findup
 *
 */
public class MainMenuActivity extends Activity {

	private ListView listview;
	
	static final String keyItemName = "ItemName";
	static final String keyPackage = "Package";
	static final String keyActivity = "Activity";
	
	@SuppressWarnings({ "unchecked", "serial" })
	static final List<HashMap<String,String>> menuItems = 
		Arrays.asList(
				new HashMap<String, String>(){
					{ 
						put(keyItemName, "Sensor"); 
						put(keyPackage, SensorTestActivity.class.getPackage().getName());
						put(keyActivity, SensorTestActivity.class.getName());
					}},
				new HashMap<String, String>(){
					{ 
						put(keyItemName, "Geocode"); 
						put(keyPackage, GeoTestActivity.class.getPackage().getName());
						put(keyActivity, GeoTestActivity.class.getName());
						}},
				new HashMap<String, String>(){
					{ 
						put(keyItemName, "CameraRecorder"); 
						put(keyPackage, CameraView.class.getPackage().getName());
						put(keyActivity, CameraView.class.getName());
						}}
				);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.main);
		
		listview = (ListView)findViewById(R.id.ListView01);
		
		SimpleAdapter adaptor = new SimpleAdapter(this, menuItems, 
				android.R.layout.simple_list_item_1, 
				new String[]{keyItemName, keyActivity}, 
				new int[]{android.R.id.text1});
	
		listview.setAdapter(adaptor);
		listview.setOnItemClickListener(listener);
		
		super.onCreate(savedInstanceState);
	}

	private OnItemClickListener listener = new OnItemClickListener() {
		
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
				
			Intent intent = new Intent();
			HashMap<String, String> item = menuItems.get(arg2);
			intent.setClassName(item.get(keyPackage), item.get(keyActivity));
			startActivity(intent);
		}
	};
	
}
