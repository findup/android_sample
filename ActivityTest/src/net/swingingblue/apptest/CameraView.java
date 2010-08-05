package net.swingingblue.apptest;

import java.io.FileDescriptor;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraView extends Activity {

	private MediaRecorder recorder;
	private Preview mPreview;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mPreview = new Preview(this);
		setContentView(mPreview);

		recorder = new MediaRecorder();

		String PATH_NAME = "/sdcard/test.mp4";

		recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		recorder.setVideoFrameRate(15);
		recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);

		recorder.setOutputFile(PATH_NAME);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

	}

	class Preview extends SurfaceView implements SurfaceHolder.Callback {
		SurfaceHolder mHolder;

		Preview(Context context) {
			super(context);
			mHolder = getHolder();
			mHolder.addCallback(this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		public void surfaceCreated(SurfaceHolder holder) {

		}

		public void surfaceDestroyed(SurfaceHolder holder) {

			recorder.stop();
			recorder.reset();
			recorder.release();
			recorder = null;
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			recorder.setPreviewDisplay(holder.getSurface());

			try {
				recorder.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			recorder.start();			
		}
	}

}