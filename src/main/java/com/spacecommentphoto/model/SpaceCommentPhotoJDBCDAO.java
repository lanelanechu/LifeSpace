package com.spacecommentphoto.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eventphoto.model.EventPhotoJDBCDAO;
import com.eventphoto.model.EventPhotoVO;

public class SpaceCommentPhotoJDBCDAO implements SpaceCommentPhotoDAO_interface{
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/life_space_01?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";

	private static final String INSERT_STMT = 
		"INSERT INTO space_comment_photo (space_comment_photo_id,order_id,space_photo) VALUES (?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT space_comment_photo_id,order_id,space_photo,created_time FROM space_comment_photo order by space_comment_photo_id";
	private static final String GET_ONE_STMT = 
		"SELECT space_comment_photo_id,order_id,space_photo,created_time FROM space_comment_photo where space_comment_photo_id = ?";
	private static final String DELETE = 
		"DELETE FROM space_comment_photo where space_comment_photo_id = ?";
	private static final String UPDATE = 
		"UPDATE space_comment_photo set order_id=?, space_photo=? where space_comment_photo_id = ?";
	
	// 獲取下一個流水號
    private String getSpaceCommentPhotoId() throws SQLException {
        String nextId = "SC001"; // 預設初始值
        String pref = "SC";  // 改成你的表格的流水號開頭

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
        }

        String query = "SELECT MAX(space_comment_photo_id) FROM space_comment_photo";
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
	public void insert(SpaceCommentPhotoVO spaceCommentPhotoVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, getSpaceCommentPhotoId());
			pstmt.setString(2, spaceCommentPhotoVO.getOrderId());
			pstmt.setString(3, spaceCommentPhotoVO.getSpacePhoto());
			
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
	public void update(SpaceCommentPhotoVO spaceCommentPhotoVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, spaceCommentPhotoVO.getOrderId());
			pstmt.setString(2, spaceCommentPhotoVO.getSpacePhoto());
			pstmt.setString(3, spaceCommentPhotoVO.getSpaceCommentPhotoId());
	
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
	public void delete(String spaceCommentPhotoId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, spaceCommentPhotoId);

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
	public SpaceCommentPhotoVO findByPrimaryKey(String spaceCommentPhotoId) {

		SpaceCommentPhotoVO spaceCommentPhotoVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, spaceCommentPhotoId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				spaceCommentPhotoVO = new SpaceCommentPhotoVO();
				spaceCommentPhotoVO.setSpaceCommentPhotoId(rs.getString("space_comment_photo_id"));
				spaceCommentPhotoVO.setOrderId(rs.getString("order_id"));
				spaceCommentPhotoVO.setSpacePhoto(rs.getString("space_photo"));
				spaceCommentPhotoVO.setCreatedTime(rs.getTimestamp("created_time"));
				
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

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				spaceCommentPhotoVO = new SpaceCommentPhotoVO();
				spaceCommentPhotoVO.setSpaceCommentPhotoId(rs.getString("space_comment_photo_id"));
				spaceCommentPhotoVO.setOrderId(rs.getString("order_id"));
				spaceCommentPhotoVO.setSpacePhoto(rs.getString("space_photo"));
				spaceCommentPhotoVO.setCreatedTime(rs.getTimestamp("created_time"));
				list.add(spaceCommentPhotoVO); // Store the row in the list
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

		SpaceCommentPhotoJDBCDAO dao = new SpaceCommentPhotoJDBCDAO();

		// 新增
		SpaceCommentPhotoVO spaceCommentPhotoVO1 = new SpaceCommentPhotoVO();
		spaceCommentPhotoVO1.setSpaceCommentPhotoId("SC006");
		spaceCommentPhotoVO1.setOrderId("OR005");
		spaceCommentPhotoVO1.setSpacePhoto("C:/Users/Tibame/Desktop/images/event_photo/event_photo_5.jpg");

		dao.insert(spaceCommentPhotoVO1);
		System.out.println("新增成功");
		
		// 修改
		SpaceCommentPhotoVO spaceCommentPhotoVO2 = new SpaceCommentPhotoVO();
		spaceCommentPhotoVO2.setSpaceCommentPhotoId("SC005");
		spaceCommentPhotoVO2.setOrderId("OR005");
		spaceCommentPhotoVO2.setSpacePhoto("C:/Users/Tibame/Desktop/images/event_photo/event_photo_5.jpg");
		
		dao.update(spaceCommentPhotoVO2);
		System.out.println("修改成功");
		
		// 刪除	
		dao.delete("SC005");
		System.out.println("刪除成功");

		// 查詢
		SpaceCommentPhotoVO spaceCommentPhotoVO3 = dao.findByPrimaryKey("SC001");
		System.out.print(spaceCommentPhotoVO3.getSpaceCommentPhotoId() + ",");
		System.out.print(spaceCommentPhotoVO3.getOrderId() + ",");
		System.out.print(spaceCommentPhotoVO3.getSpacePhoto() + ",");
		System.out.println(spaceCommentPhotoVO3.getCreatedTime());
		System.out.println("---------------------");

		// 查詢
		List<SpaceCommentPhotoVO> list = dao.getAll();
		for (SpaceCommentPhotoVO aSpaceCommentPhotoVO : list) {
			System.out.print(aSpaceCommentPhotoVO.getSpaceCommentPhotoId() + ",");
			System.out.print(aSpaceCommentPhotoVO.getOrderId() + ",");
			System.out.print(aSpaceCommentPhotoVO.getSpacePhoto() + ",");
			System.out.print(aSpaceCommentPhotoVO.getCreatedTime());
			System.out.println();
		}
	}

}
