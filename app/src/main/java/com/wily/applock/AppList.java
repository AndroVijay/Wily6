package com.wily.applock;

import android.graphics.drawable.Drawable;

public class AppList {

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    private String name;

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    private String packagename;
    Drawable icon;

    public AppList() {

    }

    public String getName(){
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }
}
