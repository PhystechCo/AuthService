/**
 * 
 */
package co.phystech.aosorio.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;

import co.phystech.aosorio.config.Constants;
import co.phystech.aosorio.controllers.IModel;
import co.phystech.aosorio.controllers.Sql2oModel;
import co.phystech.aosorio.controllers.SqlController;
import co.phystech.aosorio.exceptions.WrongPasswordException;
import co.phystech.aosorio.exceptions.WrongUserException;
import co.phystech.aosorio.models.User;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

/**
 * @author AOSORIO
 *
 */
public class AuthenticationJWT implements IAuthentication {

	private final static Logger slf4jLogger = LoggerFactory.getLogger(AuthenticationJWT.class);

	private static Object token;

	@Override
	public void doAuthentication(String pUsername, String pPassword) throws Exception {

		try {

			Sql2o sql2o = SqlController.getInstance().getAccess();
			IModel model = new Sql2oModel(sql2o);

			User currentUser = model.getUser(pUsername);

			validatePassword(currentUser, pPassword);

			setToken(createJWT(currentUser));

		} catch (WrongUserException ex) {

			slf4jLogger.info(ex.getMessage());
			throw ex;

		} catch (WrongPasswordException ex) {

			slf4jLogger.info(ex.getMessage());
			throw ex;
		}

	}

	private void validatePassword(User currentUser, String pPassword) throws WrongPasswordException {

		String hash = GeneralSvc.getHash(pPassword, currentUser.getUser_uuid().toString());

		if (currentUser.getHash().equals(hash)) {
			slf4jLogger.info("Valid user");
		} else {
			throw new WrongPasswordException();
		}

	}

	private static String createJWT(User currentUser) {

		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		long expMillis = nowMillis + Constants.TOKEN_LIFETIME;
		Date exp = new Date(expMillis);

		StringBuilder subject = new StringBuilder();
		subject.append(currentUser.getUsername());

		// Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder().setId(currentUser.getUsername());
		builder.setIssuedAt(now);
		builder.setSubject(subject.toString());
		builder.setIssuer(Constants.TOKEN_ISSUER);
		builder.signWith(signatureAlgorithm, currentUser.getUser_uuid().toString());
		builder.setExpiration(exp);
		builder.setAudience(currentUser.getRole());

		return builder.compact();

	}

	public static void validateJWT(User currentUser, String compactJws) throws SignatureException {

		try {

			String pSalt = currentUser.getUser_uuid().toString();
			Jwts.parser().setSigningKey(pSalt).parseClaimsJws(compactJws);
			slf4jLogger.info("Token has a valid signature");

		} catch (SignatureException ex) {
			throw ex;
			
		}

	}

	/**
	 * @param token
	 *         the token to set
	 */
	public static void setToken(Object token) {
		AuthenticationJWT.token = token;
	}

	@Override
	public Object getToken() {
		return AuthenticationJWT.token;
	}

}
