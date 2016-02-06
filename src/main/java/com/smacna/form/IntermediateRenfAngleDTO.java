/**
 * 
 */
package com.smacna.form;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author vishal.joshi
 * @version 1.0
 */
@Entity
@Table(name="t_intermediate_renf_angle")
public class IntermediateRenfAngleDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="reinfoClass")
	private String reinfoClass;

	@Column(name="EI")
	private double ei;
	
	@Column(name="angleHHT")
	private String angleHHT;
	
	@Column(name="angleWTLF")
	private double angleWTLF;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the reinfoClass
	 */
	public String getReinfoClass() {
		return reinfoClass;
	}

	/**
	 * @param reinfoClass the reinfoClass to set
	 */
	public void setReinfoClass(String reinfoClass) {
		this.reinfoClass = reinfoClass;
	}

	/**
	 * @return the ei
	 */
	public double getEi() {
		return ei;
	}

	/**
	 * @param ei the ei to set
	 */
	public void setEi(double ei) {
		this.ei = ei;
	}

	/**
	 * @return the angleHHT
	 */
	public String getAngleHHT() {
		return angleHHT;
	}

	/**
	 * @param angleHHT the angleHHT to set
	 */
	public void setAngleHHT(String angleHHT) {
		this.angleHHT = angleHHT;
	}

	/**
	 * @return the angleWTLF
	 */
	public double getAngleWTLF() {
		return angleWTLF;
	}

	/**
	 * @param angleWTLF the angleWTLF to set
	 */
	public void setAngleWTLF(double angleWTLF) {
		this.angleWTLF = angleWTLF;
	}
}
