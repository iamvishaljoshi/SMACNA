package com.smacna.model;

import org.springframework.context.annotation.Scope;

/**
 *
 * @author vishal.joshi
 * @version 1.0
 */
@Scope("session")
public class InputScreen {

	@Override
	public String toString() {
		return "Pressure Class : " + this.pressureClass + ", posOrNeg : "
				+ this.posOrNeg + ", Dimension(H x W) : (" + this.height
				+ " x " + this.width + "), Joint Spacing : "
				+ this.jointSpacing + ", transConnS1 : " + this.transConnS1
				+ ", transConnS2 : " + this.transConnS2 + "";
	}

	private int pressureClass;

	private boolean posOrNeg = false;

	private String height;

	private String width;

	private int jointSpacing;

	private int transConnS1;

	private int transConnS2;

	private String designAvailable;



	/**
	 * @return the designAvailable
	 */
	public String getDesignAvailable() {
		return designAvailable;
	}

	/**
	 * @param designAvailable the designAvailable to set
	 */
	public void setDesignAvailable(String designAvailable) {
		this.designAvailable = designAvailable;
	}

	/**
	 * @return the pressureClass
	 */
	public int getPressureClass() {
		return pressureClass;
	}

	/**
	 * @param pressureClass
	 *            the pressureClass to set
	 */
	public void setPressureClass(int pressureClass) {
		this.pressureClass = pressureClass;
	}

	/**
	 * @return the posOrNeg
	 */
	public boolean isPosOrNeg() {
		return posOrNeg;
	}

	/**
	 * @param posOrNeg
	 *            the posOrNeg to set
	 */
	public void setPosOrNeg(boolean posOrNeg) {
		this.posOrNeg = posOrNeg;
	}

	/**
	 * @return the height
	 */
	public String getHeight() {

		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(String height) {
		this.height = height;
	}

	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * @return the jointSpacing
	 */
	public int getJointSpacing() {
		return jointSpacing;
	}

	/**
	 * @param jointSpacing
	 *            the jointSpacing to set
	 */
	public void setJointSpacing(int jointSpacing) {
		this.jointSpacing = jointSpacing;
	}

	/**
	 * @return the transConnS1
	 */
	public int getTransConnS1() {
		return transConnS1;
	}

	/**
	 * @param transConnS1
	 *            the transConnS1 to set
	 */
	public void setTransConnS1(int transConnS1) {
		this.transConnS1 = transConnS1;
	}

	/**
	 * @return the transConnS2
	 */
	public int getTransConnS2() {
		return transConnS2;
	}

	/**
	 * @param transConnS2
	 *            the transConnS2 to set
	 */
	public void setTransConnS2(int transConnS2) {
		this.transConnS2 = transConnS2;
	}



}
