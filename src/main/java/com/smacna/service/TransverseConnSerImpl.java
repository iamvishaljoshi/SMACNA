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

import com.smacna.dao.TransverseConnectionDAO;
import com.smacna.exception.SmacnaException;
import com.smacna.form.StaticPressureClassDTO;
import com.smacna.form.TransverseConnDTO;
import com.smacna.util.AppConfigPropertiesUtil;

/**
 * Description
 *
 * @author sumit.v
 * @version 1.0
 * @since 2012
 * This class fetches the list of the Transverse Connections
 */
@Service
public class TransverseConnSerImpl implements TransverseConnectionService {

	@Autowired
	TransverseConnectionDAO connectionDAO;

	private static final Log logger = LogFactory
			.getLog(TransverseConnSerImpl.class);

	@Transactional
	public List<TransverseConnDTO> getTcList() throws SmacnaException {
		logger.info("[getTcList]");
		try {
			List<TransverseConnDTO> trList = connectionDAO.getTcList();
			return trList;
		} catch (Exception e) {
			logger.error("[getTcList] Error :" + e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}
	}

	@Transactional
	public List<StaticPressureClassDTO> getStaticPCList()
			throws SmacnaException {
		logger.info("[getStaticPCList]");
		try {
			List<StaticPressureClassDTO> staPcList = connectionDAO
					.getStaticPCList();
			return staPcList;
		} catch (Exception e) {
			logger.error("[getStaticPCList] Error :" + e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}
	}

	@Transactional
	public List<TransverseConnDTO> getSelectTcList(String tcName,
			boolean include) throws SmacnaException {
		logger.info("[getSelectTcList] :  tcName = " + tcName + ", include = "
				+ include);
		try {
			List<TransverseConnDTO> trList = connectionDAO.getSelectTcList(
					tcName, include);
			return trList;
		} catch (Exception e) {
			logger.error("[getSelectTcList] Error :" + e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}
	}

	@Transactional
	public List<TransverseConnDTO> getSelectTcList(int tcId, boolean include)
			throws SmacnaException {
		logger.info("[getSelectTcList] :  tcId = " + tcId + ", include = "
				+ include);
		try {
			List<TransverseConnDTO> trList = connectionDAO.getSelectTcList(
					tcId, include);
			return trList;
		} catch (Exception e) {
			logger.error("[getSelectTcList] Error :" + e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}
	}

	@Transactional
	public float getStaticPC(int id) throws SmacnaException {
		logger.info("[getStaticPC]: id = " + id);
		try {
			StaticPressureClassDTO pressureClassDTO = connectionDAO
					.getStaticPC(id);
			float pc = 0;
			if (pressureClassDTO.getPc().equals("1/2")) {
				pc = 0.5F;
			} else {
				pc = Integer.parseInt(pressureClassDTO.getPc());
			}
			return pc;
		} catch (Exception e) {
			logger.error("[getStaticPC] Error :" + e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}
	}

	/**
	 *
	 * @param boolean checkT5
	 * @return List<TransverseConnDTO>
	 */
	@SuppressWarnings("unchecked")
	public List<TransverseConnDTO> getSelectTcList(boolean checkT5)
			throws SmacnaException {
		logger.info("[getSelectTcList]: checkT5 = " + checkT5);
		try {
			List<TransverseConnDTO> selectTcList = connectionDAO
					.getSelectTcList(checkT5);
			return selectTcList;
		} catch (Exception e) {
			logger.error("[getStaticPC] Error :" + e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}
	}

	/**
	 * @param int id: is a primary key for Transverse connection
	 * @exception : throws SmacnaException
	 * @return String: Will return the Transverse connection name.
	 * @author vishal.joshi
	 */
	public String getTransverseConnName(int id) throws SmacnaException {
		logger.info("[getTransverseConnName]: id = " + id);
		try {
			String transverseConn = connectionDAO.getTransverseConnName(id);
			return transverseConn;
		} catch (Exception e) {
			logger.error("[getStaticPC] Error :" + e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}
	}

	/**
	 *
	 * @param length
	 * @param tieRoadLoad
	 * @return String: Dim
	 */
	@SuppressWarnings("unchecked")
	public String getEmtForNegPC(int length, double tieRoadLoad) throws SmacnaException {
		logger.info("[getEmtForNegPC]: length = " + length + ", tieRoadLoad = "
				+ tieRoadLoad);
		String dim = null;
		try {
			dim = connectionDAO.getEmtForNegPC(length, tieRoadLoad);
		} catch (Exception e) {
			logger.error("[getStaticPC] Error :" + e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}

		return dim;

	}

}
