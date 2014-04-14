package com.mobioapp.artisticearth.objects;

public class Category {
	
	private String id;
	private String image;
	private String link;
	private String caption;
	private String count;

	public Category(String id,String image, String link, String caption, String count) {
		super();
		this.id = id;
		this.image = image;
		this.link = link;
		this.caption = caption;
		this.count = count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}