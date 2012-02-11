package com.viettelcdc.tinngan;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.viettelcdc.tinngan.entity.Article;
import com.viettelcdc.tinngan.entity.Category;
import com.viettelcdc.tinngan.widget.DragToggleViewPager;
import com.viettelcdc.tinngan.widget.adapter.ArticlePagerAdapter;

public class ArticleActivity extends Activity implements Constants {
	public static String CATEGORY_INDEX_EXTRA = "category_index";
	public static int DEFAULT_CATEGORY_INDEX = 0;
	public static String ARTICLE_LIST_EXTRA = "list";
	public static String ARTICLE_INDEX_EXTRA = "article_index";
	
	private ViewPager viewPager;
	private List<Article> articles;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_activity);
		Intent intent = getIntent();
		int categoryIndex = intent.getIntExtra(CATEGORY_INDEX_EXTRA, DEFAULT_CATEGORY_INDEX);
		
		if(categoryIndex == DEFAULT_CATEGORY_INDEX) {
			articles = (List<Article>)intent.getSerializableExtra(ARTICLE_LIST_EXTRA);
		}
		
		if(articles == null) {
			Category category = Category.getAll().get(categoryIndex);
			articles = category.getAllArticles();
		}
		
		int articleIndex = intent.getIntExtra(ARTICLE_INDEX_EXTRA, 0);
		
		viewPager = (DragToggleViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(new ArticlePagerAdapter(this, articles));
		viewPager.setCurrentItem(articleIndex);
	}
}
