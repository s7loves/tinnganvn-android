package com.viettelcdc.tinngan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapPrimitive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.pulltorefresh.library.PullToRefreshListView;
import com.viettelcdc.tinngan.entity.Article;
import com.viettelcdc.tinngan.util.NetworkUtil;
import com.viettelcdc.tinngan.util.asyntask.DownloadArticlesInfoTask;
import com.viettelcdc.tinngan.widget.adapter.ArticleListAdapter;

public class SearchActivity extends Activity implements OnRefreshListener, OnClickListener, OnItemClickListener, OnEditorActionListener{
	private List<Article> articles = new ArrayList<Article>();
	private EditText editText;
	private ImageButton searchButton;
	private BaseAdapter adapter;
	private PullToRefreshListView listView;
	private View loadingStub;
	private View searchBackground;
	private View searchNotFoundView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		editText = (EditText) findViewById(R.id.search_edittext);
		editText.setOnEditorActionListener(this);
		
		loadingStub = findViewById(R.id.loading_stub);
		loadingStub.setVisibility(View.INVISIBLE);
		
		searchNotFoundView = findViewById(R.id.search_not_found);
		
		searchBackground = findViewById(R.id.search_bg);
		
		searchButton = (ImageButton) findViewById(R.id.search_button);
		searchButton.setOnClickListener(this);
		
		adapter = new ArticleListAdapter(this, articles);
		
		listView = (PullToRefreshListView) findViewById(R.id.listview);
		
		listView.setDisableScrollingWhileRefreshing(true);
		listView.setOnRefreshListener(this);
		
		ListView innerListView = listView.getRefreshableView();
		innerListView.setAdapter(adapter);
		
		innerListView.setOnItemClickListener(this);
	}

	public void onRefresh() {
		new SearchAsynTask(adapter, listView, loadingStub, searchNotFoundView)
		.execute(new Object[] { editText.getText().toString(), articles.size() + 1, articles});
	}

	@Override
	public void onClick(View view) {
		hideIme();
		searchBackground.setVisibility(View.INVISIBLE);
		searchNotFoundView.setVisibility(View.INVISIBLE);
		loadingStub.setVisibility(View.VISIBLE);
		articles.clear();
		adapter.notifyDataSetChanged();
		new SearchAsynTask(adapter, listView, loadingStub, searchNotFoundView)
		.execute(new Object[] { editText.getText().toString(), 1, articles});
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
		Intent intent = new Intent(SearchActivity.this, ArticleActivity.class);
		intent.putExtra(ArticleActivity.ARTICLE_LIST_EXTRA, (Serializable) articles);
		intent.putExtra(ArticleActivity.ARTICLE_INDEX_EXTRA, pos);
		startActivity(intent);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            onClick(searchButton);
            return true;
        }
        return false;
	}

	public void hideIme() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}
}

class SearchAsynTask extends DownloadArticlesInfoTask implements Constants {

	private BaseAdapter adapter;
	private PullToRefreshListView listView;
	private View loadingStub;
	private View searchNotFoundView;
	boolean searchFromBegin = false;
	
	public SearchAsynTask(BaseAdapter adapter, PullToRefreshListView listView, View loadingStub, View searchNotFoundView) {
		this.loadingStub = loadingStub;
		this.adapter = adapter;
		this.listView = listView;
		this.searchNotFoundView = searchNotFoundView;
	}

	@Override
	public Object doInBackground(Object... params) {
		String text = (String) params[0];
		int start = (Integer) params[1];
		if(start == 1) searchFromBegin = true;
		List<Article> result = (List<Article>) params[2];
		return searchArticleByText(text, start, result);
	}

	public static Object searchArticleByText(String text, int start,
			List<Article> result) {
		Map<String, Object> soapParams = new HashMap<String, Object>();
		soapParams.put(USERNAME_PARAM, USERNAME);
		soapParams.put(PASSWORD_PARAM, PASSWORD);
		soapParams.put(SEARCH_TEXT_PARAM, text);
		soapParams.put(START_PARAM, start);
		soapParams.put(END_PARAM, start + LOAD_LIMIT - 1);

		try {
			SoapPrimitive response = (SoapPrimitive) NetworkUtil
					.invokeSoapMethod(NAMESPACE, DO_SEARCH, soapParams, WSDL);
			
			String json = (String) response.toString();
			Log.i("JSON", json);
			
			return parseJsonAsArticleList(json, result);
		} catch (Exception e) {
			return e;
		}
	}
	
	@Override
	protected void onPostExecute(Object result) {
		adapter.notifyDataSetChanged();
		listView.onRefreshComplete();
		loadingStub.setVisibility(View.INVISIBLE);
		try {
			int countResult = (Integer) result;
			if(countResult == 0 && searchFromBegin) {
				searchNotFoundView.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
