package com.event.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import com.orders.model.OrdersVO;

public class EventService {

	private EventDAO_interface dao;

	public EventService() {
		dao = new EventJDBCDAO();
	}

	public EventVO addEvent(String eventName, Timestamp eventDate, Timestamp eventStartTime,
			Timestamp eventEndTime, String eventCategory, String spaceId, String memberId,
			Integer numberOfParticipants, Integer maximumOfParticipants, String eventBriefing, String remarks,
			String hostSpeaking) {

		EventVO eventVO = new EventVO();

		eventVO.setEventName(eventName);
		eventVO.setEventDate(eventDate);
		eventVO.setEventStartTime(eventStartTime);
		eventVO.setEventEndTime(eventEndTime);
		eventVO.setEventCategory(eventCategory);
		eventVO.setSpaceId(spaceId);
		eventVO.setMemberId(memberId);
		eventVO.setNumberOfParticipants(numberOfParticipants);
		eventVO.setMaximumOfParticipants(maximumOfParticipants);
		eventVO.setEventBriefing(eventBriefing);
		eventVO.setRemarks(remarks);
		eventVO.setHostSpeaking(hostSpeaking);
		dao.insert(eventVO);

		return eventVO;
	}

	public EventVO updateEvent(String eventId, String eventName, Timestamp eventDate, Timestamp eventStartTime,
			Timestamp eventEndTime, String eventCategory, String spaceId, String memberId,
			Integer numberOfParticipants, Integer maximumOfParticipants, String eventBriefing, String remarks,
			String hostSpeaking) {

		EventVO eventVO = new EventVO();

		eventVO.setEventId(eventId);
		eventVO.setEventName(eventName);
		eventVO.setEventDate(eventDate);
		eventVO.setEventStartTime(eventStartTime);
		eventVO.setEventEndTime(eventEndTime);
		eventVO.setEventCategory(eventCategory);
		eventVO.setSpaceId(spaceId);
		eventVO.setMemberId(memberId);
		eventVO.setNumberOfParticipants(numberOfParticipants);
		eventVO.setMaximumOfParticipants(maximumOfParticipants);
		eventVO.setEventBriefing(eventBriefing);
		eventVO.setRemarks(remarks);
		eventVO.setHostSpeaking(hostSpeaking);
		dao.update(eventVO);

		return eventVO;
	}

	public void deleteEvent(String eventId) {
		dao.delete(eventId);
	}

	public EventVO getOneEvent(String eventId) {
		return dao.findByPrimaryKey(eventId);
	}
	

	public List<EventVO> getAll() {
		return dao.getAll();
	}
}
