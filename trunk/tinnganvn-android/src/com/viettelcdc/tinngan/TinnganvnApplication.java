package com.viettelcdc.tinngan;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

import com.viettelcdc.tinngan.entity.Category;
import com.viettelcdc.tinngan.util.ImageLoader;

public class TinnganvnApplication extends Application {
	private static List<Category> categories;
	
	@Override
	public void onCreate() {
		ImageLoader.makeInstance(this);
		super.onCreate();
	}
}
