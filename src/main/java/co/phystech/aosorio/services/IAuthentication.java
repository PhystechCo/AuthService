/**
 * 
 */
package co.phystech.aosorio.services;

/**
 * @author AOSORIO
 *
 */
public interface IAuthentication {
	
	public void doAuthentication(String pUsername, String pPassword) throws Exception;

	public Object getToken();
	
}
