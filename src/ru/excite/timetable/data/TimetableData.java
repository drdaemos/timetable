package ru.excite.timetable.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class TimetableData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimetableData(ArrayList<LinkedList<TtSubjectItem>> timeTableData,
			String groupName) {
		tData = timeTableData;
		name = groupName;
	}

	public TimetableData() {
	}

	public String name;
	ArrayList<LinkedList<TtSubjectItem>> tData;
	public String eTag;

	public LinkedList<TtSubjectItem> timeTableFromDay(int day) {
		return tData.get(day);
	}

	public LinkedList<TtSubjectItem> timeTableFromDay(int day, boolean week) {
		return week ? tData.get(day) : tData.get(day + 6);
	}

	public static enum dayOfTheWeek {
		Ïíä, Âòð, Ñðä, ×òâ, Ïòí, Ñáò
	}

	public LinkedList<TtSubjectItem> getFromDay(int day) {
		return tData.get(day);
	}

	public LinkedList<TtSubjectItem> getFromDay(int day, boolean week) {
		return tData.get(day + (week ? 6 : 0));
	}

	private void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		aInputStream.defaultReadObject();
	}

	/**
	 * This is the default implementation of writeObject. Customise if
	 * necessary.
	 */
	private void writeObject(ObjectOutputStream aOutputStream)
			throws IOException {
		aOutputStream.defaultWriteObject();
	}

}
