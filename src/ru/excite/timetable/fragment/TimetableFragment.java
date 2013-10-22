package ru.excite.timetable.fragment;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.ArrayAdapter;
import org.holoeverywhere.widget.ListView;

import com.fima.cardsui.StackAdapter;
import com.fima.cardsui.objects.AbstractCard;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;

import ru.excite.timetable.R;
import ru.excite.timetable.TimetableActivity;
import ru.excite.timetable.TimetableData;
import ru.excite.timetable.TimetableStorage;
import ru.excite.timetable.TimetableCard;
import ru.excite.timetable.TtSubjectItem;
import ru.excite.timetable.listeners.StorageListener;
import android.R.integer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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
		View view = inflater.inflate(R.layout.timetable_fragment, container, false);
		localStorage = ((TimetableActivity)getSupportActivity()).getGroupStorage();
		localStorage.addListener(this);
		groupName = "ΟΘαδ-31";
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
	
	private void getDataForList(CardUI cardView){			
			CardStack stack = new CardStack();
			ArrayList<TimetableCard> data = dataToCards();
			for(TimetableCard card : data){
				cardView.addCard(card);
			}
			//cardView.addCard(new TimetableCard(groupName, groupName, "12:30-15:60", "1"));
			cardView.refresh();
			Log.d("getDataForList", this.groupName);
	}

	private ArrayList<TimetableCard> dataToCards() {
		TimetableData table = localStorage.getGroupData(groupName);
		ArrayList<TimetableCard> result = new ArrayList<TimetableCard>();
		if(table != null){
			List<TtSubjectItem> data = table.getFromDay(neededday);
			int index = 1;
			for(TtSubjectItem item : data){
				TimetableCard card = new TimetableCard(new String(""), item.subject, item.time, String.valueOf(index));
				index++;
				result.add(card);
				Log.d("converted", card.getDesc());
			}
		}
		return result;
	}

	@Override
	public void onFetchComplete(TimetableStorage sender, String groupName) {
		Log.d("got", groupName);
		if(this.groupName.equals(groupName)){
			getDataForList(lessons);
			Log.d("processing", groupName);
		}		
	}

	

}
