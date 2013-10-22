package ru.excite.timetable.fragment;

import org.holoeverywhere.app.TabSwipeFragment;

import android.os.Bundle;

public class DayTabsFragment extends TabSwipeFragment {

    private static Bundle make(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("day", i);
        return bundle;
    }

    @Override
    public void onHandleTabs() {
        addTab("�����������", TimetableFragment.class, make(0));
        addTab("�������", TimetableFragment.class, make(1));
        addTab("�����", TimetableFragment.class, make(2));
        addTab("�������", TimetableFragment.class, make(3));
        addTab("�������", TimetableFragment.class, make(4));
        addTab("�������", TimetableFragment.class, make(5));
    }
}