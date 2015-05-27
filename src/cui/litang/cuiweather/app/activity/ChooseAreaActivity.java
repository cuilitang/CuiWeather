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
	public static final int LEVEL_PROVICE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	private int currentLevel;
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		lv = (ListView) findViewById(R.id.lv);
		tv_title = (TextView) findViewById(R.id.tv_title);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
		lv.setAdapter(adapter);
		
		cuiWeatherDB = CuiWeatherDB.getInstance(this);
		lv.setOnItemClickListener(new OnItemClickListener() {

			

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				
				if(currentLevel==LEVEL_PROVICE){
					provinceList.get(position);
					queryCities();
				}else if(currentLevel==LEVEL_CITY){
					cityList.get(position);
					queryCounties();
				}
			}
		});
		
		queryProvince();
	}

	protected void queryCounties() {
		// TODO Auto-generated method stub
		
	}

	protected void queryCities() {
		// TODO Auto-generated method stub
		
	}

	private void queryProvince() {
		// TODO Auto-generated method stub
		
	}
	
	

}
