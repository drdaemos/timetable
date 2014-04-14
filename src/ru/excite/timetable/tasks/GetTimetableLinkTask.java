package ru.excite.timetable.tasks;

import org.holoeverywhere.app.Activity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import ru.excite.timetable.data.GroupUrl;
import android.util.Log;

public class GetTimetableLinkTask extends
		ListenableAsyncTask<GroupUrl, Void, GroupUrl[]> {
	public GetTimetableLinkTask(
			Activity activity,
			ru.excite.timetable.tasks.ListenableAsyncTask.OnCompleteListener<Void, GroupUrl[]> listener) {
		super(activity, listener);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected GroupUrl[] doInBackground(GroupUrl... groups) {
		for (GroupUrl group : groups) {
			try {
				Document doc = Jsoup.connect(
						"http://www.ulstu.ru/schedule/students/raspisan.htm")
						.get();
				group.setUrl(doc.body()
						.getElementsContainingOwnText(group.getName()).first()
						.parent().absUrl("href"));
				Log.d("GetTimetableLinkTask",
						"ended search for " + group.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return groups;
	}
}