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
@Table(name = "t_internal_emt_conduit_size")
public class InternalEMTCondSizeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="dim")
	private String dim;
	
	@Column(name="type")
	private String type;
	
	@Column(name="rg")
	private float rg;
	
	@Column(name="length")
	private int length;
	
	@Column(name="lbs")
	private int lbs;

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
	 * @return the dim
	 */
	public String getDim() {
		return dim;
	}

	/**
	 * @param dim the dim to set
	 */
	public void setDim(String dim) {
		this.dim = dim;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the rg
	 */
	public float getRg() {
		return rg;
	}

	/**
	 * @param rg the rg to set
	 */
	public void setRg(float rg) {
		this.rg = rg;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return the lbs
	 */
	public int getLbs() {
		return lbs;
	}

	/**
	 * @param lbs the lbs to set
	 */
	public void setLbs(int lbs) {
		this.lbs = lbs;
	}
	
	
	
}
