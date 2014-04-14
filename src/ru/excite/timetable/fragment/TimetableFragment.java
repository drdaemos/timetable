package ru.excite.timetable.fragment;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;

import ru.excite.timetable.R;
import ru.excite.timetable.TimetableActivity;
import ru.excite.timetable.data.TimetableData;
import ru.excite.timetable.data.TimetableStorage;
import ru.excite.timetable.data.TtSubjectItem;
import ru.excite.timetable.listeners.StorageListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;

public class TimetableFragment extends Fragment implements StorageListener {

	int neededday;
	CardUI lessons;
	String groupName;
	TimetableStorage localStorage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("fuckcreate");
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.timetable_fragment, container,
				false);
		localStorage = ((TimetableActivity) getSupportActivity())
				.getGroupStorage();
		localStorage.addListener(this);
		groupName = getDefaultSharedPreferences().getString(
				"pref_key_groupname", "ΟΘαδ-31");
		// localStorage.loadFromServer(groupName);
		neededday = getArguments().getInt("day");
		lessons = (CardUI) view.findViewById(R.id.lessonsView);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getDataForList(lessons);
		System.out.println("fuckfragb");
	}

	private void getDataForList(CardUI cardView) {
		new CardStack();
		ArrayList<TimetableCard> data = dataToCards();
		cardView.clearCards();
		boolean showEmptyLesson = false;
		try {
			showEmptyLesson = getDefaultSharedPreferences().getBoolean(
					"pref_key_showall", false);
		} catch (Exception ex) {
			Log.d("readin preferences", "show empty lesson");
		}
		for (TimetableCard card : data) {
			if (card.getDesc().length() > 5 || showEmptyLesson) {
				cardView.addCard(card);
			}
		}
		// cardView.addCard(new TimetableCard(groupName, groupName,
		// "12:30-15:60", "1"));
		cardView.refresh();
		Log.d("getDataForList", groupName);
	}

	private ArrayList<TimetableCard> dataToCards() {
		TimetableData table = localStorage.getGroupData(groupName);
		ArrayList<TimetableCard> result = new ArrayList<TimetableCard>();
		if (table != null) {
			List<TtSubjectItem> data = table.getFromDay(neededday);
			int index = 1;
			for (TtSubjectItem item : data) {
				TimetableCard card = new TimetableCard(new String(""),
						item.subject, item.time, String.valueOf(index));
				index++;
				result.add(card);
			}
		}
		return result;
	}

	@Override
	public void onFetchComplete(TimetableStorage sender, String groupName) {
		Log.d("got", groupName);
		if (this.groupName.equals(groupName)) {
			getDataForList(lessons);
			Log.d("processing", groupName);
		}
	}

}
