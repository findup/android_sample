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

	private static final int THUMBNAIL_HEIGHT = 128;
	private static final int THUMBNAIL_WIDTH = 128;
	
	/**
	 * 指定されたUrlから画像をリサイズしながら取得して返す。
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	public final static Bitmap getBitmap(Context context, Uri uri) throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inDither = false;
		options.inJustDecodeBounds = true;
		
		InputStream is = context.getContentResolver().openInputStream(uri);
		// まずはサイズのみを取り出し（bitmapデータ自体はメモリにロードされない）
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);

		is.close();

		// 画像の幅高さから、縮尺サイズを計算
		int fixedHeight = options.outHeight / THUMBNAIL_HEIGHT;
		int fixedWidth = options.outWidth / THUMBNAIL_WIDTH;
		
		options.inSampleSize = (fixedHeight < fixedWidth ? fixedWidth : fixedHeight);
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		
		is = context.getContentResolver().openInputStream(uri);
		
		// 今度は正式にデコード
		bitmap = BitmapFactory.decodeStream(is, null, options);
		
		is.close();
		
		return bitmap;
	}
}
