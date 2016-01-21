package cui.litang.cuiweather.app.newapi.dbnew;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * 新的接口中，城市代码与原来城市不同，所以最后选择完城市之后，还需要根据选择的城市到新数据库中将城市代码读出来，然后再到服务器上去请求天气数据
 * @author Cuilitang
 *
 */
public class AreaDBDAO {
	
	private static String path;
	private static SQLiteDatabase db;
	public static void openDatabase(){
		path = "/data/data/cui.litang.cuiweather/files/area.db";
		db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
	}
	
	/**
	 * 根据传进来的城市名称查询城市编号
	 * @param number  号码
	 * @return
	 */ 
	public static String getId(String name) {
		
		openDatabase();
		
		Cursor cursor = db.rawQuery("select id from area where name = ?", new String[]{name});
		String result =null;
		while(cursor.moveToNext()){
			result = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return result;
	}

	public static ArrayList<String> loadProvince() {
		
		openDatabase();
		ArrayList<String> al = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select distinct(provcn) from area", null);
		while(cursor.moveToNext()){
			al.add(cursor.getString(0));
		}
		return al;
		
		
	}

	public static ArrayList<String> loadCityByProvince(String selectedProvince) {
		
		openDatabase();
		
		ArrayList<String> al = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select distinct(districtcn) from area where provcn = ?", new String[]{selectedProvince});
		while(cursor.moveToNext()){
			al.add(cursor.getString(0));
		}
		return al;
	}

	public static ArrayList<String> loadCountyByCity(String selectedCity) {
		
		openDatabase();
		
		ArrayList<String> al = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select id,name from area where districtcn = ?", new String[]{selectedCity});
		while(cursor.moveToNext()){
			al.add(cursor.getString(0)+","+cursor.getString(1));
		}
		return al;
	}
}
