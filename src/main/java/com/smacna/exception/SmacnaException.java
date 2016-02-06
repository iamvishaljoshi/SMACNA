package com.smacna.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is used as exception class of Smacna application.
 *
 * @author vishal.joshi
 * @version 1.0
 */
public class SmacnaException extends Exception {

	/**
         *
         */
	private static final long serialVersionUID = 1L;
	private static final Log logg = LogFactory.getLog(SmacnaException.class);
	/**
	 * the message of the TopEJBException.
	 */
	private String message;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * A public constructor for SmacnaException specifying exception message.
	 *
	 * @param msg
	 *            exception message.
	 */
	public SmacnaException(String msg) {
		logg.info("[Constructor]: (msg: " + msg + ")");
		this.message = msg;
	}
	/**
	 * A public constructor of <code>SmacnaException</code> containing message
	 * and root cause (as <code>Throwable</code>) of the exception.
	 *
	 * @param msg
	 *            exception message.
	 * @param e
	 *            Throwable object.
	 *
	 */
	public SmacnaException(String msg, Throwable e) {
		this.message = msg;
		this.initCause(e);
	}

	/**
	 * Gets the class name and exception message.
	 *
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s = getClass().getName();
		return s + ": " + message;
	}

}
