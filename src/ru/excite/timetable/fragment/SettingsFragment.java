package ru.excite.timetable.fragment;

import org.holoeverywhere.preference.Preference;
import org.holoeverywhere.preference.PreferenceFragment;

import ru.excite.timetable.R;
import ru.excite.timetable.TimetableActivity;
import ru.excite.timetable.data.TimetableStorage;
import android.os.Bundle;

public class SettingsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		Preference button = (Preference)findPreference("updBtn");
		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
		                @Override
		                public boolean onPreferenceClick(Preference arg0) { 
		                	TimetableStorage localStorage = ((TimetableActivity) getSupportActivity())
		            				.getGroupStorage();
		                	localStorage.updateCachedTimetables();
		                    return true;
		                }
		            });
	}
}