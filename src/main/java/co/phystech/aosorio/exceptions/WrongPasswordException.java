package co.phystech.aosorio.exceptions;

public class WrongPasswordException extends Exception {

	private static final long serialVersionUID = 1583344310214875253L;

	private static String errorMessage = "Wrong password";

	/**
	 * Assign message and error code to the exception
	 * 
	 * @param message
	 *            This message will be save in the exception
	 */
	public WrongPasswordException() {
		super(errorMessage);
	}

}
