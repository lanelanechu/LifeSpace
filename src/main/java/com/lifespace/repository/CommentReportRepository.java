package com.lifespace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.lifespace.entity.CommentReport;



public interface CommentReportRepository extends JpaRepository<CommentReport, String>{

	@Transactional
	@Modifying
	@Query(value = "delete from comment_report where report_id =?1", nativeQuery = true)
	void deleteByReportId(String reportId);

//	//● (自訂)條件查詢
//	@Query(value = "from CommentReport where report_id=?1 and member_id=?2 and admin_id=?3 and status=?4 order by status ASC")
//	List<CommentReport> findByOthers(String reportId , String memberId , String adminId , Integer status);
}
