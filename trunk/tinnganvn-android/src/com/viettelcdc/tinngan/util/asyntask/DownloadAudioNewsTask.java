package com.viettelcdc.tinngan.util.asyntask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;

import com.viettelcdc.tinngan.entity.AudioTrack;
import com.viettelcdc.tinngan.util.NetworkUtil;

public class DownloadAudioNewsTask extends DownloadArticlesInfoTask {

	@Override
	public Object doInBackground(Object... params) {
		List<AudioTrack> tracks = (List<AudioTrack>)params[0];
		int start = (Integer)params[1];
		
		Map<String, Object> soapParams = new HashMap<String, Object>();
		soapParams.put(USERNAME_PARAM, USERNAME);
		soapParams.put(PASSWORD_PARAM, PASSWORD);
		soapParams.put(START_PARAM, start);
		soapParams.put(END_PARAM, start + LOAD_LIMIT - 1);

		try {
			SoapPrimitive response = (SoapPrimitive) NetworkUtil
					.invokeSoapMethod(NAMESPACE, GET_AUDIO_NEWS, soapParams, WSDL);

			String json = (String) response.toString();
			JSONArray jsonArray = new JSONArray(json);

			int i = 0;
			for (;i < jsonArray.length(); ++i) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				AudioTrack track = new AudioTrack();
				track.title = jsonObject.getString(MediaJsonField.TITLE);
				track.id = jsonObject.getInt(MediaJsonField.ID);
				track.streamUrl = jsonObject.getString(MediaJsonField.URL);
				track.date = jsonObject.getString(MediaJsonField.DATE);
				
				tracks.add(track);
			}

			return i;
		} catch (Exception e) {
			return e;
		}
	}
}