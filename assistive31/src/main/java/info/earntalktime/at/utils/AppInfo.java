package info.earntalktime.at.utils;

import java.util.Locale;

import android.graphics.drawable.Drawable;

public class AppInfo implements Comparable<AppInfo> {

	String name;
	String packageName;
	Drawable icon;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "AppInfo [name=" + name + ", packageName=" + packageName + ", icon=" + icon + "]";
	}

	@Override
	public int compareTo(AppInfo another) {
		return this.name.toLowerCase(Locale.getDefault()).compareTo(another.name.toLowerCase(Locale.getDefault()));
	}

}
