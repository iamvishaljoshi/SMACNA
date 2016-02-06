package com.smacna.service;

import com.smacna.exception.SmacnaException;

public interface MidPanelTieRodService {
	public int getNumberOfTieRod(int pressureClass, float jointSpacing,
			int ductGage, int side) throws SmacnaException;
}
