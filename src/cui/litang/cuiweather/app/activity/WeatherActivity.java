package cui.litang.cuiweather.app.activity;

import cui.litang.cuiweather.R;
import cui.litang.cuiweather.app.model.City;
import cui.litang.cuiweather.app.service.AutoupdateService;
import cui.litang.cuiweather.app.util.HttpCallbackListener;
import cui.litang.cuiweather.app.util.HttpUtils;
import cui.litang.cuiweather.app.util.ResponseStringUtils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener{
	
	private LinearLayout layout_weather_info;
	private TextView tv_city_name;
	private TextView tv_current_date;
	private TextView tv_publish_text;
	private TextView tv_temp2;
	private TextView tv_title;
	private TextView tv_weather_desc;
	private TextView tv_temp1;
	private Button btn_switch_city;
	private Button btn_refresh_weather;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		//初始化各个控件
		
		layout_weather_info = (LinearLayout) findViewById(R.id.layout_weather_info);
		tv_city_name = (TextView) findViewById(R.id.tv_city_name);
		tv_current_date = (TextView) findViewById(R.id.tv_current_date);
		tv_publish_text = (TextView) findViewById(R.id.tv_publish_text);
		tv_temp1 = (TextView) findViewById(R.id.tv_temp1);
		tv_temp2 = (TextView) findViewById(R.id.tv_temp2);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_weather_desc = (TextView) findViewById(R.id.tv_weather_desc);
		
		String county_code = getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(county_code)){
			tv_publish_text.setText("同步中...");
			layout_weather_info.setVisibility(View.INVISIBLE);
			tv_city_name.setVisibility(View.INVISIBLE);
			queryWeatherCode(county_code);
		}else {
			showWeather();
		}
		
		//手动刷新天气 和 更换城市  两个功能
		btn_refresh_weather = (Button) findViewById(R.id.btn_refresh_weather);
		btn_switch_city = (Button) findViewById(R.id.btn_switch_city);
		
		btn_refresh_weather.setOnClickListener(this);
		btn_switch_city.setOnClickListener(this);
	}

	/**
	 * 从SharedPreference文件中读取存储的天气信息，并显示到界面上
	 * 启动自动更新天气服务
	 */
	private void showWeather() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		tv_city_name.setText(preferences.getString("city_name", ""));
		tv_temp1.setText(preferences.getString("temp1", ""));
		tv_temp2.setText(preferences.getString("temp2", ""));
		tv_weather_desc.setText(preferences.getString("weather_desc", ""));
		tv_publish_text.setText("今天"+preferences.getString("p_time", "")+"发布");
		tv_current_date.setText(preferences.getString("current_date", ""));
		
		layout_weather_info.setVisibility(View.VISIBLE);
		tv_city_name.setVisibility(View.VISIBLE);
		
		//启动自动更新天气服务
		Intent intent = new Intent(this, AutoupdateService.class);
		startService(intent);
		
		
		
	}

	/**
	 * 查询县级代号countycode对应的天气代号weathercode
	 * @param county_code 县级代号
	 */
	private void queryWeatherCode(String county_code) {
		
		String address = "http://www.weather.com.cn/data/list3/city"+county_code+".xml";
		queryFromServer(address,"countyCode");
	}

	/**
	 * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
	 * @param address
	 * @param type
	 */
	private void queryFromServer(final String address, final String type) {

		HttpUtils.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(final String response) {
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						String[] split = response.split("\\|");
						if(split!=null && split.length == 2){
							String weatherCode = split[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if ("weatherCode".equals(type)) {
					Log.d("MainActivity", response);
					ResponseStringUtils.handleWeatherResponse(WeatherActivity.this, response);
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							showWeather();
						}
					});
				}
				
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					public void run() {
						tv_publish_text.setText("同步失败");
					}
				});
			}
		});
	}

	/**
	 * 查询天气代号对应的天气  
	 * @param weatherCode
	 */
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromServer(address, "weatherCode");
	}

	/**
	 * 手动更新天气和更换城市功能
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_refresh_weather:
			tv_publish_text.setText("更新中...");
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			String cityid = preferences.getString("city_id", "");
			if(!TextUtils.isEmpty(cityid)){
				queryWeatherInfo(cityid);
			}
			
			break;
		case R.id.btn_switch_city:
			
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

}
