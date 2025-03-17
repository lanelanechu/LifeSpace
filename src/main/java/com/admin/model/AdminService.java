package com.admin.model;

import java.sql.Timestamp;
import java.util.List;

import com.member.model.MemberVO;

public class AdminService {
	
	private AdminDAO_interface dao;
	
	public AdminService() {
		dao = new AdminJDBCDAO();
	}
	
	public AdminVO addAdmin(String adminId, String adminName, String password, String email ,
			 Integer accountStatus, Timestamp registrationTime) {
		
		AdminVO adminVO = new AdminVO();
		
		adminVO.setAdminId(adminId);
		adminVO.setAdminName(adminName);
		adminVO.setPassword(password);
		adminVO.setEmail(email);
		adminVO.setAccountStatus(accountStatus);
		adminVO.setRegistrationTime(registrationTime);

		dao.insert(adminVO);
		
		
		return adminVO;
	}
	
	
	public AdminVO updateAdmin(String adminId, String adminName, String password, String email ,
			 Integer accountStatus, Timestamp registrationTime) {
		
		AdminVO adminVO = new AdminVO();
		
		adminVO.setAdminId(adminId);
		adminVO.setAdminName(adminName);
		adminVO.setPassword(password);
		adminVO.setEmail(email);
		adminVO.setAccountStatus(accountStatus);
		adminVO.setRegistrationTime(registrationTime);

		dao.insert(adminVO);
		
		
		return adminVO;
	}
	
	public void deleteAdmin(String adminId) {
		dao.delete(adminId);
	}
	
	public AdminVO getOneEmp(String adminId) {
		return dao.findByPrimaryKey(adminId);
	}
	
	public List<AdminVO> getAll() {
		return dao.getAll();
	}
	
	
}
