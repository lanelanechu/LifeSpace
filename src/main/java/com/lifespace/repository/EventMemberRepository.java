package com.lifespace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lifespace.entity.EventMember;

public interface EventMemberRepository extends JpaRepository<EventMember,String>{

	List<EventMember> findByMemberId(String userId);
	
}
