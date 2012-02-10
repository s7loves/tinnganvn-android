package com.viettelcdc.tinngan.widget.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.viettelcdc.tinngan.R;
import com.viettelcdc.tinngan.entity.Article;
import com.viettelcdc.tinngan.util.ImageLoader;
import com.viettelcdc.tinngan.util.Utils;

public class ArticleListAdapter extends ArrayAdapter<Article> {
	private ImageLoader imageLoader; 

	public ArticleListAdapter(Context context, List<Article> articles) {
		super(context, 0, articles);
		imageLoader = ImageLoader.getInstance();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Article article = getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.article_list_item, null);	
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.date = (TextView) convertView.findViewById(R.id.date);
			viewHolder.lead = (TextView) convertView.findViewById(R.id.lead);
			convertView.setTag(viewHolder);
		}
		
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.title.setText(article.title);
		viewHolder.date.setText(Utils.formatDate(article.date));
		viewHolder.lead.setTag(article.lead);
		
		if(article.image == null) {
			viewHolder.imageView.setVisibility(View.GONE);
		} else {
			Log.i("URL", article.image);
			
			viewHolder.imageView.setVisibility(View.VISIBLE);
			imageLoader.displayImage(article.image, viewHolder.imageView);
		}
		
		return convertView;
	}
	
	static class ViewHolder {
		ImageView imageView;
		TextView title;
		TextView date;
		TextView lead;
	}
}