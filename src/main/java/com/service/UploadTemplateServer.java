package com.service;




import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;



public class UploadTemplateServer {
	final static Logger logger = Logger.getLogger(UploadTemplateServer.class); 

	private String host;
	private Integer port;
	private String user;
	private String password;
	
	private JSch jsch;
	private Session session1;
	private Channel channel;
	private ChannelSftp sftpChannel;
	
	public UploadTemplateServer(String host, Integer port, String user, String password) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}

	public String connect() {
		String r="";
		System.out.println("connecting..."+host);
		try {
			
//			JSch jsch = new JSch();
//
//		
//			Session session = jsch.getSession(USERNAME, host, Integer.parseInt(port));
//			// session.setConfig("StrictHostKeyChecking", "no");
//			java.util.Properties config = new java.util.Properties();
//			config.put("StrictHostKeyChecking", "no");
//			session.setConfig(config);
//
//			session.setPassword(PASSWORD);
//			session.connect();
//			System.out.println("Done!");
			jsch = new JSch();
			session1 = jsch.getSession(user, host,port);
//			session1.setConfig("StrictHostKeyChecking", "no");
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session1.setConfig(config);
//			String d="192.168.0.202";
			
			session1.setPassword(password);
			r="1w";
			System.setProperty("https.proxyHost", host);
			System.setProperty("https.proxyPort", "22");
			r=r+"1w";
			session1.connect();
			r=r+"2w";
			channel = session1.openChannel("sftp");
			r=r+"2w";
			channel.connect();
			r=r+"3w";
			sftpChannel = (ChannelSftp) channel;
			r=r+"4w";
System.out.println(r);
logger.info(r);
		} catch (JSchException e) {
//			e.printStackTrace();
			r=r+e.getMessage();
			logger.info("error "+r);
		}
		return r;

	}
	
	public void disconnect() {
		System.out.println("disconnecting...");
		sftpChannel.disconnect();
		channel.disconnect();
		session1.disconnect();
	}
	
	public String upload(String fileName, String remoteDir) {
		String res="";
		FileInputStream fis = null;
		String a=connect();
		res=a;
		try {
			// Change to output directory
			sftpChannel.cd(remoteDir);
			res="1";
			// Upload file
			File file = new File(fileName);
			fis = new FileInputStream(file);
			sftpChannel.put(fis, file.getName());

			fis.close();
			res=res+"2";
			System.out.println("File uploaded successfully - "+ file.getAbsolutePath());

		} catch (Exception e) {
			e.printStackTrace();
		}
		disconnect();
		return res;
	}
	
	public String  download(String fileName, String localDir) {
		
		File file= null;
		byte[] buffer = new byte[1024];
		BufferedInputStream bis;
		connect();
		try {
			// Change to output directory
			System.out.println("fileName = "+fileName);
			String cdDir = fileName.substring(0, fileName.lastIndexOf("/") + 1);
			System.out.println("cdDir = "+cdDir);
			sftpChannel.cd(cdDir);

			 file = new File(fileName);
			 
			bis = new BufferedInputStream(sftpChannel.get(file.getName()));
System.out.println(bis);

			File newFile = new File(localDir + "" + file.getName());
			System.out.println("localDir = "+localDir);
			// Download file
			OutputStream os = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			int readCount;
			while ((readCount = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, readCount);
			}
			bis.close();
			bos.close();
			System.out.println("File downloaded successfully - "+ file.getAbsolutePath());

		} catch (Exception e) {
			return e.getMessage();
		}
		disconnect();
		return file.getAbsolutePath();
	}

	public static void main(String[] args) {
		
		
		String localPath = "D:\\DocTiger\\CRF - RP Termination.docx";
		String remotePath = "/home/ubuntu/uploaded-templates/";
		
		//UploadTemplateServer ftp = new UploadTemplateServer("35.188.238.145",22,"ubuntu","$DocTiger@123$");
//		UploadTemplateServer ftp = new UploadTemplateServer("104.196.62.35",22,"root","B1!4z5L$e#m@Or#@GIn");
	
			
	//	ftp.upload(localPath, remotePath);
		
	      
//System.out.print(a);///

		String serv222path= "/home/vil/sling\\ tomcat/apache-tomcat-6.0.35/webapps/ROOT/SFTemplateLibrary/";
		String servsavepah="D:\\docgenlocal";
		
//	ftp.download(serv222path+"DocTemplate.docx", servsavepah);
	 //ArrayNew_17-May-2019_11-40-55-238.pdf
		UploadTemplateServer ftp = new UploadTemplateServer("35.243.163.58",22,"ubuntu","$DocTiger@123$");
		String a=ftp.connect();
		 String servp="/home/ubuntu/apache-tomcat-8.5.31/webapps/ROOT/";        
		 String rp="D:\\DOCTIGER114IPProject\\testing docx\\reports\\";
		ftp.download(servp+"ArrayNew_17-May-2019_11-40-55-238.pdf", rp);

	
	
	
	
	
	}

}