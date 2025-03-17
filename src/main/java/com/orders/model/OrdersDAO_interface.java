package com.orders.model;

import java.util.List;

public interface OrdersDAO_interface {
	
	public void insert(OrdersVO orderVO);
    public void update(OrdersVO orderVO);
    public void delete(String orderId);
    public OrdersVO findByPrimaryKey(String orderId);
    public List<OrdersVO> getAll();
    //萬用複合查詢(傳入參數型態Map)(回傳 List)
//  public List<OrdersVO> getAll(Map<String, String[]> map); 


}
