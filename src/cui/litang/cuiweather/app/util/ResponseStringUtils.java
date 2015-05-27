package cui.litang.cuiweather.app.util;

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
					province.setProvinceName(provinceDetial[0]);
					province.setProvinceCode(provinceDetial[1]);
					
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
	public synchronized static boolean handleCityResponse(CuiWeatherDB cuiWeatherDB,String response) {
		if(!TextUtils.isEmpty(response)){
			String[] allc = response.split(",");
			if(allc!=null&&allc.length>0){
				for (int i = 0; i < allc.length; i++) {
					String[] cityDetail = allc[i].split("\\|");
					City city = new City();
					city.setCityName(cityDetail[0]);
					city.setCityCode(cityDetail[1]);
					
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
	public synchronized static boolean handleCountyResponse(CuiWeatherDB cuiWeatherDB,String response) {
		if(!TextUtils.isEmpty(response)){
			String[] allc = response.split(",");
			if(allc!=null&&allc.length>0){
				for (int i = 0; i < allc.length; i++) {
					String[] countyDetail = allc[i].split("\\|");
					County county = new County();
					county.setCountyName(countyDetail[0]);
					county.setCountyCode(countyDetail[1]);
					cuiWeatherDB.saveCounty(county);
					
				}
				return true;
			}
			
		}
		return false;
		
	}
	
	
	
	

}
