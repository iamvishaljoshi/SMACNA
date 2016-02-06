package com.smacna.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smacna.dao.MidPanelTieRodDAO;
import com.smacna.exception.SmacnaException;
import com.smacna.util.AppConfigPropertiesUtil;

@Service
public class MidPanelTieRodServiceImpl implements MidPanelTieRodService {

	@Autowired
	MidPanelTieRodDAO tieRodDAO;

	private static final Log logger = LogFactory
			.getLog(MidPanelTieRodServiceImpl.class);

	@Transactional
	public int getNumberOfTieRod(int pressureClass, float jointSpacing,
			int ductGage, int side) throws SmacnaException {
		logger.info("[getNumberOfTieRod]: pressureClass = " + pressureClass
				+ ", jointSpacing = " + jointSpacing + ", ductGage = "
				+ ductGage + ", Side = " + side);
		try {
			int tieRod = tieRodDAO.getNumberOfTieRod(pressureClass,
					jointSpacing, ductGage, side);
			return tieRod;
		} catch (Exception e) {
			logger.error("[getNumberOfTieRod] Error :" + e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}
	}

}
