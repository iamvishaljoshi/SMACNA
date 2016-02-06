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
 *
 * @author vishal.joshi
 * @version 1.0
 */
@Entity
@Table(name="t_transverse_conn")
public class TransverseConnDTO implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(name="TC")
    private String tc;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the tc
     */
    public String getTc() {
        return tc;
    }

    /**
     * @param tc the tc to set
     */
    public void setTc(String tc) {
        this.tc = tc;
    }

}
