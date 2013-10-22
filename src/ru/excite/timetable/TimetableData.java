package ru.excite.timetable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class TimetableData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TimetableData(ArrayList<LinkedList<String>> timeTableData, String time[], String groupName){
		this.tData = timeTableData; 
		this.time = time; 
		this.name = groupName; 
	}
	public TimetableData(){
	}
	String name;
	ArrayList<LinkedList<String>> tData; 
	String[] time; 
	public LinkedList<String> timeTableFromDay(int day){
		return tData.get(day); 
	}
	public LinkedList<String> timeTableFromDay(int day, boolean week){
		return week ? tData.get(day) : tData.get(day + 6); 
	}
	
	public static enum dayOfTheWeek {
		Ïíä, Âòð, Ñðä, ×òâ, Ïòí, Ñáò
	}
	
	public LinkedList<TtSubjectItem> getFromDay(int day){
		LinkedList<TtSubjectItem> ret = new LinkedList<TtSubjectItem>(); 
		int i = 0; 
		for (String a : tData.get(day)){
			ret.add(new TtSubjectItem(a, time[i], day, day < 6 ? true : false )); 
			i++; 
		}
		return ret; 
	}
	public LinkedList<TtSubjectItem> getFromDay(int day, boolean week){
		LinkedList<TtSubjectItem> ret = new LinkedList<TtSubjectItem>(); 
		int i = 0; 
		if (!week) { day += 6; }
		for (String a : tData.get(day)){
			ret.add(new TtSubjectItem(a, time[i], day, week)); 
			i++; 
		}
		return ret; 
	}
	
	private void readObject(
		     ObjectInputStream aInputStream
		   ) throws ClassNotFoundException, IOException {
		     //always perform the default de-serialization first
		     aInputStream.defaultReadObject();
		  }

		    /**
		    * This is the default implementation of writeObject.
		    * Customise if necessary.
		    */
	private void writeObject(
		      ObjectOutputStream aOutputStream
		    ) throws IOException {
		      //perform the default serialization for all non-transient, non-static fields
		      aOutputStream.defaultWriteObject();
		    }

}
