package com.meetp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class CommunicationService extends Service {

	private static final String TAG = "CommunicationService";
	public class ConnectionServiceImpl extends ICommunicationService.Stub{
		@Override
		public int getBalance() throws RemoteException{
			Log.v(TAG, "getBalance running");
			return 20;
			//TODO Połączenie z serwerem i pobranie balansu.
		}
		
		@Override
		public boolean getLoginAvailability(String login){
			Log.v(TAG, "getLoginAvailability running");
			return true;
			//TODO Połączenie s serwerem i pobranie dostępności loginu.
		}
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		//TODO Inicjalizowanie połączenia z serwerem.
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new ConnectionServiceImpl();
	}
	
}
