package com.sas.android.bimobile.ui.help;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sas.android.bimobile.R;

public class HelpTopicListAdapter extends BaseAdapter {

	private ArrayList<HelpTopic> mTopicsList;
	private LayoutInflater mLayoutInflater;

	public HelpTopicListAdapter(Context context, ArrayList<HelpTopic> topicsList) {
		mTopicsList = topicsList;
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mTopicsList.size();
	}

	@Override
	public HelpTopic getItem(int position) {
		return mTopicsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		//		if (convertView == null) {
		convertView = mLayoutInflater.inflate(R.layout.help_listitem, null);
		holder = new ViewHolder();
		holder.title = (TextView) convertView.findViewById(R.id.help_title);
		holder.title.setText(mTopicsList.get(position).getTitle());
		convertView.setTag(holder);
		//		} else {
		//			holder = (ViewHolder) convertView.getTag();
		//		}
		return convertView;
	}

	private class ViewHolder {

		TextView title;
	}

}
