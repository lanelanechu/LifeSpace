package com.eventmember.model;

import java.sql.Timestamp;

public class EventMemberVO {
	private String eventMemberId;
	private String eventId;
	private String memberId;
	private Integer participateStatus;
	private Timestamp participatedTime;

	public String getEventMemberId() {
		return eventMemberId;
	}

	public void setEventMemberId(String eventMemberId) {
		this.eventMemberId = eventMemberId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Integer getParticipateStatus() {
		return participateStatus;
	}

	public void setParticipateStatus(Integer participateStatus) {
		this.participateStatus = participateStatus;
	}

	public Timestamp getParticipatedTime() {
		return participatedTime;
	}

	public void setParticipatedTime(Timestamp participatedTime) {
		this.participatedTime = participatedTime;
	}

}
