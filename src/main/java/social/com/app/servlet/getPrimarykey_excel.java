package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.ParseSlingData;
import com.service.ReadHeaderExcel;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/getPrimarykey_excel" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class getPrimarykey_excel extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	
	@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws IOException {
		PrintWriter out = rep.getWriter();
		Session session = null;
       String email=null;
       //String eventName=null;
       //String EventId=null;
       //InputStream inputXls = null;
       //Node emailnode=null;
       Node ExcelNode=null;
       long lastcount;
       JSONArray hedarr=null;
       //String email2="";
       
      String  primarykey=null;
		String primarykeyvalue=null;


		try {
			
			BufferedInputStream bis = new BufferedInputStream(req.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();
			while (result != -1) {
				buf.write((byte) result);
				result = bis.read();
			}
		   String res = buf.toString("UTF-8");
		   JSONObject obj = new JSONObject(res);
		  
			email=obj.getString("email");
			
			//email2=obj.getString("email").replace("@", "_");
			
			//email2=email.replace("@", "_");
			
//out.println("email "+ email+" eventName "+eventName+" EventId "+EventId);
			session = repo.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			
			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(email);
			
		Node	doctiger =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email, session, rep );
			if(doctiger!=null) {

				if(doctiger.hasNode("Excel") ){
					ExcelNode= doctiger.getNode("Excel") ;
					//out.println("ExcelNode  "+ExcelNode);
					lastcount=	ExcelNode.getProperty("last_count").getLong();
					//out.println("lastcount  "+lastcount);
					String count=Integer.toString((int) (lastcount-1));
					if(ExcelNode.hasNode(count)) {
					Node reqFileNode=ExcelNode.getNode(count);
					//out.println("reqFileNode  "+reqFileNode);
					//String flname = reqFileNode.getProperty("filename").getString();
					//out.println("flname  "+flname);

					String filepath = reqFileNode.getProperty("filepath").getString();
					//out.println("filepath  "+reqFileNode);

					ReadHeaderExcel rm = new ReadHeaderExcel();
					
					
					if (req.getRequestPathInfo().getExtension().equals("primarykey")) {
						//eventName=obj.getString("eventname");
						//EventId=obj.getString("eventid");

					String resp = rm.callget(filepath);
					//out.println("resp  "+resp);

					JSONObject jshed = new JSONObject(resp);
					 hedarr = jshed.getJSONArray("Headers");
						//out.println("hedarr  "+hedarr);
					 
					 
						JSONObject resultjson= new JSONObject();
						resultjson.put("data", hedarr.get(0).toString());
						out.print(resultjson.toString());

					} else if(req.getRequestPathInfo().getExtension().equals("exceldata")) { 
						//out.println("flname  "+flname);
						//out.println("filepath  "+reqFileNode);

						primarykey=obj.getString("Primary_key");
								primarykeyvalue=	obj.getString("Primary_key_value");

								
					            //out.println("primarykey  "+primarykey);
								//out.println("primarykeyvalue  "+primarykeyvalue);

						String resultjsonstring = rm.getJsondatabypk( email,  filepath,  primarykey,  primarykeyvalue);

						out.print(resultjsonstring);

					}
					}else {
						
						JSONObject resultjson= new JSONObject();
						resultjson.put("data", " Excel Not found");
						out.print(resultjson.toString());
					}
				}else {
					JSONObject resultjson= new JSONObject();
					resultjson.put("data", " Excel Not found");
					out.print(resultjson.toString());
				}
				}else {
					JSONObject resultjson= new JSONObject();
					resultjson.put("data", " Invalid user");
					out.print(resultjson.toString());
				
				}
		} catch (Exception e) {
			out.print("error "+e.getMessage());
		}

	}

	
	
	}
