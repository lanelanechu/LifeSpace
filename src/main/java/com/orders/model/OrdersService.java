package com.orders.model;

import java.sql.Timestamp;
import java.util.List;

public class OrdersService {

	    private OrdersDAO_interface dao;

	    public OrdersService() {
	        dao = new OrdersJDBCDAO();
	    }

	    public OrdersVO addOrder(String orderId,String spaceId,String memberId,String branchId,String eventId,Integer totalPrice,Timestamp paymentDatetime,Timestamp orderStart,
	    		Timestamp orderEnd,Timestamp commentTime,String commentContect,Integer satisfaction,Integer accountsPayable) {
	    	
	    	OrdersVO ordersVO = new OrdersVO();
	    	
	    	ordersVO.setOrderId(orderId);
			ordersVO.setSpaceId(spaceId);
            ordersVO.setMemberId(memberId);
            ordersVO.setBranchId(branchId);
            ordersVO.setEventId(eventId);
            ordersVO.setTotalPrice(totalPrice);
            ordersVO.setPaymentDatetime(paymentDatetime);
            ordersVO.setOrderStart(orderStart);
            ordersVO.setOrderEnd(orderEnd);
            ordersVO.setCommentTime(commentTime);
            ordersVO.setCommentContect(commentContect);
            ordersVO.setSatisfaction(satisfaction);
            ordersVO.setAccountsPayable(accountsPayable);
         
	    	dao.insert(ordersVO);
	    	return ordersVO;
	    }
	    
	    //預留
	    public void addOrders(OrdersVO ordersVo) {
	    	dao.insert(ordersVo);
	    }

	    public OrdersVO updateOrders(String orderId,String spaceId,String memberId,String branchId,String eventId,Integer totalPrice,Timestamp paymentDatetime,Timestamp orderStart,
	    		Timestamp orderEnd,Timestamp commentTime,String commentContect,Integer satisfaction,Integer accountsPayable) {
	    	
            OrdersVO ordersVO = new OrdersVO();
	    	
            ordersVO.setOrderId(orderId);
			ordersVO.setSpaceId(spaceId);
            ordersVO.setMemberId(memberId);
            ordersVO.setBranchId(branchId);
            ordersVO.setEventId(eventId);
            ordersVO.setTotalPrice(totalPrice);
            ordersVO.setPaymentDatetime(paymentDatetime);
            ordersVO.setOrderStart(orderStart);
            ordersVO.setOrderEnd(orderEnd);
            ordersVO.setCommentTime(commentTime);
            ordersVO.setCommentContect(commentContect);
            ordersVO.setSatisfaction(satisfaction);
            ordersVO.setAccountsPayable(accountsPayable);;
	        dao.update(ordersVO);
	        
	        return ordersVO;
	    }

	    public void deleteOrders(String orderId) {
	        dao.delete(orderId);
	    }

	    public OrdersVO getOneOrders(String orderId) {
	        return dao.findByPrimaryKey(orderId);
	    }

	    public List<OrdersVO> getAll() {
	        return dao.getAll();
	    }

}