package application;



import java.util.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class Mailgun {
	public static ClientResponse SendSimple(String recipient, String subject, String text) {

		
		
		
        Client client = ClientBuilder.newClient();
        client.register(HttpAuthenticationFeature.basic(
            "api",
            "key-5af7c6b34339cc5917ddb562bab17a17"
        ));

        WebTarget mgRoot = client.target("https://api.mailgun.net/v3");
        
        Form reqData = new Form();
        reqData.param("from", "Admin <admin@sandboxc824413b0887470fb3623f61ec72efe8.mailgun.org>");
        //reqData.param("from", "Admin <admin@sandbox866b186bc68543b195a0840e9556f5bf.mailgun.org>");
        reqData.param("to", recipient);
        reqData.param("subject", subject);
        reqData.param("text", text);

        return mgRoot
            .path("/{domain}/messages")
            .resolveTemplate("domain", "sandboxc824413b0887470fb3623f61ec72efe8.mailgun.org")
            .request(MediaType.APPLICATION_FORM_URLENCODED)
            .buildPost(Entity.entity(reqData, MediaType.APPLICATION_FORM_URLENCODED))
            .invoke(ClientResponse.class);
    }
}