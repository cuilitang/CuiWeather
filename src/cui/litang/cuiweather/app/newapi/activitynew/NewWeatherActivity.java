package cui.litang.cuiweather.app.newapi.activitynew;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cui.litang.cuiweather.R;
import cui.litang.cuiweather.app.activity.ChooseAreaActivity;
import cui.litang.cuiweather.app.model.City;
import cui.litang.cuiweather.app.newapi.dbnew.AreaDBDAO;
import cui.litang.cuiweather.app.newapi.utilsnew.SPUtils;
import cui.litang.cuiweather.app.newapi.utilsnew.URLUtils;
import cui.litang.cuiweather.app.util.HttpCallbackListener;
import cui.litang.cuiweather.app.util.HttpUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 展示天气页面，新接口
 * @author Cuilitang
 *
 */
public class NewWeatherActivity extends Activity implements OnClickListener{
	
	private LinearLayout layout_weather_info;
	private TextView tv_city_name;
	private TextView tv_current_date;
	private TextView tv_publish_text;
	private TextView tv_weather_desc;
	private TextView tv_temp;
	private Button btn_switch_city;
	private Button btn_refresh_weather;
	private String pubTime;
	
	
	Map<String , String> mainWeather = new HashMap<String , String>(){{
		    put("00", "晴");
		    put("01", "多云");
		    put("02", "阴");
		    put("03", "阵雨");
		    put("04", "雷阵雨");
		    put("05", "雷阵雨伴有冰雹");
		    put("06", "雨夹雪");
		    put("07", "小雨");
		    put("08", "中雨");
		    put("09", "大雨");
		    put("10", "暴雨");
		    put("11", "大暴雨");
		    put("12", "特大暴雨");
		    put("13", "阵雪");
		    put("14", "小雪");
		    put("15", "中雪");
		    put("16", "大雪");
		    put("17", "暴雪");
		    put("18", "雾");
		    put("19", "冻雨");
		    put("20", "沙尘暴");
		    put("21", "小到中雨");
		    put("22", "中到大雨");
		    put("23", "大到暴雨");
		    put("24", "暴雨到大暴雨");
		    put("25", "大暴雨到特大暴雨");
		    put("26", "小到中雪");
		    put("27", "中到大雪");
		    put("28", "大到暴雪");
		    put("29", "浮尘");
		    put("30", "扬沙");
		    put("31", "强沙尘暴");
		    put("53", "霾");
		    put("99", "未知");
		    }};
	private TextView tv_wind;
	
	List<String> windDirection = new ArrayList<String>(Arrays.asList("无持续风向","东北风","东风","东南风","南风","西南风","西风","西北风","北风","旋转风"));
	List<String> windLevel = new ArrayList<String>(Arrays.asList("微风","3-4 级","4-5 级","5-6 级","6-7 级","7-8 级","8-9 级","9-10 级","10-11 级","11-12 级"));
	private String url;
	private String selectedCountyName;
	private String countyId;
	private String pubHour;
	private String pubDay;
		   
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_weather_layout);
		
		//初始化各个控件
		layout_weather_info = (LinearLayout) findViewById(R.id.layout_weather_info);
		tv_city_name = (TextView) findViewById(R.id.tv_city_name);
		tv_current_date = (TextView) findViewById(R.id.tv_current_date);
		tv_publish_text = (TextView) findViewById(R.id.tv_publish_text);
		tv_temp = (TextView) findViewById(R.id.tv_temp);
		tv_weather_desc = (TextView) findViewById(R.id.tv_weather_desc);
		tv_wind = (TextView)findViewById(R.id.tv_wind);
		
		countyId = getIntent().getStringExtra("selected_county");
		selectedCountyName = getIntent().getStringExtra("selected_county_name");
		tv_city_name.setText(selectedCountyName);
		System.out.println(selectedCountyName);
	
		
		if(!TextUtils.isEmpty(countyId)){
			tv_publish_text.setText("同步中...");
			layout_weather_info.setVisibility(View.INVISIBLE);
			tv_city_name.setVisibility(View.VISIBLE);
						
			url = URLUtils.genURL(countyId);
			
			System.out.println(url);
			//天气预报的时效性比较强，所以就不用缓存了，但是在联网情况下后台服务会自动去更新天气json信息。
			getDataFromServer(url);
		}else {
			//showWeather();
		}
		
		//手动刷新天气 和 更换城市  两个功能
		btn_refresh_weather = (Button) findViewById(R.id.btn_refresh_weather);
		btn_switch_city = (Button) findViewById(R.id.btn_switch_city);
		
		btn_refresh_weather.setOnClickListener(this);
		btn_switch_city.setOnClickListener(this);
	}

	/**
	 * 从网络下载天气信息
	 * @param url
	 */
	private void getDataFromServer(String url) {
		
		HttpUtils.sendHttpRequest(url, new HttpCallbackListener() {

			@Override
			public void onFinish(final String response) {
				
				runOnUiThread(new Runnable() {
					public void run() {
						try {
							parseData(response);
						} catch (JSONException e) {
							// Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
			}

			@Override
			public void onError(Exception e) {

				runOnUiThread(new Runnable() {
					public void run() {
						tv_publish_text.setText("同步失败");
					}
				});
			}});

		
		
	}

	/**
	 * 解析json
	 * @throws JSONException 
	 */
	protected void parseData(String response) throws JSONException {
		
		System.out.println(response);
		
		JSONObject jo = new JSONObject(response);
		JSONObject object = jo.getJSONObject("f");
		pubTime = (String) object.get("f0");
		JSONArray array = object.getJSONArray("f1");
		JSONObject f0 = array.getJSONObject(0);  //今天的预报
		JSONObject f1 = array.getJSONObject(1);  //明天的预报

		String todayWeather = parseForcast(f0);
		String tomorrowWeather = parseForcast(f1);
		String nowDay = new SimpleDateFormat("dd").format(new Date());
		pubDay = pubTime.substring(6,8);
		
		
		if(pubDay.equals(nowDay)){
			
				showWeather(todayWeather);
				tv_publish_text.setText("今天"+pubTime.substring(8,10)+":00发布");
		}else{
			
			showWeather(tomorrowWeather);
			tv_publish_text.setText("昨天"+pubTime.substring(8,10)+":00发布");

		}
		
		
	}
		
	/**
	 * 显示天气的方法
	 * @param 天气字符串
	 */
	private void showWeather(String weather) {
		
		
		String[] weathers = weather.split(",");
		tv_current_date.setText(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
		tv_weather_desc.setText(mainWeather.get(weathers[0]));
		tv_temp.setText(weathers[1]);
		if(!weathers[2].equals("0")){
			tv_wind.setText(windDirection.get(Integer.parseInt(weathers[2]))+windLevel.get(Integer.parseInt(weathers[3])));
		}
		layout_weather_info.setVisibility(View.VISIBLE);
		
		
	}

	public String parseForcast(JSONObject f) throws JSONException{
		
		String weather;
		String dayweather = f.getString("fa");  //白天天气现象
		String nightweather = f.getString("fb");  //晚上天气现象
		String daytemp = f.getString("fc");  //白天温度
		String nighttemp = f.getString("fd");  //晚上温度
		String daywinddirection = f.getString("fe");  //白天风向
		String nightwinddirection = f.getString("ff");  //晚上风向
		String daywindlevel = f.getString("fg");  //白天风力
		String nightwindlevel = f.getString("fh");  //晚上风力
		
		System.out.println("白天天气"+dayweather);
		System.out.println("白天气温"+daytemp+"~"+nighttemp);
		System.out.println("白天风"+daywinddirection+daywindlevel);
		System.out.println("发布时间"+pubTime);
		pubHour = pubTime.substring(8,10);
		if(!pubHour.equals("18")){
			weather = dayweather+","+daytemp+"℃ ~ "+nighttemp+"℃"+","+daywinddirection+","+daywindlevel+","+pubTime;
		}else{
			weather = nightweather+","+nighttemp+"℃"+","+nightwinddirection+","+nightwindlevel+","+pubTime;
		}
		return weather;
		
	}	
	
	
	/**
	 * 手动更新天气和更换城市功能
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_refresh_weather:
			tv_publish_text.setText("更新中...");
			getDataFromServer(url);
			break;
		case R.id.btn_switch_city:
			
			Intent intent = new Intent(this, NewChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}
	


}
