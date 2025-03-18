package com.comments.model;
import java.sql.Timestamp;

public class CommentsVO implements java.io.Serializable{
	private String commentId;
	private String eventMemberId;
	private Integer commentHide;
	private String commentMessage;
	private Timestamp commentTime;
	
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public String getEventMemberId() {
		return eventMemberId;
	}
	public void setEventMemberId(String eventMemberId) {
		this.eventMemberId = eventMemberId;
	}
	public Integer getCommentHide() {
		return commentHide;
	}
	public void setCommentHide(Integer commentHide) {
		this.commentHide = commentHide;
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
}
