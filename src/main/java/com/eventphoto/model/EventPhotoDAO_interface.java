package com.eventphoto.model;

import java.util.List;

public interface EventPhotoDAO_interface {

	 public void insert(EventPhotoVO eventPhotoVO);
     public void update(EventPhotoVO eventPhotoVO);
     public void delete(String photoId);
     public EventPhotoVO findByPrimaryKey(String photoId);
     public List<EventPhotoVO> getAll();
     //萬用複合查詢(傳入參數型態Map)(回傳 List)
//   public List<EventPhotoVO> getAll(Map<String, String[]> map); 
}
