package application;



import java.util.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class Mailgun {
	/**This class manages the Mailgun API
	 * It is used to send out emails using the SendSimple method*/
	public static ClientResponse SendSimple(String recipient, String subject, String text) {

		
		
		
        Client client = ClientBuilder.newClient();
        client.register(HttpAuthenticationFeature.basic(
            "api",
            "key-5af7c6b34339cc5917ddb562bab17a17"
            // the public key of the API (setup on the website by me)
        ));

        WebTarget mgRoot = client.target("https://api.mailgun.net/v3");
        // the url of the API that I am using
        
        Form reqData = new Form();
        reqData.param("from", "Admin <admin@sandboxc824413b0887470fb3623f61ec72efe8.mailgun.org>"); 
        // sandboxc824413b0887470fb3623f61ec72efe8.mailgun.org is the free domain that I have control over
        // over this domain, through the website, I could create users that would be able to send out emails.
        // I created only one user called "admin"
        reqData.param("to", recipient);
        // the email of the recipient
        reqData.param("subject", subject);
        // the subject of the email
        reqData.param("text", text);
        // the content of the email
        
        return mgRoot
            .path("/{domain}/messages")
            .resolveTemplate("domain", "sandboxc824413b0887470fb3623f61ec72efe8.mailgun.org")
            // the free domain I had control over as I have described above
            .request(MediaType.APPLICATION_FORM_URLENCODED)
            .buildPost(Entity.entity(reqData, MediaType.APPLICATION_FORM_URLENCODED))
            .invoke(ClientResponse.class);
        // mgroot essentially returns what the response of the client was (as in if there was any error or something while sending the email)
    }
}