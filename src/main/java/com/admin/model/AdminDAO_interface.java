package com.admin.model;

import java.util.List;

import com.member.model.MemberVO;

public interface AdminDAO_interface {
	public void insert(AdminVO adminVO);
	public void update(AdminVO adminVO);
	public void delete(String adminId);
	public AdminVO findByPrimaryKey(String adminId);
	public List<AdminVO> getAll();

}
