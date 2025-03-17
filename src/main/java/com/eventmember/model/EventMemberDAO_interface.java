package com.eventmember.model;

import java.util.List;

public interface EventMemberDAO_interface {
	 public void insert(EventMemberVO eventMemberVO);
     public void update(EventMemberVO eventMemberVO);
     public void delete(String eventMemberId);
     public EventMemberVO findByPrimaryKey(String eventMemberId);
     public List<EventMemberVO> getAll();
     //萬用複合查詢(傳入參數型態Map)(回傳 List)
//   public List<EmpVO> getAll(Map<String, String[]> map);
}
