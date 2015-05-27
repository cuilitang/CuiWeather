package cui.litang.cuiweather.app.db;

import java.util.ArrayList;
import java.util.List;

import cui.litang.cuiweather.app.model.City;
import cui.litang.cuiweather.app.model.County;
import cui.litang.cuiweather.app.model.Provice;
import android.R.color;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
	 * 插入provice值
	 * @param provice provice对象
	 */
	public void saveProvice(Provice provice){
		if(provice!=null){
			ContentValues contentValues = new ContentValues();
			contentValues.put("provice_name", provice.getProviceName());
			contentValues.put("provice_code", provice.getProviceCode());
			db.insert("Provice", null, contentValues);
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
	 * 加载所有Provice列表
	 * @return List<Provice>
	 */
	public List<Provice> loadProvice() {
		
		ArrayList<Provice> list = new ArrayList<Provice>();
		Cursor cursor = db.query("Provice", null, null, null, null, null, null);
		
		if(cursor.moveToFirst()){
			do{
				Provice provice = new Provice();
				provice.setId(cursor.getInt(cursor.getColumnIndex("id")));
				provice.setProviceName(cursor.getString(cursor.getColumnIndex("provice_name")));
				provice.setProviceCode(cursor.getString(cursor.getColumnIndex("provice_code")));
				list.add(provice);
			}while(cursor.moveToNext());
		}
		
		if(cursor!=null){
			cursor.close();
			cursor =null;
		}
		
		return list;
		
		
	}
	
	
	/**
	 * 加载当前Provice下所有City列表
	 * @param ProviceId 当前Provice的ID
	 * @return List<City> City列表
	 */
	public List<City> loadCity(int ProviceId) {
		
		ArrayList<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "provice_id = ?", new String[]{String.valueOf(ProviceId)}, null, null, null);
		
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
