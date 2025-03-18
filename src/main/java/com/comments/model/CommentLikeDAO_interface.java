package com.comments.model;

import java.util.List;

public interface CommentLikeDAO_interface {
	public void insert(CommentLikeVO commentLikeVO);
    public void update(CommentLikeVO commentLikeVO);
    public void delete(Integer likeId);
    public CommentLikeVO findByPrimaryKey(Integer likeId);
    public List<CommentLikeVO> getAll();
}
