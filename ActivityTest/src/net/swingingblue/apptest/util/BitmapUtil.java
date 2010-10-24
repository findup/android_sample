package net.swingingblue.apptest.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

/**
 * Bitmap系の共通処理を切り出したユーティリティクラス
 * @author findup
 *
 */
public class BitmapUtil {
	
	/**
	 * 指定されたUrlから画像をリサイズしながら取得して返す。
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	public final static Bitmap getBitmap(Context context, Uri uri) throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 20;
		options.inPurgeable = true;
		options.inDither = false;
		
		InputStream is = context.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
		is.close();
		
		return bitmap;
	}
}
