package cui.litang.cuiweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import cui.litang.cuiweather.app.db.CuiWeatherDB;
import cui.litang.cuiweather.app.model.City;
import cui.litang.cuiweather.app.model.County;
import cui.litang.cuiweather.app.model.Province;


/**
 * 解析服务器返回的省市县字符串并保存到本地SQLite数据库
 * @author Cuilitang
 * @Date 2015年5月27日
 */
public class ResponseStringUtils {
	
	/**
	 * 解析和处理服务器返回的省级数据：01|北京,02|南京...
	 * @param cuiWeatherDB 获取数据库连接
	 * @param response 升级数据字符串
	 * @return 解析是否成功
	 */
	public synchronized static boolean handleProvinceResponse(CuiWeatherDB cuiWeatherDB,String response) {
		if(!TextUtils.isEmpty(response)){
			String[] allp = response.split(",");
			if(allp!=null&&allp.length>0){
				for (int i = 0; i < allp.length; i++) {
					String[] provinceDetial = allp[i].split("\\|");
					Province province = new Province();
					province.setProvinceName(provinceDetial[1]);
					province.setProvinceCode(provinceDetial[0]);
					
					cuiWeatherDB.saveProvince(province);
					
				}
				return true;
			}
			
		}
		return false;
		
	}
	

	/**
	 * 解析和处理服务器返回的市级数据：01|济南,02|青岛...
	 * @param cuiWeatherDB 获取数据库连接
	 * @param response 升级数据字符串
	 * @return 解析是否成功
	 */
	public synchronized static boolean handleCityResponse(CuiWeatherDB cuiWeatherDB,String response,int provinceId) {
		if(!TextUtils.isEmpty(response)){
			String[] allc = response.split(",");
			if(allc!=null&&allc.length>0){
				for (int i = 0; i < allc.length; i++) {
					String[] cityDetail = allc[i].split("\\|");
					City city = new City();
					city.setCityName(cityDetail[1]);
					city.setCityCode(cityDetail[0]);
					city.setProvinceId(provinceId);
					
					
					cuiWeatherDB.saveCity(city);
					
				}
				return true;
			}
			
		}
		return false;
		
	}
	
	/**
	 * 解析和处理服务器返回的县级数据：01|历下,02|槐荫...
	 * @param cuiWeatherDB 获取数据库连接
	 * @param response 升级数据字符串
	 * @return 解析是否成功
	 */
	public synchronized static boolean handleCountyResponse(CuiWeatherDB cuiWeatherDB,String response,int cityId) {
		if(!TextUtils.isEmpty(response)){
			String[] allc = response.split(",");
			if(allc!=null&&allc.length>0){
				for (int i = 0; i < allc.length; i++) {
					String[] countyDetail = allc[i].split("\\|");
					County county = new County();
					county.setCountyName(countyDetail[1]);
					county.setCountyCode(countyDetail[0]);
					county.setCityId(cityId);
					cuiWeatherDB.saveCounty(county);
					
				}
				return true;
			}
			
		}
		return false;
		
	}
	
	
	/**
	 * 解析天气信息json
	 * @param context
	 * @param response  返服务器返回的天气信息json字符串
	 */
	public static void handleWeatherResponse(Context context,String response) {
		
		try{
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherinfoNode = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherinfoNode.getString("city");
			String cityid = weatherinfoNode.getString("cityid");
			String temp1 = weatherinfoNode.getString("temp1");
			String temp2 = weatherinfoNode.getString("temp2");
			String weatherDesc = weatherinfoNode.getString("weather");
			String ptime = weatherinfoNode.getString("ptime");
			saveWeahterInfo(context,cityName,cityid,temp1,temp2,weatherDesc,ptime);
			
			
			
			
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}


	/**
	 * 将天气信息存储到SharedPerference
	 * @param context
	 * @param cityName
	 * @param cityid
	 * @param temp1
	 * @param temp2
	 * @param weatherDesc
	 * @param ptime
	 */
	private static void saveWeahterInfo(Context context,String cityName,String cityid,String temp1,String temp2,String weatherDesc,String ptime) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyy年M月d日",Locale.CHINA);
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("city_id", cityid);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desc", weatherDesc);
		editor.putString("p_time", ptime);
		editor.putString("current_date", format.format(new Date()));
		editor.commit();
		
		
		
	}
	

}
