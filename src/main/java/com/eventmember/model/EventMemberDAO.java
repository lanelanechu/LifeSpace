package com.eventmember.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class EventMemberDAO implements EventMemberDAO_interface {

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
		"INSERT INTO event_member (event_member_id, event_id, member_id, participate_status) VALUES (?, ?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT event_member_id, event_id, member_id, participate_status, participated_time FROM event_member order by event_member_id";
	private static final String GET_ONE_STMT = 
		"SELECT event_member_id, event_id, member_id, participate_status, participated_time FROM event_member where event_member_id = ?";
	private static final String DELETE = 
		"DELETE FROM event_member where event_member_id = ?";
	private static final String UPDATE = 
		"UPDATE event_member set event_id=?, member_id=?, participate_status=? where event_member_id = ?";
	
	

	@Override
	public void insert(EventMemberVO eventMemberVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, eventMemberVO.getEventMemberId());
			pstmt.setString(2, eventMemberVO.getEventId());
			pstmt.setString(3, eventMemberVO.getMemberId());
			pstmt.setInt(4, eventMemberVO.getParticipateStatus());

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
	public void update(EventMemberVO eventMemberVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, eventMemberVO.getEventId());
			pstmt.setString(2, eventMemberVO.getMemberId());
			pstmt.setInt(3, eventMemberVO.getParticipateStatus());
			pstmt.setString(4, eventMemberVO.getEventMemberId());

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
	public void delete(String eventMemberId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, eventMemberId);

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
	public EventMemberVO findByPrimaryKey(String eventMemberId) {

		EventMemberVO eventMemberVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, eventMemberId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// eventMemberVO 也稱為 Domain objects
				eventMemberVO = new EventMemberVO();
				eventMemberVO.setEventMemberId(rs.getString("event_member_id"));
				eventMemberVO.setEventId(rs.getString("event_id"));
				eventMemberVO.setMemberId(rs.getString("member_id"));
				eventMemberVO.setParticipateStatus(rs.getInt("participate_status"));
				eventMemberVO.setParticipatedTime(rs.getTimestamp("participated_time"));
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
		return eventMemberVO;
	}

	@Override
	public List<EventMemberVO> getAll() {
		List<EventMemberVO> list = new ArrayList<EventMemberVO>();
		EventMemberVO eventMemberVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// eventMemberVO 也稱為 Domain objects
				eventMemberVO = new EventMemberVO();
				eventMemberVO.setEventMemberId(rs.getString("event_member_id"));
				eventMemberVO.setEventId(rs.getString("event_id"));
				eventMemberVO.setMemberId(rs.getString("member_id"));
				eventMemberVO.setParticipateStatus(rs.getInt("participate_status"));
				eventMemberVO.setParticipatedTime(rs.getTimestamp("participated_time"));
				list.add(eventMemberVO); 
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