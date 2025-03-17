package com.eventmember.model;

import java.util.List;

public class EventMemberService {

	private EventMemberDAO_interface dao;

	public EventMemberService() {
		dao = new EventMemberJDBCDAO();
	}

	public EventMemberVO addEventMember(String eventId, String memberId,
			Integer participateStatus ) {

		EventMemberVO eventMemberVO = new EventMemberVO();

		eventMemberVO.setEventId(eventId);
		eventMemberVO.setMemberId(memberId);
		eventMemberVO.setParticipateStatus(participateStatus);
		dao.insert(eventMemberVO);

		return eventMemberVO;
	}

	public EventMemberVO updateEventMember(String eventMemberId, String eventId, String memberId,
			Integer participateStatus ) {

		EventMemberVO eventMemberVO = new EventMemberVO();

		eventMemberVO.setEventMemberId(eventMemberId);
		eventMemberVO.setEventId(eventId);
		eventMemberVO.setMemberId(memberId);
		eventMemberVO.setParticipateStatus(participateStatus);
		dao.update(eventMemberVO);

		return eventMemberVO;
	}

	public void deleteEventMember(String eventMemberId) {
		dao.delete(eventMemberId);
	}

	public EventMemberVO getOneEventMember(String eventMemberId) {
		return dao.findByPrimaryKey(eventMemberId);
	}

	public List<EventMemberVO> getAll() {
		return dao.getAll();
	}
}