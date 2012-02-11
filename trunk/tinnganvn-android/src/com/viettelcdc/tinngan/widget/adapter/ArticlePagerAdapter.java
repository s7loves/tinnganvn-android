package com.viettelcdc.tinngan.widget.adapter;

import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.viettelcdc.tinngan.Constants;
import com.viettelcdc.tinngan.R;
import com.viettelcdc.tinngan.entity.Article;
import com.viettelcdc.tinngan.util.Utils;
import com.viettelcdc.tinngan.widget.DragToggleViewPager;

public class ArticlePagerAdapter extends PagerAdapter {

	private List<Article> articles;
	private Context context;
	
	public ArticlePagerAdapter(Context context, List<Article> articles) {
		this.articles = articles;
		this.context = context;
	}

	@Override
	public int getCount() {
		return articles.size();
	}

	@Override
	public Object instantiateItem(final View collection, int index) {
		View view = LayoutInflater.from(context).inflate(R.layout.article_view, null);
		Article article = articles.get(index);
		TextView textView = (TextView)view.findViewById(R.id.title);
		textView.setText(article.title);
		textView = (TextView)view.findViewById(R.id.date);
		textView.setText(Utils.formatDate(article.date));
		
		Button nextButton = (Button) view.findViewById(R.id.next_btn);
		Button prevButton = (Button) view.findViewById(R.id.prev_btn);
		
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((DragToggleViewPager)collection).showNext();
			}
		});
		
		prevButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((DragToggleViewPager)collection).showPrevious();
			}
		});
		
		WebView webView = (WebView)view.findViewById(R.id.content);

		webView.getSettings().setJavaScriptEnabled(true);
	    webView.getSettings().setAllowFileAccess(true);
	    webView.getSettings().setPluginsEnabled(true);

		webView.loadData(Utils.wrapByHtmlTag(article.content, Constants.BASE_URl), "text/html", "UTF-8");
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
