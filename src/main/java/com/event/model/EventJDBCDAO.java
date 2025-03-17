package com.event.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.orders.model.OrdersVO;


public class EventJDBCDAO implements EventDAO_interface{
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/life_space_01?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";
	
	private static final String INSERT_STMT = 
			"INSERT INTO event (event_id,event_name,event_date,event_start_time,event_end_time,event_category,space_id,member_id,number_of_participants,maximum_of_participants,event_briefing,remarks,host_speaking) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		private static final String GET_ALL_STMT = 
			"SELECT event_id,event_name,event_date,event_start_time,event_start_time,event_end_time,event_category,space_id,member_id,number_of_participants,maximum_of_participants,event_briefing,remarks,host_speaking,created_time FROM event order by event_id";
		private static final String GET_ONE_STMT = 
			"SELECT event_id,event_name,event_date,event_start_time,event_start_time,event_end_time,event_category,space_id,member_id,number_of_participants,maximum_of_participants,event_briefing,remarks,host_speaking,created_time FROM event where event_id = ?";
		private static final String DELETE = 
			"DELETE FROM event where event_id = ?";
		private static final String UPDATE = 
			"UPDATE event set event_name=?, event_date=?, event_start_time=?, event_end_time=?, event_category=?, space_id=?, member_id=?, number_of_participants=?, maximum_of_participants=?, event_briefing=?, remarks=?, host_speaking=? where event_id = ?";

		// 獲取下一個流水號
		private String getNextEventId() throws SQLException {
		    String nextId = "E001"; // 預設初始值
		    String pref = "E";  // 改成你的表格的流水號開頭

		    try {
		        Class.forName(driver);
		    } catch (ClassNotFoundException e) {
		        throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		    }
		    
		    String query = "SELECT MAX(event_id) FROM event";
		    try (
		        Connection conn = DriverManager.getConnection(url, userid, passwd);
		        PreparedStatement pstmt = conn.prepareStatement(query);
		        ResultSet rs = pstmt.executeQuery()
		    ) {
		        if (rs.next() && rs.getString(1) != null) {
		            String currentId = rs.getString(1);
		            int numericPart = Integer.parseInt(currentId.substring(1));
		            numericPart++;
		            nextId = pref + String.format("%03d", numericPart);
		        }
		    } catch (SQLException se) {
		        throw new RuntimeException("A database error occurred. " + se.getMessage());
		    }
		    return nextId;
		}
		
		@Override
		public void insert(EventVO eventVO) {

			Connection con = null;
			PreparedStatement pstmt = null;

			try {

				Class.forName(driver);
				con = DriverManager.getConnection(url, userid, passwd);
				pstmt = con.prepareStatement(INSERT_STMT);

				pstmt.setString(1, getNextEventId());  // 獲取下一個流水號
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

				// Handle any driver errors
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Couldn't load database driver. "
						+ e.getMessage());
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
		public void update(EventVO eventVO) {

			Connection con = null;
			PreparedStatement pstmt = null;

			try {

				Class.forName(driver);
				con = DriverManager.getConnection(url, userid, passwd);
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
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Couldn't load database driver. "
						+ e.getMessage());
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
		public void delete(String eventId) {

			Connection con = null;
			PreparedStatement pstmt = null;

			try {

				Class.forName(driver);
				con = DriverManager.getConnection(url, userid, passwd);
				pstmt = con.prepareStatement(DELETE);

				pstmt.setString(1, eventId);

				pstmt.executeUpdate();

				// Handle any driver errors
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Couldn't load database driver. "
						+ e.getMessage());
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
		public EventVO findByPrimaryKey(String eventId) {

			EventVO eventVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {

				Class.forName(driver);
				con = DriverManager.getConnection(url, userid, passwd);
				pstmt = con.prepareStatement(GET_ONE_STMT);

				pstmt.setString(1, eventId);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					// empVo 也稱為 Domain objects
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
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Couldn't load database driver. "
						+ e.getMessage());
				// Handle any SQL errors
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

				Class.forName(driver);
				con = DriverManager.getConnection(url, userid, passwd);
				pstmt = con.prepareStatement(GET_ALL_STMT);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					// empVO 也稱為 Domain objects
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
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Couldn't load database driver. "
						+ e.getMessage());
				// Handle any SQL errors
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

		public static void main(String[] args) {

			EventJDBCDAO dao = new EventJDBCDAO();

			// 新增
			EventVO eventVO1= new EventVO();
			eventVO1.setEventId("E006");
			eventVO1.setEventName("66666終極無敵狼人殺");
			eventVO1.setEventDate(Timestamp.valueOf("2025-02-28 15:00:00"));
			eventVO1.setEventStartTime(Timestamp.valueOf("2025-02-28 19:00:00"));
			eventVO1.setEventEndTime(Timestamp.valueOf("2025-02-28 21:00:00"));
			eventVO1.setEventCategory("真人遊戲");
			eventVO1.setSpaceId("S001");
			eventVO1.setMemberId("M001");
			eventVO1.setNumberOfParticipants(12);
			eventVO1.setMaximumOfParticipants(20);
			eventVO1.setEventBriefing("刺激好玩多人混戰");
			eventVO1.setRemarks("啥都不用帶");
			eventVO1.setHostSpeaking("一起來認識認識");
			dao.insert(eventVO1);
			
			System.out.println("新增成功");
			
			// 修改
			EventVO eventVO2= new EventVO();
			eventVO2.setEventId("E001");
			eventVO2.setEventName("終極無敵狼人殺");
			eventVO2.setEventDate(Timestamp.valueOf("2025-02-28 15:00:00"));
			eventVO2.setEventStartTime(Timestamp.valueOf("2025-02-28 19:00:00"));
			eventVO2.setEventEndTime(Timestamp.valueOf("2025-02-28 21:00:00"));
			eventVO2.setEventCategory("真人遊戲");
			eventVO2.setSpaceId("S001");
			eventVO2.setMemberId("M001");
			eventVO2.setNumberOfParticipants(12);
			eventVO2.setMaximumOfParticipants(20);
			eventVO2.setEventBriefing("刺激好玩多人混戰");
			eventVO2.setRemarks("啥都不用帶");
			eventVO2.setHostSpeaking("一起來認識認識");
			dao.update(eventVO2);
			
			System.out.println("修改成功");
			// 刪除
//			dao.delete("E005");

			// 查詢
			EventVO eventVO3 = dao.findByPrimaryKey("E001");
			System.out.print(eventVO3.getEventId() + ",");
			System.out.print(eventVO3.getEventName() + ",");
			System.out.print(eventVO3.getEventDate() + ",");
			System.out.print(eventVO3.getEventStartTime() + ",");
			System.out.print(eventVO3.getEventEndTime() + ",");
			System.out.print(eventVO3.getEventCategory() + ",");
			System.out.print(eventVO3.getSpaceId() + ",");
			System.out.print(eventVO3.getMemberId() + ",");
			System.out.print(eventVO3.getNumberOfParticipants() + ",");
			System.out.print(eventVO3.getMaximumOfParticipants() + ",");
			System.out.print(eventVO3.getEventBriefing() + ",");
			System.out.print(eventVO3.getRemarks() + ",");
			System.out.print(eventVO3.getHostSpeaking() + ",");
			System.out.println(eventVO3.getCreatedTime());
			System.out.println("---------------------");

			// 查詢
			List<EventVO> list = dao.getAll();
			for (EventVO aEvent : list) {
				
			System.out.print(aEvent.getEventId() + ",");
			System.out.print(aEvent.getEventName() + ",");
			System.out.print(aEvent.getEventDate() + ",");
			System.out.print(aEvent.getEventStartTime() + ",");
			System.out.print(aEvent.getEventEndTime() + ",");
			System.out.print(aEvent.getEventCategory() + ",");
			System.out.print(aEvent.getSpaceId() + ",");
			System.out.print(aEvent.getMemberId() + ",");
			System.out.print(aEvent.getNumberOfParticipants() + ",");
			System.out.print(aEvent.getMaximumOfParticipants() + ",");
			System.out.print(aEvent.getEventBriefing() + ",");
			System.out.print(aEvent.getRemarks() + ",");
			System.out.print(aEvent.getHostSpeaking() + ",");
			System.out.print(aEvent.getCreatedTime());
			System.out.println();
			}
		}
}
