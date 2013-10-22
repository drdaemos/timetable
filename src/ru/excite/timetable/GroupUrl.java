package ru.excite.timetable;

public class GroupUrl {
	private String name;
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
	private String url;
	public GroupUrl(String name, String url){
		this.name = name;
		this.url = url;
	}
}

