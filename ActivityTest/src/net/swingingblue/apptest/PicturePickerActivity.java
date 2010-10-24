package net.swingingblue.apptest;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class PicturePickerActivity extends Activity {

	private Button btnPick;
	private ListView listview;
	private ArrayAdapter<String> adapter;
	
	private int PICK_REQUEST_CODE = 1234;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.picturepicker);
		
		btnPick = (Button)findViewById(R.id.BtnPick);
		btnPick.setOnClickListener(lister);
		
		// リスト部品初期化
		listview = (ListView)findViewById(R.id.ListView);
		adapter = new ArrayAdapter<String>(this, R.layout.list);
		listview.setAdapter(adapter);
		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		ContentResolver cr = getContentResolver();
		Cursor cl = MediaStore.Images.Media.query(cr, null, null);

		
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	private OnClickListener lister = new OnClickListener() {
		
		public void onClick(View v) {
			Intent i = new Intent();
			i.setAction(Intent.ACTION_PICK);
			i.setType("image/*");
			startActivityForResult(i, PICK_REQUEST_CODE);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == PICK_REQUEST_CODE) {
			Log.d(this.getClass().getName(), "resultCode " + resultCode);
			Uri uri = data.getData();
			
			adapter.add(uri.getPath());
			
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
}
