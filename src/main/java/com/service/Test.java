package com.service;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.sling.commons.json.JSONArray;

public class Test {

	public static void main(String[] args) {
		/*double[] myList= {1, 5, 5, 5, 5, 1};
		double max= myList[0];
		int indexOfmax= 0;
		for(int i=1; i<myList.length; i++) {
			if(myList[i]>max) {
				max= myList[i];
				System.out.println("max: "+max);
				indexOfmax =i;
				System.out.println("indeofmax:"+indexOfmax);
			}*/
		int[] a= new int[0];
		System.out.println(a.length);
		
		

        String languages = "";// tejal.biz.com,ghgu@gmail.com,C++,Python,Ruby,Scala

        // splitting String by comma, it will return array
        String[] array = languages.split(",");
        System.out.println("comma separated String: " + languages);

        JSONArray jsa=new JSONArray();
        for(int i=0;i<array.length;i++) {
        	
        	jsa.put(array[i]);
        }
        System.out.println("comma separated jsa: " + jsa);
        // let's print input and output
        System.out.println("comma separated String: " + languages);
        System.out.println("array of String: " + Arrays.toString(array));
//        System.out.println("list of String: " + list);
		}
		
	}

/*  {"Email":"viki@gmail.com","EventId":"18","EventName":"evtscr221114","database":"ws","Primery_key":"ID","SFObject":"","Primery_key_value":"1","SFData":{"response":[{
"Received":"18",
"Processed":"18",
"Successfully_Processed":"15",
"Failed":"3",
"Email":"tejal.jabade@bizlem.com",
"UnRecognized":"12",
"Total_No_of_rows":"7",
"Rejects":"5",
"Reason":"test",
"Date":"5/14/19",
"fromId":"scorpiorisalert@gmail.com",
"fromPass":"bizlem786",
"Cc":"tejal.jabade@bizlem.com",
"Bcc":"tejal.jabade@bizlem.com,tejal.jabade@bizlem.com",
"attachments":"",
"attachmentPath":""

}]}}*/