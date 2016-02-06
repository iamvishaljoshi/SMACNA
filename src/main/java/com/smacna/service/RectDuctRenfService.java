package com.smacna.service;

import com.smacna.exception.SmacnaException;

public interface RectDuctRenfService {

	public int getRectDuctRenfForTOne(int pressureClass, int sx,
			int jointSpacing, int transverConnection)throws SmacnaException;

	public String getRectDuctRenfForT25(int pressureClass, int sx,
			float jointSpacing, int transverConnection, boolean RSequalsJS)throws SmacnaException;

	public String getGageFromRectDuctRenf(int transverseConnection,
			String reinforClass)throws SmacnaException;

	public String getMidSpanIntermediateReinforcement(String reinforClass)throws SmacnaException;
	
	public String calculationForTtenToTtwelve(int pressureClass, int sx,
			float jointSpacing, int transverConnection, boolean RSequalsJS)throws SmacnaException;

}
