package cui.litang.cuiweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CuiWeatheOpenHelper extends SQLiteOpenHelper {

	//Province、City、 County建表语句
	String SQL_PROVICE = "create table Province("
			+ "id integer primary key autoincrement,"
			+ "province_name text,"
			+ "province_code text)";
	
	String SQL_CITY = "create table City("
			+ "id integer primary key autoincrement,"
			+ "city_name text,"
			+ "city_code text,"
			+ "province_id integer)";
	
	String SQL_COUNTY = "create table County("
			+ "id integer primary key autoincrement,"
			+ "county_name text,"
			+ "county_code text,"
			+ "city_id integer)";
	
	
	public CuiWeatheOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(SQL_PROVICE);
		db.execSQL(SQL_CITY);
		db.execSQL(SQL_COUNTY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
