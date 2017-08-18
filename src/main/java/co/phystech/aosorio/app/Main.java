/**
 * 
 */
package co.phystech.aosorio.app;

import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.before;

//import co.phystech.aosorio.services.StatisticsSvc;
import co.phystech.aosorio.config.CorsFilter;
import co.phystech.aosorio.config.Routes;
import co.phystech.aosorio.controllers.UserController;
import co.phystech.aosorio.services.AuthenticationSvc;
import co.phystech.aosorio.services.GeneralSvc;

/**
 * @author AOSORIO
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		port(getHerokuAssignedPort());

		CorsFilter.apply();

		get("/hello", (req, res) -> "Login service deployed");

		post(Routes.AUTH + "login/", AuthenticationSvc::doLogin, GeneralSvc.json());

		// ... User control - request access

		post(Routes.AUTH + "access/", AuthenticationSvc::checkAccess, GeneralSvc.json());

		// ... Administrative services

		before(Routes.ADMIN + "*", AuthenticationSvc::authAdmin);

		post(Routes.ADMIN + "users/", UserController::createUser, GeneralSvc.json());

		options("/*", (request, response) -> {

			String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
			if (accessControlRequestHeaders != null) {
				response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
			}
			String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
			if (accessControlRequestMethod != null) {
				response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
			}
			return "OK";
		});

	}

	static int getHerokuAssignedPort() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		if (processBuilder.environment().get("PORT") != null) {
			return Integer.parseInt(processBuilder.environment().get("PORT"));
		}
		return 4568;
	}

}
