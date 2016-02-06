/**
 *
 */
package com.smacna.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smacna.dao.IntermediateRenfAngleDAO;
import com.smacna.exception.SmacnaException;
import com.smacna.form.IntermediateRenfAngleDTO;
import com.smacna.util.AppConfigPropertiesUtil;

/**
 * @author vishal.joshi
 * @version 1.0
 */
@Service
public class IntermediateRenfAngleServiceImpl implements
		IntermediateRenfAngleService {

	@Autowired
	IntermediateRenfAngleDAO renfAngleDAO;

	private static final Log logger = LogFactory
			.getLog(IntermediateRenfAngleServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.smacna.service.IntermediateRenfAngleService#getIntermediateRenfAngle
	 * (java.lang.String)
	 */
	@Transactional
	public List<IntermediateRenfAngleDTO> getIntermediateRenfAngle(
			String reinforcementClass) throws SmacnaException {
		logger.info("[getIntermediateRenfAngle]: reinforcementClass = "+reinforcementClass);
		List<IntermediateRenfAngleDTO> list;
		try {
			list = renfAngleDAO
					.getIntermediateRenfAngle(reinforcementClass);
			return list;
		} catch (Exception e) {
			logger.error("[getIntermediateRenfAngle] Error :" + e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}
	}

}
