package org.alphacloud.wefoundit.adapter.model;

public class FPNewsListItem {
	// fields
	private String title;
	private String desc;
	private String icon; // resouce id of the icon
	
	public FPNewsListItem(String title, String desc, String icon) {
		super();
		this.title = title;
		this.desc = desc;
		this.icon = icon;
	}

	public FPNewsListItem(String title, String desc) {
		super();
		this.title = title;
		this.desc = desc;
		this.icon = "";
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
}
