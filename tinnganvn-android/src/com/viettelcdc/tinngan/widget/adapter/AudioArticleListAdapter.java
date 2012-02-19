package com.viettelcdc.tinngan.widget.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.viettelcdc.tinngan.R;
import com.viettelcdc.tinngan.entity.AudioTrack;

public class AudioArticleListAdapter extends ArrayAdapter<AudioTrack> {
	public AudioArticleListAdapter(Context context, List<AudioTrack> articles) {
		super(context, 0, articles);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AudioTrack track = getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.audionews_item, null);	
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(viewHolder);
		}
		
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.title.setText(track.title);
		viewHolder.date.setText(Html.fromHtml(track.date));
		
		return convertView;
	}
	
	static class ViewHolder {
		ImageView imageView;
		TextView title;
		TextView date;
		TextView lead;
	}
}