package com.viettelcdc.tinngan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.viettelcdc.tinngan.entity.Category;
import com.viettelcdc.tinngan.util.NetworkUtil;
import com.viettelcdc.tinngan.util.Utils;
import com.viettelcdc.tinngan.util.asyntask.DownloadArticlesInfoTask;

public class SplashScreenActivity extends Activity implements Constants {
	public static int DEFAULT_SLEEP_TIME = 1000;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        ImageView logo = (ImageView)findViewById(R.id.logo);

        Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
        logo.startAnimation(blinkAnimation);
        
        if(!Utils.isConnectedToInternet(this)) {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("Ứng dụng không thể hoạt động khi không kết nối Internet")
        	       .setCancelable(false)
        	       .setPositiveButton("Wireless Setting", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	        	   startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        	               finish();
        	           }
        	       })
        	       .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	                dialog.cancel();
        	                finish();
        	           }
        	       });
        	AlertDialog alertDialog = builder.create();
        	alertDialog.show();
        } else {
        
	        new AsyncTask<Void, Void, Object>() {
	        	long startTime;
	        	Category category0;
	       	
				@Override
				protected Object doInBackground(Void... params) {
					startTime  = System.currentTimeMillis();
			    	Object result = loadAllCategories();
			    	
			    	if(result instanceof Exception) {
			    		((Exception) result).printStackTrace();
			    		return result;
			    	}
			    	
			    	category0 = Category.getAll().get(0);
			
			    	result = DownloadArticlesInfoTask.loadArticleToCategory(category0, 1);
			    	
			    	if(result instanceof Exception) {
			    		((Exception) result).printStackTrace();
			    	}
			    	
					return result;
				}
				
				protected void onPostExecute(Object result) {
					if(result instanceof Exception) {
						(Toast.makeText(SplashScreenActivity.this, "Có lỗi xảy ra khi tải dữ liệu!", Toast.LENGTH_LONG)).show();
						finish();
						return;
					}
					
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
    
    static String CATEGORY_ID = "ID";
	static String CATEGORY_NAME = "Name";
    
    public static Object loadAllCategories() {
		List<Category> categories = Category.getAll();
		
		Map<String, Object> soapParams = new HashMap<String, Object>();
		soapParams.put(USERNAME_PARAM, USERNAME);
		soapParams.put(PASSWORD_PARAM, PASSWORD);

		try {
			SoapPrimitive response = (SoapPrimitive) NetworkUtil
					.invokeSoapMethod(NAMESPACE, GET_ALL_CATEGORIES, soapParams, WSDL);

			String json = (String) response.toString();
			JSONArray jsonArray = new JSONArray(json);
			
			categories.clear();
			Category.getAll().add(HOMEPAGE);
			
			for (int i = 0; i < jsonArray.length(); ++i) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				
				String name = jsonObject.getString(CATEGORY_NAME);
				int id = jsonObject.getInt(CATEGORY_ID);
				Category category = new Category(name, id);
				
				categories.add(category);
			}

			return categories;
		} catch (Exception e) {
			return e;
		}
	}
}