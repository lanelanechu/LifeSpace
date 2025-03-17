package com.eventphoto.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EventPhotoJDBCDAO implements EventPhotoDAO_interface {
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/life_space_01?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";

	private static final String INSERT_STMT = 
		"INSERT INTO event_photo (photo_id,event_id,photo) VALUES (?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT photo_id,event_id,photo,created_time FROM event_photo order by photo_id";
	private static final String GET_ONE_STMT = 
		"SELECT photo_id,event_id,photo,created_time FROM event_photo where photo_id = ?";
	private static final String DELETE = 
		"DELETE FROM event_photo where photo_id = ?";
	private static final String UPDATE = 
		"UPDATE event_photo set event_id=?, photo=? where photo_id = ?";

	// 獲取下一個流水號
    private String getNextEventPhotoId() throws SQLException {
        String nextId = "P001"; // 預設初始值
        String pref = "P";  // 改成你的表格的流水號開頭

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
        }

        String query = "SELECT MAX(photo_id) FROM event_photo";
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
	public void insert(EventPhotoVO eventPhotoVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, getNextEventPhotoId());
			pstmt.setString(2, eventPhotoVO.getEventId());
			pstmt.setString(3, eventPhotoVO.getPhoto());
			
			

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
	public void update(EventPhotoVO eventPhotoVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, eventPhotoVO.getEventId());
			pstmt.setString(2, eventPhotoVO.getPhoto());
			pstmt.setString(3, eventPhotoVO.getPhotoId());
	
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
	public void delete(String photoId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, photoId);

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
	public EventPhotoVO findByPrimaryKey(String photoId) {

		EventPhotoVO eventPhotoVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, photoId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				eventPhotoVO = new EventPhotoVO();
				eventPhotoVO.setPhotoId(rs.getString("photo_id"));
				eventPhotoVO.setEventId(rs.getString("event_id"));
				eventPhotoVO.setPhoto(rs.getString("photo"));
				eventPhotoVO.setCreatedTime(rs.getTimestamp("created_time"));
				
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
		return eventPhotoVO;
	}

	@Override
	public List<EventPhotoVO> getAll() {
		List<EventPhotoVO> list = new ArrayList<EventPhotoVO>();
		EventPhotoVO eventPhotoVO = null;

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
				eventPhotoVO = new EventPhotoVO();
				eventPhotoVO.setPhotoId(rs.getString("photo_id"));
				eventPhotoVO.setEventId(rs.getString("event_id"));
				eventPhotoVO.setPhoto(rs.getString("photo"));
				eventPhotoVO.setCreatedTime(rs.getTimestamp("created_time"));
				list.add(eventPhotoVO); // Store the row in the list
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

		EventPhotoJDBCDAO dao = new EventPhotoJDBCDAO();

		// 新增
		EventPhotoVO eventPhotoVO1 = new EventPhotoVO();
//		eventPhotoVO1.setPhotoId("P011");
		eventPhotoVO1.setEventId("E001");
		eventPhotoVO1.setPhoto("C:/Users/Tibame/Desktop/images/event_photo/event_photo_5.jpg");

		dao.insert(eventPhotoVO1);
		System.out.println("新增成功");
		
		// 修改
		EventPhotoVO eventPhotoVO2 = new EventPhotoVO();
		eventPhotoVO2.setPhotoId("P001");
		eventPhotoVO2.setEventId("E001");
		eventPhotoVO2.setPhoto("C:/Users/Tibame/Desktop/images/event_photo/event_photo_5.jpg");
		
		dao.update(eventPhotoVO2);
		System.out.println("修改成功");
		
		// 刪除	
		dao.delete("P005");
		System.out.println("刪除成功");

		// 查詢
		EventPhotoVO eventPhotoVO3 = dao.findByPrimaryKey("P001");
		System.out.print(eventPhotoVO3.getPhotoId() + ",");
		System.out.print(eventPhotoVO3.getEventId() + ",");
		System.out.print(eventPhotoVO3.getPhoto() + ",");
		System.out.println(eventPhotoVO3.getCreatedTime());
		System.out.println("---------------------");

		// 查詢
		List<EventPhotoVO> list = dao.getAll();
		for (EventPhotoVO aEventPhotoVO : list) {
			System.out.print(aEventPhotoVO.getPhotoId() + ",");
			System.out.print(aEventPhotoVO.getEventId() + ",");
			System.out.print(aEventPhotoVO.getPhoto() + ",");
			System.out.print(aEventPhotoVO.getCreatedTime());
			System.out.println();
		}
	}

}
