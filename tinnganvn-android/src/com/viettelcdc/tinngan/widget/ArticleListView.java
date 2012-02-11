package com.viettelcdc.tinngan.widget;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.pulltorefresh.library.PullToRefreshListView;
import com.viettelcdc.tinngan.ArticleActivity;
import com.viettelcdc.tinngan.entity.Article;
import com.viettelcdc.tinngan.widget.adapter.ArticleListAdapter;


public class ArticleListView extends PullToRefreshListView implements OnRefreshListener{
	private ArticleListAdapter adapter;
	private ListView actualListView;
	private List<Article> articles;
	private AsyncTask<Object, ?, Object> asyncTask;
	private Object[] params;
	
	public ArticleListView(final Context context, AttributeSet attrs) {
		super(context, attrs);
		actualListView = getRefreshableView();
		
		setDisableScrollingWhileRefreshing(true);
		setOnRefreshListener(this);
		
		actualListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
				Intent intent = new Intent(context, ArticleActivity.class);
				intent.putExtra(ArticleActivity.ARTICLE_LIST_EXTRA, (Serializable)articles);
				intent.putExtra(ArticleActivity.ARTICLE_INDEX_EXTRA, pos);
				context.startActivity(intent);
			}
		});
	}
	
	public void setAsyncTask(AsyncTask<Object, ?, Object> asyncTask) {
		this.asyncTask = asyncTask;
	}


	public void setAsyncTaskParams(Object[] params) {
		this.params = params;
	}
	
	public ArticleListAdapter getAdapter() {
		return adapter;
	}
	
	public void setList(List<Article> articles) {
		this.articles = articles;
		adapter = new ArticleListAdapter(getContext(), articles);
		actualListView.setAdapter(adapter);
	}

	@Override
	public void onRefresh() {
		if(asyncTask != null) {
			asyncTask.execute(params);
		}
	}
}