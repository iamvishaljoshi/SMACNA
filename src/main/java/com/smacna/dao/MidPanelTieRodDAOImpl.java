package com.smacna.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.smacna.exception.SmacnaException;

/**
 * 
 * @author vishal.joshi
 * @version 1.0 
 * This class is use to get the total number of tie rod need to be
 *          used to design the duct
 */
@Repository
public class MidPanelTieRodDAOImpl implements MidPanelTieRodDAO {

	@Autowired
	SessionFactory sessionFactory;

	private static final Log logger = LogFactory
			.getLog(MidPanelTieRodDAOImpl.class);

	/**
	 * @param int pressureClass
	 * @param float jointSpacing
	 * @param int ductGage
	 * @param int side
	 * @throws SmacnaException
	 * @author vishal.joshi
	 */
	@SuppressWarnings("unchecked")
	public int getNumberOfTieRod(int pressureClass, float jointSpacing,
			int ductGage, int side) throws SmacnaException {

		logger.info("[getNumberOfTieRod] : pressureClass = " + pressureClass
				+ ", jointSpacing = " + jointSpacing + ", ductGage = "
				+ ductGage + " and Side = " + side);

		String selectColName = null;
		String colNameOne = null;
		String colNameTwo = null;

		if (ductGage == 16) {
			selectColName = "tieRodSixteen";
			colNameOne = "sixteenGageFrom";
			colNameTwo = "sixteenGageTo";
		} else if (ductGage == 18) {
			selectColName = "tieRodEighteen";
			colNameOne = "eighteenGageFrom";
			colNameTwo = "eighteenGageTo";
		} else if (ductGage == 20) {
			selectColName = "tieRodTwenty";
			colNameOne = "twentyGageFrom";
			colNameTwo = "twentyGageTo";
		} else if (ductGage == 22) {
			selectColName = "tieRodTwentyTwo";
			colNameOne = "twentyTwoGageFrom";
			colNameTwo = "twentyTwoGageTo";
		} else if (ductGage == 24) {
			selectColName = "tieRodTwentyFour";
			colNameOne = "twentyFourGageFrom";
			colNameTwo = "twentyFourGageTo";
		} else if (ductGage == 26) {
			selectColName = "tieRodTwentySix";
			colNameOne = "twentySixGageFrom";
			colNameTwo = "twentySixGageTo";
		}

		logger.info("[getNumberOfTieRod] : selectColName = " + selectColName
				+ ", colNameOne = " + colNameOne + " and colNameTwo = "
				+ colNameTwo);
		Session session = sessionFactory.openSession();
		List<Integer> list = session.createQuery(
				"select " + selectColName
						+ " from MidPanelTieRodDTO where spId = "
						+ pressureClass + " and jointSpacing = " + jointSpacing
						+ " and " + side + " between " + colNameOne + " and "
						+ colNameTwo + "").list();
		session.close();
		if (!list.isEmpty()) {

			for (Integer noOfTieRod : list) {

				logger.info("[getNumberOfTieRod] : noOfTieRod = " + noOfTieRod);
				return noOfTieRod;
			}

		} else {
			return 0;
		}

		return 0;
	}

}
