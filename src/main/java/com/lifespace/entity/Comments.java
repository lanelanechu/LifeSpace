package com.lifespace.entity;
import java.sql.Timestamp;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lifespace.constant.CommentsCommentHide;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "comments")
public class Comments implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "comment_id", updatable = false)
	@GeneratedValue(generator = "custom-id")
	@GenericGenerator(name = "custom-id", strategy = "com.lifespace.util.CommentsCustomStringIdGenerator")
	private String commentId;
	
	@ManyToOne(fetch = FetchType.EAGER) // 預設是 EAGER，也可以明確寫
	@JoinColumn(name = "event_member_id", referencedColumnName = "event_member_id")
	private EventMember eventMember;
	
//	@Column(name = "event_member_id")
//	@NotEmpty(message="活動參與會員: 請勿空白")
//	@Pattern(regexp = "^EM\\\\d{3}$", message = "活動參與會員: 格式必須為EM001, EM002, ...")
//	private String eventMemberId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "comment_hide")
	private CommentsCommentHide commentHide;
	
	@Column(name = "comment_message")
//	@NotEmpty(message="留言內容: 請勿空白")
	private String commentMessage;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Column(name = "comment_time", insertable = false)
	@UpdateTimestamp
	private Timestamp commentTime;
	
	public Comments() {
	}
	
	public String getCommentId() {
		return commentId;
	}
	
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	
	public EventMember getEventMember() {
		return eventMember;
	}

	public void setEventMember(EventMember eventMember) {
		this.eventMember = eventMember;
	}
	
//	public String getEventMemberId() {
//		return eventMemberId;
//	}
//	
//	public void setEventMemberId(String eventMemberId) {
//		this.eventMemberId = eventMemberId;
//	}
	
	public CommentsCommentHide getCommentHide() {
		return commentHide;
	}

	public void setCommentHide(CommentsCommentHide commentHide) {
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
