package ru.excite.timetable.listeners;

import ru.excite.timetable.TimetableStorage;

public interface StorageListener {
	public void onFetchComplete(TimetableStorage sender, String groupName);

}
