package com.lifespace.dto;

import java.sql.Timestamp;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lifespace.constant.EventStatus;

public class EventRequest {

	private String orderId;
	
	private String organizerId;
	
 	@NotBlank(message = "活動名稱不可為空")
 	private String eventName;
 	
    @NotNull(message = "活動開始時間不可為空")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Timestamp eventStartTime;
    
    @NotNull(message = "活動結束時間不可為空")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Timestamp eventEndTime;

    @NotBlank(message = "活動類別不可為空")
    private String eventCategory;
    
    @NotNull
    private EventStatus eventStatus = EventStatus.SCHEDULED;
    	    
    @NotNull(message = "人數上限不可為空")
    @Min(value = 1, message = "人數上限必須為正整數")
    private Integer maximumOfParticipants;
    
    private String eventBriefing;
    
    private String remarks;

    private String hostSpeaking;
    
    private List<MultipartFile> photos; // 接收多個檔案
    
	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
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

	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
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

	public List<MultipartFile> getPhotos() {
		return photos;
	}

	public void setPhotos(List<MultipartFile> photos) {
		this.photos = photos;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	    
}
