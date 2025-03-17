package com.member.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

public interface MemberDAO_interface {
	
	public void insert(MemberVO memberVO);
	public void update(MemberVO memberVO);
	public void delete(String memberId);
	public MemberVO findByPrimaryKey(String memberId);
	public List<MemberVO> getAll();
	public List<MemberVO> searchMembers(String memberId, String memberName, String phone, String email);
	public List<MemberVO> searchMembers2(Integer accountStatus, Date registrationTime, Date birthdayl);
	public void saveMemberImage(String memberId, String imagePath);
	public byte[] getMemberImage(String memberId);

}
