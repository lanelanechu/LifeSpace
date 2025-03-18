package com.comments.model;

import java.util.List;


public class CommentsService {

	private CommentsDAO_interface dao;
	
	public CommentsService() {
		dao = new CommentsJDBCDAO();
	}
	
	public CommentsVO addComments(String eventMemberId, Integer commentHide,
			String commentMessage) {

		CommentsVO commentsVO = new CommentsVO();

		commentsVO.setEventMemberId(eventMemberId);
		commentsVO.setCommentHide(commentHide);
		commentsVO.setCommentMessage(commentMessage);
		dao.insert(commentsVO);

		return commentsVO;
	}

	public CommentsVO updateComments(String commentId, String eventMemberId,
			Integer commentHide, String commentMessage) {

		CommentsVO commentsVO = new CommentsVO();
		
		commentsVO.setCommentId(commentId);
		commentsVO.setEventMemberId(eventMemberId);
		commentsVO.setCommentHide(commentHide);
		commentsVO.setCommentMessage(commentMessage);
		dao.update(commentsVO);

		return commentsVO;
	}

	public void deleteComments(String commentId) {
		dao.delete(commentId);
	}

	public CommentsVO getOneComments(String commentId) {
		return dao.findByPrimaryKey(commentId);
	}

	public List<CommentsVO> getAll() {
		return dao.getAll();
	}
}
