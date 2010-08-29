package net.swingingblue.apptest;

import java.io.IOException;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	SurfaceHolder mHolder;
	
	// MediaRecoderのインスタンスを引きずりまわすのではなく、
	// 本当はHandlerかListnerか別のパターンでActivtyと結合したい
	MediaRecorder _recorder;

	CameraPreview(Context context, MediaRecorder recorder) {
		super(context);
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		_recorder = recorder;
	}

	public void surfaceCreated(SurfaceHolder holder) {

		Log.d("CameraPreview", "SurfaceCreated");
	}

	public void surfaceDestroyed(SurfaceHolder holder) {

		_recorder.stop();
		_recorder.reset();
		_recorder.release();
		_recorder = null;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w,
			int h) {
		_recorder.setPreviewDisplay(holder.getSurface());

		try {
			_recorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		_recorder.start();			
	}
}
