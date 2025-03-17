package com.spacecommentphoto.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class SpaceCommentPhotoDAO implements SpaceCommentPhotoDAO_interface {

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
		"INSERT INTO space_comment_photo (space_comment_photo_id, order_id, space_photo ) VALUES (?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT space_comment_photo_id, order_id, space_photo, created_time FROM space_comment_photo order by space_comment_photo_id";
	private static final String GET_ONE_STMT = 
		"SELECT space_comment_photo_id, order_id, space_photo, created_time FROM space_comment_photo where space_comment_photo_id = ?";
	private static final String DELETE = 
		"DELETE FROM space_comment_photo where space_comment_photo_id = ?";
	private static final String UPDATE = 
		"UPDATE space_comment_photo set order_id=?, space_photo=? where space_comment_photo_id = ?";

	@Override
	public void insert(SpaceCommentPhotoVO spaceCommentPhotoVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, spaceCommentPhotoVO.getSpaceCommentPhotoId());
			pstmt.setString(2, spaceCommentPhotoVO.getOrderId());
			pstmt.setString(3, spaceCommentPhotoVO.getSpacePhoto());

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
	public void update(SpaceCommentPhotoVO spaceCommentPhotoVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, spaceCommentPhotoVO.getOrderId());
			pstmt.setString(2, spaceCommentPhotoVO.getSpacePhoto());
			pstmt.setString(3, spaceCommentPhotoVO.getSpaceCommentPhotoId());

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
	public void delete(String spaceCommentPhotoId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, spaceCommentPhotoId);

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
	public SpaceCommentPhotoVO findByPrimaryKey(String spaceCommentPhotoId) {

		SpaceCommentPhotoVO spaceCommentPhotoVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, spaceCommentPhotoId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// spaceCommentPhotoVO 也稱為 Domain objects
				spaceCommentPhotoVO = new SpaceCommentPhotoVO();
				spaceCommentPhotoVO.setSpaceCommentPhotoId(rs.getString("space_comment_photo_id"));
				spaceCommentPhotoVO.setOrderId(rs.getString("order_id"));
				spaceCommentPhotoVO.setSpacePhoto(rs.getString("space_photo"));
				spaceCommentPhotoVO.setCreatedTime(rs.getTimestamp("created_time"));
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
		return spaceCommentPhotoVO;
	}

	@Override
	public List<SpaceCommentPhotoVO> getAll() {
		List<SpaceCommentPhotoVO> list = new ArrayList<SpaceCommentPhotoVO>();
		SpaceCommentPhotoVO spaceCommentPhotoVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// spaceCommentPhotoVO 也稱為 Domain objects
				spaceCommentPhotoVO = new SpaceCommentPhotoVO();
				spaceCommentPhotoVO.setSpaceCommentPhotoId(rs.getString("space_comment_photo_id"));
				spaceCommentPhotoVO.setOrderId(rs.getString("order_id"));
				spaceCommentPhotoVO.setSpacePhoto(rs.getString("space_photo"));
				spaceCommentPhotoVO.setCreatedTime(rs.getTimestamp("created_time"));
				list.add(spaceCommentPhotoVO); // Store the row in the list
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