package ru.excite.timetable.tasks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import org.holoeverywhere.app.Activity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ru.excite.timetable.GroupUrl;
import ru.excite.timetable.TimetableData;
import android.util.Log;

public class GetTimetableTask extends
		ListenableAsyncTask<GroupUrl, Void, TimetableData[]> {
	private static final int LESSONS_QTY = 8;

	public GetTimetableTask(
			Activity activity,
			ru.excite.timetable.tasks.ListenableAsyncTask.OnCompleteListener<Void, TimetableData[]> listener) {
		super(activity, listener);
		// TODO Auto-generated constructor stub
	}

	String[] days = { "Пнд", "Втр", "Срд", "Чтв", "Птн", "Сбт" };

	protected LinkedList<String> getFromDay(String str, Document doc,
			boolean week) {
		LinkedList<String> ret = new LinkedList<String>();
		if (week) {
			for (Element a : doc.body().getElementsMatchingOwnText(str).first()
					.parents().get(4).getElementsByTag("P")) // 4 - потому что
																// текст дня
																// недели лежит
																// в теге 4ой
																// степени
																// вложенности
																// от TR в
																// котором
																// хранится
																// рассписание
																// на весь день
			{
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
				.getElementsMatchingOwnText("Время").first().parents().get(2)
				.getElementsByTag("P").listIterator(1);
		while (listIterator.hasNext()) {
			ret[i] = listIterator.next().text();
			System.out.println(ret[i]);
		}
		return ret;
	}

	@Override
	protected TimetableData[] doInBackground(GroupUrl... urls) {
		TimetableData[] result = new TimetableData[urls.length];
		int _count = 12;
		for (int groupI = 0; groupI < urls.length; groupI++) {
			ArrayList<LinkedList<String>> table = new ArrayList<LinkedList<String>>(
					_count);
			String time[] = null;
			try {
				Document doc = Jsoup.connect(urls[groupI].getUrl()).get();
				for (int i = 0; i < _count; i++) {
					if (i < 6) {
						table.add(getFromDay(days[i], doc, true));
					} else {
						table.add(getFromDay(days[i - 6], doc, false));
					}
				}
				time = getTimeList(doc);
				String name = urls[groupI].getName();
				result[groupI] = new TimetableData(table, time, name);
				Log.d("GetTimetableTask", "ended fetch for " + name);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// TODO:Group Name Findout!
		return result;
	}
}