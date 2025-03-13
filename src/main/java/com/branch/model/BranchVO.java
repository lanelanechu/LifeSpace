package com.branch.model;
import java.sql.Timestamp;

public class BranchVO implements java.io.Serializable {
    private String branch_id;
    private String branch_name;
    private String branch_addr;
    private Integer space_qty;
    private Double latitude;
    private Double longitude;
    private Integer branchStatus;
    private Timestamp created_time;
    
    
    // 空構造函數
    public BranchVO() {
    }
    
    // getter 和 setter 方法
    public String getBranch_id() {
        return branch_id;
    }
    
    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }
    
    public String getBranch_name() {
        return branch_name;
    }
    
    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }
    
    public String getBranch_addr() {
        return branch_addr;
    }
    
    public void setBranch_addr(String branch_addr) {
        this.branch_addr = branch_addr;
    }
    
    public Integer getSpace_qty() {
        return space_qty;
    }
    
    public void setSpace_qty(Integer space_qty) {
        this.space_qty = space_qty;
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
    
    public Timestamp getCreated_time() {
        return created_time;
    }
    
    public void setCreated_time(Timestamp created_time) {
        this.created_time = created_time;
    }
    
 
}