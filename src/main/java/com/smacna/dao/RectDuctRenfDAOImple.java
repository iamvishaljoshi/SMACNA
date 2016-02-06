package com.smacna.dao;

import java.util.List;

import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smacna.exception.SmacnaException;

/**
 * Description
 * @author vishal.joshi
 * @version 1.0
 * @since 2012
 */
@Repository("rectDuctRenfDAO")
@PersistenceContext
public class RectDuctRenfDAOImple implements RectDuctRenfDAO {

	@Autowired
	private SessionFactory sessionFactory;
	private static final Log logger = LogFactory
			.getLog(RectDuctRenfDAOImple.class);

	/**
	 * @param int pressureClass
	 * @param int jointSpacing
	 * @param int transverConnection
	 * @param int sx
	 * @return int noRenForTFive
	 * @throws SmacnaException
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public int getRectDuctRenfForTOne(int pressureClass, int sx,
			int jointSpacing, int transverConnection) throws SmacnaException {
		logger.info("[getRectDuctRenfForTOne]");
		if (transverConnection == 1) {

			if (jointSpacing == 6) {
				logger
						.debug("[getRectDuctRenfForTOne] if block: transverConnection = "
								+ transverConnection + " and joint spacing = 6");
				Session session = sessionFactory.openSession();
				List<Integer> list = session
						.createQuery(
								"select noRenForTOneJSVI from RectDuctRenfDTO where "
										+ sx
										+ " between ductDimOne and ductDimTwo and spId = "
										+ pressureClass + " ").list();
				session.close();

				if (!list.isEmpty()) {

					for (Integer noRenForTOneJSVI : list) {

						return noRenForTOneJSVI;
					}

				} else {
					return 0;
				}

			} else if (jointSpacing == 5 || jointSpacing == 4) {
				logger
						.debug("[getRectDuctRenfForTOne] if block: transverConnection = "
								+ transverConnection
								+ " and joint spacing = "
								+ jointSpacing);
				Session session = sessionFactory.openSession();
				List<Integer> list = session
						.createQuery(
								"select noRenTOneJSVAndIV from RectDuctRenfDTO where "
										+ sx
										+ " between ductDimOne and ductDimTwo and spId = "
										+ pressureClass + " ").list();
				session.close();
				if (!list.isEmpty()) {

					for (Integer noRenTOneJSVAndIV : list) {

						return noRenTOneJSVAndIV;
					}

				} else {
					return 0;
				}
			}
		} else if (transverConnection == 2) {

			logger
					.debug("[getRectDuctRenfForTOne] if block: transverConnection = "
							+ transverConnection
							+ " and joint spacing = "
							+ jointSpacing);
			Session session = sessionFactory.openSession();
			List<Integer> list = session.createQuery(
					"select noRenForTFive from RectDuctRenfDTO where " + sx
							+ " between ductDimOne and ductDimTwo and spId = "
							+ pressureClass + " ").list();
			session.close();
			if (!list.isEmpty()) {

				for (Integer noRenForTFive : list) {

					return noRenForTFive;
				}

			} else {
				return 0;
			}
		}
		return 0;
	}

	/**
	 * @param int pressureClass
	 * @param float jointSpacing
	 * @param int transverConnection
	 * @param int sx
	 * @param boolean RSequalsJS
	 * @return String ductGage
	 * @throws SmacnaException
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String calculationForTtenToTtwelve(int pressureClass, int sx,
			float jointSpacing, int transverConnection, boolean RSequalsJS)
			throws SmacnaException {
		logger
				.debug("[calculationForTtenToTtwelve] if block: transverConnection = "
						+ transverConnection
						+ " and joint spacing = "
						+ jointSpacing);
		String selectColumn = null;
		if (RSequalsJS) {

			if (jointSpacing == 6) {
				selectColumn = "sixFt";
			} else if (jointSpacing == 5) {
				selectColumn = "fiveFt";
			} else if (jointSpacing == 4) {
				selectColumn = "fourFt";
			}

		} else {

			if (jointSpacing == 3) {
				selectColumn = "threeFt";
			} else if (jointSpacing == 2.5) {
				selectColumn = "twoAnHalfFt";
			} else if (jointSpacing == 2) {
				selectColumn = "twoFt";
			}

		}
		Session session = sessionFactory.openSession();
		List<String> list = session.createQuery(
				"select " + selectColumn + " from RectDuctRenfDTO where " + sx
						+ " between ductDimOne and ductDimTwo and spId = "
						+ pressureClass + " ").list();
		session.close();
		if (!list.isEmpty()) {

			for (String ductGage : list) {

				return ductGage;
			}

		} else {
			return null;
		}

		return null;

	}

	/**
	 * @param int pressureClass
	 * @param float jointSpacing
	 * @param int transverConnection
	 * @param int sx
	 * @param boolean RSequalsJS
	 * @return String ductGage
	 * @throws SmacnaException
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String getRectDuctRenfForT25(int pressureClass, int sx,
			float jointSpacing, int transverConnection, boolean RSequalsJS)
			throws SmacnaException {
		logger.info("[getRectDuctRenfForT25] if block: transverConnection = "
				+ transverConnection + " and joint spacing = " + jointSpacing);
		String selectColumn = null;
		if (RSequalsJS) {

			if (jointSpacing == 6) {
				selectColumn = "sixFt";
			} else if (jointSpacing == 5) {
				selectColumn = "fiveFt";
			} else if (jointSpacing == 4) {
				selectColumn = "fourFt";
			}

		} else {

			if (jointSpacing == 3) {
				selectColumn = "threeFt";
			} else if (jointSpacing == 2.5) {
				selectColumn = "twoAnHalfFt";
			} else if (jointSpacing == 2) {
				selectColumn = "twoFt";
			}

		}
		Session session = sessionFactory.openSession();
		List<String> list = session.createQuery(
				"select " + selectColumn + " from RectDuctRenfDTO where " + sx
						+ " between ductDimOne and ductDimTwo and spId = "
						+ pressureClass + " ").list();
		session.close();
		if (!list.isEmpty()) {

			for (String ductGage : list) {

				return ductGage;
			}

		} else {
			return null;
		}

		return null;

	}

	/**
	 * @param int transverConnection
	 * @param String reinforClass
	 * @return String ductGage
	 * @throws SmacnaException
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String getGageFromRectDuctRenf(int transverseConnection,
			String reinforClass) throws SmacnaException {
		logger.info("[getGageFromRectDuctRenf] : transverConnection = "
				+ transverseConnection);

		String selectColumn = null;

		if (transverseConnection == 3) {
			selectColumn = "t10HT";
		} else if (transverseConnection == 4) {
			selectColumn = "t11HT";
		} else if (transverseConnection == 5) {
			selectColumn = "t12HT";
		} else if (transverseConnection == 6) {
			selectColumn = "t25abHT";
		}
		Session session = sessionFactory.openSession();
//		List<String> list = session
//				.createQuery(
//						"select "
//								+ selectColumn
//								+ " from TransverseJointRenfDTO where t25abTieRod != 1 and renfClass = '"
//								+ reinforClass + "'").list();
		
		List<String> list = session
		.createQuery(
				"select "
						+ selectColumn
						+ " from TransverseJointRenfDTO where renfClass = '"
						+ reinforClass + "'").list();
		session.close();
		if (!list.isEmpty()) {

			for (String ductGage : list) {

				return ductGage;
			}

		} else {
			return null;
		}
		return null;
	}

	/**
	 * @param String reinforClass
	 * @return String midSpanInterMediateRI
	 * @throws SmacnaException
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String getMidSpanIntermediateReinforcement(String reinforClass)
			throws SmacnaException {
		logger.info("[getMidSpanIntermediateReinforcement] : reinforClass = "
				+ reinforClass);
		Session session = sessionFactory.openSession();
		List<String> list = session.createQuery(
				"select angleHHT from IntermediateRenfAngleDTO where reinfoClass = '"
						+ reinforClass + "'").list();
		session.close();
		if (!list.isEmpty()) {

			for (String midSpanInterMediateRI : list) {

				return midSpanInterMediateRI;
			}

		} else {
			return null;
		}
		return null;
	}
}
