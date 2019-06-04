package social.com.app.servlet;

//http://prod.bizlem.io:8082/portal/process/shoppingcart/service_updated12
import com.service.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/process/shoppingcart/service_sampletests" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class ShoppingCartService_sampletests extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	
	
	//private ParseSlingData parseSlingData;

@Override
protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
		throws ServletException, IOException {

	PrintWriter out = response.getWriter();
	Session session = null;
	Node dtaNode=null;
	String usrid=null;
	String group=null;
	JSONArray arr =null;
try {
	 out = response.getWriter();

	usrid=request.getParameter("email");
	//group=request.getParameter("group");
	//out.println("usrid "+usrid +" group  "+group);
		session = repo.login(new SimpleCredentials(CrRuleConstValue.StringConstant.ADMIN.value(),
		CrRuleConstValue.StringConstant.ADMIN.value().toCharArray()));
		FreeTrialandCart cart = new FreeTrialandCart();
		String freetrialstatus = cart.checkfreetrial(usrid);
		//out.println("freetrialstatus: "+freetrialstatus);
		arr = getListOfAssignedGroups("1", usrid,group, session, response);
	 out.println(arr);

	
}catch(Exception e){
	e.printStackTrace();
    out.println();}
}




public JSONArray getListOfAssignedGroups(String freetrialstatus, String email, String group, Session session1,
		SlingHttpServletResponse response) {
	PrintWriter out = null;
	Node contentNode = null;
    Node emailNode=null;
    Node adminserviceidNode=null;
    String adminserviceid=null;
    JSONArray arr=null;
	try {
		out = response.getWriter();
		arr =new JSONArray();
		if (session1.getRootNode().hasNode("content")) {
			contentNode = session1.getRootNode().getNode("content");
		} else {
			contentNode = session1.getRootNode().addNode("content");
		}
	// out.println("contentNode "+contentNode);

		if (!freetrialstatus.equalsIgnoreCase("0")) {
			if (contentNode.hasNode("user") && contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
				emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
				if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("doctiger")
						&& emailNode.getNode("services").getNode("doctiger").hasNodes()) {
					NodeIterator itr = emailNode.getNode("services").getNode("doctiger").getNodes();
					while (itr.hasNext()) {
						adminserviceidNode=itr.nextNode();
						adminserviceid = adminserviceidNode.getName();
						if(!adminserviceid.equalsIgnoreCase("DocTigerFreeTrial")) {
							break;
						}
					}
					//out.println("adminserviceidNode "+adminserviceidNode);
					if(adminserviceidNode.hasNodes()) {
						NodeIterator itr2 = adminserviceidNode.getNodes();
						while(itr2.hasNext()) {
							Node groupNode =itr2.nextNode();
							arr.put(groupNode.getName());
						}
					}
					
				}
			}

		}
		
	}catch(Exception e) {e.printStackTrace();}
	return arr;
}
public Node getDocTigerAdvNode(String freetrialstatus, String email, String group, Session session1,
		SlingHttpServletResponse response) {

	// freetrialstatus="0";
	PrintWriter out = null;
	// out.println("in getDocTigerAdvNode");

	Node contentNode = null;
	Node appserviceNode = null;
	Node appfreetrialNode = null;
	Node emailNode = null;
	Node DoctigerAdvNode = null;

	Node adminserviceidNode = null;
	String adminserviceid = "";
	try {
		out = response.getWriter();

		// out.println("freetrialstatus "+freetrialstatus);
		// out.println("email "+email);

		// session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		if (session1.getRootNode().hasNode("content")) {
			contentNode = session1.getRootNode().getNode("content");
		} else {
			contentNode = session1.getRootNode().addNode("content");
		}
	// out.println("contentNode "+contentNode);

		if (freetrialstatus.equalsIgnoreCase("0")) {

			if (contentNode.hasNode("services")) {
				appserviceNode = contentNode.getNode("services");

				// out.println("appserviceNode "+appserviceNode);

				if (appserviceNode.hasNode("freetrial")) {
					appfreetrialNode = appserviceNode.getNode("freetrial");

					//out.println("appfreetrialNode "+appfreetrialNode);

					if (appfreetrialNode.hasNode("users")
							&& appfreetrialNode.getNode("users").hasNode(email.replace("@", "_"))) {
						emailNode = appfreetrialNode.getNode("users").getNode(email.replace("@", "_"));
						// out.println("emailNode "+emailNode);
						if (emailNode.hasNode("DocTigerAdvanced")) {
							DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
						} else {
							DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
						}
						 //out.println("DoctigerAdvNode "+DoctigerAdvNode);

					} else {
						// emailNode=appfreetrialNode.getNode("users").addNode(email.replace("@", "_"));
					}
				} else {
					// appfreetrialNode=appserviceNode.addNode("freetrial");
				}
			} else {
				// appserviceNode=contentNode.addNode("services");
			}

		} else {

		// out.println("in else");

			if (contentNode.hasNode("user") && contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
				emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
				if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("doctiger")
						&& emailNode.getNode("services").getNode("doctiger").hasNodes()) {
					NodeIterator itr = emailNode.getNode("services").getNode("doctiger").getNodes();
					while (itr.hasNext()) {
						adminserviceid = itr.nextNode().getName();
						if(!adminserviceid.equalsIgnoreCase("DocTigerFreeTrial")) {
							break;
						}
					}
				}
			}
			//out.println("adminserviceid "+adminserviceid);
			if ((adminserviceid != "") && (!adminserviceid.equals("DocTigerFreeTrial"))) {

				if (contentNode.hasNode("services")) {
					appserviceNode = contentNode.getNode("services");
				} else {
					appserviceNode = contentNode.addNode("services");
				}
				//out.println("appserviceNode "+appserviceNode);

				if (appserviceNode.hasNode(adminserviceid)) {
					appfreetrialNode = appserviceNode.getNode(adminserviceid);
				} else {
					appfreetrialNode = appserviceNode.addNode(adminserviceid);
				}
				if (appfreetrialNode.hasNode(group)) {
					emailNode = appfreetrialNode.getNode(group);
				} else {
					emailNode = appfreetrialNode.addNode(group);
				}
              //out.println("emailNode "+emailNode);
				if (emailNode.hasNode("DocTigerAdvanced")) {
					DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
				} else {
					DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
				}
			}
		}

		// session.save();
	} catch (Exception e) {
		// TODO Auto-generated catch block
	//out.println(e.getMessage());
		DoctigerAdvNode=null;
	}

	return DoctigerAdvNode;
}





}
