package com.viettelcdc.tinngan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.viettelcdc.tinngan.entity.Category;
import com.viettelcdc.tinngan.widget.DragToggleViewPager;
import com.viettelcdc.tinngan.widget.adapter.ArticlePagerAdapter;

public class ArticleActivity extends Activity implements Constants {
	public static String CATEGORY_INDEX_EXTRA = "category_index";
	public static int DEFAULT_CATEGORY_INDEX = 0;
	public static String CATEGORY_EXTRA = "category";
	public static String ARTICLE_INDEX_EXTRA = "article_index";
	
	private ViewPager viewPager;
	private Category category;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_activity);
		Intent intent = getIntent();
		int categoryIndex = intent.getIntExtra(CATEGORY_INDEX_EXTRA, DEFAULT_CATEGORY_INDEX);
		
		Log.i("CT_IDX", "" + categoryIndex);
		
		if(categoryIndex == DEFAULT_CATEGORY_INDEX) {
			category = (Category)intent.getSerializableExtra(CATEGORY_EXTRA);
		}
		
		if(category == null) {
			category = Category.getAll().get(categoryIndex);
		}
		
		int articleIndex = intent.getIntExtra(ARTICLE_INDEX_EXTRA, 0);
		
		viewPager = (DragToggleViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(new ArticlePagerAdapter(this, category.getAllArticles()));
		viewPager.setCurrentItem(articleIndex);
	}
}
