package ru.excite.timetable.tasks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import org.holoeverywhere.app.Activity;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ru.excite.timetable.data.GroupUrl;
import ru.excite.timetable.data.TimetableData;
import ru.excite.timetable.data.TtSubjectItem;
import android.util.Log;

public class GetTimetableTask extends
		ListenableAsyncTask<GroupUrl, Void, TimetableData[]> {
	public static final int NOT_MODIFIED = 304;
	private static final int LESSONS_QTY = 8;

	public GetTimetableTask(
			Activity activity,
			ru.excite.timetable.tasks.ListenableAsyncTask.OnCompleteListener<Void, TimetableData[]> listener) {
		super(activity, listener);
		// TODO Auto-generated constructor stub
	}

	String[] days = { "Ïíä", "Âòð", "Ñðä", "×òâ", "Ïòí", "Ñáò" };

	protected LinkedList<String> getFromDay(String str, Document doc,
			boolean week) {
		LinkedList<String> ret = new LinkedList<String>();
		if (week) {
			for (Element a : doc.body().getElementsMatchingOwnText(str).first()
					.parents().get(4).getElementsByTag("P")) {
				ret.add(a.text());
			}
		} else {
			for (Element a : doc.body().getElementsMatchingOwnText(str).last()
					.parents().get(4).getElementsByTag("P")) {
				ret.add(a.text());
			}
		}
		ret.removeFirst();
		return ret;
	}

	protected String[] getTimeList(Document doc) {
		String[] ret = new String[LESSONS_QTY];
		int i = 0;
		ListIterator<Element> listIterator = doc.body()
				.getElementsMatchingOwnText("Âðåìÿ").first().parents().get(2)
				.getElementsByTag("P").listIterator(1);
		while (listIterator.hasNext()) {
			ret[i] = listIterator.next().text();
			System.out.println(ret[i]);
			i++;
		}
		return ret;
	}

	@Override
	protected TimetableData[] doInBackground(GroupUrl... urls) {
		TimetableData[] result = new TimetableData[urls.length];
		int _count = 12;
		for (int groupI = 0; groupI < urls.length; groupI++) {
			ArrayList<LinkedList<TtSubjectItem>> table = new ArrayList<LinkedList<TtSubjectItem>>(
					_count);
			String time[] = null;
			try {
				Connection con = Jsoup.connect(urls[groupI].getUrl());
				if(urls[groupI].geteTag() != null){
					con.header("If-None-Match", urls[groupI].geteTag());	
				}
				Document doc = con.get();
				if(con.response().statusCode() != NOT_MODIFIED){	
					Log.d("GetTimetableTask", urls[groupI].getName() + " has been modified. Updating data.");
					time = getTimeList(doc);
					for (int i = 0; i < _count; i++) {
						if (i < 6) {
							// table.add(getFromDay(days[i], doc, true));
							LinkedList<String> subjectStrings = getFromDay(days[i],
									doc, true);
							LinkedList<TtSubjectItem> convertedLessons = new LinkedList<TtSubjectItem>();
							int lessonI = 0;
							for (String subject : subjectStrings) {
								convertedLessons.add(new TtSubjectItem(subject,
										time[lessonI], i, false));
								//Log.d("writing data to ttsubj", time[lessonI] + "|"+ lessonI);
								lessonI++;
							}
							table.add(convertedLessons);
						} else {
							// table.add(getFromDay(days[i - 6], doc, false));
							LinkedList<String> subjectStrings = getFromDay(
									days[i - 6], doc, false);
							LinkedList<TtSubjectItem> convertedLessons = new LinkedList<TtSubjectItem>();
							int lessonI = 0;
							for (String subject : subjectStrings) {
								convertedLessons.add(new TtSubjectItem(subject,
										time[lessonI], i, true));
								Log.d("writing data to ttsubj", time[lessonI] + "|"
										+ lessonI);
								lessonI++;
							}
							table.add(convertedLessons);
						}
					}
					String name = urls[groupI].getName();
					result[groupI] = new TimetableData(table, name);
					result[groupI].eTag = con.response().header("ETag");
					Log.d("GetTimetableTask", "ended fetch for " + name);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// TODO:Group Name Findout!
		return result;
	}
}