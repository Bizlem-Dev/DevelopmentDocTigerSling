package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Date;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;

import com.mongodb.connection.Connection;
import com.service.ActivateWorkflow;
import com.service.ParseSlingData;
import com.service.ReadHeaderExcel;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;
import com.sun.jersey.core.util.Base64;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
 @Property(name = "service.description", value = "Save product Servlet"),
 @Property(name = "service.vendor", value = "VISL Company"),
 @Property(name = "sling.servlet.paths", value = {"/servlet/service/getUserChatPassword12"}),
 @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
 @Property(name = "sling.servlet.extensions", value = {"hotproducts",  "cat",  "latestproducts",  "brief",  "prodlist",  "catalog",  
		 "viewcart",  "productslist",  "addcart",  "createproduct",  "checkmodelno",  "productEdit" })})
@SuppressWarnings("serial")
public class getUserChatPasswordServ extends SlingAllMethodsServlet {

 @Reference
 private SlingRepository repo;
//http://35.200.169.114:8082/portal/servlet/service/getUserChatPassword12

 @Override
 protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
 throws ServletException, IOException {
	  PrintWriter out = response.getWriter();
	  String uid = request.getParameter("uid");
	  Connection conn = null; 
		 String password="";
		 String entityId="";
		 ResultSet rs=null;
		 try {
			 out.println("classpath :: "+request.getRealPath("/"));
			 Class.forName("com.mysql.jdbc.Driver").newInstance();
		 conn =(Connection) DriverManager.getConnection("jdbc:mysql//localhost:3306/rave2?" +
		 "user=root&password=password");
		 String sql="select encrypt_password,entity_id from person where username='"+uid+"'";
		 java.sql.Statement smt=((java.sql.Connection) conn).createStatement();
		 rs=smt.executeQuery(sql);

		 while(rs.next()){
		 password=rs.getString("encrypt_password");
		 entityId=rs.getString("entity_id");
		 }
		 } catch (Exception e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 //password="fail"+e.getMessage();
		 }
		 out.println("new ");
		 out.println(password+"-@#@-"+entityId);
  
 }

 @SuppressWarnings("deprecation")
@Override
 protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
 throws ServletException, IOException {
  // TODO Auto-generated method stub

  
 }
 

}