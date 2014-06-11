package org.ses.android.soap.tasks;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.AsyncTask;

public class FormListTask extends AsyncTask<String,String,String> {
	
	private String lstForms;
	 
    @Override
	protected String doInBackground(String... params) {
    	
    	String resul= "";

		String urlserver = params[1];
    	final String NAMESPACE = urlserver+"/";
		final String URL=NAMESPACE+"WSSEIS/WSParticipante.asmx";
		final String METHOD_NAME = "ListadoFormatos";
		final String SOAP_ACTION = NAMESPACE+METHOD_NAME;
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("CodigoUsuario", params[0]);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE transporte = new HttpTransportSE(URL);
		transporte.debug = true;
		try 
		{
			transporte.call(SOAP_ACTION, envelope);

			SoapObject resSoap =(SoapObject)envelope.getResponse();
			SoapObject crfs = (SoapObject)resSoap.getProperty(0);
			
			lstForms = crfs.getProperty(0).toString();
			
			if (resSoap.getPropertyCount()>0){
				resul = lstForms;
			}

		} 
		catch (Exception e) 
		{
			resul = null;
		} 
 
        return resul;
    }
    
}
