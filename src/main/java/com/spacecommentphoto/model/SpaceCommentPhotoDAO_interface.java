package com.spacecommentphoto.model;

import java.util.List;

import com.eventphoto.model.EventPhotoVO;

public interface SpaceCommentPhotoDAO_interface {

	 public void insert(SpaceCommentPhotoVO spaceCommentPhotoVO);
     public void update(SpaceCommentPhotoVO spaceCommentPhotoVO);
     public void delete(String spaceCommentPhotoId);
     public SpaceCommentPhotoVO findByPrimaryKey(String spaceCommentPhotoId);
     public List<SpaceCommentPhotoVO> getAll();
     //萬用複合查詢(傳入參數型態Map)(回傳 List)
//   public List<SpaceCommentPhotoVO> getAll(Map<String, String[]> map);
}
