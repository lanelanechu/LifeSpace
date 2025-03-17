package com.event.model;

import java.util.List;

public interface EventDAO_interface {

	public void insert(EventVO EventVO);
	public void update(EventVO EventVO);
	public void delete(String eventId);
	public EventVO findByPrimaryKey(String eventId);

	public List<EventVO> getAll();
	// 萬用複合查詢(傳入參數型態Map)(回傳 List)
	// public List<OrdersVO> getAll(Map<String, String[]> map);

}
