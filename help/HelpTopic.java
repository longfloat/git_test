package com.sas.android.bimobile.ui.help;

import java.util.ArrayList;
import java.util.Locale;

public class HelpTopic {

	private String mTitle;
	private String mHtml;
	private String mFileContent;
	private ArrayList<HelpTopic> mSubTopics;
	private HelpTopic mParentTopic;

	public HelpTopic() {
		mSubTopics = new ArrayList<HelpTopic>();
	}

	public boolean isContainKeyword(String keyword) {
		//		uppercase;
		return (mFileContent + "").toUpperCase(Locale.getDefault()).indexOf(
				keyword.toUpperCase(Locale.getDefault())) >= 0
				|| (mTitle + "").toUpperCase(Locale.getDefault()).indexOf(
						keyword.toUpperCase(Locale.getDefault())) >= 0;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getHtml() {
		return mHtml;
	}

	public void setHtml(String mHtml) {
		this.mHtml = mHtml;
	}

	public ArrayList<HelpTopic> getSubTopics() {
		if (mSubTopics != null) {
			if (mSubTopics.size() == 0) {
				return null;
			}
		}
		return mSubTopics;
	}

	public void addSubTopic(HelpTopic subTopic) {
		this.mSubTopics.add(subTopic);
	}

	public HelpTopic getParentTopic() {
		return mParentTopic;
	}

	public void setParentTopic(HelpTopic mParentTopic) {
		this.mParentTopic = mParentTopic;
	}

	public String getFileContent() {
		return mFileContent;
	}

	public void setFileContent(String mFileContent) {
		this.mFileContent = mFileContent;
	}

}
