package com.viettelcdc.tinngan.widget.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.viettelcdc.tinngan.R;
import com.viettelcdc.tinngan.entity.VideoArticle;
import com.viettelcdc.tinngan.util.ImageLoader;

public class VideoArticleListAdapter extends ArrayAdapter<VideoArticle> {
	private ImageLoader imageLoader;
	
	public VideoArticleListAdapter(Context context, List<VideoArticle> articles) {
		super(context, 0, articles);
		imageLoader = ImageLoader.getInstance();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VideoArticle video = getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.video_article_item, null);	
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(viewHolder);
		}
		
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.title.setText(video.title);
		
		imageLoader.displayImage(video.image, viewHolder.imageView);
		return convertView;
	}
	
	static class ViewHolder {
		ImageView imageView;
		TextView title;
	}
}