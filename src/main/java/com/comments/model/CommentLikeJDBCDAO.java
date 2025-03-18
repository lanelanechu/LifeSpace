package com.comments.model;

public class CommentLikeJDBCDAO {
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/life_space_01?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";

	private static final String INSERT_STMT = 
		"INSERT INTO comment_like (like_id,member_id,comment_id,created_time) VALUES (?, ?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT like_id,member_id,comment_id,created_time FROM comment_like order by created_time DESC";
	private static final String GET_ONE_STMT = 
		"SELECT like_id,member_id,comment_id,created_time FROM comment_like where like_id = ?";
	private static final String DELETE = 
		"DELETE FROM comment_id where like_id = ?";
	private static final String UPDATE = 
		"UPDATE comment_like set like_id=?, member_id=?, comment_id=?, created_time=? where like_id = ?";
	
	
}
