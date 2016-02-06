/**
 * 
 */
package com.smacna.service;

import java.util.List;

import com.smacna.exception.SmacnaException;
import com.smacna.form.IntermediateRenfAngleDTO;

/**
 * @author vishal.joshi
 * @version 1.0
 */
public interface IntermediateRenfAngleService {

	public List<IntermediateRenfAngleDTO> getIntermediateRenfAngle(
			String reinforcementClass) throws SmacnaException;
}
