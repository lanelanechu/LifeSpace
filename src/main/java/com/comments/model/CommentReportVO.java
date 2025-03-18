package com.comments.model;

import java.sql.Timestamp;

public class CommentReportVO implements java.io.Serializable{
	private String reportId;
	private String memberId;
	private String adminId;
	private String commentId;
	private Integer manageType;
	private Timestamp closeTime;
	private String reportMessage;
	private String reportReason;
	private Integer status;
//	private Timestamp createdTime;
	
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public Integer getManageType() {
		return manageType;
	}
	public void setManageType(Integer manageType) {
		this.manageType = manageType;
	}
	public Timestamp getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(Timestamp closeTime) {
		this.closeTime = closeTime;
	}
	public String getReportMessage() {
		return reportMessage;
	}
	public void setReportMessage(String reportMessage) {
		this.reportMessage = reportMessage;
	}
	public String getReportReason() {
		return reportReason;
	}
	public void setReportReason(String reportReason) {
		this.reportReason = reportReason;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
//	public Timestamp getCreatedTime() {
//		return createdTime;
//	}
//	public void setCreatedTime(Timestamp createdTime) {
//		this.createdTime = createdTime;
//	}
}
