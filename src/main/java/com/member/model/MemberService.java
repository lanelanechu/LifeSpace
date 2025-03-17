package com.member.model;


import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


public class MemberService {
	
	private MemberDAO_interface dao;

	public MemberService() {
		dao = new MemberJDBCDAO();
	}

	public MemberVO addMember(String memberName,String email ,java.sql.Date birthday,
			String phone, Integer accountStatus, String password) {

		MemberVO memberVO = new MemberVO();

		memberVO.setMemberName(memberName);
		memberVO.setEmail(email);
		memberVO.setPhone(phone);
		memberVO.setAccountStatus(accountStatus);
		memberVO.setPassword(password);
		memberVO.setBirthday(birthday);
		memberVO.setRegistrationTime(Timestamp.valueOf(LocalDateTime.now()));
		dao.insert(memberVO);

		return memberVO;
	}
//	public MemberVO updateMember(String memberId, String memberName, String email, Date birthday,
//			String registrationTime, String phone, String accountStatus, String password) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public MemberVO updateMember(String memberId, String memberName, byte[] memberImage ,String email ,Date birthday,
			 String phone, Integer accountStatus, String password) {

		MemberVO memberVO = new MemberVO();

		memberVO.setMemberId(memberId);
		memberVO.setMemberName(memberName);
		memberVO.setMemberImage(memberImage);
		memberVO.setEmail(email);
		//memberVO.setRegistrationTime(registrationTime);
		memberVO.setPhone(phone);
		memberVO.setAccountStatus(accountStatus);
		memberVO.setPassword(password);
		memberVO.setBirthday(birthday);
		dao.update(memberVO);

		return memberVO;
	}

	public void deleteMember(String memberId) {
		dao.delete(memberId);
	}

	public MemberVO getOneEmp(String memberId) {
		return dao.findByPrimaryKey(memberId);
	}

	public List<MemberVO> getAll() {
		return dao.getAll();
	}
	
	public List<MemberVO> searchMembers(String memberId, String memberName, String phone, String email) {
		 return dao.searchMembers(memberId, memberName, phone, email);
	}
	
	public List<MemberVO> searchMembers2(Integer accountStatus, Date registrationTime, Date birthday) {
		 return dao.searchMembers2(accountStatus, registrationTime, birthday);
	}
	
	public byte[] getMemberImage(String memberId) {
	    return dao.getMemberImage(memberId);
	}


	
	

}
