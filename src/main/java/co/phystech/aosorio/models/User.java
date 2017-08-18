/**
 * 
 */
package co.phystech.aosorio.models;

import java.sql.Date;
import java.util.UUID;

/**
 * @author AOSORIO
 *
 */
public class User {

	UUID user_uuid;
	String username;
	String hash;
	String role;
	String affiliation;
	Date date_added;

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}
	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}
	/**
	 * @return the user_uuid
	 */
	public UUID getUser_uuid() {
		return user_uuid;
	}
	/**
	 * @param user_uuid the user_uuid to set
	 */
	public void setUser_uuid(UUID user_uuid) {
		this.user_uuid = user_uuid;
	}
	/**
	 * @return the affiliation
	 */
	public String getAffiliation() {
		return affiliation;
	}
	/**
	 * @param affiliation the affiliation to set
	 */
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
	/**
	 * @return the date_added
	 */
	public Date getDate_added() {
		return date_added;
	}
	/**
	 * @param date_added the date_added to set
	 */
	public void setDate_added(Date date_added) {
		this.date_added = date_added;
	}

	
	
}
