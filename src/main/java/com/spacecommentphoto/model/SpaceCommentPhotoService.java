package com.spacecommentphoto.model;

import java.util.List;

import com.spacecommentphoto.model.SpaceCommentPhotoVO;

public class SpaceCommentPhotoService {
	
	private SpaceCommentPhotoDAO_interface dao;

	public SpaceCommentPhotoService() {
		dao = new SpaceCommentPhotoJDBCDAO();
	}

	public SpaceCommentPhotoVO addSpaceCommentPhoto(String spaceCommentPhotoId, String orderId, String spacePhoto) {

		SpaceCommentPhotoVO spaceCommentPhotoVO = new SpaceCommentPhotoVO();

		spaceCommentPhotoVO.setSpaceCommentPhotoId(spaceCommentPhotoId);
		spaceCommentPhotoVO.setOrderId(orderId);
		spaceCommentPhotoVO.setSpacePhoto(spacePhoto);
		dao.insert(spaceCommentPhotoVO);

		return spaceCommentPhotoVO;
	}

	public SpaceCommentPhotoVO updateSpaceCommentPhoto(String spaceCommentPhotoId, String orderId, String spacePhoto) {

		SpaceCommentPhotoVO spaceCommentPhotoVO = new SpaceCommentPhotoVO();

		spaceCommentPhotoVO.setSpaceCommentPhotoId(spaceCommentPhotoId);
		spaceCommentPhotoVO.setOrderId(orderId);
		spaceCommentPhotoVO.setSpacePhoto(spacePhoto);
		dao.update(spaceCommentPhotoVO);

		return spaceCommentPhotoVO;
	}

	public void deleteEventPhoto(String spaceCommentPhotoId) {
		dao.delete(spaceCommentPhotoId);
	}

	public SpaceCommentPhotoVO getOneEventPhoto(String spaceCommentPhotoId) {
		return dao.findByPrimaryKey(spaceCommentPhotoId);
	}

	public List<SpaceCommentPhotoVO> getAll() {
		return dao.getAll();
	}
}


