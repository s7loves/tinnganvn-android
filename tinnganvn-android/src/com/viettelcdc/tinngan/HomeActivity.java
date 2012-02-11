package com.viettelcdc.tinngan;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class HomeActivity extends TabActivity {
	
	private TabHost tabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		setupTabHost();
	}
	
	private void setupTabHost() {
		tabHost = getTabHost();

		addTab(R.string.tab_news, R.drawable.tab_home_selector, NewsActivity.class);
		addTab(R.string.tab_audio, R.drawable.tab_about_selector, AudioNewsActivity.class);
		addTab(R.string.tab_search, R.drawable.ic_btn_search, SearchActivity.class);
	}

	private String addTab(int titleResource, int drawableId, Class<?> clazz) {
		Intent intent = new Intent(this, clazz);
		View indicator = getLayoutInflater().inflate(R.layout.tab_indicator, null);
		TextView title = (TextView) indicator.findViewById(R.id.title);
		title.setText(titleResource);
		ImageView icon = (ImageView) indicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		String tag = title.getText().toString();
		TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
		tabSpec.setIndicator(indicator);
		tabSpec.setContent(intent);
		tabHost.addTab(tabSpec);
		return tag;
	}

}
