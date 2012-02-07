package com.viettelcdc.tinngan.widget;

import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.viettelcdc.tinngan.R;
import com.viettelcdc.tinngan.entity.Article;
import com.viettelcdc.tinngan.util.TextUtil;

public class ArticlePagerAdapter extends PagerAdapter {

	private List<Article> articles;
	private Context context;
	
	public ArticlePagerAdapter(Context context, List<Article> articles) {
		this.articles = articles;
		this.context = context;
	}

	@Override
	public int getCount() {
		Log.i("SIZE", "" + articles.size());
		return articles.size();
	}

	@Override
	public Object instantiateItem(View collection, int index) {
		View view = LayoutInflater.from(context).inflate(R.layout.article_view, null);
		Article article = articles.get(index);
		TextView textView = (TextView)view.findViewById(R.id.title);
		textView.setText(article.title);
		textView = (TextView)view.findViewById(R.id.date);
		textView.setText(TextUtil.countHours(article.date, " giờ trước"));
		WebView content = (WebView)view.findViewById(R.id.content);
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
		content.loadData(header + article.content, "text/html", "UTF-8");
		((ViewPager) collection).addView(view, 0);
		return view;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0){}
	
	@Override
	public void finishUpdate(View arg0) {}
	
	@Override
	public void destroyItem(View collection, int position, Object view) {
	     ((ViewPager) collection).removeView((View) view);
	}
}
