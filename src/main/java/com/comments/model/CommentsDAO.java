package com.comments.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class CommentsDAO implements CommentsDAO_interface{
	
	// 一個應用程式中,針對一個資料庫 ,共用一個DataSource即可
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TestDB2");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private static final String INSERT_STMT = 
		"INSERT INTO comments (comment_id,event_member_id,comment_hide,comment_message,comment_time) VALUES (?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT comment_id,event_member_id,comment_hide,comment_message,comment_time FROM comments order by comment_time DESC";
	private static final String GET_ONE_STMT = 
		"SELECT comment_id,event_member_id,comment_hide,comment_message,comment_time FROM comments where comment_id = ?";
	private static final String DELETE = 
		"DELETE FROM comments where comment_id = ?";
	private static final String UPDATE = 
		"UPDATE comments set comment_id=?, event_member_id=?, comment_hide=?, comment_message=?, comment_time=? where comment_id = ?";
		
	
	@Override
	public void insert(CommentsVO commentsVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, commentsVO.getCommentId());
			pstmt.setString(2, commentsVO.getEventMemberId());
			pstmt.setInt(3, commentsVO.getCommentHide());
			pstmt.setString(4, commentsVO.getCommentMessage());
			pstmt.setTimestamp(5, commentsVO.getCommentTime());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}
	@Override
	public void update(CommentsVO commentsVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, commentsVO.getCommentId());
			pstmt.setString(2, commentsVO.getEventMemberId());
			pstmt.setInt(3, commentsVO.getCommentHide());
			pstmt.setString(4, commentsVO.getCommentMessage());
			pstmt.setTimestamp(5, commentsVO.getCommentTime());
			pstmt.setString(6, commentsVO.getCommentId());
			
			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}
	@Override
	public void delete(String commentId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, commentId);

			pstmt.executeUpdate();
			
			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}
	@Override
	public CommentsVO findByPrimaryKey(String commentId) {
		
		CommentsVO commentsVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setString(1, commentId);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				commentsVO = new CommentsVO();
				commentsVO.setCommentId(rs.getString("comment_id"));
				commentsVO.setEventMemberId(rs.getString("event_member_id"));
				commentsVO.setCommentHide(rs.getInt("comment_hide"));
				commentsVO.setCommentMessage(rs.getString("comment_message"));
				commentsVO.setCommentTime(rs.getTimestamp("comment_time"));
			}
			
			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return commentsVO;
	}
	@Override
	public List<CommentsVO> getAll() {

		List<CommentsVO> list = new ArrayList<CommentsVO>();
		CommentsVO commentsVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				// empVO 也稱為 Domain objects
				commentsVO = new CommentsVO();
				commentsVO.setCommentId(rs.getString("comment_id"));
				commentsVO.setEventMemberId(rs.getString("event_member_id"));
				commentsVO.setCommentHide(rs.getInt("comment_hide"));
				commentsVO.setCommentMessage(rs.getString("comment_message"));
				commentsVO.setCommentTime(rs.getTimestamp("comment_time"));
				list.add(commentsVO); // Store the row in the list
			}
			
			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}
}
