package com.lifespace.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "com.lifespace.util.OrdersCustomStringIdGenerator")
    @Id
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "space_id")
    private String spaceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private  Member member;

    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "payment_datetime")
    private Timestamp paymentDatetime;

    @Column(name = "order_start")
    private Timestamp orderStart;

    @Column(name = "order_end")
    private Timestamp orderEnd;

    @Column(name = "comment_time")
    private Timestamp commentTime;

    @Column(name = "comment_content")
    private String commentContent;

    @Column(name = "satisfaction")
    private Integer satisfaction;

    @Column(name = "accounts_payable")
    private Integer accountsPayable;

    @Column(name = "order_status")
    private Integer orderStatus;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RentalItemDetails> rentalItemDetails = new ArrayList<>();

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY)
    private List<SpaceCommentPhoto> spaceCommentPhotos = new ArrayList<>();

    public Orders() {

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


    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<RentalItemDetails> getRentalItemDetails() {
        return rentalItemDetails;
    }

    public void setRentalItemDetails(List<RentalItemDetails> rentalItemDetails) {
        this.rentalItemDetails = rentalItemDetails;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Integer getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Integer satisfaction) {
        this.satisfaction = satisfaction;
    }

    public Member getMemberId() {
        return member;
    }

    public void setMemberId(Member memberId) {
        this.member = member;
    }

    public List<SpaceCommentPhoto> getSpaceCommentPhotos() {
        return spaceCommentPhotos;
    }

    public void setSpaceCommentPhotos(List<SpaceCommentPhoto> spaceCommentPhotos) {
        this.spaceCommentPhotos = spaceCommentPhotos;
    }
}
