package com.lifespace.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrdersDTO {

    private String orderId;
    private String spaceId;
    private String branchId;
    private Timestamp orderStart;
    private Timestamp orderEnd;
    private Integer totalPrice;
    private Timestamp paymentDatetime;
    private Integer orderStatus;
    private EventDTO eventDTO;
    private List<RentalItemDetailsDTO> rentalItemDetailsDTOList = new ArrayList<>();

    public OrdersDTO(){

    }

    public List<RentalItemDetailsDTO> getRentalItemDetailsDTOList() {
        return rentalItemDetailsDTOList;
    }

    public void setRentalItemDetailsDTOList(List<RentalItemDetailsDTO> rentalItemDetailsDTOList) {
        this.rentalItemDetailsDTOList = rentalItemDetailsDTOList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public EventDTO getEventDTO() {
        return eventDTO;
    }

    public void setEventDTO(EventDTO eventDTO) {
        this.eventDTO = eventDTO;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }


    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

}
