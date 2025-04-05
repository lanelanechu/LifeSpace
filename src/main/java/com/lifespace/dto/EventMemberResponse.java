package com.lifespace.dto;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class EventMemberResponse {

	
	 private String eventId;
	 
	 private String memberId;
	 
	 private String eventName;
	 
	 private Timestamp eventStartTime;
	 
	 private Timestamp eventEndTime;
	 
	 private String eventCategory;
	 
	 private String eventStatus;
	  
	 private Integer numberOfParticipants;
	 
	 private Integer maximumOfParticipants;
	 
	 private String participateStatus;
	 
	 private Timestamp createdTime;
	 
	 private List<String> photoUrls;

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

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Timestamp getEventStartTime() {
		return eventStartTime;
	}

	public void setEventStartTime(Timestamp eventStartTime) {
		this.eventStartTime = eventStartTime;
	}

	public Timestamp getEventEndTime() {
		return eventEndTime;
	}

	public void setEventEndTime(Timestamp eventEndTime) {
		this.eventEndTime = eventEndTime;
	}

	public String getEventCategory() {
		return eventCategory;
	}

	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public Integer getNumberOfParticipants() {
		return numberOfParticipants;
	}

	public void setNumberOfParticipants(Integer numberOfParticipants) {
		this.numberOfParticipants = numberOfParticipants;
	}

	public Integer getMaximumOfParticipants() {
		return maximumOfParticipants;
	}

	public void setMaximumOfParticipants(Integer maximumOfParticipants) {
		this.maximumOfParticipants = maximumOfParticipants;
	}

	public String getParticipateStatus() {
		return participateStatus;
	}

	public void setParticipateStatus(String participateStatus) {
		this.participateStatus = participateStatus;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public List<String> getPhotoUrls() {
		return photoUrls;
	}

	public void setPhotoUrls(List<String> photoUrls) {
		this.photoUrls = photoUrls;
	}

	public EventMemberResponse(String eventId, String memberId, String eventName, Timestamp eventStartTime,
			Timestamp eventEndTime, String eventCategory, String eventStatus, Integer numberOfParticipants,
			Integer maximumOfParticipants, String participateStatus, Timestamp createdTime, String photoUrls) {
		super();
		this.eventId = eventId;
		this.memberId = memberId;
		this.eventName = eventName;
		this.eventStartTime = eventStartTime;
		this.eventEndTime = eventEndTime;
		this.eventCategory = eventCategory;
		this.eventStatus = eventStatus;
		this.numberOfParticipants = numberOfParticipants;
		this.maximumOfParticipants = maximumOfParticipants;
		this.participateStatus = participateStatus;
		this.createdTime = createdTime;
		
		// 如果 photoUrls 是字符串，需要將其轉換為 List
        if (photoUrls != null) {
            this.photoUrls = Arrays.asList(photoUrls.split(","));
        }
	}

	 
}
