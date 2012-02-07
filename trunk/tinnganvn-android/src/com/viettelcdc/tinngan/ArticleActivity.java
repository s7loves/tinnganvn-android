package com.viettelcdc.tinngan;

import android.app.Activity;
import android.os.Bundle;

import com.viettelcdc.tinngan.entity.Category;
import com.viettelcdc.tinngan.widget.ArticlePagerAdapter;
import com.viettelcdc.tinngan.widget.DragToggleViewPager;

public class ArticleActivity extends Activity implements Constants {
	public static String CATEGORY_EXTRA = "category";
	
	private DragToggleViewPager viewPager;
	private Category category;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_activity);
		category = (Category) getIntent().getSerializableExtra(CATEGORY_EXTRA);
		viewPager = (DragToggleViewPager) findViewById(R.id.viewpager);
		viewPager.setDragEnabled(false);
		viewPager.setAdapter(new ArticlePagerAdapter(this, category.getAllArticles()));
	}
}
