package com.comments.model;

import java.util.List;

public interface CommentReportDAO_interface {
	public void insert(CommentReportVO commentReportVO);
    public void update(CommentReportVO commentReportVO);
    public void delete(String reportId);
    public CommentReportVO findByPrimaryKey(String reportId);
    public List<CommentReportVO> getAll();
}
