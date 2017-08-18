/**
 * 
 */
package co.phystech.aosorio.config;

/**
 * @author AOSORIO
 *
 */
public class Constants {

	public static final String CONFIG_FILE = "src/main/resources/config.properties";
	
	public static final int HTTP_BAD_REQUEST = 400;
	
	public static final String TOKEN_ISSUER = "phystech.co";
	//public static final long TOKEN_LIFETIME = 1000 * 1800; // 30 minutes
	public static final long TOKEN_LIFETIME = 604800000; // 1 week
	
}
