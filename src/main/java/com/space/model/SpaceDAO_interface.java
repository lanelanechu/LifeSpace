package com.space.model;

import java.util.List;

public interface SpaceDAO_interface {
	public void insert(SpaceVO spaceVO);

	public void update(SpaceVO spaceVO);

	public void delete(String spaceId);

	public SpaceVO findByPrimaryKey(String spaceId);

	public List<SpaceVO> getAll();
	// 萬用複合查詢(傳入參數型態Map)(回傳 List)
//      public List<EmpVO> getAll(Map<String, String[]> map); 
}