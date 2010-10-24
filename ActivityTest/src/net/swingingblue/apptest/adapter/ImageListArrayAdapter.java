package net.swingingblue.apptest.adapter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.swingingblue.apptest.R;
import net.swingingblue.apptest.data.ImageListData;
import net.swingingblue.apptest.util.BitmapUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * list_picture向けArrayAdaptorクラス
 * @author findup
 *
 */
public class ImageListArrayAdapter extends ArrayAdapter<ImageListData> {

	private static final String LOG_TAG = ImageListArrayAdapter.class.getName();
	
	LayoutInflater inflater;
	ExecutorService execute = Executors.newSingleThreadExecutor();
	
	/**
	 * すでにList<ImageListData>が存在する場合に指定するコンストラクタ
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public ImageListArrayAdapter(Context context, int textViewResourceId,
			List<ImageListData> objects) {
		super(context, textViewResourceId, objects);
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 空のList<ImageListData>を作成し、動的にaddする場合に使うコンストラクタ
	 * @param context
	 * @param textViewResourceId
	 */
	public ImageListArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * ListViewから各行のレンダリングの際に呼び出される。
	 * convertViewもしくは内部でListのレイアウトからViewを生成し、その各部品にデータをセットしていく。
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.d(LOG_TAG, "position " + position);
		
		// convertviewに値が入ってくるのかは不明
		View view = convertView;
		if (view == null) {
			Log.d(LOG_TAG, "Create new convertview.");
			// list_pictureレイアウトをインスタンス化
			view = inflater.inflate(R.layout.list_picture, null);
		}
		
		final Context context = view.getContext();
		// Layoutをオブジェクトにすると、findViewById()が使えるので目的のUI部品を指定する
		final ImageView imageview = (ImageView)view.findViewById(R.id.ImageViewBitmap);
		imageview.setImageBitmap(null);
		// 対応する番号のインスタンスを取り出し
		final ImageListData listdata = getItem(position);
		final Handler handler = new Handler();	
		
		execute.submit(new Runnable() {
			
			public void run() {
				try {
					final Bitmap bitmap = BitmapUtil.getBitmap(context, listdata.getUri());
					
					handler.post(new Runnable() {
						public void run() {
							imageview.setImageBitmap(bitmap);
						}
					});
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}});
		
		TextView textview = (TextView)view.findViewById(R.id.TextViewPath);
		textview.setText(listdata.getPath());
		
		return view;
	}

}
