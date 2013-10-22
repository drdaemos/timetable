package ru.excite.timetable;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.TextView;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.fima.cardsui.objects.Card;

public class TimetableCard extends Card {
	String time;
	String index;
	public TimetableCard(String title, String desc, String time, String index){
		super(title);
		this.desc = desc;
		this.time = time;
		this.index = index;		
	}

	@Override
	public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_picture, null);
        ((TextView) view.findViewById(R.id.index)).setText(index);
        ((TextView) view.findViewById(R.id.description)).setText(desc);
        ((TextView) view.findViewById(R.id.time)).setText(time);
		return view;
	}

}
