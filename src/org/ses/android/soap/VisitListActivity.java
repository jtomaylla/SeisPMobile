/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ses.android.soap;

import java.util.concurrent.ExecutionException;

import org.ses.android.seispapp.R;
import org.ses.android.soap.database.Visitas;
import org.ses.android.soap.preferences.PreferencesActivity;
import org.ses.android.soap.tasks.FormListTask;
import org.ses.android.soap.tasks.VisitaListTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VisitListActivity extends Activity {
	static final int DATE_DIALOG_ID = 999;
	private static final int PROGRESS_DIALOG = 1;
	private String url= "";
	String codigopaciente = "";
	String patientname = "";
	private AsyncTask<String, String, Visitas[]> loadVisitas;
	SharedPreferences mPreferences ;	
	private String mAlertMsg;
	private TextView lbl_novisits;
	private ListView lstVisit;
	private TextView lbl_nombres;
	private Visitas[] datos;


    public ProgressDialog mProgressDialog;
	private VisitaListTask mVisitaListTask;
	private AsyncTask<String, String, String> formListTask;
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//    	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
        setContentView(R.layout.visits_list);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        url = mPreferences.getString(PreferencesActivity.KEY_SERVER_URL,
                getString(R.string.default_server_url));		
		lbl_nombres = (TextView) findViewById(R.id.lbl_nombres);
    	codigopaciente = mPreferences.getString("CodigoPaciente", "");
    	patientname = mPreferences.getString("patient_name", "");
    	lbl_nombres.setText(patientname);
    	mAlertMsg = getString(R.string.please_wait);	
 
        if (getLastNonConfigurationInstance() instanceof VisitaListTask) {
            mVisitaListTask = (VisitaListTask) getLastNonConfigurationInstance();
            if (mVisitaListTask.getStatus() == AsyncTask.Status.FINISHED) {
                try {
                    dismissDialog(PROGRESS_DIALOG);
                } catch (IllegalArgumentException e) {
                    Log.i("Login", "Attempting to close a dialog that was not previously opened");
                }
                mVisitaListTask = null;
            }
        }

		showDialog(PROGRESS_DIALOG);
        loadVisitsListView();
        dismissDialog(PROGRESS_DIALOG);

    	
        lstVisit.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
            	
            	//Alternativa 1:
            	Visitas vis = (Visitas)a.getAdapter().getItem(position);
//            	String opcionSeleccionada = 
//            			((Visitas)a.getAdapter().getItem(position)).Visita;
            	//test
                // Getting the Container Layout of the ListView
                LinearLayout linearLayoutParent = (LinearLayout) v;
 
                // Getting the inner Linear Layout
                LinearLayout linearLayoutChild = (LinearLayout) linearLayoutParent.getChildAt(0);
                // Getting the Country TextView
                TextView tvCountry = (TextView) linearLayoutChild.getChildAt(1);
 
                Toast.makeText(getBaseContext(), tvCountry.getText().toString(), Toast.LENGTH_SHORT).show();
            }
              //test

            	//Alternativa 2:
            	//String opcionSeleccionada = 
            	//		((TextView)v.findViewById(R.id.LblTitulo))
            	//			.getText().toString();
            	
//            	lbl_novisits.setText("Opción seleccionada: " + opcionSeleccionada);
//				// Remote Server
//                mPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//              //testSP
//                SharedPreferences prefs = getSharedPreferences("demopref",Context.MODE_WORLD_READABLE);
//              //testSP
//                String url = mPreferences.getString(PreferencesActivity.KEY_SERVER_URL,
//                        getString(R.string.default_server_url));		        
//                String userid = mPreferences.getString(PreferencesActivity.KEY_USERID, "");
//                String local_id = mPreferences.getString(PreferencesActivity.KEY_LOCAL_ID, "");
//                String project_id = vis.CodigoProyecto;
//                String visit_group_id = vis.CodigoGrupoVisita;
//                String visit_id = vis.CodigoVisita;     
//                
//		        Editor editor = mPreferences.edit();
//		        
//				FormListTask formList=new FormListTask();
//				formListTask=formList.execute(userid,local_id,project_id,visit_group_id,visit_id,url);
////				String filterForms;
//				try {
//					String filterForms = formList.get();
//					
//					Log.i("menu", ".filterForms:"+filterForms );
//					editor.putString(PreferencesActivity.KEY_FILTERFORMS, filterForms);
//					editor.commit();
//					//testSP
//					SharedPreferences.Editor editor1 = prefs.edit();
//					editor1.clear();
//					editor1.commit();
//		            editor1.putString("stringFilterForms", filterForms);
//		            editor1.commit();
//		            //testSP
//					// Call ODK
//		        	AlertDialog.Builder builder = new AlertDialog.Builder(VisitListActivity.this);
//		        	builder.setMessage(getString(R.string.call_odk))
//		        	        .setTitle(getString(R.string.warning))
//		        	        .setCancelable(false)
//		        	        .setPositiveButton(getString(R.string.answer_yes),
//		        	                new DialogInterface.OnClickListener() {
//		        	                    @Override
//										public void onClick(DialogInterface dialog, int id) {
//		        	        				Intent i;
//		        	        				PackageManager manager = getPackageManager();
//		        	        				try {
//		        	        				    i = manager.getLaunchIntentForPackage("org.odk.collect.android");
//		        	        				    if (i == null)
//		        	        				        throw new PackageManager.NameNotFoundException();
//		        	        				    i.addCategory(Intent.CATEGORY_LAUNCHER);
////		        	        				    i.addCategory(Intent.CATEGORY_HOME);
////		        	        				    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		        	        				    startActivity(i);
//		        	        				} catch (PackageManager.NameNotFoundException e) {
//
//		        	        				}
//		        	                    }
//		        	                })
//		        	        .setNegativeButton(getString(R.string.answer_no),
//		 	                new DialogInterface.OnClickListener() {
//		 	                    @Override
//								public void onClick(DialogInterface dialog, int id) {
//		 	                        dialog.cancel();
//		 	                    }
//		 	                });
//		        	AlertDialog alert = builder.create();
//		        	alert.show(); 
//
//					// Call ODK
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (ExecutionException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//
//				// Remote Server	
//            }
            
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
	public void loadVisitsListView(){
		VisitaListTask tareaVisits = new VisitaListTask();
		
		loadVisitas = tareaVisits.execute(codigopaciente,url);

		try {

			datos = loadVisitas.get();
		     
	            
	        lbl_novisits = (TextView)findViewById(R.id.lbl_novisits);
	        lstVisit = (ListView)findViewById(R.id.lstVisit);
			if (datos != null){
		        AdaptadorVisitas adaptador = 
		            	new AdaptadorVisitas(this);
		        lstVisit.setAdapter(adaptador);
		    }else{
		        lbl_novisits.setText(R.string.no_visits);
			}

		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case PROGRESS_DIALOG:
	            mProgressDialog = new ProgressDialog(this);
	            mProgressDialog = new ProgressDialog(this);
	            mProgressDialog.setTitle(getString(R.string.downloading_data));
	            mProgressDialog.setMessage(mAlertMsg);
	            mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
	            mProgressDialog.setIndeterminate(true);
	            mProgressDialog.setCancelable(false);
	            return mProgressDialog;	        
	        }
	        return null;
    }
	
    class AdaptadorVisitas extends ArrayAdapter<Visitas> {
    	
    	Activity context;
    	
    	AdaptadorVisitas(Activity context) {
    		super(context, R.layout.visits_row, datos);
    		this.context = context;
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.visits_row, null);
			
			TextView lblTitulo = (TextView)item.findViewById(R.id.LblTitulo);
			lblTitulo.setText(datos[position].Proyecto+"/"+datos[position].Visita);
			
			TextView lblSubtitulo = (TextView)item.findViewById(R.id.LblSubTitulo);
			lblSubtitulo.setText(datos[position].FechaVisita+"-"+datos[position].HoraCita+"-"+datos[position].EstadoVisita);

//			TextView lblFiltro = (TextView)item.findViewById(R.id.LblFiltro);
//			lblFiltro.setText(datos[position].CodigoProyecto+"-"+datos[position].CodigoGrupoVisita+"-"+datos[position].CodigoVisita);			
			ImageButton imbEstado = (ImageButton) findViewById(R.id.imbEstado);
//			imbEstado.setImageResource(R.drawable.ic_list_arrow);
			
//			imbEstado.setOnClickListener(
//					new OnClickListener() {
//					      String s = "Visita";
//					    @Override
//					    public void onClick(View v) {
//					        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();          
//					    }
//					}
//			);
			
			return(item);
		}
    	
    }
   
}

