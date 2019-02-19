package info.earntalktime.at.assistivebean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

import info.earntalktime.at.utils.Constants;

/**
 * Created by PANDEY on 12-07-2017.
 */

public class AssistiveBean implements Serializable {


    private int itemId;


    private byte[] icon;
//    private byte icon;
    private int icon_list2;

    public String getIsclicked() {
        return isclicked;
    }

    public void setIsclicked(String isclicked) {
        this.isclicked = isclicked;
    }

    private String isclicked;





    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;
    private  String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }


    public Drawable getBackground() {
        return background;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }

    /* public int getBackground() {
            return background;
        }

        public void setBackground(int background) {
            this.background = background;
        }

        private int background;*/
   private  Drawable background;






    private  int itemId_list2;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    public Drawable getIcon() {
        return Constants.getDrawableFromByte(icon);
    }

    public void setIcon(Drawable icon) {

        this.icon = Constants.getByteFromDrawable(icon);
    }





    public int getIcon_list2() {
        return icon_list2;
    }

    public void setIcon_list2(int icon_list2) {
        this.icon_list2 = icon_list2;
    }
    public int getItemId_list2() {
        return itemId_list2;
    }

    public void setItemId_list2(int itemId_list2) {
        this.itemId_list2 = itemId_list2;
    }






}
