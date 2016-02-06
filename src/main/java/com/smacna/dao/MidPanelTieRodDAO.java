package com.smacna.dao;

import com.smacna.exception.SmacnaException;

/**
 * 
 * @author vishal.joshi
 * @version 1.0
 */
public interface MidPanelTieRodDAO {

	public int getNumberOfTieRod(int pressureClass, float jointSpacing,
			int ductGage, int side) throws SmacnaException;

}
