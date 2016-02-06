package com.smacna.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smacna.dao.RectDuctRenfDAO;
import com.smacna.exception.SmacnaException;
import com.smacna.util.AppConfigPropertiesUtil;

/**
 * Description
 * @author vishal.joshi
 * @version 1.0
 * @since 2012
 * 
 */
@Service("rectDuctRenfService")
public class RectDuctRenfServiceImpl implements RectDuctRenfService {
	@Autowired
	private RectDuctRenfDAO rectDuctRenfDAO;

	private static final Log logger = LogFactory
			.getLog(RectDuctRenfServiceImpl.class);

	/**
	 * @param int pressureClass
	 * @param int sx
	 * @param int jointSpacing
	 * @param int transverseConnection
	 * @throws SmacnaException
	 */
	@Transactional
	public int getRectDuctRenfForTOne(int pressureClass, int sx,
			int jointSpacing, int transverConnection) throws SmacnaException {
		logger.info("[getRectDuctRenfForTOne]");
		int minimumDuctGage;
		try {
			minimumDuctGage = rectDuctRenfDAO.getRectDuctRenfForTOne(
					pressureClass, sx, jointSpacing, transverConnection);
			return minimumDuctGage;
		} catch (Exception e) {
			logger.error("[getRectDuctRenfForTOne] Error :" + e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}
	}

	@Transactional
	public String getRectDuctRenfForT25(int pressureClass, int sx,
			float jointSpacing, int transverConnection, boolean RSequalsJS)
			throws SmacnaException {
		logger.info("[getRectDuctRenfForT25] : transverConnection = "
				+ transverConnection + " and joint spacing = " + jointSpacing);

		String ductGage = null;
		try {
			ductGage = rectDuctRenfDAO.getRectDuctRenfForT25(pressureClass, sx,
					jointSpacing, transverConnection, RSequalsJS);
			return ductGage;
		} catch (Exception e) {
			logger.error("[getRectDuctRenfForT25] Error :" + e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}
	}

	@Transactional
	public String calculationForTtenToTtwelve(int pressureClass, int sx,
			float jointSpacing, int transverConnection, boolean RSequalsJS)
			throws SmacnaException {
		logger.info("[calculationForTtenToTtwelve] : pressureClass = "
				+ pressureClass + ", sx = " + sx + ", transverConnection = "
				+ transverConnection + ", jointSpacing = " + jointSpacing
				+ " and RSequalsJS = " + RSequalsJS);

		try {
			String ductGage = rectDuctRenfDAO.getRectDuctRenfForT25(
					pressureClass, sx, jointSpacing, transverConnection,
					RSequalsJS);
			return ductGage;
		} catch (Exception e) {
			logger.error("[calculationForTtenToTtwelve] Error :"
					+ e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}

	}

	@Transactional
	public String getGageFromRectDuctRenf(int transverseConnection,
			String reinforClass) throws SmacnaException {
		logger
				.info("[getGageFromRectDuctRenf]: transverConnection = "
						+ transverseConnection + "  and reinforClass = "
						+ reinforClass);
		String ductGage;
		try {
			ductGage = rectDuctRenfDAO.getGageFromRectDuctRenf(
					transverseConnection, reinforClass);
			return ductGage;
		} catch (Exception e) {
			logger.error("[getGageFromRectDuctRenf] Error :" + e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}

	}

	@Transactional
	public String getMidSpanIntermediateReinforcement(String reinforClass)
			throws SmacnaException {
		logger.info("[getMidSpanIntermediateReinforcement]: reinforClass = "
				+ reinforClass);
		try {
			String midSpanIntermediateRI = rectDuctRenfDAO
					.getMidSpanIntermediateReinforcement(reinforClass);
			return midSpanIntermediateRI;
		} catch (Exception e) {
			logger.error("[getMidSpanIntermediateReinforcement] Error :"
					+ e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}
	}

}
