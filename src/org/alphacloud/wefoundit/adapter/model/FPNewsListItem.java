package org.alphacloud.wefoundit.adapter.model;

public class FPNewsListItem {
	// fields
	private String title;
	private String desc;
	private int icon; // resouce id of the icon
	
	public FPNewsListItem(String title, String desc, int icon) {
		super();
		this.title = title;
		this.desc = desc;
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}
	
	
}
