package info.earntalktime.at.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import info.earntalktime.at.assistivebean.AssistiveBean;

/**
 * Created by PANDEY on 18-07-2017.
 */

public class Assistive_Db extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Wily_ASSISTIVE1";
    private static final int DATABASE_VERSION = 1;
    private static final String CRAETE_TABLE = "CRAETE TABLE wily_Assistive(app_id number,package_name TEXT,image BLOB)";
    public static String table_name = "Wily_Assist";
    String colom_one = "app_id";
    String colom_two = "package";
    String colom_three = "image";
    String colom_four="staus";
    public static String app_table_colom1 = "Appname";
    public static String table_colom_package= "Package";
    Cursor c;
    /*  private static final String CREATE_QUERY =
              "CREATE TABLE "+"Wily_UserProfile" +"("+"user_pattern"+" TEXT,"+
                      "user_pin"+" TEXT);";
  */
    String app_table_colom2 = "App_status";
    Context _cntx;


    public Assistive_Db(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
_cntx=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            String CREATE_QUERY = "CREATE TABLE  " + table_name + "(" + colom_one + " TEXT," + colom_two + " TEXT,"+colom_three + " BLOB," +colom_four +" TEXT "+");";
//    String Creat_app_table="CREATE TABLE  "+app_table +"("+ app_table_colom1+ " TEXT,"+app_table_colom2+" TEXT);";
//            String Creat_app_table = "CREATE TABLE  " + app_table + "("  + table_colom_package + " TEXT);";




            if (!isTableExists(db, table_name)) {
                db.execSQL(CREATE_QUERY);
            } else {
//                Toast.makeText(_cntx, ""+isTableExists(db,table_name), Toast.LENGTH_SHORT).show();

                Log.e("DATABASE_OPERATION", "Table Created");
            }


        } catch (Exception e) {
            System.out.println("err" + e);
        }


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


    Object lock=new Object();
   public ArrayList<AssistiveBean> getAllData(){
       ArrayList<AssistiveBean> list = new ArrayList<AssistiveBean>();

       SQLiteDatabase db = this.getWritableDatabase();
       try {
           // Select All Query

           String selectQuery = "SELECT  * FROM " + table_name;
           Cursor cursor = db.rawQuery(selectQuery, null);
           if (cursor.moveToFirst()) {
               do {
                   AssistiveBean usage = new AssistiveBean();
                   usage.setItemId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(colom_one))));
                   usage.setPackageName(cursor.getString(cursor.getColumnIndex(colom_two)));
//                   usage.setIcon(((cursor.getBlob(cursor.getColumnIndex(colom_three)))));

                   // Adding data to list
                   list.add(usage);
               } while (cursor.moveToNext());
           }
       } catch (Exception e) {
           e.printStackTrace();
       }

       db.close();
       return list;

    }
   /* public static byte[] serializeObject(Object o) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(o);
            out.close();

            // Get the bytes of the serialized object
            byte[] buf = bos.toByteArray();

            return buf;
        } catch (IOException ioe) {
            Log.e("serializeObject", "error", ioe);

            return null;
        }
    }

    public static Object deserializeObject(byte[] b) {
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
            Object object = in.readObject();
            in.close();

            return object;
        } catch (ClassNotFoundException cnfe) {
            Log.e("deserializeObject", "class not found error", cnfe);

            return null;
        } catch (IOException ioe) {
            Log.e("deserializeObject", "io error", ioe);

            return null;
        }
    }*/

    public void update_byID(int id, String v1,byte [] v2,String staus){

        SQLiteDatabase db=null;
        try{
            db = this.getWritableDatabase();
//            String sql = "UPDATE "+table_name+" WHERE "+colom_one+" = "+id;
            String sqlupdate="";
            ContentValues values = new ContentValues();
            values.put(colom_two, v1);
            values.put(colom_three, v2);
            values.put(colom_four, staus);
            db.update(table_name, values, colom_one+"="+id, null);

        }
        catch (Exception e){
System.out.println("exeption"+e);
        }


    }

    private  void updatedata(){

    }


    public void insertData(String id,String package_name,byte[] image,String satus) {
        SQLiteDatabase db = null;
        long returnValue = 0;
        long dateTime_Millis = 0;
//        synchronized (lock) {
//        SQLiteDatabase database = null;
            try {

                db = this.getWritableDatabase();

//                 database = getWritableDatabase();
                String sql = "INSERT INTO "+table_name+" VALUES (?,?,?,?)";

                SQLiteStatement statement = db.compileStatement(sql);
                statement.clearBindings();

                statement.bindString(1, id);
                statement.bindString(2, package_name);
                statement.bindBlob(3, image);
                statement.bindString(4,satus);

                statement.executeInsert();




                   /* ContentValues values = new ContentValues();
                    values.clear();
                    values.put(colom_one,beanList.getItemId());
//					values.put(COL_itemName, bean.getItemName());
//					values.put(COL_itemActionName, bean.getItemActionName());
                    values.put(colom_two, beanList.getPackageName());
					values.put(colom_three, serializeObject(beanList.getIcon()));
                    returnValue = db.insert(table_name, null, values);*/

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                db.close();
//                database.close();
            }
       // }
//        if (returnValue > 0l) {
//            return String.valueOf(dateTime_Millis);
//        } else {
//            return "";
//        }
    }

    public String getSingleDate(String apid) {
        Cursor cursor = null;
        String empName = "";
        SQLiteDatabase db = null;
        db=this.getReadableDatabase();
        try {
            cursor = db.rawQuery("SELECT "+colom_two+" FROM "+table_name+" WHERE "+colom_one+"=?", new String[] {apid + ""});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                empName = cursor.getString(cursor.getColumnIndex(colom_two));
            }
            return empName;
        }finally {
            cursor.close();
        }
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
