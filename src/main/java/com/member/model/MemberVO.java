package com.member.model;

import java.sql.Date;
import java.sql.Timestamp;

public class MemberVO implements java.io.Serializable{
	
	private String memberId;
	private String memberName;
	private byte[] memberImage;
	private String email;
	private Timestamp registrationTime;
	private String phone;
	private Integer accountStatus;
	private String password;
	private Date birthday;
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public byte[] getMemberImage() {
		return memberImage;
	}
	public void setMemberImage(byte[] memberImage) {
		this.memberImage = memberImage;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Timestamp getRegistrationTime() {
		return registrationTime;
	}
	public void setRegistrationTime(Timestamp registrationTime) {
		this.registrationTime = registrationTime;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	
	
	

}
