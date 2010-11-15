package com.meetp.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.meetp.communication.Balance;
import com.meetp.communication.BalanceRequest;

import com.meetp.exceptions.FailedException;


public class ServerAdapter {
	//konfiguracja serwera
	private static final String SERVER_NAME = "lewy.com.pl";
	private static final int SERVER_PORT = 2222;
	
	//konfiguracja magazynu kluczy
	private static final String TRUSTED_STORE = "meetpClient.keystore";
	private static final char [] KEYS_PASSWORD = "1m1e1e1t1p1".toCharArray();
	
	//wejściowy i wyjściowy strumień obiektów
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	private SSLSocketFactory ssf;
	private SSLSocket ClientSocket;
	
	public void connectToServer() throws FailedException{
		try {
			/*Prawie to samo co w przypadku serwera, czyli konfiguracja połączenia szyfrowanego.
			 * Wczytuje zbiór kluczy zaufanych, w których znajduje się certyfikat serwera.
			 * Tworze managera zaufania w oparciu o algorytm SUNX509.
			 * Tworze Context i na jego podstawie fabryke tworzącą gniazdo zaszyfrowane TLS'em.
			 * Z gniazda łączę sie z określonym serwerem na określonym porcie.
			 */
			KeyStore zaufanyMagazynKluczy = KeyStore.getInstance("JKS");
			zaufanyMagazynKluczy.load(new FileInputStream(TRUSTED_STORE), KEYS_PASSWORD);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(zaufanyMagazynKluczy);
			SSLContext context = SSLContext.getInstance("TLS");
			TrustManager[] TrustManagers = tmf.getTrustManagers();
			context.init(null, TrustManagers, null);			
			ssf = context.getSocketFactory();		
			ClientSocket = (SSLSocket) ssf.createSocket(SERVER_NAME, SERVER_PORT);
			
		} catch(IOException e){
			throw new FailedException();
		} catch (KeyStoreException e) {
			throw new FailedException();
		} catch (NoSuchAlgorithmException e) {
			throw new FailedException();
		} catch (KeyManagementException e) {
			throw new FailedException();
		} catch (CertificateException e) {
			throw new FailedException();
		}
		
		//Ustawiam stałe wejście i wyjście na naszym sockecie
		try {
			oos = new ObjectOutputStream(ClientSocket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(ClientSocket.getInputStream());
		} catch (IOException e) {
			throw new FailedException();
		}		
	}
	
	public Balance getBalance() throws FailedException{
		Balance b = null;
		Object o = null;
		try {
			oos.writeObject(new BalanceRequest());
			oos.flush();
			o = ois.readObject();
			if(o instanceof Balance){
				b = (Balance)o;
				return b;
			}
		} catch (IOException e) {
			throw new FailedException();
		}catch (ClassNotFoundException e) {
			throw new FailedException();
		}
		
		return b;
	}
}
