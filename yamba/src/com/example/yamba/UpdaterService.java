package com.example.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {
	static final String TAG = "UpdaterService";
	static final int DELAY = 30;
	Twitter twitter;
	boolean running = false;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		twitter = new Twitter("student", "password");
		twitter.setAPIRootUrl("http://yamba.marakana.com/api");
		Log.d(TAG, "onCreated");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		running = true;
		new Thread() {
				public void run() {
					try {
						while (running) {
							List<Status> timeline = twitter.getPublicTimeline();
							
							for (Status status : timeline) {
								Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
							}
							Thread.sleep(DELAY*1000);
						}
					} catch (TwitterException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
		}.start();
		
		Log.d(TAG, "onStarted");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		running = false;
		Log.d(TAG, "onDestroyed");
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
