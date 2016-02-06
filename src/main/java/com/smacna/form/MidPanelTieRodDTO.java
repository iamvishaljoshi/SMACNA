package com.smacna.form;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author vishal.joshi
 * @version 1.0
 */
@Entity
@Table(name = "t_midpanel_tie_rod")
public class MidPanelTieRodDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "spId", nullable = false)
	private StaticPressureClassDTO spId;

	@Column(name = "jointSpacing")
	private int jointSpacing;

	@Column(name = "sixteenGageFrom")
	private int sixteenGageFrom;

	@Column(name = "sixteenGageTo")
	private int sixteenGageTo;

	@Column(name = "tieRodSixteen")
	private int tieRodSixteen;

	@Column(name = "eighteenGageFrom")
	private int eighteenGageFrom;

	@Column(name = "eighteenGageTo")
	private int eighteenGageTo;

	@Column(name = "tieRodEighteen")
	private int tieRodEighteen;

	@Column(name = "twentyGageFrom")
	private int twentyGageFrom;

	@Column(name = "twentyGageTo")
	private int twentyGageTo;

	@Column(name = "tieRodtwenty")
	private int tieRodTwenty;

	@Column(name = "twentyTwoGageFrom")
	private int twentyTwoGageFrom;

	@Column(name = "twentyTwoGageTo")
	private int twentyTwoGageTo;

	@Column(name = "tieRodTwentyTwo")
	private int tieRodTwentyTwo;

	@Column(name = "twentyFourGageFrom")
	private int twentyFourGageFrom;

	@Column(name = "twentyFourGageTo")
	private int twentyFourGageTo;

	@Column(name = "tieRodTwentyFour")
	private int tieRodTwentyFour;

	@Column(name = "twentySixGageFrom")
	private int twentySixGageFrom;

	@Column(name = "twentySixGageTo")
	private int twentySixGageTo;

	@Column(name = "tieRodtwentySix")
	private int tieRodTwentySix;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public StaticPressureClassDTO getSpId() {
		return spId;
	}

	public void setSpId(StaticPressureClassDTO spId) {
		this.spId = spId;
	}

	public int getJointSpacing() {
		return jointSpacing;
	}

	public void setJointSpacing(int jointSpacing) {
		this.jointSpacing = jointSpacing;
	}

	public int getSixteenGageFrom() {
		return sixteenGageFrom;
	}

	public void setSixteenGageFrom(int sixteenGageFrom) {
		this.sixteenGageFrom = sixteenGageFrom;
	}

	public int getSixteenGageTo() {
		return sixteenGageTo;
	}

	public void setSixteenGageTo(int sixteenGageTo) {
		this.sixteenGageTo = sixteenGageTo;
	}

	public int getTieRodSixteen() {
		return tieRodSixteen;
	}

	public void setTieRodSixteen(int tieRodSixteen) {
		this.tieRodSixteen = tieRodSixteen;
	}

	public int getEighteenGageFrom() {
		return eighteenGageFrom;
	}

	public void setEighteenGageFrom(int eighteenGageFrom) {
		this.eighteenGageFrom = eighteenGageFrom;
	}

	public int getEighteenGageTo() {
		return eighteenGageTo;
	}

	public void setEighteenGageTo(int eighteenGageTo) {
		this.eighteenGageTo = eighteenGageTo;
	}

	public int getTieRodEighteen() {
		return tieRodEighteen;
	}

	public void setTieRodEighteen(int tieRodEighteen) {
		this.tieRodEighteen = tieRodEighteen;
	}

	public int getTwentyGageFrom() {
		return twentyGageFrom;
	}

	public void setTwentyGageFrom(int twentyGageFrom) {
		this.twentyGageFrom = twentyGageFrom;
	}

	public int getTwentyGageTo() {
		return twentyGageTo;
	}

	public void setTwentyGageTo(int twentyGageTo) {
		this.twentyGageTo = twentyGageTo;
	}

	/**
	 * @return the tieRodTwenty
	 */
	public int getTieRodTwenty() {
		return tieRodTwenty;
	}

	/**
	 * @param tieRodTwenty the tieRodTwenty to set
	 */
	public void setTieRodTwenty(int tieRodTwenty) {
		this.tieRodTwenty = tieRodTwenty;
	}

	public int getTwentyTwoGageFrom() {
		return twentyTwoGageFrom;
	}

	public void setTwentyTwoGageFrom(int twentyTwoGageFrom) {
		this.twentyTwoGageFrom = twentyTwoGageFrom;
	}

	public int getTwentyTwoGageTo() {
		return twentyTwoGageTo;
	}

	public void setTwentyTwoGageTo(int twentyTwoGageTo) {
		this.twentyTwoGageTo = twentyTwoGageTo;
	}

	public int getTieRodTwentyTwo() {
		return tieRodTwentyTwo;
	}

	public void setTieRodTwentyTwo(int tieRodTwentyTwo) {
		this.tieRodTwentyTwo = tieRodTwentyTwo;
	}

	public int getTwentyFourGageFrom() {
		return twentyFourGageFrom;
	}

	public void setTwentyFourGageFrom(int twentyFourGageFrom) {
		this.twentyFourGageFrom = twentyFourGageFrom;
	}

	public int getTwentyFourGageTo() {
		return twentyFourGageTo;
	}

	public void setTwentyFourGageTo(int twentyFourGageTo) {
		this.twentyFourGageTo = twentyFourGageTo;
	}

	public int getTieRodTwentyFour() {
		return tieRodTwentyFour;
	}

	public void setTieRodTwentyFour(int tieRodTwentyFour) {
		this.tieRodTwentyFour = tieRodTwentyFour;
	}

	public int getTwentySixGageFrom() {
		return twentySixGageFrom;
	}

	public void setTwentySixGageFrom(int twentySixGageFrom) {
		this.twentySixGageFrom = twentySixGageFrom;
	}

	public int getTwentySixGageTo() {
		return twentySixGageTo;
	}

	public void setTwentySixGageTo(int twentySixGageTo) {
		this.twentySixGageTo = twentySixGageTo;
	}

	/**
	 * @return the tieRodTwentySix
	 */
	public int getTieRodTwentySix() {
		return tieRodTwentySix;
	}

	/**
	 * @param tieRodTwentySix the tieRodTwentySix to set
	 */
	public void setTieRodTwentySix(int tieRodTwentySix) {
		this.tieRodTwentySix = tieRodTwentySix;
	}

}
