package com.smacna.dao;

import java.util.List;

import com.smacna.exception.SmacnaException;
import com.smacna.form.StaticPressureClassDTO;
import com.smacna.form.TransverseConnDTO;

public interface TransverseConnectionDAO {

	public List<TransverseConnDTO> getTcList() throws SmacnaException;

	public List<StaticPressureClassDTO> getStaticPCList()
			throws SmacnaException;

	public StaticPressureClassDTO getStaticPC(int id) throws SmacnaException;

	public List<TransverseConnDTO> getSelectTcList(String tcName,
			boolean include) throws SmacnaException;

	public List<TransverseConnDTO> getSelectTcList(int tcId, boolean include)
			throws SmacnaException;

	public List<TransverseConnDTO> getSelectTcList(boolean checkT5)
			throws SmacnaException;

	public String getTransverseConnName(int id) throws SmacnaException;

	public String getEmtForNegPC(int length, double tieRoadLoad)
			throws SmacnaException;
}
