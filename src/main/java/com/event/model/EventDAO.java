package com.event.model;

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

public class EventDAO implements EventDAO_interface {

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

	private static final String INSERT_STMT = "INSERT INTO event (event_id,event_name,event_date,event_start_time,event_end_time,event_category,space_id,member_id,number_of_participants,maximum_of_participants,event_briefing,remarks,host_speaking) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = "SELECT event_id,event_name,event_date,event_start_time,event_end_time,event_category,space_id,member_id,number_of_participants,maximum_of_participants,event_briefing,remarks,host_speaking,created_time FROM event order by event_id";
	private static final String GET_ONE_STMT = "SELECT event_id,event_name,event_date,event_start_time,event_end_time,event_category,space_id,member_id,number_of_participants,maximum_of_participants,event_briefing,remarks,host_speaking,created_time FROM event where event_id = ?";
	private static final String DELETE = "DELETE FROM event where event_id = ?";
	private static final String UPDATE = "UPDATE event set event_name=?, event_date=?, event_start_time=?, event_end_time=?, event_category=?, space_id=?, member_id=?, number_of_participants=?, maximum_of_participants=?, event_briefing=?, remarks=?, host_speaking=? where event_id = ?";

	@Override
	public void insert(EventVO eventVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, eventVO.getEventId());
			pstmt.setString(2, eventVO.getEventName());
			pstmt.setTimestamp(3, eventVO.getEventDate());
			pstmt.setTimestamp(4, eventVO.getEventStartTime());
			pstmt.setTimestamp(5, eventVO.getEventEndTime());
			pstmt.setString(6, eventVO.getEventCategory());
			pstmt.setString(7, eventVO.getSpaceId());
			pstmt.setString(8, eventVO.getMemberId());
			pstmt.setInt(9, eventVO.getNumberOfParticipants());
			pstmt.setInt(10, eventVO.getMaximumOfParticipants());
			pstmt.setString(11, eventVO.getEventBriefing());
			pstmt.setString(12, eventVO.getRemarks());
			pstmt.setString(13, eventVO.getHostSpeaking());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	public void update(EventVO eventVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, eventVO.getEventName());
			pstmt.setTimestamp(2, eventVO.getEventDate());
			pstmt.setTimestamp(3, eventVO.getEventStartTime());
			pstmt.setTimestamp(4, eventVO.getEventEndTime());
			pstmt.setString(5, eventVO.getEventCategory());
			pstmt.setString(6, eventVO.getSpaceId());
			pstmt.setString(7, eventVO.getMemberId());
			pstmt.setInt(8, eventVO.getNumberOfParticipants());
			pstmt.setInt(9, eventVO.getMaximumOfParticipants());
			pstmt.setString(10, eventVO.getEventBriefing());
			pstmt.setString(11, eventVO.getRemarks());
			pstmt.setString(12, eventVO.getHostSpeaking());
			pstmt.setString(13, eventVO.getEventId());

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	public void delete(String eventId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, eventId);

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	public EventVO findByPrimaryKey(String eventId) {

		EventVO eventVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, eventId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// eventVO 也稱為 Domain objects
				eventVO = new EventVO();
				eventVO.setEventId(rs.getString("event_id"));
				eventVO.setEventName(rs.getString("event_name"));
				eventVO.setEventDate(rs.getTimestamp("event_date"));
				eventVO.setEventStartTime(rs.getTimestamp("event_start_time"));
				eventVO.setEventEndTime(rs.getTimestamp("event_end_time"));
				eventVO.setEventCategory(rs.getString("event_category"));
				eventVO.setSpaceId(rs.getString("space_id"));
				eventVO.setMemberId(rs.getString("member_id"));
				eventVO.setNumberOfParticipants(rs.getInt("number_of_participants"));
				eventVO.setMaximumOfParticipants(rs.getInt("maximum_of_participants"));
				eventVO.setEventBriefing(rs.getString("event_briefing"));
				eventVO.setRemarks(rs.getString("remarks"));
				eventVO.setHostSpeaking(rs.getString("host_speaking"));
				eventVO.setCreatedTime(rs.getTimestamp("created_time"));
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
		return eventVO;
	}

	@Override
	public List<EventVO> getAll() {
		List<EventVO> list = new ArrayList<EventVO>();
		EventVO eventVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// eventVO 也稱為 Domain objects
				eventVO = new EventVO();
				eventVO.setEventId(rs.getString("event_id"));
				eventVO.setEventName(rs.getString("event_name"));
				eventVO.setEventDate(rs.getTimestamp("event_date"));
				eventVO.setEventStartTime(rs.getTimestamp("event_start_time"));
				eventVO.setEventEndTime(rs.getTimestamp("event_end_time"));
				eventVO.setEventCategory(rs.getString("event_category"));
				eventVO.setSpaceId(rs.getString("space_id"));
				eventVO.setMemberId(rs.getString("member_id"));
				eventVO.setNumberOfParticipants(rs.getInt("number_of_participants"));
				eventVO.setMaximumOfParticipants(rs.getInt("maximum_of_participants"));
				eventVO.setEventBriefing(rs.getString("event_briefing"));
				eventVO.setRemarks(rs.getString("remarks"));
				eventVO.setHostSpeaking(rs.getString("host_speaking"));
				eventVO.setCreatedTime(rs.getTimestamp("created_time"));
				list.add(eventVO); // Store the row in the list
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
		return list;
	}
}
