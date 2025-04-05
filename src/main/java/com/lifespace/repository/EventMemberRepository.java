package com.lifespace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lifespace.constant.EventMemberStatus;
import com.lifespace.dto.EventMemberResponse;
import com.lifespace.entity.EventMember;

public interface EventMemberRepository extends JpaRepository<EventMember,String>{

	//根據eventId和memberId查詢欄位是否存在
		Optional<EventMember> findByEventEventIdAndMemberMemberId(String eventId, String memberId);
		
		//根據活動id查詢候補(QUEUED)的人選id並依照時間排序(早到晚)
		List<EventMember> findByEvent_EventIdAndParticipateStatusOrderByCreatedTimeAsc(String eventId, EventMemberStatus participateStatus);
		
		
		//根據活動id查詢活動，為了取消活動用
		List<EventMember> findByEvent_EventId(String eventId);
		
		//根據條件查詢活動，為了使用者活動頁面一覽分類用
		
		@Query(value = "SELECT " +
			    "em.event_id, em.member_id, e.event_name, e.event_start_time, e.event_end_time, "+
			    "e.event_category, e.event_status, e.number_of_participants, "+
			    "e.maximum_of_participants, em.participate_status, em.created_time, "+
			    "GROUP_CONCAT(ep.photo) AS photo_urls " +
			    "FROM event_member em "+
			    "LEFT JOIN event e ON e.event_id = em.event_id "+
	            "LEFT JOIN event_photo ep ON ep.event_id = em.event_id "+
	            "LEFT JOIN orders ord ON e.event_id = ord.event_id "+ 
	            "WHERE ( em.member_id = :memberId ) "+
	            "AND (:participateStatus IS NULL OR em.participate_status LIKE CONCAT('%', :participateStatus, '%')) "+
				"AND (:eventStatus IS NULL OR e.event_status LIKE CONCAT('%', :eventStatus, '%')) "+
				"AND (:organizerId IS NULL OR ord.member_id = :organizerId) "+
				"GROUP BY em.event_id, em.participate_status, em.created_time, em.member_id"
				,countQuery = "SELECT " +
					    "em.event_id, em.member_id, e.event_name, e.event_start_time, e.event_end_time, "+
					    "e.event_category, e.event_status, e.number_of_participants, "+
					    "e.maximum_of_participants, em.participate_status, em.created_time, "+
					    "GROUP_CONCAT(ep.photo) AS photo_urls " +
					    "FROM event_member em "+
					    "LEFT JOIN event e ON e.event_id = em.event_id "+
			            "LEFT JOIN event_photo ep ON ep.event_id = em.event_id "+
			            "LEFT JOIN orders ord ON e.event_id = ord.event_id "+ 
			            "WHERE ( em.member_id = :memberId ) "+
			            "AND (:participateStatus IS NULL OR em.participate_status LIKE CONCAT('%', :participateStatus, '%')) "+
						"AND (:eventStatus IS NULL OR e.event_status LIKE CONCAT('%', :eventStatus, '%')) "+
						"AND (:organizerId IS NULL OR ord.member_id = :organizerId) "+
						"GROUP BY em.event_id, em.participate_status, em.created_time, em.member_id"
				,nativeQuery = true)
	 	Page<EventMemberResponse> getEventByMemberConditions( 
	 			@Param("memberId") String memberId,
	 			@Param("participateStatus") String participateStatus,
	            @Param("eventStatus") String eventStatus,
	            @Param("organizerId") String organizerId,
	            Pageable pageable);
		
}
