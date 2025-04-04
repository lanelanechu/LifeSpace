package com.lifespace.dto;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class SpaceCommentResponse {
			
	private String spaceId;
	
	private String spaceName;
	
	private String branchId;
	  
	private String commentContent;
	
	private Integer satisfaction;
	  
	private Timestamp commentTime;
	  
	private List<String> photosUrls;

	public String getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public Integer getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(Integer satisfaction) {
		this.satisfaction = satisfaction;
	}

	public Timestamp getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(Timestamp commentTime) {
		this.commentTime = commentTime;
	}

	public List<String> getPhotosUrls() {
		return photosUrls;
	}

	public void setPhotosUrls(List<String> photosUrls) {
		this.photosUrls = photosUrls;
	}

	public SpaceCommentResponse(String spaceId, String spaceName, String branchId, String commentContent,
			Integer satisfaction, Timestamp commentTime, String photosUrls) {
		this.spaceId = spaceId;
		this.spaceName = spaceName;
		this.branchId = branchId;
		this.commentContent = commentContent;
		this.satisfaction = satisfaction;
		this.commentTime = commentTime;
		
		 // 如果 photosUrls 是字符串，需要將其轉換為 List
        if (photosUrls != null) {
            this.photosUrls = Arrays.asList(photosUrls.split(","));
        }
	}

}
