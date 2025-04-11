package com.lifespace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lifespace.constant.EventStatus;
import com.lifespace.dto.EventResponse;
import com.lifespace.entity.Event;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventRepository extends JpaRepository<Event,String>{

	List<Event> findByEventStatusAndEventStartTimeAfter( EventStatus eventStatus, Timestamp timeStart );

	List<Event> findByEventStartTimeBetweenAndEventStatus(
			Timestamp start, Timestamp end, EventStatus status );
	
	@Query(value = "SELECT " +
		    "e.event_id, e.event_name, e.event_start_time, e.event_end_time, " +
		    "ec.event_category_name, e.event_status, e.number_of_participants, " +
		    "e.maximum_of_participants, e.event_briefing, e.remarks, e.host_speaking, " +
		    "e.created_time, br.branch_addr AS space_address, br.latitude, br.longitude, mem.member_name AS organizer, GROUP_CONCAT(ep.photo) AS photo_urls " +
		    "FROM event e "+
		    "LEFT JOIN event_photo ep ON e.event_id = ep.event_id " +
		    "LEFT JOIN orders ord ON e.event_id = ord.event_id " +
		    "LEFT JOIN member mem ON ord.member_id = mem.member_id " +
		    "LEFT JOIN branch br ON ord.branch_id = br.branch_id " +
		    "LEFT JOIN event_category ec ON ec.event_category_id = e.event_category_id "+
		    "WHERE (:eventName IS NULL OR e.event_name LIKE CONCAT('%', :eventName, '%')) " +
		    "AND (:startTime IS NULL OR e.event_start_time >= :startTime) " +
		    "AND (:endTime IS NULL OR e.event_end_time <= :endTime) " +
		    "AND (:category IS NULL OR ec.event_category_id = :category) "+
		    "AND (:branch IS NULL OR br.branch_id = :branch) "+
		    "GROUP BY e.event_id, br.branch_addr, ord.member_id, br.latitude, br.longitude",
		    countQuery = "SELECT " +
				    "e.event_id, e.event_name, e.event_start_time, e.event_end_time, " +
				    "ec.event_category_name, e.event_status, e.number_of_participants, " +
				    "e.maximum_of_participants, e.event_briefing, e.remarks, e.host_speaking, " +
				    "e.created_time, br.branch_addr AS space_address, br.latitude, br.longitude, mem.member_name AS organizer, GROUP_CONCAT(ep.photo) AS photo_urls " +
				    "FROM event e "+
				    "LEFT JOIN event_photo ep ON e.event_id = ep.event_id " +
				    "LEFT JOIN orders ord ON e.event_id = ord.event_id " +
				    "LEFT JOIN member mem ON ord.member_id = mem.member_id " +
				    "LEFT JOIN branch br ON ord.branch_id = br.branch_id " +
				    "LEFT JOIN event_category ec ON ec.event_category_id = e.event_category_id "+
				    "WHERE (:eventName IS NULL OR e.event_name LIKE CONCAT('%', :eventName, '%')) " +
				    "AND (:startTime IS NULL OR e.event_start_time >= :startTime) " +
				    "AND (:endTime IS NULL OR e.event_end_time <= :endTime) " +
				    "AND (:category IS NULL OR ec.event_category_id = :category) "+
				    "AND (:branch IS NULL OR br.branch_id = :branch) "+
				    "GROUP BY e.event_id, br.branch_addr, ord.member_id, br.latitude, br.longitude",
		    nativeQuery = true)
	    Page<EventResponse> findEventsByConditions(
	            @Param("eventName") String eventName,
	            @Param("startTime") Timestamp startTime,
	            @Param("endTime") Timestamp endTime,
	            @Param("category") String category,
	            @Param("branch") String branch,
	            Pageable pageable);
	 
	 
//	 	List<Event> findByMemberId(String userId);
	 	
	 	
	 	@Query(value = "SELECT " +
			    "e.event_id, e.event_name, e.event_start_time, e.event_end_time, " +
			    "ec.event_category_name, e.event_status, e.number_of_participants, " +
			    "e.maximum_of_participants, e.event_briefing, e.remarks, e.host_speaking, " +
			    "e.created_time, br.branch_addr AS space_address, br.latitude, br.longitude, mem.member_name AS organizer, GROUP_CONCAT(ep.photo) AS photo_urls " +
			    "FROM event e "+
			    "LEFT JOIN event_photo ep ON e.event_id = ep.event_id " +
			    "LEFT JOIN orders ord ON e.event_id = ord.event_id " +
			    "LEFT JOIN member mem ON ord.member_id = mem.member_id " +
			    "LEFT JOIN branch br ON ord.branch_id = br.branch_id " +
			    "LEFT JOIN event_category ec ON ec.event_category_id = e.event_category_id "+
			    "WHERE ( e.event_id = :eventId )" +
			    "GROUP BY e.event_id, br.branch_addr, mem.member_name, br.latitude, br.longitude",nativeQuery = true)
	 	EventResponse getOneEvent(@Param("eventId") String eventId);

	 
//	 	@Query(value = "SELECT " +
//	 			"e.event_id, e.event_name, e.event_start_time, e.event_end_time, " +
//			    "e.event_category, e.event_status, e.number_of_participants, " +
//			    "e.maximum_of_participants, e.event_briefing, e.remarks, e.host_speaking, " +
//			    "e.created_time, br.branch_addr AS space_address, mem.member_name AS organizer, GROUP_CONCAT(ep.photo) AS photo_urls " +
//			    "FROM event e "+
//			    "LEFT JOIN event_photo ep ON e.event_id = ep.event_id " +
//			    "LEFT JOIN orders ord ON e.event_id = ord.event_id " +
//			    "LEFT JOIN member mem ON ord.member_id = mem.member_id " +
//			    "LEFT JOIN branch br ON ord.branch_id = br.branch_id " +
//			    "GROUP BY e.event_id, br.branch_addr, mem.member_name",nativeQuery = true)
//	 	List<EventResponse> getAllEvents();

	 
}
