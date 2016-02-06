/**
 *
 */
package com.smacna.dao;

import java.util.List;

import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.smacna.exception.SmacnaException;
import com.smacna.form.StaticPressureClassDTO;

/**
 * Description
 * @author sumit.v
 * @version 1.0
 * @since 2012
 */
@Repository
@PersistenceContext
public class StaticPcDAOImpl implements StaticPcDAO {

	@Autowired
	private SessionFactory sessionFactory;

	private static final Log logger = LogFactory.getLog(StaticPcDAOImpl.class);

	@SuppressWarnings("unchecked")
	/**
	 * @return List<StaticPressureClassDTO>
	 * @throws SmacnaException
	 */
	public List<StaticPressureClassDTO> getList() throws SmacnaException {
		logger.info("[getList]");
		Session session = sessionFactory.openSession();
		List<StaticPressureClassDTO> staPcList = session.createQuery(
				"FROM StaticPressureClassDTO WHERE unit = 1").list();
		session.close();
		return staPcList;
	}

}
