package com.sas.android.bimobile.ui.help;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.sas.android.bimobile.R;
import com.sas.android.bimobile.ui.share.HtmlMailTemplateParser;

public class HelpDocsManager {

	private static String ALL_TOPICS = "alltopics";
	private static String TITLE = "title";
	private static String HTML = "html";
	private static String SUB_TOPICS = "subtopics";

	private HelpTopic mRootTopic;
	private ArrayList<HelpTopic> mResultList;
	private Context mContext;

	public HelpDocsManager(Context context) {
		mResultList = new ArrayList<HelpTopic>();
		mContext = context;
	}

	public void parseHelpTopics(String helpTopicsJSON) {
		try {
			JSONArray allTopics = new JSONObject(helpTopicsJSON).getJSONArray(ALL_TOPICS);
			if (mRootTopic == null)
				mRootTopic = new HelpTopic();
			mRootTopic.setTitle(mContext.getString(R.string.help_title));
			getHelpTopic(allTopics, mRootTopic);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void getHelpTopic(JSONArray topics, HelpTopic parentTopic) throws JSONException {
		for (int i = 0; i < topics.length(); i++) {
			JSONObject topic = topics.optJSONObject(i);
			HelpTopic helpTopic = new HelpTopic();
			helpTopic.setTitle(topic.optString(TITLE));
			helpTopic.setHtml(topic.optString(HTML));
			helpTopic.setFileContent(getFileContent(topic.optString(HTML)));
			helpTopic.setParentTopic(parentTopic);
			parentTopic.addSubTopic(helpTopic);
			JSONArray subTopics = topic.optJSONArray(SUB_TOPICS);
			if (subTopics != null) {
				getHelpTopic(subTopics, helpTopic);
			}
		}
	}

	public ArrayList<HelpTopic> searchHelp(String keyword) {
		if (keyword.trim().equals("")) {
			return mRootTopic.getSubTopics();
		}
		mResultList.clear();
		helpTopicContainKeyword(mRootTopic, keyword);
		mResultList.remove(mRootTopic);
		return mResultList;
	}

	public ArrayList<HelpTopic> findTopicList(String keyword, ArrayList<Integer> intArray) {
		ArrayList<HelpTopic> topicList = new ArrayList<HelpTopic>();
		//		if (keyword == null || keyword.equals(""))
		topicList = searchHelp(keyword);
		//		else
		//			topicList = mRootTopic;
		for (int i = 0; i < intArray.size(); i++) {
			topicList = topicList.get(intArray.get(i)).getSubTopics();
		}
		return topicList;
	}

	private void helpTopicContainKeyword(HelpTopic topic, String keyword) {
		if (topic.isContainKeyword(keyword))
			mResultList.add(topic);
		if (topic.getSubTopics() != null) {
			for (HelpTopic subTopic : topic.getSubTopics()) {
				helpTopicContainKeyword(subTopic, keyword);
			}
		}
	}

	public boolean is1stLevelTopic(HelpTopic topic) {
		return topic.getParentTopic().getParentTopic() == null;
	}

	public ArrayList<HelpTopic> get1stLevelTopics() {
		return mRootTopic.getSubTopics();
	}

	public ArrayList<HelpTopic> getSubTopics(HelpTopic topic) {
		return topic.getSubTopics();
	}

	public String getFileContent(String html) {
		if (!(html + "").equals("")) {
			try {
				InputStream stream = mContext.getResources().getAssets().open(
						mContext.getString(R.string.help_path) + html + ".html");
				String htmlContent = HtmlMailTemplateParser.inputStream2String(stream);
				return android.text.Html.fromHtml(htmlContent).toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public ArrayList<HelpTopic> getParentTopics(HelpTopic topic) {
		if (topic.getParentTopic().getParentTopic() != null)
			return topic.getParentTopic().getParentTopic().getSubTopics();
		else
			return mRootTopic.getSubTopics();
	}
}
