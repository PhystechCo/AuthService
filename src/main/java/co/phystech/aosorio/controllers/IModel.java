/**
 * 
 */
package co.phystech.aosorio.controllers;

import java.util.List;
import java.util.UUID;

import co.phystech.aosorio.exceptions.WrongUserException;
import co.phystech.aosorio.models.User;

/**
 * @author AOSORIO
 *
 */
public interface IModel {

	
	UUID createUser(String username, 
			String role, 
			String password);

	User getUser(String username) throws WrongUserException;
	
	boolean updateUser(String username);
	
	boolean deleteUser(String username);

	List<User> getAllUsers();
	
	boolean userExists(String username);
	
	
}
