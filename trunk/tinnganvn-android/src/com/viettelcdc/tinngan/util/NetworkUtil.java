package com.viettelcdc.tinngan.util;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public abstract class NetworkUtil {
	public static Object invokeSoapMethod(String namespace, String methodName,
			Map<String, Object> params, String url) throws IOException,
			XmlPullParserException {
		SoapObject request = new SoapObject(namespace, methodName);
		Set<Entry<String, Object>> entries = params.entrySet();
		
		for(Entry<String, Object> entry: entries) {
			request.addProperty(entry.getKey(), entry.getValue());
		}
		
		for(Entry<String, Object> entry: entries) {
			Log.i(entry.getKey(), request.getProperty(entry.getKey()).toString());
		}
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11); // put all required data into a soap
										// envelope
		envelope.setOutputSoapObject(request); // prepare request
		HttpTransportSE httpTransport = new HttpTransportSE(url);
		httpTransport.debug = true;
		envelope.dotNet = true;
		httpTransport.call("http://tempuri.org/" + methodName, envelope);
		Object result = envelope.getResponse();
		return result;
	}
}
