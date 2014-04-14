package ru.excite.timetable.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.holoeverywhere.app.Activity;

import ru.excite.timetable.listeners.StorageListener;
import ru.excite.timetable.tasks.GetTimetableLinkTask;
import ru.excite.timetable.tasks.GetTimetableTask;
import ru.excite.timetable.tasks.ListenableAsyncTask.OnCompleteListener;

public class TimetableStorage {
	private static final String CACHE_LIST_NAME = "cached_list";
	Map<String, TimetableData> groupsData;
	StoredGroupsData cacheData;
	Activity owner;
	List<StorageListener> listeners;

	OnCompleteListener<Void, GroupUrl[]> getLinkListener = new OnCompleteListener<Void, GroupUrl[]>() {
		@Override
		public void onComplete(GroupUrl[] result) {
			GetTimetableTask getTable = new GetTimetableTask(owner,
					getTableListener);
			getTable.execute(result);
		}

		@Override
		public void onProgress(Void... progress) {
			// TODO Auto-generated method stub

		}
	};
	OnCompleteListener<Void, TimetableData[]> getTableListener = new OnCompleteListener<Void, TimetableData[]>() {
		@Override
		public void onComplete(TimetableData[] result) {
			for (TimetableData group : result) {
				if (group != null) {
					writeToCache(group);
					fetchComplete(group.name);
				}
			}
		}

		@Override
		public void onProgress(Void... progress) {
			// TODO Auto-generated method stub

		}
	};

	public TimetableStorage(Activity owner) {
		groupsData = new HashMap<String, TimetableData>();
		this.owner = owner;
		listeners = new LinkedList<StorageListener>();
		readCachedList();
		fillFromCache();
	}

	public void addListener(StorageListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	public void removeListener(StorageListener listener) {
		if (listener != null) {
			listeners.remove(listener);
		}
	}

	private void readCachedList() {
		File inputFile = new File(owner.getCacheDir(), CACHE_LIST_NAME);
		try {
			FileInputStream fos = new FileInputStream(inputFile);
			ObjectInputStream out = new ObjectInputStream(fos);
			cacheData = (StoredGroupsData) out.readObject();
			out.close();
			fos.close();
		} catch (FileNotFoundException e) {
			writeCachedList();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param e
	 * @return
	 */
	protected void writeCachedList() {
		File outputFile = new File(owner.getCacheDir(), CACHE_LIST_NAME);
		FileOutputStream fos;
		if (cacheData == null) {
			cacheData = new StoredGroupsData();
		}
		try {
			fos = new FileOutputStream(outputFile);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(cacheData);
			out.close();
			fos.close();
		} catch (FileNotFoundException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	private void fetchComplete(String groupName) {
		for (StorageListener listener : listeners) {
			listener.onFetchComplete(this, groupName);
		}
	}

	private void fillFromCache() {
		if (cacheData != null) {
			for (String group : cacheData.getData()) {
				readFromCache(group);
			}
		}
	}

	private void readFromCache(String groupName) {
		File inputFile = new File(owner.getCacheDir(), groupName);
		TimetableData group = null;
		try {
			FileInputStream fos = new FileInputStream(inputFile);
			ObjectInputStream out = new ObjectInputStream(fos);
			group = (TimetableData) out.readObject();
			out.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (group != null) {
			groupsData.put(groupName, group);
		}
	}
	
	/*
	 * Gives TimetableData for chosen group name or null if it is not found.
	 */
	public TimetableData getGroupData(String groupName) {
		TimetableData groupData = groupsData.get(groupName);
		if (groupData == null) {
			loadFromServer(groupName);
		}
		return groupData;
	}
	
	public void updateCachedTimetables(){
		for(TimetableData group : groupsData.values()){
			if (group != null) {
				loadFromServer(group.name, group.eTag);
			}
		}
	}

	public void loadFromServer(String groupName) {
		GetTimetableLinkTask task = new GetTimetableLinkTask(owner,
				getLinkListener);
		task.execute(new GroupUrl[] { new GroupUrl(groupName, null, null) });
	}
	
	public void loadFromServer(String groupName, String eTag) {
		GetTimetableLinkTask task = new GetTimetableLinkTask(owner,
				getLinkListener);
		task.execute(new GroupUrl[] { new GroupUrl(groupName, null, eTag) });
	}

	private void writeToCache(TimetableData group) {
		if (group != null) {
			groupsData.put(group.name, group);
			File outputFile = new File(owner.getCacheDir(), group.name);
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(outputFile);
				ObjectOutputStream out = new ObjectOutputStream(fos);
				out.writeObject(group);
				cacheData.addGroup(group.name);
				writeCachedList();
				out.close();
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
