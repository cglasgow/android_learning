package com.example.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener {
	static final String TAG = "StatusActivity";
	EditText editStatus;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Debug.startMethodTracing("Yamba.trace");
        setContentView(R.layout.activity_status);
        
        editStatus = (EditText) findViewById(R.id.edit_status);
        
    }
    
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//Debug.stopMethodTracing();
	}


	@Override
	public void onClick(View v) {
		final String statusText = editStatus.getText().toString();
		
		new PostToTwitter().execute(statusText);
		
		Log.d(TAG, "onClicked! with text: " + statusText);
	}
	
	
	//Menu Stuff
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		//return super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.status, menu);
        return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, UpdaterService.class);
		switch(item.getItemId()) {
			case R.id.item_start_service:
				startService(intent);
				return true;
			case R.id.item_stop_service:
				stopService(intent);
				return true;
			default:
				return false;
		}
	}

	

	class PostToTwitter extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				Twitter twitter = new Twitter("student", "password");
				twitter.setAPIRootUrl("http://yamba.marakana.com/api");
				twitter.setStatus(params[0]);
				Log.d(TAG, "Successfully posted: " + params[0]);
				return "Successfully posted: " + params[0];
			} catch (TwitterException e) {
				Log.e(TAG, "Died", e);
				e.printStackTrace();
				return "Failed posting: " + params[0];
			}
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
		}
	}
}
