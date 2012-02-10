package com.viettelcdc.tinngan.util.asyntask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.os.AsyncTask;
import android.util.Log;

import com.viettelcdc.tinngan.Constants;
import com.viettelcdc.tinngan.TinnganvnApplication;
import com.viettelcdc.tinngan.entity.Category;
import com.viettelcdc.tinngan.util.NetworkUtil;

public class DownloadAllCategoriesInfoTask extends AsyncTask<Object, Void, Object>
		implements Constants {

	static interface CategoryJsonField {
		static String ID = "ID";
		static String NAME = "Name";
	}
	
	@Override
	public Object doInBackground(Object... params) {
		return loadAllCategories();
	}
	
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
			
			for (int i = 0; i < jsonArray.length(); ++i) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Category category = new Category();
				category.name = jsonObject.getString(CategoryJsonField.NAME);
				category.id = jsonObject.getInt(CategoryJsonField.ID);

				categories.add(category);
			}

			return categories;
		} catch (Exception e) {
			return e;
		}
	}
}
