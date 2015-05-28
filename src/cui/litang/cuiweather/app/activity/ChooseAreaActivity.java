package cui.litang.cuiweather.app.activity;


import java.util.ArrayList;
import java.util.List;

import cui.litang.cuiweather.R;
import cui.litang.cuiweather.app.db.CuiWeatherDB;
import cui.litang.cuiweather.app.model.City;
import cui.litang.cuiweather.app.model.County;
import cui.litang.cuiweather.app.model.Province;
import cui.litang.cuiweather.app.util.HttpCallbackListener;
import cui.litang.cuiweather.app.util.HttpUtils;
import cui.litang.cuiweather.app.util.ResponseStringUtils;
import android.app.Activity;
import android.app.DownloadManager.Query;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	
	private ListView lv;
	private TextView tv_title;
	private List<String> dataList = new ArrayList<String>();
	private CuiWeatherDB cuiWeatherDB;
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	private int currentLevel;
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	private ArrayAdapter<String> adapter;
	private Province selectedProvince;
	private City selectedCity;
	private ProgressDialog progressDialog;
	
	//是否从天气界面转过来
	private Boolean isFromWeatherActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		//若是有已经选中的默认城市，则直接进入天气显示，不再从选择地域开始
		//若是不是从天气界面转回来的（更换默认城市），则直接进入天气显示界面，若是从天气界面转回来的，则继续执行选择地域功能
		if(prefs.getBoolean("city_selected", false)&&!isFromWeatherActivity){
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		lv = (ListView) findViewById(R.id.lv);
		tv_title = (TextView) findViewById(R.id.tv_title);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
		lv.setAdapter(adapter);
		
		cuiWeatherDB = CuiWeatherDB.getInstance(this);
		lv.setOnItemClickListener(new OnItemClickListener() {

			

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				
				if(currentLevel==LEVEL_PROVINCE){
					selectedProvince = provinceList.get(position);
					queryCities();
				}else if(currentLevel==LEVEL_CITY){
					selectedCity = cityList.get(position);
					queryCounties();
				}else if(currentLevel==LEVEL_COUNTY){
					String countyCode = countyList.get(position).getCountyCode();
					Intent intent = new Intent(ChooseAreaActivity.this,WeatherActivity.class);
					intent.putExtra("county_code", countyCode);
					startActivity(intent);
					finish();
				}
			}
		});
		
		queryProvince();
	}

	/**
	 * 查询数据，更新ui
	 *   查询数据时候，会先去数据库load，若是为空，则queryFromServer，然后save在数据库中，然后再调用load  
	 */
	private void queryCounties() {
		countyList = cuiWeatherDB.loadCounty(selectedCity.getId());
		
		
		if(countyList.size()>0){
			dataList.clear();
			for(County c:countyList){
				dataList.add(c.getCountyName());
			}
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			tv_title.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		}else {
			queryFromServer(selectedCity.getCityCode(),"county");
		}
	}

	private void queryCities() {
		cityList = cuiWeatherDB.loadCity(selectedProvince.getId());
		
		
		if(cityList.size()>0){
			dataList.clear();
			for(City p:cityList){
				dataList.add(p.getCityName());
			}
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			tv_title.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		}else {
			queryFromServer(selectedProvince.getProvinceCode(),"city");
		}
	}

	private void queryProvince() {
		provinceList = cuiWeatherDB.loadProvince();
		
		if(provinceList.size()>0){
			dataList.clear();
			for(Province p:provinceList){
				dataList.add(p.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			tv_title.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		}else {
			queryFromServer(null,"province");
		}
	}

	private void queryFromServer(final String code, final String type) {

		String address;
		if(!TextUtils.isEmpty(code)){
			address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
		}else {
			address = "http://www.weather.com.cn/data/list3/city.xml";

		}
		showProgressDialog();
		
		HttpUtils.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				boolean result = false;
				if("province".equals(type)){
					result = ResponseStringUtils.handleProvinceResponse(cuiWeatherDB, response);
				}else if ("city".equals(type)) {
					result = ResponseStringUtils.handleCityResponse(cuiWeatherDB, response,selectedProvince.getId());
				}else if ("county".equals(type)) {
					result = ResponseStringUtils.handleCountyResponse(cuiWeatherDB, response,selectedCity.getId());
					
				}
				
				if(result){
					runOnUiThread(
							new Runnable() {
								
								@Override
								public void run() {
									closeProgressDialog();
									if("province".equals(type)){
										queryProvince();
									}
									else if("city".equals(type)){
										queryCities();
									}else if("county".equals(type)){
										queryCounties();
									}
								}
							}
						);
				}
				
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
						
					}
				});
				
			}
		});
	}
	
	/**
	 * 打开和关闭进度对话框
	 */
	private void showProgressDialog(){
		if(progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	private void closeProgressDialog(){
		if(progressDialog!=null){
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	@Override
	public void onBackPressed(){
		
		if(currentLevel==LEVEL_COUNTY){
			queryCities();
		}else if (currentLevel==LEVEL_CITY) {
			queryProvince();
			
		}/*else if (isFromWeatherActivity) {
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
		}*/
		else{
			finish();
		}
	}
	

}
