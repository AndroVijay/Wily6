package info.earntalktime.at.db;

import java.util.Vector;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import info.earntalktime.at.utils.ATItem;
import info.earntalktime.at.utils.ATUtils;

public class ATSettingsTable {

	public static final String TABLE_NAME = "mSettings";

	public static final String COL_itemName = "itemName";
	public static final String COL_itemPos = "itemPos";
	Object lock = new Object();

	private AppDb appDb;

	public ATSettingsTable(AppDb appDb) {
		this.appDb = appDb;
	}

	public void createTable(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + //
				" (" + COL_itemName + " text PRIMARY KEY," + //
				COL_itemPos + " INTEGER)";
		db.execSQL(CREATE_TABLE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME);
	}

	public String insertSingleData(String name, int pos) {
		SQLiteDatabase db = null;
		long returnValue = 0;
		long dateTime_Millis = 0;
		synchronized (lock) {
			try {
				db = appDb.getWritableDatabase(TABLE_NAME);
				ContentValues values = new ContentValues();
				values.clear();
				values.put(COL_itemName, name);
				values.put(COL_itemPos, pos);
				returnValue = db.insert(TABLE_NAME, null, values);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				appDb.closeDB();
			}
		}
		if (returnValue > 0l) {
			return String.valueOf(dateTime_Millis);
		} else {
			return "";
		}
	}

	public int updateData(String name, int pos) {
		int rowsAffected = -1;
		SQLiteDatabase db = null;
		synchronized (lock) {
			try {
				db = appDb.getWritableDatabase(TABLE_NAME);
				try {
					if (isPresent(name)) {
						ContentValues values = new ContentValues();
						values.put(COL_itemName, name);
						values.put(COL_itemPos, pos);
						String selection = COL_itemName + "=? ";
						String[] selectionArgs = new String[] { name };
						rowsAffected = db.update(TABLE_NAME, values, selection, selectionArgs);
					} else {
						insertSingleData(name, pos);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				appDb.closeDB();
			}
		}
		return rowsAffected;
	}

	public int getAllData(String name) {
		synchronized (lock) {
			SQLiteDatabase db = null;
			Cursor cursor = null;
			try {
				db = appDb.getWritableDatabase(TABLE_NAME);
				String selection = COL_itemName + "=? ";
				String[] selectionArgs = new String[] { name };
				String[] columns = new String[] { COL_itemName, COL_itemPos };
				cursor = db.query(true, TABLE_NAME, columns, selection, selectionArgs, null, null, null, null);
				if (cursor.getCount() > 0 && cursor.moveToFirst()) {
					do {
						int itemName = cursor.getInt(cursor.getColumnIndex(COL_itemPos));
						return itemName;
					} while (cursor.moveToNext());
				} else {
					return 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null)
					cursor.close();
				appDb.closeDB();
			}
		}
		return 0;
	}

	public boolean isPresent(String id) {
		SQLiteDatabase db = null;
		boolean isPresent = false;
		synchronized (lock) {
			try {
				String countQuery = "SELECT * FROM " + TABLE_NAME + " where " + COL_itemName + "=?;";
				db = appDb.getWritableDatabase(TABLE_NAME);
				Cursor cursor = db.rawQuery(countQuery, new String[] { id });
				if (cursor.getCount() > 0) {
					isPresent = true;
				} else {
					isPresent = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				appDb.closeDB();
			}
		}
		return isPresent;
	}

	public boolean isDataPresent() {
		SQLiteDatabase db = null;
		synchronized (lock) {
			try {
				db = appDb.getWritableDatabase(TABLE_NAME);
				String query = "SELECT * FROM " + TABLE_NAME;
				Cursor cursor = db.rawQuery(query, null);
				if (cursor.getCount() > 0) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				appDb.closeDB();
			}
		}
		return false;
	}

	public static Vector<ATItem> getSecondList() {
		Vector<ATItem> itemList = new Vector<ATItem>();
		ATItem item;

		item = new ATItem();
		itemList.add(item);

		item = new ATItem();
		itemList.add(item);

		item = new ATItem();
		itemList.add(item);

		item = new ATItem();
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ATUtils.ACTION_BACK);
		item.setItemName("");
		item.setStatus(1);
		itemList.add(item);

		item = new ATItem();
		itemList.add(item);

		item = new ATItem();
		itemList.add(item);

		item = new ATItem();
		itemList.add(item);

		item = new ATItem();
		itemList.add(item);

		return itemList;
	}

}