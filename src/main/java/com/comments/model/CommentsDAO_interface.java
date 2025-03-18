package com.comments.model;

import java.util.*;

public interface CommentsDAO_interface {
	public void insert(CommentsVO commentsVO);
    public void update(CommentsVO commentsVO);
    public void delete(String commentId);
    public CommentsVO findByPrimaryKey(String commentId);
    public List<CommentsVO> getAll();
    //public List<CommentsVO> findByMemberId(String eventMemberId);
}