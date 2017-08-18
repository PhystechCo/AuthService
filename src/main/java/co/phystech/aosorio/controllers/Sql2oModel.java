/**
 * 
 */
package co.phystech.aosorio.controllers;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import co.phystech.aosorio.exceptions.WrongUserException;
import co.phystech.aosorio.models.User;
import co.phystech.aosorio.services.GeneralSvc;
import co.phystech.aosorio.services.IUuidGenerator;
import co.phystech.aosorio.services.RandomUuidGenerator;

/**
 * @author AOSORIO
 *
 */
public class Sql2oModel implements IModel {

	private final static Logger slf4jLogger = LoggerFactory.getLogger(Sql2oModel.class);

	private Sql2o sql2o;
	private IUuidGenerator uuidGenerator;

	public Sql2oModel(Sql2o sql2o) {
		this.sql2o = sql2o;
		uuidGenerator = new RandomUuidGenerator();
	}

	@Override
	public UUID createUser(String username, String role, String password) {
		
		try (Connection conn = sql2o.beginTransaction()) {
			UUID userUuid = uuidGenerator.generate();
			String hash =  GeneralSvc.getHash(password, userUuid.toString());
			
			conn.createQuery(
					"insert into users(user_uuid, username, hash, role, affiliation, date_added) VALUES (:user_uuid, :username, :hash, :role, :affiliation, :date_added)")
					.addParameter("user_uuid", userUuid)
					.addParameter("username", username)
					.addParameter("hash", hash)
					.addParameter("role", role)
					.addParameter("affiliation", "phystech")
					.addParameter("date_added", new Date()).executeUpdate();
			conn.commit();
			
			slf4jLogger.info("Added new user: " + username);
			
			return userUuid;
		}
		
	}

	@Override
	public User getUser(String username) throws WrongUserException{

		try (Connection conn = sql2o.open()) {
			List<User> userQuery = conn.createQuery("select * from users where username=:username")
					.addParameter("username", username).executeAndFetch(User.class);

			if (userQuery.isEmpty()) {
				throw new WrongUserException();
			} else
				return userQuery.get(0);
		}

	}

	@Override
	public boolean updateUser(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUser(String username) {
		try (Connection conn = sql2o.open()) {
			conn.createQuery("delete from users where username=:username")
			.addParameter("username", username).executeUpdate();
			return true;
		}
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean userExists(String username) {
		
		try {	
			this.getUser(username);
		
		} catch ( WrongUserException ex ) {
			slf4jLogger.info("User does not exists: " + username);
			return false;
		}
		
		return true;
	}
}
