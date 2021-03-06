/**
 * 
 */
package co.phystech.aosorio.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import co.phystech.aosorio.config.Constants;
import co.phystech.aosorio.models.BackendMessage;
import co.phystech.aosorio.models.NewUserPayload;
import co.phystech.aosorio.models.TestUser;
import spark.Request;
import spark.Response;

/**
 * @author AOSORIO
 *
 */
public class UserController {
	
private final static Logger slf4jLogger = LoggerFactory.getLogger(UserController.class);
	
	public static Object createUser(Request pRequest, Response pResponse) {
		
		Sql2o sql2o = SqlController.getInstance().getAccess();
		
		IModel model = new Sql2oModel(sql2o);
			
		BackendMessage returnMessage = new BackendMessage();
		
		pResponse.type("application/json");
		
		try {
				
			ObjectMapper mapper = new ObjectMapper();
			
			NewUserPayload userBody = mapper.readValue(pRequest.body(), NewUserPayload.class);
			
			if (!userBody.isValid()) {
				slf4jLogger.info("Invalid body object: " + pRequest.body());
				pResponse.status(Constants.HTTP_BAD_REQUEST);
				return returnMessage.getNotOkMessage("Invalid body object");
			} 
			
			if ( !model.userExists(userBody.getUsername())) {
				
				UUID id = model.createUser(userBody.getUsername(), 
						userBody.getRole(), 
						userBody.getPassword());
				
				pResponse.status(200);				
				return returnMessage.getOkMessage(String.valueOf(id));
			
			} else {
				pResponse.status(200);
				return returnMessage.getNotOkMessage("User already exists");
			}
				
		} catch (UnrecognizedPropertyException ex ) {
			
			slf4jLogger.info("Problem adding new user");
			pResponse.status(Constants.HTTP_BAD_REQUEST);
			return returnMessage.getNotOkMessage("Problem adding user");
			
		} catch (IOException jpe) {

			slf4jLogger.info("Problem adding user");
			pResponse.status(Constants.HTTP_BAD_REQUEST);
			return returnMessage.getNotOkMessage("Problem adding user");
		} 
	
	}
	
public static Object getTestUsers(Request pRequest, Response pResponse) {
					
		pResponse.type("application/json");
		
		List<TestUser> users = new ArrayList<TestUser>();
		
		TestUser user = new TestUser();
		user.setNumber(101);
		user.setFirstName("Jhon");
		user.setLastName("Smith");
		user.setPassword("xxxxx");
		user.setUsername("jsmith");		
		users.add(user);
		
		user = new TestUser();
		user.setNumber(102);
		user.setFirstName("Rebecca");
		user.setLastName("Smith");
		user.setPassword("xxxxx");
		user.setUsername("rsmith");		
		users.add(user);
		
		return users;
		
	}
	
}
