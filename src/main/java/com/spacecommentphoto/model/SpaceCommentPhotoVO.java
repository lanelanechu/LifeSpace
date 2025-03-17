package com.spacecommentphoto.model;

import java.sql.Timestamp;

public class SpaceCommentPhotoVO {
	private String spaceCommentPhotoId;
	private String orderId;
	private String spacePhoto;
	private Timestamp createdTime;
	
	public String getSpaceCommentPhotoId() {
		return spaceCommentPhotoId;
	}
	public void setSpaceCommentPhotoId(String spaceCommentPhotoId) {
		this.spaceCommentPhotoId = spaceCommentPhotoId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getSpacePhoto() {
		return spacePhoto;
	}
	public void setSpacePhoto(String spacePhoto) {
		this.spacePhoto = spacePhoto;
	}
	public Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	
	
}
