package com.viettelcdc.tinngan;

import android.app.Application;

import com.viettelcdc.tinngan.util.ImageLoader;

public class TinnganvnApplication extends Application implements Constants {	
	@Override
	public void onCreate() {
		ImageLoader.makeInstance(this);
		super.onCreate();
	}
}
