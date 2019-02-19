package com.wily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

public class AppDb {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "atdb";
	private SQLiteOpenHelper helper;

	private ATSettingsTable mainSettingTable;
	private ATMainListTable mainTable;
	private ATFavListTable favTable;
	private ATSettingsListTable settingsTable;

	private static AppDb instance = null;
	private AtomicInteger mOpenCounter = new AtomicInteger();
	private SQLiteDatabase mDatabase;

	public synchronized SQLiteDatabase getWritableDatabase(String tableName) {
		if (mOpenCounter.incrementAndGet() == 1 && helper != null) {
			mDatabase = helper.getWritableDatabase();
		}
		return mDatabase;
	}

	private AppDb(Context context) {
		helper = new SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
			@Override
			public void onCreate(SQLiteDatabase db) {
				mainTable.createTable(db);
				favTable.createTable(db);
				settingsTable.createTable(db);
				mainSettingTable.createTable(db);
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				mainTable.onUpgrade(db, oldVersion, newVersion);
				favTable.onUpgrade(db, oldVersion, newVersion);
				settingsTable.onUpgrade(db, oldVersion, newVersion);
				mainSettingTable.onUpgrade(db, oldVersion, newVersion);
				onCreate(db);
			}

		};
		mainTable = new ATMainListTable(this);
		favTable = new ATFavListTable(this);
		settingsTable = new ATSettingsListTable(this);
		mainSettingTable = new ATSettingsTable(this);
	}

	public static synchronized AppDb getInstance(Context context) {
		if (instance == null) {
			instance = new AppDb(context);
		}
		return instance;
	}

	public void distroyInstance() {
		instance = null;
	}

	public Object getTableObject(String tableName) {
		if (tableName.equalsIgnoreCase(ATMainListTable.TABLE_NAME)) {
			return mainTable;
		} else if (tableName.equalsIgnoreCase(ATFavListTable.TABLE_NAME)) {
			return favTable;
		} else if (tableName.equalsIgnoreCase(ATSettingsListTable.TABLE_NAME)) {
			return settingsTable;
		} else if (tableName.equalsIgnoreCase(ATSettingsTable.TABLE_NAME)) {
			return mainSettingTable;
		}
		return null;
	}

	public void closeDB() {
		if (mOpenCounter.decrementAndGet() == 0 && helper != null && mDatabase != null) {
			mDatabase.close();
		}
	}
}