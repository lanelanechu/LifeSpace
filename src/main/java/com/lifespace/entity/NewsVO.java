package com.lifespace.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity	//要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "news")
public class NewsVO {

	@Id
	@Column(name = "news_id")
	//@GeneratedValue(generator = "news_id")
	//@GenericGenerator(name = "news_id", strategy = "com.lifespace.util.NewsCustomStringIdGenerator")
	private String newsId;

	@Column(name = "news_title")
	private String newsTitle;

	@ManyToOne
	@JoinColumn(name = "news_category_id_fk",referencedColumnName = "news_category_id")
	@JsonBackReference  //避免雙向關聯導致無限遞迴（會略過該欄位的輸出）
	private NewsCategoryVO newsCategory;

	@ManyToOne
	@JoinColumn(name = "news_status_id_fk",referencedColumnName = "news_status_id")
	@JsonBackReference 
	private NewsStatusVO newsStatus;

	@Column(name = "admin_id_fk")
	private String adminId;

	@Column(name = "news_content")
	private String newsContent;

	@Column(name = "news_start_date")
	private Timestamp newsStartDate;

	@Column(name = "news_end_date")
	private Timestamp newsEndDate;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "news_img")
	private byte[] newsImg;
	
	
	public String getNewsId() {
		return newsId;
	}
	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}
	public String getNewsTitle() {
		return newsTitle;
	}
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public NewsCategoryVO getNewsCategory() {
		return newsCategory;
	}
	public void setNewsCategory(NewsCategoryVO newsCategory) {
		this.newsCategory = newsCategory;
	}
	public String getNewsContent() {
		return newsContent;
	}
	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}
	public Timestamp getNewsStartDate() {
		return newsStartDate;
	}
	public void setNewsStartDate(Timestamp newsStartDate) {
		this.newsStartDate = newsStartDate;
	}
	public Timestamp getNewsEndDate() {
		return newsEndDate;
	}
	public void setNewsEndDate(Timestamp newsEndDate) {
		this.newsEndDate = newsEndDate;
	}
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	public NewsStatusVO getNewsStatus() {
		return newsStatus;
	}
	public void setNewsStatus(NewsStatusVO newsStatus) {
		this.newsStatus = newsStatus;
	}
	public byte[] getNewsImg() {
		return newsImg;
	}
	public void setNewsImg(byte[] newsImg) {
		this.newsImg = newsImg;
	}
	
	
}
