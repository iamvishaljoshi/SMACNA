package com.smacna.dao;

import java.util.List;

import com.smacna.exception.SmacnaException;
import com.smacna.form.IntermediateRenfAngleDTO;

/**
 * 
 * @author vishal.joshi
 * @version 1.0
 */
public interface IntermediateRenfAngleDAO {

	public List<IntermediateRenfAngleDTO> getIntermediateRenfAngle(
			String reinforcementClass) throws SmacnaException;

}
