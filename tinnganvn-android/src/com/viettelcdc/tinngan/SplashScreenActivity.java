package com.viettelcdc.tinngan;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.viettelcdc.tinngan.entity.Category;
import com.viettelcdc.tinngan.util.asyntask.DownloadAllCategoriesInfoTask;
import com.viettelcdc.tinngan.util.asyntask.DownloadArticlesInfoTask;

public class SplashScreenActivity extends Activity {
	public static int DEFAULT_SLEEP_TIME = 1000;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new AsyncTask<Void, Void, Void>() {
        	long startTime;
        	Category category0;
       	
			@Override
			protected Void doInBackground(Void... params) {
				startTime  = System.currentTimeMillis();
		    	Object result = DownloadAllCategoriesInfoTask.loadAllCategories();
		    	
		    	if(result instanceof Exception) {
		    		((Exception) result).printStackTrace();
		    	}
		    	
		    	category0 = Category.getAll().get(0);
		
		    	result = DownloadArticlesInfoTask.loadArticleToCategory(category0, 1);
		    	
		    	if(result instanceof Exception) {
		    		((Exception) result).printStackTrace();
		    	} else {
		    		Log.i("Loaded", "" + (Integer) result + " articles");
		    	}
		    	
				return null;
			}
			
			protected void onPostExecute(Void result) {
				long time = System.currentTimeMillis() - startTime;
		    	time = DEFAULT_SLEEP_TIME - time;
		    	if(time < 0) {
		    		time = 0;
		    	}
		    	
		    	(new Handler()).postDelayed(new Runnable() {
					
					@Override
					public void run() {
						Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
				    	startActivity(intent);
				    	finish();
					}
				}, time);
		    	
			};
	    }.execute();
    }
}