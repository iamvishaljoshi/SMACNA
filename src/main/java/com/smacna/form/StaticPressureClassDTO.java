package com.smacna.form;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author vishal.joshi
 * @version 1.0
 */
@Entity
@Table(name = "t_static_pc")
public class StaticPressureClassDTO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="pc")
	private String pc;

//	@Column(name="unitId")
	@ManyToOne(cascade=CascadeType.ALL, targetEntity=UnitDTO.class)
	@JoinColumn(name="unitId", referencedColumnName="id", nullable= false)
	private UnitDTO unitId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPc() {
		return pc;
	}

	public void setPc(String pc) {
		this.pc = pc;
	}


	public UnitDTO getUnitId() {
		return unitId;
	}

	public void setUnitId(UnitDTO unitId) {
		this.unitId = unitId;
	}

}
