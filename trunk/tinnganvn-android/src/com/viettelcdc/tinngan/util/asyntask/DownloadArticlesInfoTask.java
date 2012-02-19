package com.viettelcdc.tinngan.util.asyntask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.os.AsyncTask;

import com.viettelcdc.tinngan.Constants;
import com.viettelcdc.tinngan.entity.Article;
import com.viettelcdc.tinngan.entity.Category;
import com.viettelcdc.tinngan.util.NetworkUtil;
import com.viettelcdc.tinngan.util.Utils;

public class DownloadArticlesInfoTask extends AsyncTask<Object, Void, Object>
		implements Constants {

	private static DateFormat DATE_FORMAT = new SimpleDateFormat("M/d/yyyy h:m:s a");

	@Override
	public Object doInBackground(Object... params) {
		Category category = (Category)params[0];
		int start = (Integer)params[1];
		return loadArticleToCategory(category, start);
	}
	
	public static Object loadArticleToCategory(Category category, int start) {
		Map<String, Object> soapParams = new HashMap<String, Object>();
		soapParams.put(USERNAME_PARAM, USERNAME);
		soapParams.put(PASSWORD_PARAM, PASSWORD);
		soapParams.put(CID_PARAM, category.id);
		soapParams.put(START_PARAM, start);
		soapParams.put(END_PARAM, start + LOAD_LIMIT - 1);

		try {
			SoapPrimitive response = (SoapPrimitive) NetworkUtil
					.invokeSoapMethod(NAMESPACE, GET_BY_CATEGORY, soapParams, WSDL);

			String json = (String) response.toString();
			List<Article> articles = category.getAllArticles();
			return parseJsonAsArticleList(json, articles);
		} catch (Exception e) {
			return e;
		}
	}
	
	public static Object parseJsonAsArticleList(String json, List<Article> result) {
		try {
			JSONArray jsonArray = new JSONArray(json);

			int i = 0;
			for (;i < jsonArray.length(); ++i) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Article article = new Article();
				article.title = jsonObject.getString(ArticleJsonField.TITLE);
				article.id = jsonObject.getInt(ArticleJsonField.ID);
				article.pid = jsonObject.getInt(ArticleJsonField.PID);
				article.lead = Utils.fromUtf8(jsonObject.getString(ArticleJsonField.LEAD));
				
				article.content = Utils.fromUtf8(
						jsonObject.getString(ArticleJsonField.CONTENT));
				
				String date = jsonObject.getString(ArticleJsonField.DATE);
				article.date = DATE_FORMAT.parse(date);
				
				article.image = jsonObject.getString(ArticleJsonField.IMAGE);
				result.add(article);
			}

			return i;
		} catch (Exception e) {
			return e;
		}
	}
}
