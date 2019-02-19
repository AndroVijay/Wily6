package com.wily.databasehandelr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by PANDEY on 05-05-2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Wily_APPLOCK";
    private static final int DATABASE_VERSION = 1;
    private static final String CRAETE_TABLE = "CRAETE TABLE wily_userprofile(user_pattern TEXT,user_pin TEXT)";
    public static String table_name = "Wily_UserProfile";
    String colom_one = "user_pattern";
    String colom_two = "user_pin";
    String app_table = "App_lock_packages";
    public static String app_table_colom1 = "Appname";
    public static String table_colom_package= "Package";
    Cursor c;
    /*  private static final String CREATE_QUERY =
              "CREATE TABLE "+"Wily_UserProfile" +"("+"user_pattern"+" TEXT,"+
                      "user_pin"+" TEXT);";
  */
    String app_table_colom2 = "App_status";
    Context _cntx;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("DATABASE_OPERATION", "Database Created / Opened...");
        _cntx = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_QUERY = "CREATE TABLE  " + table_name + "(" + colom_one + " TEXT," + colom_two + " TEXT);";
//    String Creat_app_table="CREATE TABLE  "+app_table +"("+ app_table_colom1+ " TEXT,"+app_table_colom2+" TEXT);";
            String Creat_app_table = "CREATE TABLE  " + app_table + "("  + table_colom_package + " TEXT);";


            if (isTableExists1(db, app_table)) {
//                Toast.makeText(_cntx, "", Toast.LENGTH_SHORT).show();
            } else {
                db.execSQL(Creat_app_table);
            }

            if (isTableExists(db, table_name)) {

//            Toast.makeText(_cntx, ""+isTableExists(db,table_name), Toast.LENGTH_SHORT).show();

            } else {

                db.execSQL(CREATE_QUERY);
                Log.e("DATABASE_OPERATION", "Table Created");
            }


        } catch (Exception e) {
            System.out.println("err" + e);
        }

    }


    public void insert_app( String pack,SQLiteDatabase db) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(table_colom_package,pack);
        db.insert(app_table, null, contentValues);
    }


//    public Cursor search_app(String app_name,SQLiteDatabase db){
//        String search="SELECT * FROM "+app_table +"WHERE "+app_table_colom1+"="+app_name+";";
//
//        Cursor cursor;
//        String[] columns = {colom_one, colom_two};
//
//        cursor = db.query(table_name, columns, null, null, null, null, null);
//        return cursor;
//
//    }


    public void insert_data(String pat, String pin, SQLiteDatabase db) {

//            db.isOpen();

        ContentValues contentValues = new ContentValues();
        contentValues.put(colom_one, pat);
        contentValues.put(colom_two, pin);

//            db.insert()

        db.insert(table_name, null, contentValues);
        Log.d("DATA", "ONE ROW IMSERTED");
//        db.close();


    }

    public void delete_appname(String pack, SQLiteDatabase db) {

//        String delet_app_nane="";
        // delete from tablename where id='1' and name ='jack'
        db.delete(app_table, table_colom_package + "=?", new String[]{pack});
        deleteDataFromDatabase(pack);
//        db.execSQL("DELETE  FROM "+app_table+" WHERE "+app_table_colom1+"="+app_name+";");
        //SQLiteDatabase db = this.getWritableDatabase();
        // db.delete(TABLE_NAME,null,null);
        //db.execSQL("delete * from"+ TABLE_NAME);
//        db.execSQL("DROP TABLE " + table_name);
//        db.close();
    }

    public void deleteDataFromDatabase(String pack) {
        SQLiteDatabase db1 = this.getWritableDatabase();

        try {
            db1.delete(app_table, table_colom_package + "=?", new String[]{pack});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db1.close();
        }
    }

    public Cursor getInformations(SQLiteDatabase db) {
        Cursor cursor;
        String[] columns = {colom_one, colom_two};

        cursor = db.query(table_name, columns, null, null, null, null, null);
        return cursor;
    }

    public Cursor fetch_appname(SQLiteDatabase db) {
        Cursor cursor;
        String[] columns = {table_colom_package};

        cursor = db.query(app_table, columns, null, null, null, null, null);
        return cursor;

    }


    boolean isTableExists(SQLiteDatabase db, String tableName) {
        String check_table = "SELECT name " + DATABASE_NAME + "WHERE type='table' AND name='" + table_name + "'";
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    boolean isTableExists1(SQLiteDatabase db, String tableName) {
        String check_table = "SELECT name " + DATABASE_NAME + "WHERE type='table' AND name='" + app_table + "'";
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public void resetPassword(String pat, String pin) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues value = new ContentValues();
            value.put(colom_one, pat);
            value.put(colom_two, pin);

            db.update(table_name, value, null,null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


//        Toast.makeText(_cntx, "On_upgrade", Toast.LENGTH_SHORT).show();

    }
}
