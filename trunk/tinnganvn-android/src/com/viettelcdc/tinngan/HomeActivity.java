package com.viettelcdc.tinngan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapPrimitive;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.viettelcdc.tinngan.entity.Article;
import com.viettelcdc.tinngan.util.NetworkUtil;

public class HomeActivity extends Activity implements Constants {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		new DownloadArticlesTask().execute(new Integer[]{1});
	}

	static class DownloadArticlesTask extends
			AsyncTask<Integer, Void, List<Article>> {

		@Override
		protected List<Article> doInBackground(Integer... params) {
			Map<String, Object> soapParams = new HashMap<String, Object>();
			soapParams.put(USERNAME_PARAM, USERNAME);
			soapParams.put(PASSWORD_PARAM, PASSWORD);
			soapParams.put(CID_PARAM, params[0]);
			soapParams.put(START_PARAM, 1);
			soapParams.put(END_PARAM, 10);
			
			try {
				SoapPrimitive response = (SoapPrimitive) NetworkUtil.invokeSoapMethod(NAMESPACE,
							GET_BY_CATEGORY, soapParams, WSDL);
				
				String json = (String) response.toString();
				
				Log.i("JSON", json);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}
}
