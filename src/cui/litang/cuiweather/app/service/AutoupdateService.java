package cui.litang.cuiweather.app.service;

import cui.litang.cuiweather.app.receiver.AutoupdateRecevier;
import cui.litang.cuiweather.app.util.HttpCallbackListener;
import cui.litang.cuiweather.app.util.HttpUtils;
import cui.litang.cuiweather.app.util.ResponseStringUtils;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

public class AutoupdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				//updateWeather();
				updateWeatherNew();
				
			}
		}).start();
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 60*60*1000; //一小时
		int anMin = 60*1000;   //一分钟
		long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
		Intent intent2 = new Intent(this,AutoupdateRecevier.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent2, 0);
		alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}

	protected void updateWeatherNew() {

		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String cityId = preferences.getString("city_id", "");
		String address = "http://www.weather.com.cn/data/cityinfo/"+cityId+".html";
		HttpUtils.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				Log.d("MainActivity", response);
				ResponseStringUtils.handleWeatherResponse(AutoupdateService.this, response);
			}
			
			@Override
			public void onError(Exception e) {

				e.printStackTrace();
			}
		});
	
		
	}

	/**
	 * 更新天气信息
	 */
	private void updateWeather() {
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String cityId = preferences.getString("city_id", "");
		String address = "http://www.weather.com.cn/data/cityinfo/"+cityId+".html";
		HttpUtils.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				Log.d("MainActivity", response);
				ResponseStringUtils.handleWeatherResponse(AutoupdateService.this, response);
			}
			
			@Override
			public void onError(Exception e) {

				e.printStackTrace();
			}
		});
	}
	
	

}
