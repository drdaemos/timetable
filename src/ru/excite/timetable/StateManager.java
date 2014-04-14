package ru.excite.timetable;

import org.holoeverywhere.app.Fragment;

import ru.excite.timetable.fragment.DayTabsFragment;
import ru.excite.timetable.fragment.SettingsFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class StateManager {
	enum ActivityState {
		Settings, Timetable
	}

	ActivityState currentState;
	TimetableActivity owner;
	boolean shownWeek;
	boolean menuVisible;

	public StateManager(TimetableActivity owner, ActivityState startingState) {
		currentState = startingState;
		shownWeek = false;
		menuVisible = true;
		this.owner = owner;
		switch (currentState) {
			case Settings:
				showSettings();
				break;
			case Timetable:
			default:
				showTimetable(shownWeek);
				break;
		}
	}

	public void showSettings() {
		if (currentState != ActivityState.Settings) {
			replaceFragment(SettingsFragment.class);
			owner.getSupportActionBar().setTitle(R.string.settings_view);
			owner.getSupportActionBar().setSubtitle("");
			owner.setMenuStatus(false);
			currentState = ActivityState.Settings;
		}
	}

	public void showTimetable(boolean secondWeek) {
		owner.getSupportFragmentManager().popBackStack(
				DayTabsFragment.class.toString(),
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
		replaceFragment(DayTabsFragment.class, prepareArgs(secondWeek));
		owner.getSupportActionBar().setTitle(
				owner.getDefaultSharedPreferences().getString(
						"pref_key_groupname", "Группа не найдена"));
		owner.getSupportActionBar().setSubtitle(
				secondWeek ? R.string.second_week : R.string.first_week);
		owner.setMenuStatus(true);
		currentState = ActivityState.Timetable;
		shownWeek = secondWeek;
	}

	public void toggleWeek() {
		showTimetable(!shownWeek);
	}

	public void exitSettings() {
		owner.getSupportFragmentManager().popBackStack();
		currentState = ActivityState.Timetable;
		owner.getSupportActionBar().setTitle(
				owner.getDefaultSharedPreferences().getString(
						"pref_key_groupname", "Группа не найдена"));
		owner.getSupportActionBar().setSubtitle(
				shownWeek ? R.string.second_week : R.string.first_week);
		owner.setMenuStatus(true);
		if (owner.getSupportFragmentManager().getBackStackEntryCount() < 1) {
			showTimetable(shownWeek);
		}
	}

	public void replaceFragment(Class<? extends Fragment> fragment) {
		Fragment fragm = Fragment.instantiate(fragment);
		replaceFragment(fragm);
	}

	public void replaceFragment(Class<? extends Fragment> fragment,
			Bundle fragmentArgs) {
		Fragment fragm = Fragment.instantiate(fragment, fragmentArgs);
		replaceFragment(fragm);
	}

	private void replaceFragment(Fragment fragm) {
		fragm.setRetainInstance(true);
		// Add the fragment to the 'fragment_container' FrameLayout
		owner.getSupportFragmentManager().beginTransaction()
				.replace(R.id.view_container, fragm)
				.addToBackStack(fragm.getClass().toString())
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();
	}

	/**
	 * @return
	 */
	private Bundle prepareArgs(boolean secondWeekToShow) {
		Bundle tabArgs = new Bundle();
		tabArgs.putBoolean("week", secondWeekToShow);
		return tabArgs;
	}

}
