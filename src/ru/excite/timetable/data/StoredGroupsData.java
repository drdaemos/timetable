package ru.excite.timetable.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class StoredGroupsData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<String> data;

	public StoredGroupsData() {
		data = new LinkedList<String>();
	}

	public void addGroup(String group) {
		if (!hasGroup(group)) {
			data.add(group);
		}
	}

	public List<String> getData() {
		return data;
	}

	public boolean hasGroup(String groupName) {
		return data.contains(groupName);
	}
}
