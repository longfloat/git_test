package com.sas.android.bimobile.ui.help;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;

import com.sas.android.bimobile.BIMobile;
import com.sas.android.bimobile.R;
import com.sas.android.bimobile.ui.share.HtmlMailTemplateParser;

public class HelpActivity extends Activity {

	private static String IS_WEBVIEW_SHOWN = "webView";
	private static String IS_LISTVIEW_SHOWN = "listView";
	private static String WEBVIEW_URL = "web_url";
	private static String TITLE = "title";
	private static String SEARCH_KEYWORD = "keyword";
	private static String IS_CONTENTMENU_SHOWN = "contentMenu";
	private static String ORDER_OF_TOPIC = "orderOfTopic";
	private static String HELP_TOPICS_JSON = "helpTopics.json";
	private String keyword;
	private boolean mContent_is_shown = false;
	//	private Stack<Pair<HelpTopic, String>> backStack;
	private ArrayList<Integer> orderOfTopic;

	private WebView mWebView;
	private MenuItem mContentsMenu;
	//	private TextView mNoResultHint;
	private ListView mTopicListView;
	private ArrayList<HelpTopic> mTopicList;
	private SearchView mSearchView;

	private HelpTopicListAdapter mAdapter;
	private HelpDocsManager helpDocsManager;

	private void generateTopicList() {
		mTopicList = new ArrayList<HelpTopic>();
		try {
			InputStream stream = this.getResources().getAssets().open(
					getString(R.string.help_path) + HELP_TOPICS_JSON);
			String jsonTopic = HtmlMailTemplateParser.inputStream2String(stream);
			helpDocsManager = new HelpDocsManager(this);
			helpDocsManager.parseHelpTopics(jsonTopic);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setTopicList(helpDocsManager.get1stLevelTopics());
	}

	private void initWindow() {
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
				WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		getWindow().setSoftInputMode(BIND_ADJUST_WITH_ACTIVITY);
		setFinishOnTouchOutside(false);

		orderOfTopic = new ArrayList<Integer>();

		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int width = displayMetrics.widthPixels * 3 / 4; //?
		int height = displayMetrics.heightPixels * 9 / 10;
		if (displayMetrics.heightPixels > displayMetrics.widthPixels) {
			width = displayMetrics.widthPixels * 9 / 10;
			height = displayMetrics.heightPixels * 3 / 4;
		}
		LayoutParams params = this.getWindow().getAttributes();
		params.alpha = 1.0f;
		params.dimAmount = 0.5f;
		getWindow().setAttributes(params);
		getWindow().setLayout(width, height);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initWindow();

		setContentView(R.layout.help_fragment);

		setTitle(getString(R.string.help_title));

		generateTopicList();

		setupUI(findViewById(R.id.help_fragment));

		//		mNoResultHint = (TextView) findViewById(R.id.no_results);
		mTopicListView = (ListView) findViewById(R.id.help_title_list);
		mAdapter = new HelpTopicListAdapter(this, mTopicList);
		mTopicListView.setAdapter(mAdapter);
		mTopicListView.setTextFilterEnabled(true);
		mTopicListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//				mSearchView.onActionViewCollapsed();
				mSearchView.clearFocus();

				if (mTopicList.get(arg2).getTitle().equals(
						helpDocsManager.get1stLevelTopics().get(0).getTitle())) {
					BIMobile.openSplash(HelpActivity.this);
				} else if (mTopicList.get(arg2).getSubTopics() == null) {
					String url = getString(R.string.help_url) + mTopicList.get(arg2).getHtml()
							+ ".html";
					showWebView();
					mWebView.loadUrl(url);
					mContentsMenu.setVisible(true);
					setTitle(mTopicList.get(arg2).getTitle());
				} else {
					//					if (keyword != null)
					orderOfTopic.add(arg2);
					mContentsMenu.setVisible(true);
					setTitle(mTopicList.get(arg2).getTitle());
					setTopicList(mTopicList.get(arg2).getSubTopics());
					mAdapter.notifyDataSetChanged();
				}
			}
		});

		// Load the web view content
		mWebView = (WebView) findViewById(R.id.web_view);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setBackgroundColor(Color.TRANSPARENT); // prevent flash of white

	}

	private void setTopicList(ArrayList<HelpTopic> newTopics) {
		mTopicList.clear();
		mTopicList.addAll(newTopics);
	}

	private void showTopicList() {
		mTopicListView.setVisibility(View.VISIBLE);
		mWebView.setVisibility(View.GONE);
	}

	private void showWebView() {
		mWebView.setVisibility(View.VISIBLE);
		mTopicListView.setVisibility(View.GONE);
	}

	private void showNoResult() {
		mWebView.setVisibility(View.GONE);
		mTopicListView.setVisibility(View.GONE);
	}

	private void setListTitle(HelpTopic topic) {
		if (helpDocsManager.is1stLevelTopic(mTopicList.get(0)))
			setTitle(getString(R.string.help_title));
		else
			setTitle(topic.getParentTopic().getTitle());
	}

	private void showContentMenu(HelpTopic topic) {
		if (helpDocsManager.is1stLevelTopic(topic))
			mContentsMenu.setVisible(false);
		else
			mContentsMenu.setVisible(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.help_menu, menu);
		mContentsMenu = menu.findItem(R.id.help_content);
		if (mContent_is_shown)
			mContentsMenu.setVisible(true);
		else
			mContentsMenu.setVisible(false);
		mSearchView = (SearchView) menu.findItem(R.id.help_menu_search).getActionView();
		mSearchView.setIconified(true);
		mSearchView.setSubmitButtonEnabled(false);
		mSearchView.setQueryHint(getResources().getString(R.string.search_action));
		if (keyword != null && !keyword.equals("")) {
			mSearchView.setIconified(false);
			mSearchView.setQuery(keyword, true);
		}
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				orderOfTopic.clear();
				ArrayList<HelpTopic> searchResult = helpDocsManager.searchHelp(newText);
				keyword = newText;
				if (searchResult.size() == 0) {
					showNoResult();
				} else {
					setTopicList(searchResult);
					mAdapter.notifyDataSetChanged();
					showTopicList();
				}
				return true;
			}
		});

		mSearchView.setOnSearchClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTopicList();
			}
		});

		mSearchView.setOnCloseListener(new OnCloseListener() {

			@Override
			public boolean onClose() {
				setListTitle(mTopicList.get(0));
				if (!mWebView.isShown())
					showContentMenu(mTopicList.get(0));
				return false;
			}
		});
		return true;
	}

	@Override
	@SuppressLint("CommitTransaction")
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				goBack();
				return true;
			case R.id.help_content:
				goHome();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		setTitle(savedInstanceState.getString(TITLE));
		orderOfTopic = savedInstanceState.getIntegerArrayList(ORDER_OF_TOPIC);
		keyword = savedInstanceState.getString(SEARCH_KEYWORD);
		if (savedInstanceState.getBoolean(IS_CONTENTMENU_SHOWN))
			mContent_is_shown = true;
		else
			mContent_is_shown = false;
		if (savedInstanceState.getBoolean(IS_LISTVIEW_SHOWN)) {
			if (keyword != null && orderOfTopic != null) {
				setTopicList(helpDocsManager.findTopicList(keyword, orderOfTopic));
			} else {
				setTopicList(helpDocsManager.findTopicList("", orderOfTopic));
			}
			mAdapter.notifyDataSetChanged();
			showTopicList();
		} else if (savedInstanceState.getBoolean(IS_WEBVIEW_SHOWN)) {
			mWebView.loadUrl(savedInstanceState.getString(WEBVIEW_URL));
			showWebView();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putIntegerArrayList(ORDER_OF_TOPIC, orderOfTopic);
		outState.putString(TITLE, this.getTitle().toString());
		outState.putBoolean(IS_WEBVIEW_SHOWN, mWebView.isShown());
		outState.putBoolean(IS_LISTVIEW_SHOWN, mTopicListView.isShown());
		if (mContentsMenu != null)
			outState.putBoolean(IS_CONTENTMENU_SHOWN, mContentsMenu.isVisible());
		if (keyword != null && !keyword.equals(""))
			outState.putString(SEARCH_KEYWORD, keyword);
		if (mWebView.isShown())
			outState.putString(WEBVIEW_URL, mWebView.getOriginalUrl());
	}

	@Override
	public void onBackPressed() {
		goBack();
	}

	/** Return the web view to the home page. */
	private void goHome() {
		setTitle(getString(R.string.help_title));
		mWebView.clearView();
		mContentsMenu.setVisible(false);
		mWebView.setVisibility(View.GONE);
		mTopicListView.setVisibility(View.VISIBLE);
		setTopicList(helpDocsManager.get1stLevelTopics());
		mAdapter.notifyDataSetChanged();
	}

	private void goBack() {
		if (mWebView.isShown()) {
			showTopicList();
			if (!orderOfTopic.isEmpty()) {
				if (keyword == null)
					keyword = "";
				setTopicList(helpDocsManager.findTopicList(keyword, orderOfTopic));
			} else if (keyword != null) {
				setTopicList(helpDocsManager.searchHelp(keyword));
			}
			showContentMenu(mTopicList.get(0));
			setListTitle(mTopicList.get(0));
			mAdapter.notifyDataSetChanged();
		} else {
			if (helpDocsManager.is1stLevelTopic(mTopicList.get(0))) {
				orderOfTopic.clear();
				finish();
			} else {
				showContentMenu(mTopicList.get(0).getParentTopic());
				setListTitle(mTopicList.get(0).getParentTopic());
				if (!orderOfTopic.isEmpty()) {
					orderOfTopic.remove(orderOfTopic.size() - 1);
					if (keyword == null)
						keyword = "";
					setTopicList(helpDocsManager.findTopicList(keyword, orderOfTopic));
				} else if (keyword != null) {
					//					setTopicList(helpDocsManager.searchHelp(keyword));
					finish();
				}
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			hideSoftKeyboard(HelpActivity.this);
		}
		return false;
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}

	public void setupUI(View view) {
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideSoftKeyboard(HelpActivity.this);
				return false;
			}
		});

		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				View innerView = ((ViewGroup) view).getChildAt(i);
				setupUI(innerView);
			}
		}
	}

}
