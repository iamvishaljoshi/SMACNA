/**
 *
 */
package com.smacna.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smacna.exception.SmacnaException;
import com.smacna.form.StaticPressureClassDTO;
import com.smacna.form.TransverseConnDTO;

/**
 * Description
 *
 * @author vishal.joshi
 * @version 1.0
 * @since 2012
 */
@Repository
public class TransverseConnectionDAOImpl implements TransverseConnectionDAO {

	@Autowired
	private SessionFactory sessionFactory;

	private static final Log logger = LogFactory
			.getLog(TransverseConnectionDAOImpl.class);

	/**
	 * @return List<TransverseConnDTO>
	 * @throws SmacnaException
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<TransverseConnDTO> getTcList() throws SmacnaException {
		logger.info("getTcList");
		List<TransverseConnDTO> trasverseList = null;
		try {
			if (sessionFactory != null) {
				Session session = sessionFactory.openSession();
				if (session != null) {
					trasverseList = session.createCriteria(
							TransverseConnDTO.class).list();
					if (trasverseList != null) {
						session.close();
					} else
						System.out.println("List coming null");
				} else
					System.out.print("session object coming null");
			} else
				System.out.print("sessionFactory object coming null");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return trasverseList;
	}

	/**
	 * @return List<TransverseConnDTO> will return the list of Static pressure
	 *         Class
	 * @throws SmacnaException
	 */
	@SuppressWarnings("unchecked")
	public List<StaticPressureClassDTO> getStaticPCList()
			throws SmacnaException {
		logger.info("[getStaticPCList]");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(StaticPressureClassDTO.class);
		crit.createCriteria("unitId").add(Restrictions.eq("id", 1));
		List<StaticPressureClassDTO> staPcList = crit.list();
		session.close();
		return staPcList;
	}

	/**
	 * @param int id input integer which cannot be 0.
	 * @return StaticPressureClassDTO will return the object based on its
	 *         primary key
	 * @throws SmacnaException
	 */
	public StaticPressureClassDTO getStaticPC(int id) throws SmacnaException {
		logger.info("[getStaticPC]: id " + id);
		Session session = sessionFactory.openSession();
		StaticPressureClassDTO pressureClassDTO = (StaticPressureClassDTO) session
				.get(StaticPressureClassDTO.class, id);
		session.close();
		return pressureClassDTO;
	}

	/**
	 * @param String
	 *            tcName input source string which cannot be 0-length.
	 * @param boolean include
	 * @return List<TransverseConnDTO>
	 */
	@SuppressWarnings("unchecked")
	public List<TransverseConnDTO> getSelectTcList(String tcName,
			boolean include) throws SmacnaException {
		logger.info("[getSelectTcList]: tcName " + tcName + ", include = "
				+ include);
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(TransverseConnDTO.class);
		List<TransverseConnDTO> selectTcList = null;
		if (include) {
			selectTcList = crit.add(Restrictions.eq("tc", tcName)).list();
		} else {
			selectTcList = crit.add(Restrictions.ne("tc", tcName)).list();
		}
		session.close();
		return selectTcList;
	}

	/**
	 * @param int tcId input source string which cannot be 0.
	 * @param boolean include
	 * @return List<TransverseConnDTO>
	 */
	@SuppressWarnings("unchecked")
	public List<TransverseConnDTO> getSelectTcList(int tcId, boolean include)
			throws SmacnaException {
		logger.info("[getSelectTcList]: tcId " + tcId + ", include = "
				+ include);
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(TransverseConnDTO.class);
		List<TransverseConnDTO> selectTcList = null;
		if (include) {
			selectTcList = crit.add(Restrictions.eq("id", tcId)).list();
		} else {
			selectTcList = crit.add(Restrictions.ne("id", tcId)).list();
		}
		session.close();
		return selectTcList;
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
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(TransverseConnDTO.class);
		List<TransverseConnDTO> selectTcList = null;
		if (checkT5) {
			selectTcList = crit.add(Restrictions.eq("id", 1)).list();
		} else {
			crit = crit.add(Restrictions.or(Property.forName("id").eq(1),
					Property.forName("id").eq(2)));
			// crit.add(Restrictions.disjunction().add(
			// Restrictions.eq("name", "A")).add(
			// Restrictions.eq("name", "B")).add(
			// Restrictions.eq("name", "C")));
			selectTcList = crit.list();
		}
		session.close();
		return selectTcList;
	}

	/**
	 * @param int id: is a primary key for Transverse connection
	 * @exception : throws SmacnaException
	 * @return String: Will return the Transverse connection name.
	 * @author vishal.joshi
	 */
	@SuppressWarnings("unchecked")
	public String getTransverseConnName(int id) throws SmacnaException {
		logger.info("[getTransverseConnName]: id = " + id);
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(TransverseConnDTO.class);
		String tc = null;
		List<TransverseConnDTO> selectTcList = null;
		selectTcList = crit.add(Restrictions.eq("id", id)).list();
		for (TransverseConnDTO transverseConnDTO : selectTcList) {
			tc = transverseConnDTO.getTc();
		}
		session.close();
		return tc;
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
		Session session = sessionFactory.openSession();

		List<Object[]> list = session.createQuery(
				"select min(id), dim from InternalEMTCondSizeDTO where "
						+ length + " <= length and " + tieRoadLoad + " <=lbs")
				.list();
		if (!list.isEmpty()) {
			logger.info("[getEmtForNegPC]: in If condition");
			for (Object[] objects : list) {
				logger.info("[getEmtForNegPC]: for loop");
				dim = String.valueOf(objects[1]);
			}

		}
		session.close();
		return dim;

	}

}
