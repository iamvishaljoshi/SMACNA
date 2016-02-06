/**
 *
 */
package com.smacna.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.smacna.exception.SmacnaException;
import com.smacna.form.IntermediateRenfAngleDTO;

/**
 * @author vishal.joshi
 * @version 1.0
 * @since 2012
 * 
 * This class is use to know intermediate reinforcement for constructing the duct.
 */
@Repository
public class IntermediateRenfAngleDAOImpl implements IntermediateRenfAngleDAO {

	@Autowired
	private SessionFactory sessionFactory;

	private static final Log logger = LogFactory
			.getLog(IntermediateRenfAngleDAOImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.smacna.dao.IntermediateRenfAngleDAO#getIntermediateRenfAngle()
	 */
	/**
	 * @param String reinforcementClass input and its length should not be 0
	 * @return List<IntermediateRenfAngleDTO> 
	 * @throws SmacnaException
	 * @author vishal.joshi
	 */
	@SuppressWarnings("unchecked")
	public List<IntermediateRenfAngleDTO> getIntermediateRenfAngle(
			String reinforcementClass) throws SmacnaException{
		logger.info("[getIntermediateRenfAngle]");
		Session session = sessionFactory.openSession();
		List<IntermediateRenfAngleDTO> intermediateRenfAngleList = null;
		intermediateRenfAngleList = session.createQuery(
				"from IntermediateRenfAngleDTO").list();
		session.close();
		return intermediateRenfAngleList;
	}

}
