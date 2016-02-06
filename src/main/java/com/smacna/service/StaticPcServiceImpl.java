/**
 *
 */
package com.smacna.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smacna.dao.StaticPcDAO;
import com.smacna.exception.SmacnaException;
import com.smacna.form.StaticPressureClassDTO;
import com.smacna.util.AppConfigPropertiesUtil;

/**
 * Description
 * @author sumit.v
 * @version 1.0
 * @since 2012
 * This class fetches the list of the Pressure Class
 */
public class StaticPcServiceImpl implements StaticPcService {

	@Autowired
	private StaticPcDAO staticPcDAO;

	private static final Log logg = LogFactory
			.getLog(StaticPcServiceImpl.class);

	@Transactional
	public List<StaticPressureClassDTO> getList() throws SmacnaException {
		logg.info("[getList]");
		try {
			List<StaticPressureClassDTO> staPcList = staticPcDAO.getList();
			return staPcList;
		} catch (Exception e) {
			logg.error("[getList] Error :" + e.getMessage());
			throw new SmacnaException(AppConfigPropertiesUtil
					.getMessagesInstance(null).getValue(
							"error.message.RESPONSE_IS_NULL"));
		}
	}
}
