package ru.excite.timetable;

import org.holoeverywhere.app.Activity;

import ru.excite.timetable.StateManager.ActivityState;
import ru.excite.timetable.data.TimetableStorage;
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
	StateManager stateManager;
	Menu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("onCreate", " ");
		setContentView(R.layout.activity_timetable);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		groupStorage = new TimetableStorage(this);

		stateManager = new StateManager(this, ActivityState.Timetable);
	}

	/**
	 * 
	 */

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		Log.d("onSaveInstanceState", " ");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("onStop", " ");
	}

	@Override
	protected void onPause() {

		super.onPause();
		Log.d("onPause", " ");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.d("onRestoreInstanceState", " ");
	}

	public TimetableStorage getGroupStorage() {
		return groupStorage;
	}

	@Override
	public void onBackPressed() {
		try {
			if (!searchView.isIconified()) {
				Log.d("superback", "hideSearch");
				MenuItemCompat.collapseActionView(searchItem);
			} else if (stateManager.currentState == ActivityState.Settings) {
				stateManager.exitSettings();
				Log.d("superback", "hideSettings");
			} else if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
				Log.d("superback", "exit");
				getSupportFragmentManager().popBackStack();
				super.onBackPressed();
			} else {
				Log.d("superback", "defaults");
				super.onBackPressed();
			}

		} catch (Exception ex) {
			Log.d("superback", "exception");
			ex.printStackTrace();
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timetable, menu);
		this.menu = menu;
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

	public void setMenuStatus(boolean visible) {
		if (menu != null) {
			menu.setGroupVisible(R.id.action_group, visible);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.action_settings:
				stateManager.showSettings();
				return true;
			case R.id.action_week:
				stateManager.toggleWeek();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
