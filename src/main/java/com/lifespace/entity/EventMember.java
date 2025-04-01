package com.lifespace.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="event_member")
public class EventMemberEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	
	 @Id
	 @Column(name="event_member_id")
	 private String eventMemberId;
	 
	 @ManyToOne
	 @JoinColumn(name = "event_id", referencedColumnName = "event_id")
	 private Event event;

	 @Column(name="member_id")
	 private String memberId;
	 
	 @Column(name="participate_status")
	 private Integer participateStatus;
	 
	 @Column(name="participated_time")
	 private Timestamp participatedTime;

	public String getEventMemberId() {
		return eventMemberId;
	}

	public void setEventMemberId(String eventMemberId) {
		this.eventMemberId = eventMemberId;
	}

	

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public EventMemberEntity() {
		
	}

	public EventMemberEntity(String eventMemberId, Event event, String memberId, Integer participateStatus,
                             Timestamp participatedTime) {
		this.eventMemberId = eventMemberId;
		this.event = event;
		this.memberId = memberId;
		this.participateStatus = participateStatus;
		this.participatedTime = participatedTime;
	}

	
	 
	
	
}
