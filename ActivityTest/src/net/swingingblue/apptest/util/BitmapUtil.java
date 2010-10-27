package net.swingingblue.apptest.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

/**
 * Bitmap系の共通処理を切り出したユーティリティクラス
 * @author findup
 *
 */
public class BitmapUtil {

	private static final int THUMBNAIL_HEIGHT = 128;
	private static final int THUMBNAIL_WIDTH = 128;
	
	private static final String LOG_TAG = BitmapUtil.class.getSimpleName();
	
	private static final int MAX_THUMBNAIL_HEIGHT = 128;
	private static final int MAX_THUMBNAIL_WIDTH = 128;
	
	/**
	 * 指定されたUrlから画像をリサイズしながら取得して返す。
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	public final static synchronized Bitmap getBitmap(Context context, Uri uri) throws IOException {
		
//		Log.d(LOG_TAG, "getBitmap() start. " + uri);
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inDither = false;
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inInputShareable = true;

		InputStream is = context.getContentResolver().openInputStream(uri);
		// まずはBitmapのサイズのみを取得（outHeight,outWidthに入ってくる）
		BitmapFactory.decodeStream(is, null, options);
		is.close();
		
		int fixedWidth = options.outWidth / MAX_THUMBNAIL_WIDTH;
		int fixedHeight = options.outHeight / MAX_THUMBNAIL_HEIGHT;

		options.inSampleSize = (fixedWidth < fixedHeight ? fixedHeight : fixedWidth);
		
		if (options.inSampleSize % 2 == 1) {
			options.inSampleSize ++;
		}
		
		options.inJustDecodeBounds = false;
		
//		Log.d(LOG_TAG, "getBitmap() fixedW: " + fixedWidth + " fixedH: " + fixedHeight + " inSampleSize: " + options.inSampleSize);

		is = context.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
		is.close();

//		Log.d(LOG_TAG, "getBitmap() end.");

		return bitmap;
	}
}
