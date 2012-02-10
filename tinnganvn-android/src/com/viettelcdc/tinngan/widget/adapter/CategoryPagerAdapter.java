package com.viettelcdc.tinngan.widget.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.pulltorefresh.library.PullToRefreshListView;
import com.viettelcdc.tinngan.ArticleActivity;
import com.viettelcdc.tinngan.R;
import com.viettelcdc.tinngan.entity.Category;
import com.viettelcdc.tinngan.util.asyntask.DownloadArticlesInfoTask;
import com.viewpagerindicator.TitleProvider;

public class CategoryPagerAdapter extends PagerAdapter implements TitleProvider {

	private List<Category> categories;
	private Context context;
	private View[] viewCache;
	
	public CategoryPagerAdapter(Context context, List<Category> categories) {
		this.categories = categories;
		this.context = context;
		viewCache = new View[categories.size()];
	}

	@Override
	public int getCount() {
		return categories.size();
	}

	@Override
	public Object instantiateItem(View collection, int index) {
		View view = viewCache[index];
		if(view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.category_view, null);			
			viewCache[index] = view;

			Category category = categories.get(index);
			if(category.getAllArticles().size() != 0) {
				showList(index);				
			}
		}
		((ViewPager) collection).addView(view, 0);
		return view;
	}
	
	public void showList(final int index) {
		View view = getCachedViewAt(index);
		final Category category = categories.get(index);
		final BaseAdapter adapter = new ArticleListAdapter(context, category.getAllArticles());
		
		final PullToRefreshListView wrapper = (PullToRefreshListView)view.findViewById(R.id.wrapping_listview);
		
		wrapper.setDisableScrollingWhileRefreshing(true);
		wrapper.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				(new DownloadArticlesInfoTask() {
					@Override
					protected void onPostExecute(Object result) {
						adapter.notifyDataSetChanged();
						
						// Call onRefreshComplete when the list has been refreshed.
						wrapper.onRefreshComplete();
					}
				}).execute(new Object[]{category, category.getAllArticles().size() + 1});
			}
		});
		
		ListView listView = wrapper.getRefreshableView();
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
				Intent intent = new Intent(context, ArticleActivity.class);
				intent.putExtra(ArticleActivity.CATEGORY_INDEX_EXTRA, index);
				intent.putExtra(ArticleActivity.ARTICLE_INDEX_EXTRA, pos);
				context.startActivity(intent);
			}
		});
		
		View loadingStub = view.findViewById(R.id.loading_stub);
		
		listView.setAdapter(adapter);
		loadingStub.setVisibility(View.INVISIBLE);
	}
	
	public View getCachedViewAt(int index) {
		try {
			return viewCache[index];
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return null;
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

	@Override
	public String getTitle(int position) {
		return categories.get(position).name;
	}	
}
