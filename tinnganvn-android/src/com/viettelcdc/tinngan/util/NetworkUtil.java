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

public abstract class NetworkUtil {
	public static Object invokeSoapMethod(String namespace, String methodName,
			Map<String, Object> params, String url) throws IOException,
			XmlPullParserException {
		SoapObject request = new SoapObject(namespace, methodName);
		Set<Entry<String, Object>> entries = params.entrySet();
		
		for(Entry<String, Object> entry: entries) {
			request.addProperty(entry.getKey(), entry.getValue());
		}
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request); // prepare request
		HttpTransportSE httpTransport = new HttpTransportSE(url);
		//httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		httpTransport.debug = true;
		envelope.dotNet = true;
		httpTransport.call(namespace + methodName, envelope);
		Object result = envelope.getResponse();
		return result;
	}
}
