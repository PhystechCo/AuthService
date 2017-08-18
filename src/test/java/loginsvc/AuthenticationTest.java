package loginsvc;

import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;

import co.phystech.aosorio.controllers.IModel;
import co.phystech.aosorio.controllers.Sql2oModel;
import co.phystech.aosorio.controllers.SqlController;
import co.phystech.aosorio.exceptions.WrongPasswordException;
import co.phystech.aosorio.exceptions.WrongUserException;
import co.phystech.aosorio.models.User;
import co.phystech.aosorio.services.AuthenticationJWT;
import co.phystech.aosorio.services.GeneralSvc;
import co.phystech.aosorio.services.IAuthentication;
import io.jsonwebtoken.SignatureException;

public class AuthenticationTest {

	static String username = "aosorio@phystech";
	static String password = "12345678";
	static String role = "test";

	static Object testToken;

	private final static Logger slf4jLogger = LoggerFactory.getLogger(AuthenticationTest.class);

	@BeforeClass
	public static void beforeClass() {
		// Add some user
		Sql2o sql2o = SqlController.getInstance().getAccess();
		IModel model = new Sql2oModel(sql2o);

		UUID testUuid = model.createUser(username, role, password);

		slf4jLogger.info("New test user UUID" + testUuid.toString());

	}

	@AfterClass
	public static void afterClass() {
		// Remove test user
		Sql2o sql2o = SqlController.getInstance().getAccess();
		IModel model = new Sql2oModel(sql2o);

		boolean result = model.deleteUser(username);

		if (result)
			slf4jLogger.info("Delete test user");
		else
			slf4jLogger.info("Warning: could not delete test user");

	}

	@Test
	public void authenticationTest() {

		try {

			IAuthentication authMethod = new AuthenticationJWT();

			authMethod.doAuthentication(username, password);

			testToken = authMethod.getToken();

			String username = GeneralSvc.getTokenClaim(testToken.toString(), "sub");

			slf4jLogger.info("Token: " + testToken.toString());
			slf4jLogger.info("Subject: " + username);

			Sql2o sql2o = SqlController.getInstance().getAccess();
			IModel model = new Sql2oModel(sql2o);

			User currentUser = model.getUser(username);

			AuthenticationJWT.validateJWT(currentUser, testToken.toString());

		} catch (WrongUserException ex) {
			slf4jLogger.info(ex.getMessage());
			
		} catch (WrongPasswordException ex) {
			slf4jLogger.info(ex.getMessage());
			
		} catch (SignatureException ex) {
			slf4jLogger.info(ex.getMessage());
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
