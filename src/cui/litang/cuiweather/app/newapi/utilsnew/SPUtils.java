package cui.litang.cuiweather.app.newapi.utilsnew;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPerference 工具类
 * @author Cuilitang
 *
 */
public class SPUtils {
	
	private static final String SP_NAME = "config";

	/**
	 * 读boolean值
	 * @param context 上下文
	 * @param key
	 * @param defValue 默认值
	 * @return 得到的值
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
	
	/**
	 * 写boolean值
	 * @param context 上下文
	 * @param key 
	 * @param value 要写入的值
	 */
	public static void setBoolean(Context context, String key, boolean value) {
		
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	
	/**
	 * 写入String值
	 * @param context 上下文
	 * @param key key
	 * @param value value
	 */
	public static void setString(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	
	/**
	 * 取得String值
	 * @param context 上下文
	 * @param key key
	 * @param value 默认value
	 */
	public static String getString(Context context, String key,String defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

}
