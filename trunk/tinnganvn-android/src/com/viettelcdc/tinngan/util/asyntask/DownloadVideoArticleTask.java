package com.viettelcdc.tinngan.util.asyntask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.util.Log;

import com.viettelcdc.tinngan.entity.VideoArticle;
import com.viettelcdc.tinngan.util.NetworkUtil;

public class DownloadVideoArticleTask extends DownloadArticlesInfoTask {
	private static Pattern IMG_PATTERN = Pattern.compile("https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]+(jpg|png|gif|bmp)");
	
	@Override
	public Object doInBackground(Object... params) {
		List<VideoArticle> videos = (List<VideoArticle>)params[0];
		int start = (Integer)params[1];
		
		Map<String, Object> soapParams = new HashMap<String, Object>();
		soapParams.put(USERNAME_PARAM, USERNAME);
		soapParams.put(PASSWORD_PARAM, PASSWORD);
		soapParams.put(START_PARAM, start);
		soapParams.put(END_PARAM, start + LOAD_LIMIT - 1);

		try {
			SoapPrimitive response = (SoapPrimitive) NetworkUtil
					.invokeSoapMethod(NAMESPACE, GET_VIDEO_NEWS, soapParams, WSDL);

			String json = (String) response.toString();
			JSONArray jsonArray = new JSONArray(json);

			int i = 0;
			for (;i < jsonArray.length(); ++i) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				VideoArticle video = new VideoArticle();
				video.title = jsonObject.getString(MediaJsonField.TITLE);
				video.id = jsonObject.getInt(MediaJsonField.ID);
				video.player = jsonObject.getString(MediaJsonField.URL);
				
				Matcher matcher = IMG_PATTERN.matcher(video.player);
				
				if(matcher.matches()) {
					video.image = matcher.group();
					Log.i("IMAGE", video.image);
				}
				
				videos.add(video);
			}

			return i;
		} catch (Exception e) {
			return e;
		}
	}
}