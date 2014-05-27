package org.ses.android.soap;


import org.ses.android.seispapp.R;
import org.ses.android.soap.preferences.AdminPreferencesActivity;
import org.ses.android.soap.preferences.PreferencesActivity;
import org.ses.android.soap.utilities.AppStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Menu_principal extends Activity {
	private Button btnRegistrarParticipante;
	private Button btnGenerarVisita;
	private Button btnCerrarSesion;
//	private static final int PASSWORD_DIALOG = 1;
    // menu options
    private static final int MENU_PREFERENCES = Menu.FIRST;
//    private static final int MENU_ADMIN = Menu.FIRST + 1;
    @SuppressWarnings("unused")
	private SharedPreferences mAdminPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_principal);
		if (AppStatus.getInstance(this).isOnline(this)) {
		    Toast.makeText(this,R.string.online,Toast.LENGTH_SHORT).show();

		} else {

		    Toast.makeText(this,R.string.no_connection,Toast.LENGTH_SHORT).show();
		}
		btnRegistrarParticipante = (Button)findViewById(R.id.btnRegistrarParticipante);
		btnGenerarVisita = (Button)findViewById(R.id.btnGenerarVisita);
		btnCerrarSesion = (Button)findViewById(R.id.btnCerrarSesion);
        mAdminPreferences = this.getSharedPreferences(AdminPreferencesActivity.ADMIN_PREFERENCES, 0);
        
        btnRegistrarParticipante.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {		
				Intent intent=new Intent(Menu_principal.this,ParticipanteDNIActivity.class); 
                startActivity(intent);
			}
		});
        
        btnGenerarVisita.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {		
//				Toast.makeText(getBaseContext(), "Opcion deshabilitada!!",Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(Menu_principal.this,ParticipanteBusquedaActivity.class); 
                startActivity(intent);
			}
		});
        
        btnCerrarSesion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

	        	AlertDialog.Builder builder = new AlertDialog.Builder(Menu_principal.this);
	        	builder.setMessage("¿Desea Salir?")
	        	        .setTitle("Advertencia")
	        	        .setCancelable(false)
	        	        .setPositiveButton("Si",
	        	                new DialogInterface.OnClickListener() {
	        	                    @Override
									public void onClick(DialogInterface dialog, int id) {
	        	                    	SharedPreferences sharedpreferences = getSharedPreferences
	        	                      	      (AdminPreferencesActivity.ADMIN_PREFERENCES, Context.MODE_PRIVATE);
	        	                    	Editor editor = sharedpreferences.edit();
	        	                    	editor.clear();
	        	                    	editor.commit();
//	        	                    	moveTaskToBack(true); 
//	        	                    	Menu_principal.this.finish();
	        	                        // metodo que se debe implementar
	        	                    	//envia al otro activity login
	        	                    	Intent intent = new Intent(Menu_principal.this, MainActivity.class);
	        	                        startActivity(intent);
	        	                        finish();
	        	                    }
	        	                })
	        	        .setNegativeButton("No",
	 	                new DialogInterface.OnClickListener() {
	 	                    @Override
							public void onClick(DialogInterface dialog, int id) {
	 	                        dialog.cancel();
	 	                    }
	 	                });
	        	AlertDialog alert = builder.create();
	        	alert.show(); 
			}
		});
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_PREFERENCES, 0, getString(R.string.general_preferences)).setIcon(
                android.R.drawable.ic_menu_preferences);
//        menu.add(0, MENU_ADMIN, 0, getString(R.string.admin_preferences)).setIcon(
//                R.drawable.ic_menu_login);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_PREFERENCES:

                Intent ig = new Intent(this, PreferencesActivity.class);
                startActivity(ig);
                return true;
//            case MENU_ADMIN:
//
////                String pw = mAdminPreferences.getString(AdminPreferencesActivity.KEY_ADMIN_PW, "");
////                if ("".equalsIgnoreCase(pw)) {
//                    Intent i = new Intent(this, AdminPreferencesActivity.class);
//                    startActivity(i);
////                } else {
////                    showDialog(PASSWORD_DIALOG);
////
////                }
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}