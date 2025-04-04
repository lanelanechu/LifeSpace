package com.lifespace.entity;

import java.sql.Timestamp;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity	//要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "news_category")
public class NewsCategoryVO {

	@Id
	@Column(name = "news_category_id")
	//@GeneratedValue(generator = "news_category_id")
	//@GenericGenerator(name = "news_category_id", strategy = "com.lifespace.util.NewsCategoryCustomStringIdGenerator")
	private String newsCategoryId;
	
	@Column(name = "category_name")
	private String categoryName;
	
	@Column(name = "created_time")
	private Timestamp createdTime;
	
	// fetch 預設為 LAZY
	@OneToMany(mappedBy = "newsCategory",cascade = CascadeType.ALL)
	@OrderBy("news_id asc")
	@JsonManagedReference //搭配使用，保留輸出的那一端
	private Set<NewsVO> newsSet;
	
	public NewsCategoryVO() {
		super();
		
	}
	
	public NewsCategoryVO(String newsCategoryId, String categoryName, Timestamp createdTime) {
		super();
		this.newsCategoryId = newsCategoryId;
		this.categoryName = categoryName;
		this.createdTime = createdTime;
	}


	public String getNewsCategoryId() {
		return newsCategoryId;
	}
	
	public void setNewsCategoryId(String newsCategoryId) {
		this.newsCategoryId = newsCategoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	public Set<NewsVO> getNewsSet() {
		return newsSet;
	}
	public void setNewsSet(Set<NewsVO> newsSet) {
		this.newsSet = newsSet;
	}
	
}
