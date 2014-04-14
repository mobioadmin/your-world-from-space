package com.mobioapp.yourworldfromspace.objects;

public class Photo {
	private String id;
	private String link;
	private String caption;
	private String likes;
	private String description;

	public Photo(String id, String link, String caption, String likes,String description) {
		super();
		this.id = id;
		this.link = link;
		this.caption = caption;
		this.likes = likes;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
