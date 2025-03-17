package com.orders.model;

import java.sql.Timestamp;

import com.event.model.EventVO;

public class OrdersVO implements java.io.Serializable{

		private String orderId;
		private String spaceId;
		private String memberId;
		private String branchId;
		private	String eventId;
		private Integer totalPrice;
		private Timestamp paymentDatetime;
		private Timestamp orderStart;
		private Timestamp orderEnd;
		private Timestamp commentTime;
		private String commentContect;
		private Integer satisfaction;
		private Integer accountsPayable;
		private Integer orderStatus;
		private Timestamp createdTime;
		private EventVO eventVO;
		
		public void setEventVO(EventVO eventVO) {
			this.eventVO = eventVO;
		}
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		public String getSpaceId() {
			return spaceId;
		}
		public void setSpaceId(String spaceId) {
			this.spaceId = spaceId;
		}
		public String getMemberId() {
			return memberId;
		}
		public void setMemberId(String memberId) {
			this.memberId = memberId;
		}
		public String getBranchId() {
			return branchId;
		}
		public void setBranchId(String branchId) {
			this.branchId = branchId;
		}
		public String getEventId() {
			return eventId;
		}
		public void setEventId(String eventId) {
			this.eventId = eventId;
		}
		public Integer getTotalPrice() {
			return totalPrice;
		}
		public void setTotalPrice(Integer totalPrice) {
			this.totalPrice = totalPrice;
		}
		public Timestamp getPaymentDatetime() {
			return paymentDatetime;
		}
		public void setPaymentDatetime(Timestamp paymentDatetime) {
			this.paymentDatetime = paymentDatetime;
		}
		public Timestamp getOrderStart() {
			return orderStart;
		}
		public void setOrderStart(Timestamp orderStart) {
			this.orderStart = orderStart;
		}
		public Timestamp getOrderEnd() {
			return orderEnd;
		}
		public void setOrderEnd(Timestamp orderEnd) {
			this.orderEnd = orderEnd;
		}
		public Timestamp getCommentTime() {
			return commentTime;
		}
		public void setCommentTime(Timestamp commentTime) {
			this.commentTime = commentTime;
		}
		public String getCommentContect() {
			return commentContect;
		}
		public void setCommentContect(String commentContect) {
			this.commentContect = commentContect;
		}
		public Integer getSatisfaction() {
			return satisfaction;
		}
		public void setSatisfaction(Integer satisfaction) {
			this.satisfaction = satisfaction;
		}
		public Integer getAccountsPayable() {
			return accountsPayable;
		}
		public void setAccountsPayable(Integer accountsPayable) {
			this.accountsPayable = accountsPayable;
		}
		public Integer getOrderStatus() {
			return orderStatus;
		}
		public void setOrderStatus(Integer orderStatus) {
			this.orderStatus = orderStatus;
		}
		public Timestamp getCreatedTime() {
			return createdTime;
		}
		public void setCreatedTime(Timestamp createdTime) {
			this.createdTime = createdTime;
		}
		// for join eventName from EventId
		public com.event.model.EventVO getEventVO() {
		    com.event.model.EventService eventSvc = new com.event.model.EventService();
		    com.event.model.EventVO eventVO = eventSvc.getOneEvent(eventId);
		    return eventVO;
	    }
		
		
		
		
}
