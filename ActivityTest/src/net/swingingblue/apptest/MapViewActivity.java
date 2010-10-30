package net.swingingblue.apptest;

import java.util.ArrayList;
import java.util.Iterator;

import net.swingingblue.apptest.util.LineOverlay;
import net.swingingblue.apptest.util.MapApiUtil;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class MapViewActivity extends MapActivity {

	private Button btnDirection;
	private EditText editTextSource;
	private EditText editTextDestination;
	private MapView mapView;
	
	private MapApiUtil mapApiUtil;
	private LineOverlay lineOverlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mapview);
		
		mapView = (MapView)findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		mapView.setFocusable(true);
		mapView.setEnabled(true);
		mapView.setClickable(true);
		
		btnDirection = (Button)findViewById(R.id.BtnDirection);
		editTextSource = (EditText)findViewById(R.id.EditTextSource);
		editTextDestination = (EditText)findViewById(R.id.EditTextDestination);
		
		btnDirection.setOnClickListener(onClickDirection);
		
		// オーバーレイクラスをMapViewに追加
		lineOverlay = new LineOverlay();
		
//		lineOverlay.addPoint(new GeoPoint(33581867, 130424312));
//		lineOverlay.addPoint(new GeoPoint(33589357, 130399851));
//		mapView.getOverlays().add(lineOverlay);

		mapApiUtil = new MapApiUtil();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private OnClickListener onClickDirection = new OnClickListener() {
		
		public void onClick(View v) {
			
			// ソフトウェアキーボードを隠す
			InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editTextSource.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(editTextDestination.getWindowToken(), 0);
			
			// Direction APIへ要求発行
			ArrayList<GeoPoint> geoList = mapApiUtil.requestDirections(
					editTextSource.getText().toString(), editTextDestination.getText().toString());
			Iterator<GeoPoint> it = geoList.iterator();
			
			lineOverlay.clearPoint();
			
			// 出発地に移動
			GeoPoint geo;
			geo = it.next();
			lineOverlay.addPoint(geo);
			
			MapController controller = mapView.getController();
			controller.animateTo(geo);
			
			while (it.hasNext()) {
				geo = it.next();
				lineOverlay.addPoint(geo);
			}
			
			// 一度クリアしてもういちど
			mapView.getOverlays().clear();
			mapView.getOverlays().add(lineOverlay);
			mapView.invalidate();
		}
	};

}
