package com.news.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class NewsService {

	private NewsDAO_interface newsDao;

	public NewsService() {
		newsDao = new NewsDAO();
	}

	
	
	public NewsVO addNews(String newsTitle, String newsCategoryId, String adminId, String newsContent, Timestamp newsStartDate,
			Timestamp newsEndDate, Integer newsStatusId, byte[] newsImg ) throws SQLException {

		NewsVO newsVO = new NewsVO();

		Timestamp currentTime = new Timestamp(System.currentTimeMillis()); 
		
		
		newsVO.setNewsId(newsDao.getNextNewsId());
		newsVO.setNewsTitle(newsTitle);
		newsVO.setNewsContent(newsContent);
		newsVO.setNewsStartDate(newsStartDate);
		newsVO.setNewsEndDate(newsEndDate);
		newsVO.setNewsImg(newsImg);
		newsVO.setCreatedTime(currentTime);
		newsVO.setNewsCategoryId(newsCategoryId);
		newsVO.setAdminId(adminId);
		newsVO.setNewsStatusId(newsStatusId);
		
		newsDao.insert(newsVO);

		return newsVO;
	}

	public NewsVO updateNews(String newsId, String newsTitle, String newsCategoryId, String adminId, String newsContent, Timestamp newsStartDate,
			Timestamp newsEndDate, Integer newsStatusId, byte[] newsImg) {

		NewsVO newsVO = new NewsVO();

		Timestamp currentTime = new Timestamp(System.currentTimeMillis()); 
		
		newsVO.setNewsId(newsId);
		newsVO.setNewsTitle(newsTitle);
		newsVO.setNewsContent(newsContent);
		newsVO.setNewsStartDate(newsStartDate);
		newsVO.setNewsEndDate(newsEndDate);
		newsVO.setNewsImg(newsImg);
		newsVO.setCreatedTime(currentTime);
		newsVO.setNewsCategoryId(newsCategoryId);
		newsVO.setAdminId(adminId);
		newsVO.setNewsStatusId(newsStatusId);

		newsDao.update(newsVO);
		
		return newsVO;
	}

	public void deleteNews(String newsId) {
		newsDao.delete(newsId);
	}

	public NewsVO getOneNews(String newsId) {
		return newsDao.findByPrimaryKey(newsId);
	}

	public List<NewsVO> getAll() {
		return newsDao.getAll();
	}
	

    public List<NewsVO> getNewsByPage(String title, Integer categoryId, Integer statusId, int page, int recordsPerPage) {
        return newsDao.getNewsByPage(title, categoryId, statusId, page, recordsPerPage);
    }

    public int getNewsCount(String title, Integer categoryId, Integer statusId) {
        return newsDao.getNewsCount(title, categoryId, statusId);
    }
}
