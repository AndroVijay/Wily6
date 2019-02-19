package info.earntalktime.at.db;

import java.util.ArrayList;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import info.earntalktime.at.R;
import info.earntalktime.at.utils.ATItem;
import info.earntalktime.at.utils.ATUtils;

public class ATFavListTable {

	public static final String TABLE_NAME = "mFavList";

	public static final String COL_itemId = "itemId";
	public static final String COL_itemName = "itemName";
	public static final String COL_itemActionName = "itemActionName";
	public static final String COL_packageName = "packageName";
	public static final String COL_itemStatus = "itemStatus";
	Object lock = new Object();

	private AppDb appDb;

	public ATFavListTable(AppDb appDb) {
		this.appDb = appDb;
	}

	public void createTable(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + //
				" (" + COL_itemId + " INTEGER PRIMARY KEY AUTOINCREMENT," + //
				COL_itemName + " text," + //
				COL_itemActionName + " text," + //
				COL_packageName + " text," + //
				COL_itemStatus + " INTEGER)";
		db.execSQL(CREATE_TABLE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME);
	}

	public String insertData(Vector<ATItem> beanList) {
		SQLiteDatabase db = null;
		long returnValue = 0;
		long dateTime_Millis = 0;
		synchronized (lock) {
			try {
				db = appDb.getWritableDatabase(TABLE_NAME);

				for (ATItem bean : beanList) {
					ContentValues values = new ContentValues();
					values.clear();
					values.put(COL_itemName, bean.getItemName());
					values.put(COL_itemActionName, bean.getItemActionName());
					values.put(COL_packageName, bean.getPackageName());
					values.put(COL_itemStatus, bean.getStatus());
					returnValue = db.insert(TABLE_NAME, null, values);
				}
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

	public String insertSingleData(ATItem bean) {
		SQLiteDatabase db = null;
		long returnValue = 0;
		long dateTime_Millis = 0;
		synchronized (lock) {
			try {
				db = appDb.getWritableDatabase(TABLE_NAME);
				ContentValues values = new ContentValues();
				values.clear();
				values.put(COL_itemName, bean.getItemName());
				values.put(COL_itemActionName, bean.getItemActionName());
				values.put(COL_packageName, bean.getPackageName());
				values.put(COL_itemStatus, bean.getStatus());
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

	public int updateData(Vector<ATItem> beans) {
		int rowsAffected = -1;
		SQLiteDatabase db = null;
		synchronized (lock) {
			try {
				db = appDb.getWritableDatabase(TABLE_NAME);
				for (ATItem bean : beans) {
					try {
						if (isPresent(bean.getItemId() + "")) {
							ContentValues values = new ContentValues();
							values.put(COL_itemId, bean.getItemId());
							values.put(COL_itemName, bean.getItemName());
							values.put(COL_itemActionName, bean.getItemActionName());
							values.put(COL_itemStatus, bean.getStatus());
							values.put(COL_packageName, bean.getPackageName());
							String selection = COL_itemId + "=? ";
							String[] selectionArgs = new String[] { bean.getItemId() + "" };
							rowsAffected = db.update(TABLE_NAME, values, selection, selectionArgs);
						} else {
							insertSingleData(bean);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				appDb.closeDB();
			}
		}
		return rowsAffected;
	}

	public ArrayList<ATItem> checkAndGetData(Context context) {
		if (isDataPresent()) {
			return getAllData();
		} else {
			deleteAllData();
			insertData(getSecondList(context));
			return getAllData();
		}
	}

	public ArrayList<ATItem> getAllData() {
		ArrayList<ATItem> list = new ArrayList<ATItem>();
		synchronized (lock) {
			SQLiteDatabase db = null;
			Cursor cursor = null;
			try {
				db = appDb.getWritableDatabase(TABLE_NAME);
				String[] columns = new String[] { COL_itemId, COL_itemName, COL_itemActionName, COL_itemStatus, COL_packageName };
				cursor = db.query(true, TABLE_NAME, columns, null, null, null, null, null, null);
				if (cursor.moveToFirst()) {
					do {
						ATItem bean = new ATItem();
						bean = getData(cursor);
						list.add(bean);
					} while (cursor.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null)
					cursor.close();
				appDb.closeDB();
			}
		}
		return list;
	}

	private ATItem getData(Cursor cursor) {
		int itemId = cursor.getInt(cursor.getColumnIndex(COL_itemId));
		int status = cursor.getInt(cursor.getColumnIndex(COL_itemStatus));
		String itemName = cursor.getString(cursor.getColumnIndex(COL_itemName));
		String itemActionName = cursor.getString(cursor.getColumnIndex(COL_itemActionName));
		String packageName = cursor.getString(cursor.getColumnIndex(COL_packageName));
		ATItem bean = new ATItem();
		bean.setItemId(itemId);
		bean.setItemName(itemName);
		bean.setItemActionName(itemActionName);
		bean.setPackageName(packageName);
		bean.setStatus(status);
		return bean;
	}

	// public Vector<BudgetBean> getAllBudget(Activity baseActivity, String
	// tokenId, boolean applyChecks) {
	// AppDb db = AppDb.getInstance(baseActivity);
	// BudgetTable budgetTable = (BudgetTable)
	// db.getTableObject(BudgetTable.TABLE_NAME);
	// return budgetTable.getAllBudgets(tokenId, baseActivity, applyChecks);
	// }

	public int deleteData(String messageID) {
		SQLiteDatabase db = null;
		int rowsAffected = -1;
		synchronized (lock) {
			try {
				db = appDb.getWritableDatabase(TABLE_NAME);
				String selection = COL_itemId + "=? ";
				String[] selectionArgs = new String[] { messageID };
				rowsAffected = db.delete(TABLE_NAME, selection, selectionArgs);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				appDb.closeDB();
			}
		}
		return rowsAffected;
	}

	public int deleteAllData() {
		SQLiteDatabase db = null;
		int rowsAffected = -1;
		synchronized (lock) {
			try {
				db = appDb.getWritableDatabase(TABLE_NAME);
				rowsAffected = db.delete(TABLE_NAME, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				appDb.closeDB();
			}
		}
		return rowsAffected;
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

	public boolean isPresent(String id) {
		SQLiteDatabase db = null;
		boolean isPresent = false;
		synchronized (lock) {
			try {
				String countQuery = "SELECT * FROM " + TABLE_NAME + " where " + COL_itemId + "=?;";
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

	public static Vector<ATItem> getSecondList(Context context) {
		Vector<ATItem> itemList = new Vector<ATItem>();
		ATItem item;

		item = new ATItem();
		item.setItemActionName(ATUtils.ACTION_ADD_APP);
		item.setItemName(context.getResources().getString(R.string.add_item));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ATUtils.ACTION_ADD_APP);
		item.setItemName(context.getResources().getString(R.string.add_item));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ATUtils.ACTION_ADD_APP);
		item.setItemName(context.getResources().getString(R.string.add_item));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ATUtils.ACTION_ADD_APP);
		item.setItemName(context.getResources().getString(R.string.add_item));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ATUtils.ACTION_BACK);
		item.setItemName("");
		item.setStatus(1);
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ATUtils.ACTION_ADD_APP);
		item.setItemName(context.getResources().getString(R.string.add_item));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ATUtils.ACTION_ADD_APP);
		item.setItemName(context.getResources().getString(R.string.add_item));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ATUtils.ACTION_ADD_APP);
		item.setItemName(context.getResources().getString(R.string.add_item));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ATUtils.ACTION_ADD_APP);
		item.setItemName(context.getResources().getString(R.string.add_item));
		itemList.add(item);

		return itemList;
	}

}