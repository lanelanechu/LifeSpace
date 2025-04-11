package com.lifespace.dto;

import java.sql.Timestamp;

public class CommentsDTO {
	private String commentId;
    private String commentMessage;
    private Timestamp commentTime;
    private String memberName;
    private String eventMemberId;
//    private String branchAddr;
    
	public CommentsDTO() {
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getCommentMessage() {
		return commentMessage;
	}

	public void setCommentMessage(String commentMessage) {
		this.commentMessage = commentMessage;
	}

	public Timestamp getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(Timestamp commentTime) {
		this.commentTime = commentTime;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getEventMemberId() {
		return eventMemberId;
	}

	public void setEventMemberId(String eventMemberId) {
		this.eventMemberId = eventMemberId;
	}

//	public String getBranchAddr() {
//		return branchAddr;
//	}
//
//	public void setBranchAddr(String branchAddr) {
//		this.branchAddr = branchAddr;
//	}
       
}
