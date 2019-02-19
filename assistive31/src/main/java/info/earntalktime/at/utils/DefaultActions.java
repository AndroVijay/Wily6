package info.earntalktime.at.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import info.earntalktime.at.R;

public class DefaultActions {

	public static final String[] ActionsArray = new String[] { ATUtils.ACTION_HOME, ATUtils.ACTION_RECENT, ATUtils.ACTION_LOCK, ATUtils.ACTION_LOC, ATUtils.ACTION_WIFI, ATUtils.ACTION_AIRPLANE, ATUtils.ACTION_BLUETOOTH, ATUtils.ACTION_SCREEN_ROT, ATUtils.ACTION_FLASHLIGHT, ATUtils.ACTION_SOUND_MODE, ATUtils.ACTION_VOL_UP, ATUtils.ACTION_VOL_DOWN, ATUtils.ACTION_SCREENSHOT, ATUtils.ACTION_SETTING, ATUtils.ACTION_FAV, ATUtils.ACTION_MEDIA_VOL_UP, ATUtils.ACTION_MEDIA_VOL_DOWN };

	public static ArrayList<ATItem> getAllActions(Context context, int type) {
		List<ATItem> itemList = new ArrayList<ATItem>();
		ATItem item;

		item = new ATItem();
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ActionsArray[0]);
		item.setItemName(context.getResources().getString(R.string.home));
		itemList.add(item);
		
		if (type == 0) {
			item = new ATItem();
			item.setItemActionName(ActionsArray[13]);
			item.setItemName(context.getResources().getString(R.string.settings));
			itemList.add(item);
			
			item = new ATItem();
			item.setItemActionName(ActionsArray[14]);
			item.setItemName(context.getResources().getString(R.string.favourites));
			itemList.add(item);
		}

		item = new ATItem();
		item.setItemActionName(ActionsArray[1]);
		item.setItemName(context.getResources().getString(R.string.recents));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ActionsArray[2]);
		item.setItemName(context.getResources().getString(R.string.lock));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ActionsArray[3]);
		item.setItemName(context.getResources().getString(R.string.location));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ActionsArray[4]);
		item.setItemName(context.getResources().getString(R.string.wifi));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ActionsArray[5]);
		item.setItemName(context.getResources().getString(R.string.airplane));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ActionsArray[6]);
		item.setItemName(context.getResources().getString(R.string.bluetooth));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ActionsArray[7]);
		item.setItemName(context.getResources().getString(R.string.screen_rotate));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ActionsArray[8]);
		item.setItemName(context.getResources().getString(R.string.flashlight));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ActionsArray[9]);
		item.setItemName(context.getResources().getString(R.string.sound_mode));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ActionsArray[10]);
		item.setItemName(context.getResources().getString(R.string.volume_up));
		itemList.add(item);

		item = new ATItem();
		item.setItemActionName(ActionsArray[11]);
		item.setItemName(context.getResources().getString(R.string.volume_down));
		itemList.add(item);
		
		item = new ATItem();
		item.setItemActionName(ActionsArray[15]);
		item.setItemName(context.getResources().getString(R.string.m_volume_up));
		itemList.add(item);
		
		item = new ATItem();
		item.setItemActionName(ActionsArray[16]);
		item.setItemName(context.getResources().getString(R.string.m_volume_down));
		itemList.add(item);

		if (android.os.Build.VERSION.SDK_INT >= 21) {
			item = new ATItem();
			item.setItemActionName(ActionsArray[12]);
			item.setItemName(context.getResources().getString(R.string.screenshot));
			itemList.add(item);
		}

		return (ArrayList<ATItem>)itemList;
	}

}
