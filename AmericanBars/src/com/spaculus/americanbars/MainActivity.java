package com.spaculus.americanbars;

import java.util.ArrayList;

import com.spaculus.adapters.CustomLeftNavigationDrawerAdapter;
import com.spaculus.americanbars.fragments.FragmentArticles;
import com.spaculus.americanbars.fragments.FragmentBarTriviaGame;
import com.spaculus.americanbars.fragments.FragmentChangePassword;
import com.spaculus.americanbars.fragments.FragmentContactUs;
import com.spaculus.americanbars.fragments.FragmentHome;
import com.spaculus.americanbars.fragments.FragmentMyAlbums;
import com.spaculus.americanbars.fragments.FragmentMyDashboard;
import com.spaculus.americanbars.fragments.FragmentMyFavouriteBars;
import com.spaculus.americanbars.fragments.FragmentMyFavouriteBeers;
import com.spaculus.americanbars.fragments.FragmentMyFavouriteCocktails;
import com.spaculus.americanbars.fragments.FragmentMyFavouriteLiquors;
import com.spaculus.americanbars.fragments.FragmentPrivacySettings;
import com.spaculus.americanbars.fragments.FragmentSignUp;
import com.spaculus.americanbars.fragments.FragmentTermsOfUse;
import com.spaculus.beans.LeftNavigationDrawerItem;
import com.spaculus.helpers.CircularImageView;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
//import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainActivity extends BaseActivity {

	//  Left Navigation Drawer
	private DrawerLayout mDrawerLayout;
	private ListView listViewDrawer;
	private ActionBarDrawerToggle mDrawerToggle;

	//  For the Left Navigation Drawer Header
	private LinearLayout linearLayoutDrawerHeader;
	private TextView tvUsernameDrawer;
	private CircularImageView ivUserProfilePicDrawerCircle;
	private LayoutInflater inflater = null;
	private ViewGroup header = null;

	//  Navigation Drawer Title based on the menu item selection
	private CharSequence titleDrawer;

	//  Used to store the application title
	private CharSequence titleApplication;

	//  Array list - To store the navigation drawer item i.e. title, counter, visibility flag
	private ArrayList<LeftNavigationDrawerItem> listLeftDrawerMenuItems = null;

	//  Adapter
	private CustomLeftNavigationDrawerAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_main);

			//  For the Action bar title
			titleApplication = titleDrawer = getTitle();

			//  To set the action bar
			setActionBar();

			//  Mapping of all the views
			mappedAllViews();

			//  Create Navigation Drawer Menu Here
			createNavigationDrawerMenu();
			
			mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
					R.drawable.nav_drawer_menu_icon, // nav menu toggle icon
					R.string.app_name, // nav drawer open - description for accessibility
					R.string.app_name // nav drawer close - description for accessibility
			) {
				@Override
				public void onDrawerClosed(View view) {
					try {
						// getActionBar().setTitle(titleApplication);
						//tvTitleActionBar.setText(setActionBarTitle(titleApplication.toString()));
						setActionBarTitle(titleApplication.toString());
						// calling onPrepareOptionsMenu() to show action bar icons
						invalidateOptionsMenu();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onDrawerOpened(View drawerView) {
					try {
						// getActionBar().setTitle(titleDrawer);
						//tvTitleActionBar.setText(setActionBarTitle(titleDrawer.toString()));
						setActionBarTitle(titleDrawer.toString());
						// check here
						// To get the left menu navigation drawer data
						if (Utils.getInstance().isNetworkAvailable(MainActivity.this)) {
							// getLeftNavigationDrawerData();
						} else {
							Utils.getInstance().showToastNoInternetAvailable(MainActivity.this);
						}

						// Toast.makeText(MainActivity.this, "Drawer is opened.",
						// Toast.LENGTH_SHORT).show();
						// calling onPrepareOptionsMenu() to hide action bar icons
						invalidateOptionsMenu();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			mDrawerLayout.setDrawerListener(mDrawerToggle);

			if (savedInstanceState == null) {
				try {
					// on first time display view for first nav item
					//  Means user is already logged in
					if(SessionManager.getInstance(MainActivity.this).checkLogin()) {
						displayViewWithLogin(0);
					}
					else {
						displayViewWithoutLogin(0);
					}
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}

			// List view click event
			listViewDrawer.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					try {
						// TODO Auto-generated method stub

						// display view for selected nav drawer item
						// Here, -1 as we have added header into the left navigation
						// drawer.
						//displayViewWithLogin(position - 1);
						//  Means user is already logged in
						if(SessionManager.getInstance(MainActivity.this).checkLogin()) {
							displayViewWithLogin(position-1);
						}
						else {
							displayViewWithoutLogin(position);
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		// Associate searchable configuration with the SearchView
		// final SearchManager searchManager = (SearchManager)
		// getSystemService(Context.SEARCH_SERVICE);
		
		 * final SearchView searchView = (SearchView) menu.findItem(
		 * R.id.action_search).getActionView();
		 * searchView.setSearchableInfo(searchManager
		 * .getSearchableInfo(getComponentName()));
		 

		return super.onCreateOptionsMenu(menu);
	}*/

	/*
	 * for search bar
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		try {
			setIntent(intent);
			handleIntent(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handling intent data
	 */
	private void handleIntent(Intent intent) {
		try {
			if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
				// final String query = intent.getStringExtra(SearchManager.QUERY);
				// Log.e("Search text is ", query + "");

				/**
				 * Use this query to display search results like 1. Getting the data
				 * from SQLite and showing in listview 2. Making webrequest and
				 * displaying the data For now we just display the query only
				 */
				// Toast.makeText(MainActivity.this, "Query is " + query,
				// 1000).show();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return true;
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items

		/*
		 * active those code if you want to hide options menu when drawer is
		 * opened.
		 */
		// final boolean drawerOpen =
		// mDrawerLayout.isDrawerOpen(linearLayoutDrawerHeader);
		// menu.findItem(R.id.action_best).setVisible(!drawerOpen);
		// menu.findItem(R.id.action_liked).setVisible(!drawerOpen);

		// menu.findItem(R.id.action_rate).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Displaying fragment view for selected nav drawer list item (Without
	 * Login)
	 * */
	private void displayViewWithoutLogin(int position) {
		try {
			// update the main content by replacing fragments
			Fragment fragment = null;

			// Log.i("displayView,position:", "" +position);
			// Here, for both 0 and 1 need to set home page as while loading it
			// consider position as 0 but while click it consider position as 1.
			switch (position) {
			case 0:
				fragment = new FragmentHome();
				break;
			case 1:
				//fragment = new FragmentSignIn(true);
				break;
			case 2:
				fragment = new FragmentSignUp(true);
				break;
			default:
				break;
			}
			// Now, To show a fragment
			showFragment(fragment, position);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Displaying fragment view for selected nav drawer list item (With Login)
	 * */
	private void displayViewWithLogin(int position) {
		try {
			// update the main content by replacing fragments
			Fragment fragment = null;

			// Log.i("displayView,position:", "" +position);
			// Here, for both 0 and 1 need to set home page as while loading it
			// consider position as 0 but while click it consider position as 1.
			switch (position) {
			case 0:
				fragment = new FragmentHome();
				break;
			case 1:
				fragment = new FragmentMyDashboard(true);
				break;
			case 2:
				fragment = new FragmentMyFavouriteBars(true);
				break;
			case 3:
				fragment = new FragmentMyFavouriteBeers(true);
				break;
			case 4:
				fragment = new FragmentMyFavouriteCocktails(true);
				break;
			case 5:
				fragment = new FragmentMyFavouriteLiquors(true);
				break;
			case 6:
				fragment = new FragmentMyAlbums(true);
				break;
			case 7:
				fragment = new FragmentArticles(true);
				break;
			case 8:
				fragment = new FragmentBarTriviaGame(true);
				break;
			case 9:
				fragment = new FragmentPrivacySettings(true);
				break;
			case 10:
				fragment = new FragmentChangePassword(true);
				break;
			case 11:
				fragment = new FragmentTermsOfUse(true, ConfigConstants.CMSPageURLs.slugPrivacyPolicy);
				break;
			case 12:
				fragment = new FragmentContactUs(true);
				break;
			case 13:
				fragment = new FragmentTermsOfUse(true, ConfigConstants.CMSPageURLs.slugTermsOfUse);
				break;
			case 14:
				//  For the Logout functionality
				doLogOut();
				break;
			default:
				break;
			}
			// Now, To show a fragment
			showFragment(fragment, position);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showFragment(Fragment fragment, int position) {
		try {
			if (fragment != null) {
				final FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();

				// update selected item and title, then close the drawer
				listViewDrawer.setItemChecked(position, true);
				listViewDrawer.setSelection(position);
				// setTitle(arrayTitleMenuItems[position]);
				setTitle(listLeftDrawerMenuItems.get(position).getTitle());

				//mDrawerLayout.closeDrawer(linearLayoutDrawerHeader);
				mDrawerLayout.closeDrawer(listViewDrawer);
			} else {
				// error in creating fragment
				// Log.e("MainActivity", "Error in creating fragment");
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		try {
			titleApplication = title;
			// getActionBar().setTitle(titleApplication);
			//tvTitleActionBar.setText(setActionBarTitle(titleApplication.toString()));	
			setActionBarTitle(titleApplication.toString());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		try {
			super.onPostCreate(savedInstanceState);
			// Sync the toggle state after onRestoreInstanceState has occurred.
			mDrawerToggle.syncState();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		try {
			super.onConfigurationChanged(newConfig);
			// Pass any configuration change to the drawer toggle
			mDrawerToggle.onConfigurationChanged(newConfig);

			/* To change the action bar image logo */
			//ivLogoActionBar.setImageResource(R.drawable.actionbar_logo);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {
		try {
			super.onResume();
			//  Create Navigation Drawer Menu Here
			createNavigationDrawerMenu();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			// TODO Auto-generated method stub
			// Mapping of all the views (For the Drawer)
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			listViewDrawer = (ListView) findViewById(R.id.listViewDrawer);
			//linearLayoutDrawerHeader = (LinearLayout) findViewById(R.id.linearLayoutDrawerHeader);

			/*// Add Header of the Navigation drawer
			if(SessionManager.getInstance(MainActivity.this).checkLogin()) {
				LayoutInflater inflater = getLayoutInflater();
				ViewGroup header = (ViewGroup) inflater.inflate(R.layout.sliding_drawer_header, listViewDrawer, false);
				listViewDrawer.addHeaderView(header, null, false);

				// Header Views Mapping
				tvUsernameDrawer = (TextView) header	.findViewById(R.id.tvUsernameDrawer);
				ivUserProfilePicDrawerCircle = (CircularImageView) header.findViewById(R.id.ivUserProfilePicDrawerCircle);
			}*/
			
			inflater = getLayoutInflater();
			header = (ViewGroup) inflater.inflate(R.layout.sliding_drawer_header, listViewDrawer, false);
			// Header Views Mapping
			linearLayoutDrawerHeader = (LinearLayout) header	.findViewById(R.id.linearLayoutDrawerHeader);
			tvUsernameDrawer = (TextView) header	.findViewById(R.id.tvUsernameDrawer);
			ivUserProfilePicDrawerCircle = (CircularImageView) header.findViewById(R.id.ivUserProfilePicDrawerCircle);
			
			//  Navigation Drawer Header click event
			linearLayoutDrawerHeader.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						redirectMyProfileActivity(MainActivity.this);
						
						/* Now, simply close a drawer */
						mDrawerLayout.closeDrawer(listViewDrawer);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  This method is used to create the Navigation Drawer Menu
	private void createNavigationDrawerMenu() {
		try {
			// Initialize adapter and array list here.
			//listLeftDrawerMenuItems = new ArrayList<LeftNavigationDrawerItem>();
			
			
			//  Initialize adapter and array list for the Navigation Drawer here.
			listLeftDrawerMenuItems = new ArrayList<LeftNavigationDrawerItem>();
			
			/*//  First of all clear the list here
			if(listLeftDrawerMenuItems.size()>0) {
				listLeftDrawerMenuItems.clear();
			}*/
			
			// Add the data initially
			listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.HOME, R.drawable.menu_home));

			// Means user is already login
			if (SessionManager.getInstance(MainActivity.this).isLoggedIn()) {
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.MY_DASHBOARD, R.drawable.menu_my_dashboard));
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(	ConfigConstants.Constants.MY_FAV_BARS, R.drawable.menu_my_fav_bars));
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.MY_FAV_BEERS, R.drawable.menu_my_fav_beers));
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.MY_FAV_COCKTAILS, R.drawable.menu_my_fav_cocktails));
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.MY_FAV_LIQUORS, R.drawable.menu_my_fav_liquors));
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.MY_ALBUMS, R.drawable.menu_my_albums));
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.ARTICLES, R.drawable.menu_articles));
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.BAR_TRIVIA_GAME, R.drawable.menu_trivia_game));
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.PRIVACY_SETTINGS, R.drawable.menu_privacy_settings));
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.CHANGE_PASSWORD, R.drawable.menu_change_password));
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.PRIVACY_POLICY, R.drawable.menu_about_us));
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.CONTACT_US, R.drawable.menu_contact_us));
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.TERMS_OF_USE, R.drawable.menu_terms_of_use));
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.LOG_OUT, R.drawable.menu_log_out));
			}
			// Means user is not logged in
			else {
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.SIGN_IN, R.drawable.menu_sign_in));
				listLeftDrawerMenuItems.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.SIGN_UP, R.drawable.menu_sign_up));
			}

			//  To check whether need to add header or not
			// Add Header of the Navigation drawer
			if(SessionManager.getInstance(MainActivity.this).checkLogin()) {
				listViewDrawer.setAdapter(null);
				listViewDrawer.removeHeaderView(header);
				
				//  Now, here set the user's name and profile picture
				tvUsernameDrawer.setText(Utils.getInstance().setCapitalLetter(SessionManager.getInstance(MainActivity.this).getData(SessionManager.KEY_NAME)));
				setLogo(ConfigConstants.ImageUrls.profilePictureURL, SessionManager.getInstance(MainActivity.this).getData(SessionManager.KEY_PROFILE_IMAGE_NAME), ivUserProfilePicDrawerCircle, R.drawable.no_profile_picture);
				
				listViewDrawer.addHeaderView(header, null, false);
			}
			else {
				listViewDrawer.setAdapter(null);
				listViewDrawer.removeHeaderView(header);
			}

			adapter = new CustomLeftNavigationDrawerAdapter(MainActivity.this, R.layout.sliding_drawer_list_item, listLeftDrawerMenuItems);
			listViewDrawer.setAdapter(adapter);
			/*adapter.notifyDataSetChanged();*/
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// This method is used to set the action bar data here.
	@SuppressLint("InflateParams") 
	private void setActionBar() {
		try {
			getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_action_bar));

			View viewActionBar = getLayoutInflater().inflate(R.layout.custom_actionbar, null);

			// To center the TextView in the ActionBar
			ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

			// Now, mapped all the action bar views
			ivBackActionBar = (ImageView) viewActionBar.findViewById(R.id.ivBackActionBar);
			tvTitleActionBar = (TextView) viewActionBar.findViewById(R.id.tvTitleActionBar);
			ivLogoActionBar = (ImageView) viewActionBar.findViewById(R.id.ivLogoActionBar);
			ivMenuActionBar = (ImageView) viewActionBar.findViewById(R.id.ivMenuActionBar);
			ivSearchBarActionBar = (ImageView) viewActionBar.findViewById(R.id.ivSearchBarActionBar);

			getActionBar().setCustomView(viewActionBar, params);
			getActionBar().setDisplayShowCustomEnabled(true);
			getActionBar().setDisplayShowTitleEnabled(false);
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);
		} 
		catch (NotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//  This method is used for the login functionality.
	//  This method is used when user had done login from the navigation drawer views. i.e. from the direct child
	public void reloadData() {
		try {
			//  Now, here user has done login so need to change the navigation drawer content and need to call the method displayView to show the Home screen.
			createNavigationDrawerMenu();
			if(SessionManager.getInstance(MainActivity.this).checkLogin()) {
				displayViewWithLogin(0);
			}
			else {
				displayViewWithoutLogin(0);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
