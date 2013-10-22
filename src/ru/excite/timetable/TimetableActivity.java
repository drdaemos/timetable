package ru.excite.timetable;

import org.holoeverywhere.app.Activity;
import ru.excite.timetable.fragment.DayTabsFragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class TimetableActivity extends Activity {


	SearchView searchView;
	MenuItem searchItem;
	TimetableStorage groupStorage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timetable);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);	

		if (savedInstanceState != null) {
			return;
		}	
		
		groupStorage = new TimetableStorage(this);
		// Create an instance of ExampleFragment
		DayTabsFragment fragm = new DayTabsFragment();

		// In case this activity was started with special instructions from an
		// Intent,
		// pass the Intent's extras to the fragment as arguments
		fragm.setArguments(getIntent().getExtras());

		// Add the fragment to the 'fragment_container' FrameLayout
		getSupportFragmentManager().beginTransaction()
				.add(R.id.view_container, fragm).commit();

	}	
	public TimetableStorage getGroupStorage() {
		return groupStorage;
	}
	@Override
	public void onBackPressed() {
		try{
		if (!searchView.isIconified()) {			
			MenuItemCompat.collapseActionView(searchItem);
		} else {
			super.onBackPressed();
		}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timetable, menu);
		searchItem = menu.findItem(R.id.action_search);
		try {
			searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
			searchView.setIconifiedByDefault(true);
			searchView.setOnQueryTextListener(new OnQueryTextListener() {
				@Override
				public boolean onQueryTextChange(String newText) {
					Log.d(getLocalClassName(), "livesearch " + newText);
					return false;
				}

				@Override
				public boolean onQueryTextSubmit(String query) {
					Log.d(getLocalClassName(), "search query:" + query);
					return false;
				}
			});
		} catch (Exception ex) {
			Log.d(getLocalClassName(), "searchView ");
			ex.printStackTrace();			
		}

		MenuItemCompat.setOnActionExpandListener(searchItem,
				new OnActionExpandListener() {
					@Override
					public boolean onMenuItemActionCollapse(MenuItem item) {
						Log.d(getLocalClassName(), "onMenuItemActionCollapse");
						return true; // Return true to collapse action view
					}

					@Override
					public boolean onMenuItemActionExpand(MenuItem item) {
						Log.d(getLocalClassName(), "onMenuItemActionExpand");
						return true; // Return true to expand action view
					}
				});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.action_update:
				System.out.println("haha");
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
