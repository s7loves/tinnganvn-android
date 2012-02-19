package com.viettelcdc.tinngan;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.pulltorefresh.library.PullToRefreshListView;
import com.viettelcdc.tinngan.entity.VideoArticle;
import com.viettelcdc.tinngan.util.Utils;
import com.viettelcdc.tinngan.util.asyntask.DownloadVideoArticleTask;
import com.viettelcdc.tinngan.widget.adapter.VideoArticleListAdapter;

public class VideoNewsActivity extends Activity implements OnRefreshListener, OnItemClickListener {
	private static List<VideoArticle> videos = new ArrayList<VideoArticle>();
	private static String currentVideoPlayer;
	
	private ListView listView;
	private BaseAdapter adapter;
	private View loadingStub;
	private WebView webView;
	private PullToRefreshListView wrapListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_news_activity);
		wrapListView = (PullToRefreshListView) findViewById(R.id.listview);
		
		wrapListView.setOnRefreshListener(this);
		loadingStub = findViewById(R.id.loading_stub);
		
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
	    webView.getSettings().setAllowFileAccess(true);
	    webView.getSettings().setPluginsEnabled(true);
		
		listView = wrapListView.getRefreshableView();
		adapter = new VideoArticleListAdapter(this, videos);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		
		if(videos.size() == 0) {
			onRefresh();
		} else {
			loadingStub.setVisibility(View.INVISIBLE);
		}
	}

	public void onRefresh() {
		new DownloadVideoArticleTask() {
			protected void onPostExecute(Object result) {
				if(result instanceof Exception) {
					((Exception) result).printStackTrace();
				}
				adapter.notifyDataSetChanged();
				wrapListView.onRefreshComplete();
				loadingStub.setVisibility(View.INVISIBLE);
			};
		}.execute(new Object[] { videos, videos.size() + 1});
	}

	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
		VideoArticle video = videos.get(pos);
		/*Intent intent = new Intent(this, VideoPlayerActivity.class);
		intent.putExtra(VideoPlayerActivity.PLAYER_EXTRA, video.player);
		startActivity(intent);
		*/
		currentVideoPlayer = video.player;
		webView.loadData(Utils.wrapByHtmlTag(currentVideoPlayer, Constants.BASE_URl), "text/html", "UTF-8");
	}
	
	private static void callHiddenMethod(Object obj, String name){
	    if( obj != null ){
	        try {
	            Method method = obj.getClass().getMethod(name);
	            method.invoke(obj);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    webView.loadUrl("about:blank");
	}

	@Override
	protected void onResume() {
	    super.onResume();
	    if(currentVideoPlayer != null) {
	    	webView.loadData(Utils.wrapByHtmlTag(currentVideoPlayer, Constants.BASE_URl), "text/html", "UTF-8");
	    }
	}
}

