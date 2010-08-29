package net.swingingblue.apptest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;

public class CameraViewNetwork extends Activity {

	private MediaRecorder recorder;
	private CameraPreview mPreview;
	private Socket socket;
	private ParcelFileDescriptor fd = null;

	private final String DEST_ADDR = "192.168.0.31";
	private final int PORT = 1234;
	private final String name = "/sdcard/test2.mp4";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		recorder = new MediaRecorder();

		mPreview = new CameraPreview(this, recorder);
		setContentView(mPreview);
		
		try {
			socket = new Socket(DEST_ADDR, PORT);
			fd = ParcelFileDescriptor.fromSocket(socket);
//			byte[] buffer = new byte[] {1,2,3,4,5};
//			new FileOutputStream(fd.getFileDescriptor()).write(buffer);
			
//			File file = new File(name);
//			fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_WRITE_ONLY | ParcelFileDescriptor.MODE_CREATE);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		recorder.setVideoFrameRate(5);
		recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
//		recorder.setVideoSize(320, 240); 

		recorder.setOutputFile(fd.getFileDescriptor());
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}


}
