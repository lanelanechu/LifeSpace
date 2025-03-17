package com.branch.model;
import java.sql.Timestamp;

public class BranchVO implements java.io.Serializable {
    private String branchId;
    private String branchName;
    private String branchAddr;
    private Integer spaceQty;
    private Double latitude;
    private Double longitude;
    private Integer branchStatus;
    private Timestamp createdTime;
    
    
    // 空構造函數
    public BranchVO() {
    }
    
    // getter 和 setter 方法
    public String getBranchId() {
        return branchId;
    }
    
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
    
    public String getBranchName() {
        return branchName;
    }
    
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
    public String getBranchAddr() {
        return branchAddr;
    }
    
    public void setBranchAddr(String branchAddr) {
        this.branchAddr = branchAddr;
    }
    
    public Integer getSpaceQty() {
        return spaceQty;
    }
    
    public void setSpaceQty(Integer spaceQty) {
        this.spaceQty = spaceQty;
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
    
    public Integer getBranchStatus() {
        return branchStatus;
    }
    
//	public String getSpaceStatusText() {
//		if (branchStatus == 1) {
//			return "上架中";
//		} else if (branchStatus == -1) {
//			return "使用中";
//		};
//		
//		return "未上架";
//	}

	public void setBranchStatus(Integer branchStatus) {
		this.branchStatus = branchStatus;
	}
    
    public Timestamp getCreatedTime() {
        return createdTime;
    }
    
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
    
 
}