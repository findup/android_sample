package net.swingingblue.apptest;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaRecorder;
import android.os.Bundle;

public class CameraView extends Activity {

	private MediaRecorder recorder;
	private CameraPreview mPreview;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		recorder = new MediaRecorder();
		
		mPreview = new CameraPreview(this, recorder);
		setContentView(mPreview);

		String PATH_NAME = "/sdcard/test.mp4";

		recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		recorder.setVideoFrameRate(15);
		recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);

		recorder.setOutputFile(PATH_NAME);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}


}