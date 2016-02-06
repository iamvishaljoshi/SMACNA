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
@Table(name = "t_trans_joint_renfo")
public class TransverseJointRenfDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "renfClass")
	private String renfClass;

	@Column(name = "EI")
	private double ei;

	@Column(name = "T10HT")
	private String t10HT;

	@Column(name = "T10WTLF")
	private double t10WTLF;

	@Column(name = "T11HT")
	private String t11HT;

	@Column(name = "T11WTLF")
	private double t11WTLF;

	@Column(name = "T12HT")
	private String t12HT;

	@Column(name = "T12WTLF")
	private double t12WTLF;

	@Column(name = "T25abHT")
	private String t25abHT;

	@Column(name = "T25abWTLF")
	private double t25abWTLF;
	
	@Column(name = "T25abTieRod")
	private boolean t25abTieRod;

	/**
	 * @return the t25abTieRod
	 */
	public boolean isT25abTieRod() {
		return t25abTieRod;
	}

	/**
	 * @param t25abTieRod the t25abTieRod to set
	 */
	public void setT25abTieRod(boolean t25abTieRod) {
		this.t25abTieRod = t25abTieRod;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the renfClass
	 */
	public String getRenfClass() {
		return renfClass;
	}

	/**
	 * @param renfClass
	 *            the renfClass to set
	 */
	public void setRenfClass(String renfClass) {
		this.renfClass = renfClass;
	}

	/**
	 * @return the ei
	 */
	public double getEi() {
		return ei;
	}

	/**
	 * @param ei
	 *            the ei to set
	 */
	public void setEi(double ei) {
		this.ei = ei;
	}

	/**
	 * @return the t10HT
	 */
	public String getT10HT() {
		return t10HT;
	}

	/**
	 * @param t10ht
	 *            the t10HT to set
	 */
	public void setT10HT(String t10ht) {
		t10HT = t10ht;
	}

	/**
	 * @return the t10WTLF
	 */
	public double getT10WTLF() {
		return t10WTLF;
	}

	/**
	 * @param t10wtlf
	 *            the t10WTLF to set
	 */
	public void setT10WTLF(double t10wtlf) {
		t10WTLF = t10wtlf;
	}

	/**
	 * @return the t11HT
	 */
	public String getT11HT() {
		return t11HT;
	}

	/**
	 * @param t11ht
	 *            the t11HT to set
	 */
	public void setT11HT(String t11ht) {
		t11HT = t11ht;
	}

	/**
	 * @return the t11WTLF
	 */
	public double getT11WTLF() {
		return t11WTLF;
	}

	/**
	 * @param t11wtlf
	 *            the t11WTLF to set
	 */
	public void setT11WTLF(double t11wtlf) {
		t11WTLF = t11wtlf;
	}

	/**
	 * @return the t12HT
	 */
	public String getT12HT() {
		return t12HT;
	}

	/**
	 * @param t12ht
	 *            the t12HT to set
	 */
	public void setT12HT(String t12ht) {
		t12HT = t12ht;
	}

	/**
	 * @return the t12WTLF
	 */
	public double getT12WTLF() {
		return t12WTLF;
	}

	/**
	 * @param t12wtlf
	 *            the t12WTLF to set
	 */
	public void setT12WTLF(double t12wtlf) {
		t12WTLF = t12wtlf;
	}

	/**
	 * @return the t25abHT
	 */
	public String getT25abHT() {
		return t25abHT;
	}

	/**
	 * @param t25abHT
	 *            the t25abHT to set
	 */
	public void setT25abHT(String t25abHT) {
		this.t25abHT = t25abHT;
	}

	/**
	 * @return the t25abWTLF
	 */
	public double getT25abWTLF() {
		return t25abWTLF;
	}

	/**
	 * @param t25abWTLF
	 *            the t25abWTLF to set
	 */
	public void setT25abWTLF(double t25abWTLF) {
		this.t25abWTLF = t25abWTLF;
	}

}
