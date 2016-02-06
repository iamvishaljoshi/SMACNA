/**
 *
 */
package com.smacna.dao;

import java.util.List;

import com.smacna.exception.SmacnaException;
import com.smacna.form.StaticPressureClassDTO;

/**
 * @author sumit.v
 *
 */
public interface StaticPcDAO {

    public List<StaticPressureClassDTO> getList() throws SmacnaException;
}
