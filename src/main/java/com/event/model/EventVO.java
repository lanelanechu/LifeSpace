package com.event.model;

import java.sql.Date;
import java.sql.Timestamp;

public class EventVO implements java.io.Serializable{
	private String eventId;
	private String eventName;
	private Timestamp eventDate;
	private Timestamp eventStartTime;
	private Timestamp eventEndTime;
	private String eventCategory;
	private String spaceId;
	private String memberId;
	private Integer numberOfParticipants;
	private Integer maximumOfParticipants;
	private String eventBriefing;
	private String remarks;
	private String hostSpeaking;
	private Timestamp createdTime;
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public Timestamp getEventDate() {
		return eventDate;
	}
	public void setEventDate(Timestamp eventDate) {
		this.eventDate = eventDate;
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
	public String getEventBriefing() {
		return eventBriefing;
	}
	public void setEventBriefing(String eventBriefing) {
		this.eventBriefing = eventBriefing;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getHostSpeaking() {
		return hostSpeaking;
	}
	public void setHostSpeaking(String hostSpeaking) {
		this.hostSpeaking = hostSpeaking;
	}
	public Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	
}