package com.eventmember.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EventMemberJDBCDAO implements EventMemberDAO_interface{
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/life_space_01?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";
	private static final String INSERT_STMT = 
		"INSERT INTO event_member(event_member_id,event_id,member_id,participate_status) VALUES (?, ?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT event_member_id, event_id, member_id, participate_status, participated_time FROM event_member order by event_member_id";
	private static final String GET_ONE_STMT = 
		"SELECT event_member_id, event_id, member_id, participate_status, participated_time FROM event_member where event_member_id = ?";
	private static final String DELETE = 
		"DELETE FROM event_member where event_member_id = ?";
	private static final String UPDATE = 
		"UPDATE event_member set event_id=?, member_id=?, participate_status=? where event_member_id = ?";
	
	// 獲取下一個流水號
    private String getNextEventMemberId() throws SQLException {
        String nextId = "EM001"; // 預設初始值
        String pref = "EM";  // 改成你的表格的流水號開頭

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
        }

        String query = "SELECT MAX(event_member_id) FROM event_member";
        try (
            Connection conn = DriverManager.getConnection(url, userid, passwd);
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()
        ) {
            if (rs.next() && rs.getString(1) != null) {
                String currentId = rs.getString(1);
                int numericPart = Integer.parseInt(currentId.substring(2));
                numericPart++;
                nextId = pref + String.format("%03d", numericPart);
            }
        } catch (SQLException se) {
            throw new RuntimeException("A database error occurred. " + se.getMessage());
        }
        return nextId;
    }

	@Override
	public void insert(EventMemberVO eventMemberVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, getNextEventMemberId());
			pstmt.setString(2, eventMemberVO.getEventId());
			pstmt.setString(3, eventMemberVO.getMemberId());
			pstmt.setInt(4, eventMemberVO.getParticipateStatus());


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
	public void update(EventMemberVO eventMemberVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, eventMemberVO.getEventId());
			pstmt.setString(2, eventMemberVO.getMemberId());
			pstmt.setInt(3, eventMemberVO.getParticipateStatus());
			pstmt.setString(4, eventMemberVO.getEventMemberId());

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
	public void delete(String eventMemberId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, eventMemberId);

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
	public EventMemberVO findByPrimaryKey(String eventMemberId) {

		EventMemberVO eventMemberVO= null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, eventMemberId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				eventMemberVO = new EventMemberVO();
				eventMemberVO.setEventMemberId(rs.getString("event_member_id"));
				eventMemberVO.setEventId(rs.getString("event_id"));
				eventMemberVO.setMemberId(rs.getString("member_id"));
				eventMemberVO.setParticipateStatus(rs.getInt("participate_status"));
				eventMemberVO.setParticipatedTime(rs.getTimestamp("participated_time"));
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

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				eventMemberVO = new EventMemberVO();
				eventMemberVO.setEventMemberId(rs.getString("event_member_id"));
				eventMemberVO.setEventId(rs.getString("event_id"));
				eventMemberVO.setMemberId(rs.getString("member_id"));
				eventMemberVO.setParticipateStatus(rs.getInt("participate_status"));
				eventMemberVO.setParticipatedTime(rs.getTimestamp("participated_time"));
				list.add(eventMemberVO); // Store the row in the list
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

		EventMemberJDBCDAO dao = new EventMemberJDBCDAO();

		// 新增
		EventMemberVO eventMemberVO1 = new EventMemberVO();
		eventMemberVO1.setEventMemberId("EM006");
		eventMemberVO1.setEventId("E005");
		eventMemberVO1.setMemberId("M002");
		eventMemberVO1.setParticipateStatus(1);
		dao.insert(eventMemberVO1);
		System.out.println("新增成功");

		// 修改
		EventMemberVO eventMemberVO2 = new EventMemberVO();
		eventMemberVO2.setEventMemberId("EM001");
		eventMemberVO2.setEventId("E001");
		eventMemberVO2.setMemberId("M001");
		eventMemberVO2.setParticipateStatus(0);
		dao.update(eventMemberVO2);
		System.out.println("更新成功");
		
		// 刪除
//		dao.delete("EM005");

		// 查詢
		EventMemberVO eventMemberVO = dao.findByPrimaryKey("EM001");
		System.out.print(eventMemberVO.getEventMemberId() + ",");
		System.out.print(eventMemberVO.getEventId() + ",");
		System.out.print(eventMemberVO.getMemberId() + ",");
		System.out.print(eventMemberVO.getParticipateStatus() + ",");
		System.out.println(eventMemberVO.getParticipatedTime());
		
		System.out.println("---------------------");

		// 查詢
		List<EventMemberVO> list = dao.getAll();
		for (EventMemberVO aEventMember : list) {
			System.out.print(aEventMember.getEventMemberId() + ",");
			System.out.print(aEventMember.getEventId() + ",");
			System.out.print(aEventMember.getMemberId() + ",");
			System.out.print(aEventMember.getParticipateStatus() + ",");
			System.out.print(aEventMember.getParticipatedTime() + ",");
		
			System.out.println();
		}
	}
}
	