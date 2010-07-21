package net.swingingblue.apptest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SampleTestActivity extends Activity {
	
	ListView listview;
	
	private final String LOGTAG = SampleTestActivity.class.getName();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        listview = (ListView)findViewById(R.id.ListView01);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        for(int i=0; i < 199; i++) {
            adapter.add("Test" + new Integer(i));
        }
                
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.d(LOGTAG, "listview onItemClick");
			}
		});
        
        listview.setAdapter(adapter);
    }
    
    @Override
	protected void onStart() {
    	
    	new Thread(new Runnable() {
			public void run() {
				// Handler�֋�̃��b�Z�[�W���M
		    	handler.sendEmptyMessage(0);
			}
		}).start();
		super.onStart();
	}

    // Handler�̐����BLooper�͂����ɐڑ������
	private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_LONG).show();
    	};
    };
    
    
}