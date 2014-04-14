package ru.excite.timetable.data;

public class GroupUrl {
	private String name;
	private String eTag;
	private String url;
	
	public String geteTag() {
		return eTag;
	}

	public void seteTag(String eTag) {
		this.eTag = eTag;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public GroupUrl(String name, String url, String eTag) {
		this.eTag = eTag;
		this.name = name;
		this.url = url;
	}
}
