package com.mobioapp.artisticearth.objects;

public class Photo {
	private String id;
	private String link;
	private String caption;
	private String likes;

	public Photo(String id, String link, String caption, String likes) {
		super();
		this.id = id;
		this.link = link;
		this.caption = caption;
		this.likes = likes;
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
}
