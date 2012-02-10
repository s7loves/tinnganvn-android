package com.viettelcdc.tinngan;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.viettelcdc.tinngan.entity.Category;
import com.viettelcdc.tinngan.util.asyntask.DownloadArticlesInfoTask;
import com.viettelcdc.tinngan.widget.DragToggleViewPager;
import com.viettelcdc.tinngan.widget.adapter.CategoryPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

public class NewsActivity extends Activity implements OnPageChangeListener {
	private ViewPager viewPager;
	private TabPageIndicator tabIndicator;
	private CategoryPagerAdapter adapter;
	private List<Category> categories;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_activity);
		
		categories = Category.getAll();
		adapter = new CategoryPagerAdapter(this, categories);
		
		viewPager = (DragToggleViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(adapter);

		tabIndicator = (TabPageIndicator)findViewById(R.id.indicator);
		tabIndicator.setViewPager(viewPager);
		
		viewPager.setOnPageChangeListener(this);
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		tabIndicator.onPageScrollStateChanged(arg0);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		tabIndicator.onPageScrolled(arg0, arg1, arg2);
	}

	@Override
	public void onPageSelected(int pageIndex) {
		Category category = categories.get(pageIndex);
		if(category.getAllArticles().size() == 0) {
			(new LoadCategoryTask(adapter, pageIndex)).execute();
		}
		tabIndicator.onPageSelected(pageIndex);
	}
}

class LoadCategoryTask extends DownloadArticlesInfoTask {
	private CategoryPagerAdapter adapter;
	private int index;
	
	public LoadCategoryTask(CategoryPagerAdapter adapter, int index) {
		this.adapter = adapter;
		this.index = index;
	}
	
	@Override
	public Object doInBackground(Object... params) {
		return loadArticleToCategory(Category.getAll().get(index), 1);
	}
	
	@Override
	protected void onPostExecute(Object result) {
		adapter.showList(index);
	}
}