package info.earntalktime.at.utils;

public class ATItem {

	private int itemId;
	private String itemName = "";
	private String itemActionName = "";
	private String packageName = "";
	private int status = 0;

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemActionName() {
		return itemActionName;
	}

	public void setItemActionName(String itemActionName) {
		this.itemActionName = itemActionName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ATItem [itemId=" + itemId + ", itemName=" + itemName + ", itemActionName=" + itemActionName + ", packageName=" + packageName + ", status=" + status + "]";
	}

}
