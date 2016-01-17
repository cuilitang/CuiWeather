package cui.litang.cuiweather.app.newapi.activitynew;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cui.litang.cuiweather.R;
import cui.litang.cuiweather.app.newapi.dbnew.AreaDBDAO;
import cui.litang.cuiweather.app.newapi.utilsnew.URLUtils;
import cui.litang.cuiweather.app.util.HttpCallbackListener;
import cui.litang.cuiweather.app.util.HttpUtils;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 展示天气页面，新接口
 * @author Cuilitang
 *
 */
public class NewWeatherActivity extends Activity{
	
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
		setContentView(R.layout.new_weather_layout);
		
		//初始化各个控件
		layout_weather_info = (LinearLayout) findViewById(R.id.layout_weather_info);
		tv_city_name = (TextView) findViewById(R.id.tv_city_name);
		tv_current_date = (TextView) findViewById(R.id.tv_current_date);
		tv_publish_text = (TextView) findViewById(R.id.tv_publish_text);
		tv_temp1 = (TextView) findViewById(R.id.tv_temp1);
		tv_temp2 = (TextView) findViewById(R.id.tv_temp2);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_weather_desc = (TextView) findViewById(R.id.tv_weather_desc);
		
		String countyName = getIntent().getStringExtra("county_name");
		
		if(!TextUtils.isEmpty(countyName)){
			tv_publish_text.setText("同步中...");
			layout_weather_info.setVisibility(View.INVISIBLE);
			tv_city_name.setVisibility(View.INVISIBLE);
			copyDB("area.db");
			System.out.println(countyName);
			String id = AreaDBDAO.getId(countyName);
			
			System.out.println(id + countyName);
			
			String url = URLUtils.genURL(id);
			
			System.out.println(url);
			
			getDataFromServer(url);
		}else {
			//showWeather();
		}
		
		//手动刷新天气 和 更换城市  两个功能
		btn_refresh_weather = (Button) findViewById(R.id.btn_refresh_weather);
		btn_switch_city = (Button) findViewById(R.id.btn_switch_city);
		
		//btn_refresh_weather.setOnClickListener(this);
		//btn_switch_city.setOnClickListener(this);
	}

	/**
	 * 从网络下载天气信息
	 * @param url
	 */
	private void getDataFromServer(String url) {
		
		HttpUtils.sendHttpRequest(url, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				
				parseData();
				
				
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
	 */
	protected void parseData() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 拷贝号码归属地数据库
	 */
	private void copyDB(String fileName) {
		try {
			File file = new File(getFilesDir(), fileName);
			if (file.exists() && file.length() > 0) {
				
			} else {
				InputStream is = getAssets().open(fileName);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
