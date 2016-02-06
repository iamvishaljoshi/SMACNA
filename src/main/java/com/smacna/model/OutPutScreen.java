package com.smacna.model;

import java.io.Serializable;

/**
 *
 * @author vishal.joshi
 * @version 1.0
 */
public class OutPutScreen implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private int sideOne;

	private int sideTwo;

	private int minDuctGageSOne;

	private int minDuctGageSTwo;

	private int finalDuctGage;

	private String longitudinalSeam = null;

	private boolean externalReinforcement;

	private String reinforcementClass;

	private String reinClassForSideOne;

	private String reinClassForSideTwo;

	private String externalRenfAngle;

	private String extRenfAngleSideOne;

	private String extRenfAngleSideTwo;

	private boolean internalReinforcement;

	private boolean externalBothSide;

	private boolean internalBothSide;

	private boolean extAndInternalEachSide;

	private boolean intAndExtEachSide;

	private int noTieRodForSideOne;

	private int noTieRodForSideTwo;

	private double tieRodLoadForSideOne;

	private double tieRodLoadForSideTwo;

	private String tieRod;

	private String tieRodForSideOne;

	private String tieRodForSideTwo;

	private boolean jtrForSideOne;

	private boolean jtrForSideTwo;

	private String ductGageForSideOne;

	private String ductGageForSideTwo;

	private double tieRoadLoadJTRForSideOne;

	private double tieRoadLoadJTRForSideTwo;

	private boolean intAndExternalOnBothSide;

	private boolean intAndExtOnSideOne;

	private boolean intAndExtOnSideTwo;

	private String jtrSizeForS1;

	private String jtrSizeForS2;

	private int internalScore;

	private int externalScore;

	private int score;

	private int priority;

	/**
	 * @return the jtrSizeForS1
	 */
	public String getJtrSizeForS1() {
		return jtrSizeForS1;
	}

	/**
	 * @param jtrSizeForS1
	 *            the jtrSizeForS1 to set
	 */
	public void setJtrSizeForS1(String jtrSizeForS1) {
		this.jtrSizeForS1 = jtrSizeForS1;
	}

	/**
	 * @return the jtrSizeForS2
	 */
	public String getJtrSizeForS2() {
		return jtrSizeForS2;
	}

	/**
	 * @param jtrSizeForS2
	 *            the jtrSizeForS2 to set
	 */
	public void setJtrSizeForS2(String jtrSizeForS2) {
		this.jtrSizeForS2 = jtrSizeForS2;
	}

	/**
	 * @return the intAndExtOnSideOne
	 */
	public boolean isIntAndExtOnSideOne() {
		return intAndExtOnSideOne;
	}

	/**
	 * @param intAndExtOnSideOne
	 *            the intAndExtOnSideOne to set
	 */
	public void setIntAndExtOnSideOne(boolean intAndExtOnSideOne) {
		this.intAndExtOnSideOne = intAndExtOnSideOne;
	}

	/**
	 * @return the intAndExtOnSideTwo
	 */
	public boolean isIntAndExtOnSideTwo() {
		return intAndExtOnSideTwo;
	}

	/**
	 * @param intAndExtOnSideTwo
	 *            the intAndExtOnSideTwo to set
	 */
	public void setIntAndExtOnSideTwo(boolean intAndExtOnSideTwo) {
		this.intAndExtOnSideTwo = intAndExtOnSideTwo;
	}

	/**
	 * @return the intAndExternalOnBothSide
	 */
	public boolean isIntAndExternalOnBothSide() {
		return intAndExternalOnBothSide;
	}

	/**
	 * @param intAndExternalOnBothSide
	 *            the intAndExternalOnBothSide to set
	 */
	public void setIntAndExternalOnBothSide(boolean intAndExternalOnBothSide) {
		this.intAndExternalOnBothSide = intAndExternalOnBothSide;
	}

	/**
	 * @return the extRenfAngleSideOne
	 */
	public String getExtRenfAngleSideOne() {
		return extRenfAngleSideOne;
	}

	/**
	 * @param extRenfAngleSideOne
	 *            the extRenfAngleSideOne to set
	 */
	public void setExtRenfAngleSideOne(String extRenfAngleSideOne) {
		this.extRenfAngleSideOne = extRenfAngleSideOne;
	}

	/**
	 * @return the extRenfAngleSideTwo
	 */
	public String getExtRenfAngleSideTwo() {
		return extRenfAngleSideTwo;
	}

	/**
	 * @param extRenfAngleSideTwo
	 *            the extRenfAngleSideTwo to set
	 */
	public void setExtRenfAngleSideTwo(String extRenfAngleSideTwo) {
		this.extRenfAngleSideTwo = extRenfAngleSideTwo;
	}

	/**
	 * @return the tieRoadLoadJTRForSideOne
	 */
	public double getTieRoadLoadJTRForSideOne() {
		return tieRoadLoadJTRForSideOne;
	}

	/**
	 * @param tieRoadLoadJTRForSideOne
	 *            the tieRoadLoadJTRForSideOne to set
	 */
	public void setTieRoadLoadJTRForSideOne(double tieRoadLoadJTRForSideOne) {
		this.tieRoadLoadJTRForSideOne = tieRoadLoadJTRForSideOne;
	}

	/**
	 * @return the tieRoadLoadJTRForSideTwo
	 */
	public double getTieRoadLoadJTRForSideTwo() {
		return tieRoadLoadJTRForSideTwo;
	}

	/**
	 * @param tieRoadLoadJTRForSideTwo
	 *            the tieRoadLoadJTRForSideTwo to set
	 */
	public void setTieRoadLoadJTRForSideTwo(double tieRoadLoadJTRForSideTwo) {
		this.tieRoadLoadJTRForSideTwo = tieRoadLoadJTRForSideTwo;
	}

	/**
	 * @return the ductGageForSideOne
	 */
	public String getDuctGageForSideOne() {
		return ductGageForSideOne;
	}

	/**
	 * @param ductGageForSideOne
	 *            the ductGageForSideOne to set
	 */
	public void setDuctGageForSideOne(String ductGageForSideOne) {
		this.ductGageForSideOne = ductGageForSideOne;
	}

	/**
	 * @return the ductGageForSideTwo
	 */
	public String getDuctGageForSideTwo() {
		return ductGageForSideTwo;
	}

	/**
	 * @param ductGageForSideTwo
	 *            the ductGageForSideTwo to set
	 */
	public void setDuctGageForSideTwo(String ductGageForSideTwo) {
		this.ductGageForSideTwo = ductGageForSideTwo;
	}

	/**
	 * @return the reinClassForSideOne
	 */
	public String getReinClassForSideOne() {
		return reinClassForSideOne;
	}

	/**
	 * @param reinClassForSideOne
	 *            the reinClassForSideOne to set
	 */
	public void setReinClassForSideOne(String reinClassForSideOne) {
		this.reinClassForSideOne = reinClassForSideOne;
	}

	/**
	 * @return the reinClassForSideTwo
	 */
	public String getReinClassForSideTwo() {
		return reinClassForSideTwo;
	}

	/**
	 * @param reinClassForSideTwo
	 *            the reinClassForSideTwo to set
	 */
	public void setReinClassForSideTwo(String reinClassForSideTwo) {
		this.reinClassForSideTwo = reinClassForSideTwo;
	}

	/**
	 * @return the tieRodForSideOne
	 */
	public String getTieRodForSideOne() {
		return tieRodForSideOne;
	}

	/**
	 * @param tieRodForSideOne
	 *            the tieRodForSideOne to set
	 */
	public void setTieRodForSideOne(String tieRodForSideOne) {
		this.tieRodForSideOne = tieRodForSideOne;
	}

	/**
	 * @return the tieRodForSideTwo
	 */
	public String getTieRodForSideTwo() {
		return tieRodForSideTwo;
	}

	/**
	 * @param tieRodForSideTwo
	 *            the tieRodForSideTwo to set
	 */
	public void setTieRodForSideTwo(String tieRodForSideTwo) {
		this.tieRodForSideTwo = tieRodForSideTwo;
	}

	/**
	 * @return the jtrForSideOne
	 */
	public boolean isJtrForSideOne() {
		return jtrForSideOne;
	}

	/**
	 * @param jtrForSideOne
	 *            the jtrForSideOne to set
	 */
	public void setJtrForSideOne(boolean jtrForSideOne) {
		this.jtrForSideOne = jtrForSideOne;
	}

	/**
	 * @return the jtrForSideTwo
	 */
	public boolean isJtrForSideTwo() {
		return jtrForSideTwo;
	}

	/**
	 * @param jtrForSideTwo
	 *            the jtrForSideTwo to set
	 */
	public void setJtrForSideTwo(boolean jtrForSideTwo) {
		this.jtrForSideTwo = jtrForSideTwo;
	}

	private String extRenfSide;

	private boolean noRenforcement;

	/**
	 * @return the externalBothSide
	 */
	public boolean isExternalBothSide() {
		return externalBothSide;
	}

	/**
	 * @param externalBothSide
	 *            the externalBothSide to set
	 */
	public void setExternalBothSide(boolean externalBothSide) {
		this.externalBothSide = externalBothSide;
	}

	/**
	 * @return the internalBothSide
	 */
	public boolean isInternalBothSide() {
		return internalBothSide;
	}

	/**
	 * @param internalBothSide
	 *            the internalBothSide to set
	 */
	public void setInternalBothSide(boolean internalBothSide) {
		this.internalBothSide = internalBothSide;
	}

	/**
	 * @return the extAndInternalEachSide
	 */
	public boolean isExtAndInternalEachSide() {
		return extAndInternalEachSide;
	}

	/**
	 * @param extAndInternalEachSide
	 *            the extAndInternalEachSide to set
	 */
	public void setExtAndInternalEachSide(boolean extAndInternalEachSide) {
		this.extAndInternalEachSide = extAndInternalEachSide;
	}

	/**
	 * @return the intAndExtEachSide
	 */
	public boolean isIntAndExtEachSide() {
		return intAndExtEachSide;
	}

	/**
	 * @param intAndExtEachSide
	 *            the intAndExtEachSide to set
	 */
	public void setIntAndExtEachSide(boolean intAndExtEachSide) {
		this.intAndExtEachSide = intAndExtEachSide;
	}

	/**
	 * @return the noRenforcement
	 */
	public boolean isNoRenforcement() {
		return noRenforcement;
	}

	/**
	 * @param noRenforcement
	 *            the noRenforcement to set
	 */
	public void setNoRenforcement(boolean noRenforcement) {
		this.noRenforcement = noRenforcement;
	}

	/**
	 * @return the tieRodLoadForSideTwo
	 */
	public double getTieRodLoadForSideTwo() {
		return tieRodLoadForSideTwo;
	}

	/**
	 * @param tieRodLoadForSideTwo
	 *            the tieRodLoadForSideTwo to set
	 */
	public void setTieRodLoadForSideTwo(double tieRodLoadForSideTwo) {
		this.tieRodLoadForSideTwo = tieRodLoadForSideTwo;
	}

	/**
	 * @return the extRenfSide
	 */
	public String getExtRenfSide() {
		return extRenfSide;
	}

	/**
	 * @param extRenfSide
	 *            the extRenfSide to set
	 */
	public void setExtRenfSide(String extRenfSide) {
		this.extRenfSide = extRenfSide;
	}

	/**
	 * @return the sideOne
	 */
	public int getSideOne() {
		return sideOne;
	}

	/**
	 * @param sideOne
	 *            the sideOne to set
	 */
	public void setSideOne(int sideOne) {
		this.sideOne = sideOne;
	}

	/**
	 * @return the sideTwo
	 */
	public int getSideTwo() {
		return sideTwo;
	}

	/**
	 * @param sideTwo
	 *            the sideTwo to set
	 */
	public void setSideTwo(int sideTwo) {
		this.sideTwo = sideTwo;
	}

	/**
	 * @return the minDuctGageSOne
	 */
	public int getMinDuctGageSOne() {
		return minDuctGageSOne;
	}

	/**
	 * @param minDuctGageSOne
	 *            the minDuctGageSOne to set
	 */
	public void setMinDuctGageSOne(int minDuctGageSOne) {
		this.minDuctGageSOne = minDuctGageSOne;
	}

	/**
	 * @return the minDuctGageSTwo
	 */
	public int getMinDuctGageSTwo() {
		return minDuctGageSTwo;
	}

	/**
	 * @param minDuctGageSTwo
	 *            the minDuctGageSTwo to set
	 */
	public void setMinDuctGageSTwo(int minDuctGageSTwo) {
		this.minDuctGageSTwo = minDuctGageSTwo;
	}

	/**
	 * @return the finalDuctGage
	 */
	public int getFinalDuctGage() {
		return finalDuctGage;
	}

	/**
	 * @param finalDuctGage
	 *            the finalDuctGage to set
	 */
	public void setFinalDuctGage(int finalDuctGage) {
		this.finalDuctGage = finalDuctGage;
	}

	/**
	 * @return the longitudinalSeam
	 */
	public String getLongitudinalSeam() {
		return longitudinalSeam;
	}

	/**
	 * @param longitudinalSeam
	 *            the longitudinalSeam to set
	 */
	public void setLongitudinalSeam(String longitudinalSeam) {
		this.longitudinalSeam = longitudinalSeam;
	}

	/**
	 * @return the externalReinforcement
	 */
	public boolean isExternalReinforcement() {
		return externalReinforcement;
	}

	/**
	 * @param externalReinforcement
	 *            the externalReinforcement to set
	 */
	public void setExternalReinforcement(boolean externalReinforcement) {
		this.externalReinforcement = externalReinforcement;
	}

	/**
	 * @return the reinforcementClass
	 */
	public String getReinforcementClass() {
		return reinforcementClass;
	}

	/**
	 * @param reinforcementClass
	 *            the reinforcementClass to set
	 */
	public void setReinforcementClass(String reinforcementClass) {
		this.reinforcementClass = reinforcementClass;
	}

	/**
	 * @return the externalRenfAngle
	 */
	public String getExternalRenfAngle() {
		return externalRenfAngle;
	}

	/**
	 * @param externalRenfAngle
	 *            the externalRenfAngle to set
	 */
	public void setExternalRenfAngle(String externalRenfAngle) {
		this.externalRenfAngle = externalRenfAngle;
	}

	/**
	 * @return the internalReinforcement
	 */
	public boolean isInternalReinforcement() {
		return internalReinforcement;
	}

	/**
	 * @param internalReinforcement
	 *            the internalReinforcement to set
	 */
	public void setInternalReinforcement(boolean internalReinforcement) {
		this.internalReinforcement = internalReinforcement;
	}

	/**
	 * @return the noTieRodForSideOne
	 */
	public int getNoTieRodForSideOne() {
		return noTieRodForSideOne;
	}

	/**
	 * @param noTieRodForSideOne
	 *            the noTieRodForSideOne to set
	 */
	public void setNoTieRodForSideOne(int noTieRodForSideOne) {
		this.noTieRodForSideOne = noTieRodForSideOne;
	}

	/**
	 * @return the noTieRodForSideTwo
	 */
	public int getNoTieRodForSideTwo() {
		return noTieRodForSideTwo;
	}

	/**
	 * @param noTieRodForSideTwo
	 *            the noTieRodForSideTwo to set
	 */
	public void setNoTieRodForSideTwo(int noTieRodForSideTwo) {
		this.noTieRodForSideTwo = noTieRodForSideTwo;
	}

	/**
	 * @return the tieRodLoadForSideOne
	 */
	public double getTieRodLoadForSideOne() {
		return tieRodLoadForSideOne;
	}

	/**
	 * @param tieRodLoadForSideOne
	 *            the tieRodLoadForSideOne to set
	 */
	public void setTieRodLoadForSideOne(double tieRodLoadForSideOne) {
		this.tieRodLoadForSideOne = tieRodLoadForSideOne;
	}

	/**
	 * @return the tieRod
	 */
	public String getTieRod() {
		return tieRod;
	}

	/**
	 * @param tieRod
	 *            the tieRod to set
	 */
	public void setTieRod(String tieRod) {
		this.tieRod = tieRod;
	}

	public int getInternalScore() {
		return internalScore;
	}

	public void setInternalScore(int internalScore) {
		this.internalScore = internalScore;
	}

	public int getExternalScore() {
		return externalScore;
	}

	public void setExternalScore(int externalScore) {
		this.externalScore = externalScore;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
