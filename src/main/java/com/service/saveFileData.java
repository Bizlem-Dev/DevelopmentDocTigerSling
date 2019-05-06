package com.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sun.jersey.core.util.Base64;

public class saveFileData {

	
	
	public String convertFile(String file_string, String file_name, String savepath) {
		try {
        byte[] bytes = Base64.decode(file_string);
        File file = new File(savepath+""+file_name);
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(bytes);
        fop.flush();
        fop.close();
        return "true";
		}catch(Exception e) {
			
			return "flase";
		}
    }
}
