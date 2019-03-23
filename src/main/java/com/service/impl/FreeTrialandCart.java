package com.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.sling.commons.json.JSONObject;

public class FreeTrialandCart {
	
	public static void main(String[] args) {
		
		new FreeTrialandCart().checkfreetrial("doctiger8@gmail.com");
	//"mohit.raj@bizlem.com"	
	}
	
	public String checkfreetrial(String userid) {
		int expireFlag=0;
		String addr = "http://dev.bizlem.io:8086/apirest/trialmgmt/trialuser/"+userid+"/DocTigerFreeTrial";
		String username = "username";
		String password = "password";
		try{
		URL url = new URL(addr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");

		conn.connect();
		InputStream in = conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String text = reader.readLine();
		System.out.println(text);
JSONObject obj = new JSONObject(text);

 expireFlag=obj.getInt("expireFlag");
	System.out.println(expireFlag);

		conn.disconnect();
		}catch(Exception ex)
		{
		ex.printStackTrace();
		System.out.println("made it here");
		}
		//return expireFlag+"";
		return "0";

		}
}
