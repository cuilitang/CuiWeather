package cui.litang.cuiweather.app.newapi.activitynew;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cui.litang.cuiweather.R;
import cui.litang.cuiweather.app.newapi.dbnew.AreaDBDAO;
import cui.litang.cuiweather.app.newapi.utilsnew.SPUtils;

public class NewChooseAreaActivity extends Activity {
	
	private ListView lv;
	private TextView tv_title;
	private List<String> dataList = new ArrayList<String>();
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	private int currentLevel;
	private ArrayAdapter<String> adapter;
	private String selectedProvince;
	private String selectedCity;
	
	//是否从天气界面转过来
	private Boolean isFromWeatherActivity;
	private ArrayList<String> idList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		//若是有已经选中的默认城市，则直接进入天气显示，不再从选择地域开始
		//若是不是从天气界面转回来的（更换默认城市），则直接进入天气显示界面，若是从天气界面转回来的，则继续执行选择地域功能
		if(SPUtils.getBoolean(getApplicationContext(),"county_selected", false)&&!isFromWeatherActivity){
			Intent intent = new Intent(this, NewWeatherActivity.class);
			startActivity(intent);
			finish();
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		lv = (ListView) findViewById(R.id.lv);
		tv_title = (TextView) findViewById(R.id.tv_title);
		
		copyDB("area.db");
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
		lv.setAdapter(adapter);
		
		//加载数据
		queryProvince();
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				
				if(currentLevel==LEVEL_PROVINCE){
					selectedProvince = dataList.get(position);
					queryCities();
				}else if(currentLevel==LEVEL_CITY){
					selectedCity = dataList.get(position);
					queryCounties();
				}else if(currentLevel==LEVEL_COUNTY){
					String selectedCounty = idList.get(position);
					String selectedCountyName = dataList.get(position);
					
					SPUtils.setBoolean(getApplicationContext(), "county_selected", true);
					SPUtils.setString(getApplicationContext(), "selected_county", selectedCounty);
					SPUtils.setString(getApplicationContext(), "selected_county_name", selectedCountyName);
					
					Intent intent = new Intent(NewChooseAreaActivity.this,NewWeatherActivity.class);
					intent.putExtra("selected_county", selectedCounty);
					intent.putExtra("selected_county_name", selectedCountyName);
					startActivity(intent);
					finish();
				}
			}
		});
		
		
	}

	/**
	 * 查询数据库
	 */
	private void queryCounties() {

		ArrayList<String> countyList = AreaDBDAO.loadCountyByCity(selectedCity);
		idList = new ArrayList<String>();
		
		if(countyList.size()>0){
			dataList.clear();
			for(String c:countyList){
				String[] strArr = c.split(",");
				idList.add(strArr[0]);
				dataList.add(strArr[1]);
			}
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			tv_title.setText(selectedCity);
			currentLevel = LEVEL_COUNTY;
		}
		
	}

	private void queryCities() {

		ArrayList<String> cityList = AreaDBDAO.loadCityByProvince(selectedProvince);
		
		if(cityList.size()>0){
			dataList.clear();
			for(String c:cityList){
				dataList.add(c);
			}
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			tv_title.setText(selectedProvince);
			currentLevel = LEVEL_CITY;
		}
	}

	private void queryProvince() {
		ArrayList<String> provinceList = AreaDBDAO.loadProvince();
		
		if(provinceList.size()>0){
			dataList.clear();
			for(String p:provinceList){
				dataList.add(p);
			}
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			tv_title.setText("中国");
			currentLevel = LEVEL_PROVINCE;
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
