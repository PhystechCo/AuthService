/**
 * 
 */
package loginsvc;

import static org.junit.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import co.phystech.aosorio.services.StatisticsSvc;
import spark.Request;
import spark.Response;

/**
 * @author AOSORIO
 *
 */
public class ServiceTest {

	private final static Logger slf4jLogger = LoggerFactory.getLogger(ServiceTest.class);
	
	//@Test
	public void bookCounterTest() {
		
		//Test the book counter service
		Request pRequest = null;
		Response pResponse = null;
		
		JsonObject json = (JsonObject) StatisticsSvc.getBasicStats(pRequest, pResponse);
		
		slf4jLogger.info("Number of users: " + json.get("users"));
	
		assertTrue(json.has("users"));
		
	}

}
