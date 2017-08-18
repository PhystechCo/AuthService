/**
 * 
 */
package co.phystech.aosorio.services;

import org.sql2o.Sql2o;

import com.google.gson.JsonObject;

import co.phystech.aosorio.controllers.IModel;
import co.phystech.aosorio.controllers.Sql2oModel;
import co.phystech.aosorio.controllers.SqlController;
import spark.Request;
import spark.Response;

/**
 * @author AOSORIO
 *
 */
public class StatisticsSvc {
	
	public static Object getBasicStats(Request pRequest, Response pResponse) {
		
		JsonObject json = new JsonObject();
		
		Sql2o sql2o = SqlController.getInstance().getAccess();

		IModel model = new Sql2oModel(sql2o);
				
		json.addProperty("users", model.getAllUsers().size());
		
		return json;
		
		
	}
	
}
