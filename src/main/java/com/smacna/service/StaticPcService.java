/**
 *
 */
package com.smacna.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smacna.exception.SmacnaException;
import com.smacna.form.StaticPressureClassDTO;

/**
 * @author sumit.v
 * 
 */
@Service
public interface StaticPcService {

	public List<StaticPressureClassDTO> getList() throws SmacnaException;
}
