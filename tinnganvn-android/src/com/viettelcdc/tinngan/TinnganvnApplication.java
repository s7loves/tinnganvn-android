package com.viettelcdc.tinngan;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

import com.viettelcdc.tinngan.entity.Category;

public class TinnganvnApplication extends Application {
	private static List<Category> categories;
	
	@Override
	public void onCreate() {
		categories = new ArrayList<Category>();
		super.onCreate();
	}

	public static List<Category> getCategories() {
		return categories;
	}
}
