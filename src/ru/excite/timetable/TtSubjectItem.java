package ru.excite.timetable;

import java.io.Serializable;

public class TtSubjectItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7717975611719364054L;
	public TtSubjectItem(String subject,String time,int day,boolean week){
		this.subject = subject; 
		this.day = day; 
		this.time = time; 
		this.week = week; 
	}
	public String subject; 
	public String time; 
	public int day; 
	public boolean week;
	@Override 
	public String toString() {
		return subject;
	}
}
