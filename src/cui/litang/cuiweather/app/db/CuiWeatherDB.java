package cui.litang.cuiweather.app.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cui.litang.cuiweather.app.model.City;
import cui.litang.cuiweather.app.model.County;
import cui.litang.cuiweather.app.model.Province;

public class CuiWeatherDB {
	
	//数据库名称
	//数据库版本
	public static final String DB_NAME = "cui_weather";
	public static final int DB_VERSION = 1;
	
	private static CuiWeatherDB cuiWeatherDB;
	private SQLiteDatabase db;
	
	/**
	 * 单例
	 * @param context
	 */
	private CuiWeatherDB(Context context) {

		CuiWeatheOpenHelper helper = new CuiWeatheOpenHelper(context, DB_NAME, null, DB_VERSION);
		db = helper.getWritableDatabase();
	}
	
	
	public synchronized static CuiWeatherDB getInstance(Context context){
		if(cuiWeatherDB==null){
			cuiWeatherDB = new CuiWeatherDB(context);
		}
		return cuiWeatherDB;
		
	}
	
	/**
	 * 插入Province值
	 * @param Province Province对象
	 */
	public void saveProvince(Province province){
		if(province!=null){
			ContentValues contentValues = new ContentValues();
			contentValues.put("Province_name", province.getProvinceName());
			contentValues.put("Province_code", province.getProvinceCode());
			db.insert("Province", null, contentValues);
		}
	}
	
	
	
	/**
	 * 插入city值
	 * @param city 对象
	 */
	public void saveCity(City city){
		if(city!=null){
			ContentValues contentValues = new ContentValues();
			contentValues.put("city_name", city.getCityName());
			contentValues.put("city_code", city.getCityCode());
			db.insert("City", null, contentValues);
		}
	}
	
	/**
	 * 插入county值
	 * @param county 对象
	 */
	public void saveCounty(County county){
		if(county!=null){
			ContentValues contentValues = new ContentValues();
			contentValues.put("county_name", county.getCountyName());
			contentValues.put("county_code", county.getCountyCode());
			db.insert("County", null, contentValues);
		}
	}
	
	/**
	 * 加载所有Province列表
	 * @return List<Province>
	 */
	public List<Province> loadProvince() {
		
		ArrayList<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		
		if(cursor.moveToFirst()){
			do{
				Province Province = new Province();
				Province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				Province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				Province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(Province);
			}while(cursor.moveToNext());
		}
		
		if(cursor!=null){
			cursor.close();
			cursor =null;
		}
		
		return list;
		
		
	}
	
	
	/**
	 * 加载当前Province下所有City列表
	 * @param ProvinceId 当前Province的ID
	 * @return List<City> City列表
	 */
	public List<City> loadCity(int ProvinceId) {
		
		ArrayList<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(ProvinceId)}, null, null, null);
		
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				list.add(city);
			}while(cursor.moveToNext());
		}
		
		if(cursor!=null){
			cursor.close();
			cursor =null;
		}
		
		return list;
		
		
	}
	
	/**
	 * 加载当前City下所有County列表
	 * @param cityId 当前City的ID
	 * @return List<County> County列表
	 */
	public List<County> loadCounty(int cityId) {
		
		ArrayList<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
		
		if(cursor.moveToFirst()){
			do{
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				list.add(county);
			}while(cursor.moveToNext());
		}
		
		if(cursor!=null){
			cursor.close();
			cursor =null;
		}
		
		return list;
		
		
	}
	
	

}
