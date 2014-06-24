package org.ses.android.soap;

import java.util.concurrent.ExecutionException;

import org.ses.android.seispapp.R;
import org.ses.android.soap.database.Participant;
import org.ses.android.soap.preferences.AdminPreferencesActivity;
import org.ses.android.soap.preferences.PreferencesActivity;
import org.ses.android.soap.tasks.ParticipantLoadTask;
import org.ses.android.soap.utilities.UrlUtils;
import org.ses.android.soap.widgets.GrupoBotones;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ParticipanteBusquedaActivity extends Activity {
	private static final int ACTIVITY_VISITLIST=1;
	private static final int VISITLIST_ID = Menu.FIRST;
	private EditText edt_dni_document;
	private Button btnSearch;
	private Button btnShowVisits;
//	private ParticipantDbAdapter mDbHelper;
	private EditText edt_nombresb;
	private EditText edt_ape_patb;
	private EditText edt_ape_matb;
	private EditText edt_fechab;
	private EditText edt_sexob;
	private GrupoBotones btgNavega;
	private AsyncTask<String, String, Participant> asyncTask;
	private SharedPreferences mPreferences ;
	String nombres,doc_identidad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.participante_busqueda_layout);
		
		edt_dni_document = (EditText)findViewById(R.id.edt_dni_document);
		edt_nombresb  = (EditText)findViewById(R.id.edt_nombresb);
		edt_ape_patb  = (EditText)findViewById(R.id.edt_ape_patb);
		edt_ape_matb  = (EditText)findViewById(R.id.edt_ape_matb);
		edt_fechab  = (EditText)findViewById(R.id.edt_fechab);
		edt_sexob  = (EditText)findViewById(R.id.edt_sexob);
//
		btnSearch = (Button)findViewById(R.id.btnSearch);
		btgNavega = (GrupoBotones)findViewById(R.id.btgNavega);
		btnShowVisits = (Button)findViewById(R.id.btnShowVisits);

		
		btnSearch.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			ParticipantLoadTask tarea = new ParticipantLoadTask();
			doc_identidad = edt_dni_document.getText().toString();
			
	        if (!UrlUtils.validData(doc_identidad, "[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")){
				Toast.makeText(getBaseContext(), "Nro. de DNI invalido!!",Toast.LENGTH_SHORT).show();
			}
	        else
			{
                mPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String url = mPreferences.getString(PreferencesActivity.KEY_SERVER_URL,
                        getString(R.string.default_server_url));
	        	asyncTask=tarea.execute(doc_identidad,url);
		        Participant objParticipante;
				try {
					objParticipante = asyncTask.get();
					Log.i("doc_identidad",doc_identidad );
					if (objParticipante == null){
						Toast.makeText(getBaseContext(), "No existe partipante con ese DNI!!",Toast.LENGTH_SHORT).show();
					}else{
						edt_nombresb.setText("Nombres : "+objParticipante.Nombres);
						edt_ape_patb.setText("Apellido Pat. : "+objParticipante.ApellidoPaterno);
						edt_ape_matb.setText("Apellido Mat. : "+objParticipante.ApellidoMaterno);
						edt_fechab.setText("Fecha Nac. : "+objParticipante.FechaNacimiento);
						nombres = objParticipante.ApellidoPaterno.trim()+" "+objParticipante.ApellidoMaterno.trim()+" "+
								objParticipante.Nombres.trim();
						String msexo="Masculino";
						if (objParticipante.Sexo==2) msexo="Femenino";
						edt_sexob.setText("Sexo : " + msexo);

                    	Editor editor = mPreferences.edit();
						editor.putString("CodigoPaciente",objParticipante.CodigoPaciente);
						editor.putString("patient_name",nombres);
						editor.commit();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		});
		
        btgNavega.setOnAnteriorListener(new GrupoBotones.OnAnteriorListener() {
			
			@Override
			public void OnAnterior() {
				Intent intent=new Intent(ParticipanteBusquedaActivity.this,Menu_principal.class); 
				startActivity(intent); 			
			}
		});

        btgNavega.setOnSiguienteListener(new GrupoBotones.OnSiguienteListener() {
			
			@Override
			public void OnSiguiente() {
				Intent intent=new Intent(ParticipanteBusquedaActivity.this,ParticipanteVisitaActivity.class); 
				startActivity(intent); 		
			}
		});		
		
		btnShowVisits.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
	        if (!UrlUtils.validData(doc_identidad, "[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")){
				Toast.makeText(getBaseContext(), "Nro. de DNI invalido!!",Toast.LENGTH_SHORT).show();
			}
	        else
			{
                VISITLIST();
			}
		}
		});
		        
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, VISITLIST_ID, 0, R.string.menu_visitlist);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case VISITLIST_ID:
    	        if (!UrlUtils.validData(doc_identidad, "[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")){
    				Toast.makeText(getBaseContext(), "Nro. de DNI invalido!!",Toast.LENGTH_SHORT).show();
    			}
    	        else
    			{
                    VISITLIST();
    			}
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }
    private void VISITLIST() {
        Intent i = new Intent(this, VisitListActivity.class);
        startActivityForResult(i, ACTIVITY_VISITLIST);
    }
	
}
