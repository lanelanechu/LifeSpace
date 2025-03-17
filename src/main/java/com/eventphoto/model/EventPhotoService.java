package com.eventphoto.model;

import java.util.List;

public class EventPhotoService {

	private EventPhotoDAO_interface dao;

	public EventPhotoService() {
		dao = new EventPhotoJDBCDAO();
	}

	public EventPhotoVO addEventPhoto(String photoId, String eventId, String photo) {

		EventPhotoVO eventPhotoVO = new EventPhotoVO();

		eventPhotoVO.setPhotoId(photoId);
		eventPhotoVO.setEventId(eventId);
		eventPhotoVO.setPhoto(photo);
		dao.insert(eventPhotoVO);

		return eventPhotoVO;
	}

	public EventPhotoVO updateEventPhoto(String photoId, String eventId, String photo) {

		EventPhotoVO eventPhotoVO = new EventPhotoVO();

		eventPhotoVO.setPhotoId(photoId);
		eventPhotoVO.setEventId(eventId);
		eventPhotoVO.setPhoto(photo);
		dao.update(eventPhotoVO);

		return eventPhotoVO;
	}

	public void deleteEventPhoto(String photoId) {
		dao.delete(photoId);
	}

	public EventPhotoVO getOneEventPhoto(String photoId) {
		return dao.findByPrimaryKey(photoId);
	}

	public List<EventPhotoVO> getAll() {
		return dao.getAll();
	}
}