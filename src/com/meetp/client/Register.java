package com.meetp.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.meetp.R;
import com.meetp.service.ICommunicationService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/** Klasa opisująca aktywność Register. Layout
 *  aktywności znajduje się w pliku register.xml.
 *  
 * 	@author Piotrek
 *
 */
public class Register extends Activity {
	
	//tag potrzebny dla LogCata
	private static final String TAG = "Register";
	
	private EditText loginREdittext;
	private EditText passwordREdittext;
	private EditText passwordRepEdittext;
	private EditText emailEdittext;
	private Spinner serverRSpinner;
	private ProgressBar balanceRProgressbar;
	private TextView balanceRTextview;
	
	/** Interfejs do serwisu */
	private ICommunicationService commService = null;
	
	/**
	 * Metoda wykonywana jest przy tworzeniu aktywności. 
	 * Inicjalizuje aktywność.
	 * 
	 * @param savedInstanceState	Zachowany wcześniej stan. Jeśli 
	 * nie zachowano stanu parametr jest null.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);
        
        loginREdittext = (EditText)findViewById(R.id.login_r_edittext);
        passwordREdittext = (EditText)findViewById(R.id.password_r_edittext);
        passwordRepEdittext = (EditText)findViewById(R.id.password_rep_edittext);
        emailEdittext = (EditText)findViewById(R.id.email_edittext);
        
        serverRSpinner = (Spinner)findViewById(R.id.server_r_spinner);
        
        balanceRProgressbar = (ProgressBar)findViewById(R.id.balance_r_progressbar);
        balanceRProgressbar.setMax(100);
        
        balanceRTextview = (TextView)findViewById(R.id.balance_r_textview);
        
        // inicjalizacja listy wyboru serwera - swiata gry
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.serwer_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serverRSpinner.setAdapter(adapter);
        
     // jesli jest zapisany stan to go przywroc
        if(savedInstanceState != null){
        	loginREdittext.setText(savedInstanceState.getString("login"));
        	passwordREdittext.setText(savedInstanceState.getString("password"));
        	passwordRepEdittext.setText(savedInstanceState.getString("passwordRep"));
        	emailEdittext.setText(savedInstanceState.getString("email"));
        	serverRSpinner.setSelection((int)savedInstanceState.getLong("server"));
        }
        
        final Button loginAvailableButton = (Button)findViewById(R.id.login_available_button);
        loginAvailableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	new CheckAvailabilityTask().execute(loginREdittext.getText().toString());;
            }
        });
        
        final Button registerRButton = (Button)findViewById(R.id.register_r_button);
        registerRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()){
                	register();
                }
            }
        });
        
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		//bindowanie z serwisem
		Log.v(TAG, "bindownaie z serwisem");
        boolean status = bindService(new Intent(ICommunicationService.class.getName()),
        		serConn, Context.BIND_AUTO_CREATE);
        if(status) Log.v(TAG, "zbindowano");
        else Log.v(TAG, "nie zbindowano");
	}

	@Override
	protected void onStop() {
		super.onStop();
		//odbindowanie od serwisu
		unbindService(serConn);
	}
	
	/** Pobiera i ustawia balans drużyn */
	public void setBalance(){
		int balance = 0;
		//TODO Przenieść komunikat do strings.xml
		ProgressDialog dialog = ProgressDialog.show(Register.this, "", 
                "Loading. Please wait...", true);
		try{
			balance = commService.getBalance();
		} catch(RemoteException e){
			//TODO Przenieść Stringa do strings.xml
			Log.e(TAG, e.getMessage(), e);
			//Toast.makeText(Register.this, "Nie można nawiązać połączenia z" +
			//		" serwerem.", Toast.LENGTH_SHORT).show();
		}
		balanceRProgressbar.setProgress(balance);
		balanceRTextview.setText(new String(balance + "% | " +
				(100 - balance) + "%"));
		dialog.dismiss();
	}

	/**
     * Pakuje wszystkie ważne informacje do Bundle stan. Metoda zachowuje aktualny stan
     * aktywności.
     * @param stan 	Boundle, do którego zachowuje stan.
     */
	@Override
	protected void onSaveInstanceState(Bundle stan) {
		super.onSaveInstanceState(stan);
		stan.putString("login", loginREdittext.getText().toString());
		stan.putString("password", passwordREdittext.getText().toString());
		stan.putString("passwordRep", passwordRepEdittext.getText().toString());
		stan.putString("email", emailEdittext.getText().toString());
		stan.putLong("server", serverRSpinner.getSelectedItemId());
	}
	
	/** Zadanie asynchroniczne - sprawdza dostępność loginu */
	private class CheckAvailabilityTask extends AsyncTask<String, Object, Integer> {
	     protected Integer doInBackground(String... args) {
	    	 boolean status = false;
	         if(loginREdittext.length() != 0){
	        	 try{
	        		 status = commService.getLoginAvailability(args[0]);
	        	 } catch(RemoteException e){
	        		 Log.e(TAG, e.getMessage(), e);
	        	 }
	        	 if(status) return 1;
	        	 else return 0;
	         } else return -1;
	     }

	     protected void onPostExecute(Integer result) {
	         switch(result){
	         case 1:
	        	 Toast.makeText(Register.this,
	        			 R.string.login_dostepny, Toast.LENGTH_SHORT).show();
	        	 break;
	         case 0:
	        	 Toast.makeText(Register.this,
	        			 R.string.login_zajety, Toast.LENGTH_SHORT).show();
	        	 break;
	         case -1:
	        	 Toast.makeText(Register.this,
	        			 R.string.wprowadz_login, Toast.LENGTH_SHORT).show();
	        	 break;
	         }
	     }
	 }
	
	//TODO Przenieść do osobnej klasy o nazwie Validator.
	private boolean validation(){
		//walidacja danych
		//jeśli chcecie to można to podzielić tak, żeby toasty wyświetlać 
		return checkEmail() && !passwordREdittext.getText().equals("")
			&& !passwordRepEdittext.getText().equals("") 
			&& passwordREdittext.getText().equals(passwordRepEdittext.getText());
		//return true;
	}
	
	private void register(){
		//wyslanie paczki rejestrujacej do serwera
	}
	
	//TODO Również do Validatora.
	/*
	 * sprawdza poprawność Emaila podanego podczas rejestracji
	 */
	private boolean checkEmail(){
		//sprawdzenie czy email jest poprawny
		String email = emailEdittext.getText().toString();
		//to jest wyrażenie regularne określające jak może wyglądać email w postaci user@domain.cos
		String emailReg = "^[a-zA-Z0-9_]+@[a-zA-Z0-9\\-]+\\.[a-zA-Z0-9\\-\\.]+$";
		//stworzenie obiektów potrzebnych do weryfikacji emaila
		Pattern pattern = Pattern.compile(emailReg);
		Matcher testEmail = pattern.matcher(email);
		//jesli find() zwróci true to znaczy że email jest poprawny
		return testEmail.find();
	}
	
	/** Klasa zawiera implementacje funkcji opisujących zachowanie
	 *  podczas podłączania i odłączania od serwisu.
	 */
	private ServiceConnection serConn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service){
			commService = ICommunicationService.Stub.asInterface(service);
			if(commService == null) Log.v(TAG, "NULL jest");
			Log.v(TAG, "polaczono z serwisem");
			
			//metody do wykonania przy starcie aktywności
			setBalance();
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name){
			commService = null;
			Log.v(TAG, "rozlaczono z serwisem");
		}
	};
}
