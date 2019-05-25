package social.com.app.servlet;

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

//http://prod.bizlem.io:8082/portal/process/shoppingcart/service_updated12
import com.service.ParseSlingData;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/process/shoppingcart/service_updated12" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class ShoppingCartService_Updated extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	
	
	//private ParseSlingData parseSlingData;
@Override
protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
		throws ServletException, IOException {
	PrintWriter out = response.getWriter();
	out.println("GET");
	
	
}
@Override
protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
		throws ServletException, IOException {

	PrintWriter out = response.getWriter();
	Session session = null;
	Node content = null;
	Node USERNode = null;
	Node servicenode1 = null;
	Node servicenode2 = null;
	Node AdminusernameNode = null;
	Node productcodenode = null;
	Node serviceidnode1 = null;
	Node serviceidnode2 = null;

	Node subusernode = null;
	Node groupnode = null;
	Node usersnode = null;
	Node subusersnode = null;
	StringBuilder builder = null;
	String username = null;
	String serviceid = null;
	String productcode = null;
	JSONArray fullloaddata = null;

	BufferedReader bufferedReaderCampaign = null;
	JSONObject fulljsonobject = null;
	try {
		out = response.getWriter();
		session = repo.login(new SimpleCredentials(CrRuleConstValue.StringConstant.ADMIN.value(),
		CrRuleConstValue.StringConstant.ADMIN.value().toCharArray()));
		content = session.getRootNode().getNode(CrRuleConstValue.StringConstant.CONTENT.value());
		if (!content.hasNode(CrRuleConstValue.StringConstant.USER.value())) {
		USERNode = content.addNode(CrRuleConstValue.StringConstant.USER.value());
		} else {
		USERNode = content.getNode(CrRuleConstValue.StringConstant.USER.value());
		}
		out.println("USERNode " + USERNode);
		if (!content.hasNode(CrRuleConstValue.StringConstant.ALLSERVICES.value())) {
		servicenode2 = content.addNode(CrRuleConstValue.StringConstant.ALLSERVICES.value());
		} else {
		servicenode2 = content.getNode(CrRuleConstValue.StringConstant.ALLSERVICES.value());
		}
		out.println("servicenode2 " + servicenode2);

		// static nodes
		// end------------------------------------------------------------------------
		builder = new StringBuilder();
		bufferedReaderCampaign = request.getReader();
		String line = null;
		while ((line = bufferedReaderCampaign.readLine()) != null) {
		builder.append(line + "\n");
		}
		fulljsonobject = new JSONObject(builder.toString());
		out.println("fulljsonobject " + fulljsonobject);

		// userId --mailId,serviceId --800,
		username = fulljsonobject.getString("userId").replace("@", "_");
		serviceid = fulljsonobject.getString("serviceId");
		productcode = fulljsonobject.getString("productCode");
		fullloaddata = fulljsonobject.getJSONArray("fullloaddata");
		out.println("username " + username);
		out.println("serviceid " + serviceid);
		out.println("fullloaddata " + fullloaddata);

		if (!USERNode.hasNode(username)) {
		AdminusernameNode = USERNode.addNode(username);
		} else {
		AdminusernameNode = USERNode.getNode(username);
		}
		out.println("AdminusernameNode " + AdminusernameNode);

		if (!AdminusernameNode.hasNode(CrRuleConstValue.StringConstant.SERVICES.value())) {
		servicenode1 = AdminusernameNode.addNode(CrRuleConstValue.StringConstant.SERVICES.value());
		} else {
		servicenode1 = AdminusernameNode.getNode(CrRuleConstValue.StringConstant.SERVICES.value());
		}
		out.println("servicenode1 " + servicenode1);

		if (!servicenode1.hasNode(productcode)) {
		productcodenode = servicenode1.addNode(productcode);
		} else {
		productcodenode = servicenode1.getNode(productcode);
		}
		out.println("productcodenode " + productcodenode);

		if (!productcodenode.hasNode(serviceid)) {
		serviceidnode1 = productcodenode.addNode(serviceid);
		} else {
		serviceidnode1 = productcodenode.getNode(serviceid);
		}
		out.println("serviceidnode1 " + serviceidnode1);

		// ...........................................................................
		// admin 2nd part add serviceid node in servicenode2

		if (!servicenode2.hasNode(serviceid)) {
		serviceidnode2 = servicenode2.addNode(serviceid);
		serviceidnode2.setProperty("producttype", productcode);
		} else {
		serviceidnode2 = servicenode2.getNode(serviceid);
		serviceidnode2.setProperty("producttype", productcode);
		}
		out.println("serviceidnode2 " + serviceidnode2);

		// admin node configuration
		// end----------------------------------------------------------------------------------
		// add for loop to save fullload users data
		for (int i = 0; i < fullloaddata.length(); i++) {
		JSONObject subobj = fullloaddata.getJSONObject(i);
		out.println("subobj "+subobj);
		String subuser = subobj.getString("user").replace("@", "_");
		out.println("subuser "+subuser);
		String role = subobj.getString("role");
		out.println("role "+role);
		String group = subobj.getString("group");
		out.println("group "+group);



		System.out.println("subuser "+ subuser+" role "+role+" group "+group);
		if (!USERNode.hasNode(subuser)) {
		// add user in bizlem call httpurl connection
		String result=	sendGet("http://prod.bizlem.io:8082/portal/process/shoppingcart/ShoppingCartUserCheckService?userId="
		+ subobj.getString("user"));
		System.out.println("result "+result);

		subusernode = USERNode.addNode(subuser);
		} else {
		subusernode = USERNode.getNode(subuser);
		}

		out.println("subusernode "+subusernode);
		if (!subusernode.hasNode(CrRuleConstValue.StringConstant.SERVICES.value())) {
		servicenode1 = subusernode.addNode(CrRuleConstValue.StringConstant.SERVICES.value());
		} else {
		servicenode1 = subusernode.getNode(CrRuleConstValue.StringConstant.SERVICES.value());
		}
		out.println("servicenode1 "+servicenode1);

		if (!servicenode1.hasNode(productcode)) {
		productcodenode = servicenode1.addNode(productcode);
		} else {
		productcodenode = servicenode1.getNode(productcode);
		}
		out.println("productcodenode "+productcodenode);

		if (!productcodenode.hasNode(serviceid)) {
		serviceidnode1 = productcodenode.addNode(serviceid);
		} else {
		serviceidnode1 = productcodenode.getNode(serviceid);
		}
		out.println("serviceidnode1 "+serviceidnode1);

		if (!serviceidnode1.hasNode(group)) {
		groupnode = serviceidnode1.addNode(group);
		} else {
		groupnode = serviceidnode1.getNode(group);
		}
		out.println("groupnode "+groupnode);

		// ...........................................................................
		// applicatio 2nd part
		if (!serviceidnode2.hasNode(group)) {
		groupnode = serviceidnode2.addNode(group);
		} else {
		groupnode = serviceidnode2.getNode(group);
		}
		out.println("groupnode "+groupnode);


		if (!groupnode.hasNode(CrRuleConstValue.StringConstant.USERS.value())) {
		usersnode = groupnode.addNode(CrRuleConstValue.StringConstant.USERS.value());
		} else {
		usersnode = groupnode.getNode(CrRuleConstValue.StringConstant.USERS.value());
		}
		if (!usersnode.hasNode(subuser)) {
		subusersnode = usersnode.addNode(subuser);
		subusersnode.setProperty("role", role);
		} else {
		subusersnode = usersnode.getNode(subuser);
		subusersnode.setProperty("role", role);
		}

		}

		// -----------------------------------------------------------------------------------------------------------

		// -------------------------------------------------------------------------------------------------------------

		/// content/services/service001(789)(Property
		/// producttype)/users/gaurav.bhandari_damacgroup.com.partial
		// jcr:root/content/user/gaurav.bhandari_damacgroup.com.partial
		/// serivces/doctiger/789/G1

		// jcr:root/content/user/gaurav.bhandari_damacgroup.com.partial
		// serivces/doctiger/789/G1

		session.save();
		} catch (Exception e) {
		out.println(e.getMessage());
		e.printStackTrace();
		}
	
}

public String sendGet(String url) throws Exception {
	StringBuffer userresult =null;
	// String url =
	// "http://35.221.160.146:8180/UserValidation/services/UserValidation/raveUserExistence?userId=doctiger100@gmail.com";
	try {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		userresult = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			userresult.append(inputLine);
		}
		in.close();

		System.out.println(userresult.toString());
		return userresult.toString();
	} catch (Exception e) {
		// e.printStackTrace();
		return e.getMessage();
	}
}
}
