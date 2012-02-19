package com.viettelcdc.tinngan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.pulltorefresh.library.PullToRefreshListView;
import com.viettelcdc.tinngan.entity.Article;
import com.viettelcdc.tinngan.util.asyntask.DownloadMostReadTask;
import com.viettelcdc.tinngan.widget.adapter.ArticleListAdapter;

public class MostReadActivity extends Activity implements OnRefreshListener, OnItemClickListener {
	private static List<Article> articles = new ArrayList<Article>();
	private ListView listView;
	private BaseAdapter adapter;
	private View loadingStub;
	private PullToRefreshListView wrapListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_view);
		wrapListView = (PullToRefreshListView) findViewById(R.id.listview);
		
		wrapListView.setOnRefreshListener(this);
		loadingStub = findViewById(R.id.loading_stub);
		
		listView = wrapListView.getRefreshableView();
		adapter = new ArticleListAdapter(this, articles);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		if(articles.size() == 0) {
			onRefresh();
		} else {
			loadingStub.setVisibility(View.INVISIBLE);
		}
	}

	public void onRefresh() {
		new DownloadMostReadTask() {
			protected void onPostExecute(Object result) {
				adapter.notifyDataSetChanged();
				wrapListView.onRefreshComplete();
				loadingStub.setVisibility(View.INVISIBLE);
			};
		}.execute(new Object[] { articles, articles.size() + 1});
	}

	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
		Intent intent = new Intent(this, ArticleActivity.class);
		intent.putExtra(ArticleActivity.ARTICLE_LIST_EXTRA, (Serializable) articles);
		intent.putExtra(ArticleActivity.ARTICLE_INDEX_EXTRA, pos);
		startActivity(intent);
	}
}

