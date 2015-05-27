package cui.litang.cuiweather.app.activity;


import java.util.ArrayList;
import java.util.List;

import cui.litang.cuiweather.R;
import cui.litang.cuiweather.app.db.CuiWeatherDB;
import cui.litang.cuiweather.app.model.City;
import cui.litang.cuiweather.app.model.County;
import cui.litang.cuiweather.app.model.Province;
import android.app.Activity;
import android.app.DownloadManager.Query;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
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
				}
			}
		});
		
		queryProvince();
	}

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

	private void queryFromServer(Object object, String string) {
		// TODO Auto-generated method stub
		
	}
	
	

}
