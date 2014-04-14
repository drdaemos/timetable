package ru.excite.timetable.fragment;

import org.holoeverywhere.app.TabSwipeFragment;

import android.os.Bundle;

public class DayTabsFragment extends TabSwipeFragment {
	private boolean week;

	private Bundle make(int i) {
		week = (Boolean) getArguments().get("week");
		Bundle bundle = new Bundle();
		if (week) {
			bundle.putInt("day", i + 6);
		} else {
			bundle.putInt("day", i);
		}
		return bundle;
	}

	@Override
	public void onHandleTabs() {
		addTab("Понедельник", TimetableFragment.class, make(0));
		addTab("Вторник", TimetableFragment.class, make(1));
		addTab("Среда", TimetableFragment.class, make(2));
		addTab("Четверг", TimetableFragment.class, make(3));
		addTab("Пятница", TimetableFragment.class, make(4));
		addTab("Суббота", TimetableFragment.class, make(5));
	}
}