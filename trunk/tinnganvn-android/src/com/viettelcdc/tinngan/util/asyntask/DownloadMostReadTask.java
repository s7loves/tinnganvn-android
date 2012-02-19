package com.viettelcdc.tinngan.util.asyntask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapPrimitive;

import com.viettelcdc.tinngan.entity.Article;
import com.viettelcdc.tinngan.util.NetworkUtil;

public class DownloadMostReadTask extends DownloadArticlesInfoTask {

	@Override
	public Object doInBackground(Object... params) {
		List<Article> articles = (List<Article>)params[0];
		int start = (Integer)params[1];
		return loadLastestNews(articles, start);
	}
	
	public static Object loadLastestNews (List<Article> articles, int start) {
		Map<String, Object> soapParams = new HashMap<String, Object>();
		soapParams.put(USERNAME_PARAM, USERNAME);
		soapParams.put(PASSWORD_PARAM, PASSWORD);
		soapParams.put(CID_PARAM, 0);
		soapParams.put(START_PARAM, start);
		soapParams.put(END_PARAM, start + LOAD_LIMIT - 1);

		try {
			SoapPrimitive response = (SoapPrimitive) NetworkUtil
					.invokeSoapMethod(NAMESPACE, GET_MOST_READ, soapParams, WSDL);

			String json = (String) response.toString();
			return parseJsonAsArticleList(json, articles);
		} catch (Exception e) {
			return e;
		}
	}
}