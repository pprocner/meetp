package com.meetp.client;

import com.meetp.R;
import com.meetp.gameViewMap.MapViewMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

/** 
 * 	@author Piotrek
 * 
 * 	Jest to klasa startowa aplikacji. Opisuje ekran logowania.
 * 	Layout aktywności znajduje się w pliku login.xml.
 * 	//Test Lewy Commit
 * 	//Lewy z HP
 */
public class Login extends Activity {
	
	private EditText loginEdittext;
	private EditText passwordEdittext;
	private CheckBox rememberCheckbox;
	private Spinner serverSpinner;
	
	/**
	 * Metoda wykonywana jest przy tworzeniu aktywności. 
	 * Inicjalizuje aktywność.
	 * @param savedInstanceState	Zachowany wcześniej stan. Jeśli 
	 * nie zachowano stanu parametr jest null.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // aktywnosc nie bedzie miec belki tytulowej
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.login);
        
        loginEdittext = (EditText)findViewById(R.id.login_edittext);
        passwordEdittext = (EditText)findViewById(R.id.password_edittext);
        rememberCheckbox = (CheckBox)findViewById(R.id.remember_checkbox);
        
        // jesli jest zapisany stan to go przywroc
        if(savedInstanceState != null){
    		loginEdittext.setText(savedInstanceState.getString("login"));
    		passwordEdittext.setText(savedInstanceState.getString("password"));
    		rememberCheckbox.setChecked(
    				savedInstanceState.getBoolean("remember"));
    		serverSpinner.setId((int)savedInstanceState.getLong("server"));
		}
        
        // inicjalizacja listy wyboru serwera - swiata gry
        serverSpinner = (Spinner)findViewById(R.id.server_spinner);
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.serwer_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serverSpinner.setAdapter(adapter);
        
        final Button registerButton = (Button)findViewById(R.id.register_button);
        
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openRegisterActivity();
            }
        });
        
        final Button loginButton = (Button)findViewById(R.id.login_button);
        
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(isLoginCorrect()){
            		openMapActivity();
            	}
            	
            }
        });
    }
    
    private void openRegisterActivity(){
    	Intent i = new Intent(this, Register.class);
    	startActivity(i);
    }
    
    private boolean isLoginCorrect(){
    	//sprawdzac czy pola do logowania nie sa puste
    	if(!loginEdittext.getText().equals("") && 
    		!passwordEdittext.getText().equals("") && 
    		serverSpinner.getSelectedItemId() != -1)
    	{
    	  return true;	
    	}else{
    		return true; // TODO Powinno byc false - cele testowe, zawsze testuje ok. 
    	}
    }
    
	  private void openMapActivity() {
	    	Intent i = new Intent(this, MapViewMain.class);
	    	startActivity(i);		
		}
    
    /**
     * Pakuje wszystkie ważne informacje do Bundle stan. Metoda zachowuje aktualny stan
     * aktywności.
     * @param stan 	Boundle, do którego zachowuje stan.
     */
    @Override
    protected void onSaveInstanceState(Bundle stan) {
        super.onSaveInstanceState(stan);
        stan.putString("login", loginEdittext.getText().toString());
        stan.putString("password", passwordEdittext.getText().toString());      
        stan.putBoolean("remember", rememberCheckbox.isChecked());
        stan.putLong("server", serverSpinner.getSelectedItemId());
    }
}