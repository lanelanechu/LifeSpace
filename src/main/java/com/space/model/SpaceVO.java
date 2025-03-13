package com.space.model;

import java.sql.Timestamp;

public class SpaceVO implements java.io.Serializable {
	private String spaceId;
	private String branchId;
	private String spaceName;
	private Integer spacePeople;
	private Double spaceSize;
	private Integer spaceHourlyFee;
	private Integer spaceDailyFee;
	private String spaceDesc;
	private Double spaceRating;
	private String spaceAlert;
	private Double spaceUsed24hr;
	private Double spaceUsed7d;
	private Integer spaceStatus;
	private String spaceAddress;
	private Double latitude;
	private Double longitude;
	private Timestamp createdTime;
	
	public SpaceVO() {
		super();
	}

	public SpaceVO(String spaceId, String branchId, String spaceName, Integer spacePeople, Double spaceSize,
			Integer spaceHourlyFee, Integer spaceDailyFee, String spaceDesc, Double spaceRating, String spaceAlert,
			Double spaceUsed24hr, Double spaceUsed7d, Integer spaceStatus, String spaceAddress, Double latitude,
			Double longitude, Timestamp createdTime) {
		super();
		this.spaceId = spaceId;
		this.branchId = branchId;
		this.spaceName = spaceName;
		this.spacePeople = spacePeople;
		this.spaceSize = spaceSize;
		this.spaceHourlyFee = spaceHourlyFee;
		this.spaceDailyFee = spaceDailyFee;
		this.spaceDesc = spaceDesc;
		this.spaceRating = spaceRating;
		this.spaceAlert = spaceAlert;
		this.spaceUsed24hr = spaceUsed24hr;
		this.spaceUsed7d = spaceUsed7d;
		this.spaceStatus = spaceStatus;
		this.spaceAddress = spaceAddress;
		this.latitude = latitude;
		this.longitude = longitude;
		this.createdTime = createdTime;
	}

	public String getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public Integer getSpacePeople() {
		return spacePeople;
	}

	public void setSpacePeople(Integer spacePeople) {
		this.spacePeople = spacePeople;
	}

	public Double getSpaceSize() {
		return spaceSize;
	}

	public void setSpaceSize(Double spaceSize) {
		this.spaceSize = spaceSize;
	}

	public Integer getSpaceHourlyFee() {
		return spaceHourlyFee;
	}

	public void setSpaceHourlyFee(Integer spaceHourlyFee) {
		this.spaceHourlyFee = spaceHourlyFee;
	}

	public Integer getSpaceDailyFee() {
		return spaceDailyFee;
	}

	public void setSpaceDailyFee(Integer spaceDailyFee) {
		this.spaceDailyFee = spaceDailyFee;
	}

	public String getSpaceDesc() {
		return spaceDesc;
	}

	public void setSpaceDesc(String spaceDesc) {
		this.spaceDesc = spaceDesc;
	}

	public Double getSpaceRating() {
		return spaceRating;
	}

	public void setSpaceRating(Double spaceRating) {
		this.spaceRating = spaceRating;
	}

	public String getSpaceAlert() {
		return spaceAlert;
	}

	public void setSpaceAlert(String spaceAlert) {
		this.spaceAlert = spaceAlert;
	}

	public Double getSpaceUsed24hr() {
		return spaceUsed24hr;
	}

	public void setSpaceUsed24hr(Double spaceUsed24hr) {
		this.spaceUsed24hr = spaceUsed24hr;
	}

	public Double getSpaceUsed7d() {
		return spaceUsed7d;
	}

	public void setSpaceUsed7d(Double spaceUsed7d) {
		this.spaceUsed7d = spaceUsed7d;
	}

    public Integer getSpaceStatus() {
        return spaceStatus;
    }
    
	public String getSpaceStatusText() {
		if (spaceStatus == 1) {
			return "上架中";
		} else if (spaceStatus == -1) {
			return "使用中";
		};
		
		return "未上架";
	}

	public void setSpaceStatus(Integer spaceStatus) {
		this.spaceStatus = spaceStatus;
	}

	
//	public void setSpaceStatusText(String spaceStatusText) {
//		if ("未上架".equals(spaceStatusText)) {
//			this.spaceStatus = 0;
//		} else if ("上架中".equals(spaceStatusText)) {
//			this.spaceStatus = 1;
//		} else {
//			this.spaceStatus = -1;
//		}
//	}
	
	public String getSpaceAddress() {
		return spaceAddress;
	}

	public void setSpaceAddress(String spaceAddress) {
		this.spaceAddress = spaceAddress;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
}
