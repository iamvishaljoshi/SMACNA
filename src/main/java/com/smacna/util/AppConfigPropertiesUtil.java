package com.smacna.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Short one line description.
 *
 * This class is an utility for reading the properties files.
 *
 * @author vishal.joshi
 * @version 1.0
 *
 */
public class AppConfigPropertiesUtil {
	static final long serialVersionUID = -5829545398534131401L;

	private static AppConfigPropertiesUtil constantsInstance;
	private static AppConfigPropertiesUtil messagesInstance;
	private Properties propertyMap = null;
	private static final Log log = LogFactory
	        .getLog(AppConfigPropertiesUtil.class);
	public static final String CONSTANTS_FILE_NAME = "constants.properties";
	public static final String MESSAGE_FILE_NAME = "emailMessages_en.properties";
	public static final String DUTCH_MESSAGE_FILE_NAME = "messageResource_nl.properties";

	public synchronized static AppConfigPropertiesUtil getConstantsInstance() {
		log.debug("AppConfigPropertiesUtil  : ");
		log.debug("[getConstantsInstance]  : ");
		if (constantsInstance == null) {
			constantsInstance = new AppConfigPropertiesUtil(CONSTANTS_FILE_NAME);
		}
		log.debug("[getConstantsInstance] constantsInstance fetched ");
		log.debug("||[getConstantsInstance] return constantsInstance : " + constantsInstance + " ||");
		return constantsInstance;
	}

	public synchronized static AppConfigPropertiesUtil getMessagesInstance(String locale) {
		log.debug("AppConfigPropertiesUtil  : ");
		log.debug("[getMessagesInstance]  : ");
		String fileName = MESSAGE_FILE_NAME;
		if(locale.equals("nl")){
			fileName = DUTCH_MESSAGE_FILE_NAME;
		}
		if (messagesInstance == null) {
			messagesInstance = new AppConfigPropertiesUtil(fileName);
		}
		log.debug("[getMessagesInstance] messagesInstance fetched ");
		log.debug("||[getMessagesInstance] return messagesInstance : " + messagesInstance + " ||");
		return messagesInstance;
	}

	private AppConfigPropertiesUtil(String configFileName) {
		log.debug("[AppConfigPropertiesUtil] : " + configFileName);
		initInternal(configFileName);
	}

	// internal init - loading properties from resource
	private void initInternal(String newConfigFileName) {
		log.debug("[initInternal] : " + newConfigFileName);
		if (propertyMap == null) {
			log.debug("[initInternal] propertyMap is NULL");
			propertyMap = new Properties();
		}
		loadPropMapFromResource(newConfigFileName, propertyMap);
	}

	/**
	 * Load the properties file specified by the resource argument into the
	 * propertyMap.
	 *
	 * @param resource
	 * @param propertyMap
	 */
	private void loadPropMapFromResource(String resource, Properties propertyMap) {
		log.debug("[loadPropMapFromResource]  resource : " + resource.toString()
				+ " properties : " + propertyMap);
		// FileInputStream input = null;
		InputStream input = null;
		try {
			input = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(resource);
			log.debug("[loadPropMapFromResource] : INPUT : " + input);
			if (input == null) {
				File f = new File(resource);
				if (f.exists()) {
					FileInputStream in = new FileInputStream(f);
					propertyMap.load(in);
					log.debug("All key in property file: "
							+ propertyMap.keySet());
					log.debug("[loadPropMapFromResource] property map loaded .....");
				}
			} else {
				propertyMap.load(input);
			}
			propertyMap = null;

		} catch (IOException ioex) {
			log.error(ioex.getMessage(), ioex);
			throw new RuntimeException(ioex.getMessage(), ioex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException ioex) {
					log.error(ioex.getMessage(), ioex);
				}
			}
		}
	}

	/**
	 * @param key
	 * @return property value with specific key
	 */
	public String getValue(String key) {
		return propertyMap.getProperty(key);
	}

	public String getValue(String key, String defaultValue) {
		return propertyMap.getProperty(key, defaultValue);
	}

	public int getIntValue(String key) {
		return Integer.valueOf(propertyMap.getProperty(key)).intValue();
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		log.debug("[toString] override toString method");
		return "AppConfigPropertiesUtil [This class is used for reading property files, The object's class serialVersionUID =" + serialVersionUID + "] ";
	}

	/*
	private static AppConfigPropertiesUtil msgInstance;
	private static AppConfigPropertiesUtil propInstance;
	private static AppConfigPropertiesUtil contractInstance;
	private static AppConfigPropertiesUtil exceptionInstance;
	public static final String CERT_FILE_NAME = "certificate.properties";
	public static final String CONTRACT_FILE_NAME = "contract.properties";
	public static final String EXCEPTION_FILE_NAME = "exception.properties";


	// get instance of this class and loading all the properties from specified
	// configuration file.
	public synchronized static AppConfigPropertiesUtil getMessageInstance() {
		log.debug("AppConfigPropertiesUtil  : ");
		log.debug("[getMessageInstance]  : ");
		if (msgInstance == null) {
			msgInstance = new AppConfigPropertiesUtil(CERT_FILE_NAME);
		}
		log.debug("[getMessageInstance] msgInstance fetched ");
		log.debug("||[getMessageInstance] return msgInstance : " + msgInstance + " ||");
		return msgInstance;
	}

	public synchronized static AppConfigPropertiesUtil getContractInstance() {
		log.debug("[getContractInstance]  : ");
		if (contractInstance == null) {
			contractInstance = new AppConfigPropertiesUtil(CONTRACT_FILE_NAME);
		}
		log.debug("||[getContractInstance] return contractInstance : " + contractInstance + " ||");
		return contractInstance;
	}

	public synchronized static AppConfigPropertiesUtil getExceptionInstance() {
		log.debug("[getExceptionInstance]  : ");
		if (exceptionInstance == null) {
			exceptionInstance = new AppConfigPropertiesUtil(EXCEPTION_FILE_NAME);
		}
		log.debug("|| [getExceptionInstance] return exceptionInstance : " + exceptionInstance + " ||");
		return exceptionInstance;
	}

	public synchronized static AppConfigPropertiesUtil getPropInstance(
			String filePath) {
		log.debug("[getPropInstance]  : " + filePath);
		if (propInstance == null) {
			propInstance = new AppConfigPropertiesUtil(filePath);
		}
		log.debug("[getPropInstance] propInstance fetched ");
		log.debug("|| [getPropInstance] return propInstance : " + propInstance + " ||");
		return propInstance;
	}*/






}
