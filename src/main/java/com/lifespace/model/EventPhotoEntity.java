package com.lifespace.model;

import java.sql.Timestamp;

import com.lifespace.model.EventEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="event_photo")
public class EventPhotoEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="photo_id")
	private String photoId;
	
	@ManyToOne
	@JoinColumn(name = "event_id", referencedColumnName = "event_id")
	private EventEntity event; 

	@Column(name="photo")
	private String photo;
	 
	@Column(name="created_time")
	private Timestamp createdTime;

	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

	

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	public EventPhotoEntity(String photoId, EventEntity event, String photo, Timestamp createdTime) {
		super();
		this.photoId = photoId;
		this.event = event;
		this.photo = photo;
		this.createdTime = createdTime;
	}

	public EventEntity getEvent() {
		return event;
	}

	public void setEvent(EventEntity event) {
		this.event = event;
	}

	public EventPhotoEntity() {
		
	}
	 
	
}
