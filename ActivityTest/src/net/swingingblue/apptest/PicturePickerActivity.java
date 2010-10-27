package net.swingingblue.apptest;

import net.swingingblue.apptest.adapter.ImageListArrayAdapter;
import net.swingingblue.apptest.data.ImageListData;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PicturePickerActivity extends Activity {

	private static final String LOG_TAG = PicturePickerActivity.class.getSimpleName();
	
	// 画面部品
	private Button btnPick;
	private ListView listview;
	private TextView textview;
	
	private ImageListArrayAdapter listadapter;
	
	private int PICK_REQUEST_CODE = 1234;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.picturepicker);
		
		btnPick = (Button)findViewById(R.id.BtnPick);
		btnPick.setOnClickListener(lister);
		
		// リスト部品初期化
		listview = (ListView)findViewById(R.id.ListView);

		// リスト部品初期化
		listadapter = new ImageListArrayAdapter(this, R.layout.list_picture);

		listview.setAdapter(listadapter);
		listview.setOnScrollListener(new OnScrollListener() {
			
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				Log.d(LOG_TAG, "onScrollStateChanged state " + scrollState);
				
				view.invalidate();
			}
			
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
//				Log.d(LOG_TAG, String.format("onScroll firstVisibleItem: %d, visibleItemCount %d, totalItemCount: %d", firstVisibleItem, visibleItemCount, totalItemCount));
			}
		});
		
		textview = (TextView)findViewById(R.id.TextViewLog);
		
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
		Cursor cl = MediaStore.Images.Media.query(cr, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null);

		cl.moveToFirst();
		int count = cl.getCount();
//		textview.append("count " + count + "\n");

		for (int i = 1; i < count; i++ ) {
			// ファイルパス名を表示
			Log.d(LOG_TAG, "now " + i + " " + cl.getString(1));

			ImageListData listdata = new ImageListData();
			
			// 画像へのUriは、EXTERNAL_CONTENT_URIにgeString(0)で取れるidを付けることで指定ができる
			Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cl.getString(0));
			// リスト用データ組み立て
			listdata.setUri(uri);
			listdata.setPath(cl.getString(1));

			listadapter.add(listdata);
			cl.moveToNext();
		}
		
		super.onResume();
	}

	@Override
	protected void onStart() {
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
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
