package com.smacna.form;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author vishal.joshi
 * @version 1.0
 * 
 */
@Entity
@Table(name = "t_rect_duct_renf")
public class RectDuctRenfDTO implements Serializable {

	/**
	 *  serialVersionUID is a unique serial version number.
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "sp_id", nullable = false)
	private StaticPressureClassDTO spId;
	
	@Column(name="ductDim1")
	private int ductDimOne;
	
	@Column(name="ductDim2")
	private int ductDimTwo;
	
	@Column(name="noRenforT5")
	private int noRenForTFive;
	
	@Column(name="noRenForT1JS6")
	private int noRenForTOneJSVI;
	
	@Column(name="noRenT1JSs4")
	private int noRenTOneJSVAndIV;
	
	@Column(name="sixFt")
	private String sixFt;
	
	@Column(name="fiveFt")
	private String fiveFt;
	
	@Column(name="fourFt")
	private String fourFt;
	
	@Column(name="threeFt")
	private String threeFt;
	
	@Column(name="twoAnHalfFt")
	private String twoAnHalfFt;
	
	@Column(name="twoFt")
	private String twoFt;
	
	@Column(name="tieRod")
	private boolean tieRod;
	
	@Column(name="tenFt")
	private String tenFt;
	
	@Column(name="eightFt")
	private String eightFt;
	
	public int getDuctDimOne() {
		return ductDimOne;
	}

	public void setDuctDimOne(int ductDimOne) {
		this.ductDimOne = ductDimOne;
	}

	public int getDuctDimTwo() {
		return ductDimTwo;
	}

	public void setDuctDimTwo(int ductDimTwo) {
		this.ductDimTwo = ductDimTwo;
	}

	public int getNoRenForTFive() {
		return noRenForTFive;
	}

	public void setNoRenForTFive(int noRenForTFive) {
		this.noRenForTFive = noRenForTFive;
	}

	public int getNoRenForTOneJSVI() {
		return noRenForTOneJSVI;
	}

	public void setNoRenForTOneJSVI(int noRenForTOneJSVI) {
		this.noRenForTOneJSVI = noRenForTOneJSVI;
	}

	public int getNoRenTOneJSVAndIV() {
		return noRenTOneJSVAndIV;
	}

	public void setNoRenTOneJSVAndIV(int noRenTOneJSVAndIV) {
		this.noRenTOneJSVAndIV = noRenTOneJSVAndIV;
	}

	public String getSixFt() {
		return sixFt;
	}

	public void setSixFt(String sixFt) {
		this.sixFt = sixFt;
	}

	public String getFiveFt() {
		return fiveFt;
	}

	public void setFiveFt(String fiveFt) {
		this.fiveFt = fiveFt;
	}

	public String getFourFt() {
		return fourFt;
	}

	public void setFourFt(String fourFt) {
		this.fourFt = fourFt;
	}

	public String getThreeFt() {
		return threeFt;
	}

	public void setThreeFt(String threeFt) {
		this.threeFt = threeFt;
	}

	public String getTwoAnHalfFt() {
		return twoAnHalfFt;
	}

	public void setTwoAnHalfFt(String twoAnHalfFt) {
		this.twoAnHalfFt = twoAnHalfFt;
	}

	public String getTwoFt() {
		return twoFt;
	}

	public void setTwoFt(String twoFt) {
		this.twoFt = twoFt;
	}

	public boolean isTieRod() {
		return tieRod;
	}

	public void setTieRod(boolean tieRod) {
		this.tieRod = tieRod;
	}

	public String getTenFt() {
		return tenFt;
	}

	public void setTenFt(String tenFt) {
		this.tenFt = tenFt;
	}

	public String getEightFt() {
		return eightFt;
	}

	public void setEightFt(String eightFt) {
		this.eightFt = eightFt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	public StaticPressureClassDTO getSpId() {
		return spId;
	}

	public void setSpId(StaticPressureClassDTO spId) {
		this.spId = spId;
	}

}
