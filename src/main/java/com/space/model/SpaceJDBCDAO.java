package com.space.model;

import java.util.*;
import java.sql.*;
import java.time.LocalDateTime;

public class SpaceJDBCDAO implements SpaceDAO_interface {
	public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String URL = "jdbc:mysql://localhost:3306/life_space_01?serverTimezone=Asia/Taipei";
	public static final String USER = "root";
	public static final String PASSWORD = "123456";

	private static final String INSERT_STMT = "INSERT INTO space (space_id, branch_id, space_name, space_people, space_size, space_hourly_fee, space_daily_fee, space_desc, space_alert, space_status, space_address, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = "SELECT space_id, branch_id, space_name, space_people, space_size, space_hourly_fee, space_daily_fee, space_desc, space_rating, space_alert, space_used_24hr, space_used_7d, space_status, space_address, latitude, longitude, created_time FROM space ORDER BY space_id";
	private static final String GET_ONE_STMT = "SELECT space_id, branch_id, space_name, space_people, space_size, space_hourly_fee, space_daily_fee, space_desc, space_rating, space_alert, space_used_24hr, space_used_7d, space_status, space_address, latitude, longitude, created_time FROM space where space_id = ?";
	private static final String DELETE = "DELETE FROM space where space_id = ?";
	private static final String UPDATE = "UPDATE space set branch_id=?, space_name=?, space_people=?, space_size=?, space_hourly_fee=?, space_daily_fee=?, space_desc=?, space_alert=?, space_status=?, space_address=?, latitude=?, longitude=? where space_id = ?";

	// 獲取下一個流水號
	private String getNextSpaceId() throws SQLException {
	    String nextId = "S001"; // 預設初始值
	    String pref = "S";  // 改成你的表格的流水號開頭

	    try {
	        Class.forName(DRIVER);
	    } catch (ClassNotFoundException e) {
	        throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
	    }
	    
	    String query = "SELECT MAX(space_id) FROM space";
	    try (
	        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
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
	public void insert(SpaceVO spaceVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, getNextSpaceId());  // 獲取下一個流水號
			pstmt.setString(2, spaceVO.getBranchId());
			pstmt.setString(3, spaceVO.getSpaceName());
			pstmt.setInt(4, spaceVO.getSpacePeople());
			pstmt.setDouble(5, spaceVO.getSpaceSize());
			pstmt.setInt(6, spaceVO.getSpaceHourlyFee());
			pstmt.setInt(7, spaceVO.getSpaceDailyFee());
			pstmt.setString(8, spaceVO.getSpaceDesc());
			pstmt.setString(9, spaceVO.getSpaceAlert());
			pstmt.setInt(10, spaceVO.getSpaceStatus());
			pstmt.setString(11, spaceVO.getSpaceAddress());
			pstmt.setDouble(12, spaceVO.getLatitude());
			pstmt.setDouble(13, spaceVO.getLongitude());

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
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
	public void update(SpaceVO spaceVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, spaceVO.getBranchId());
			pstmt.setString(2, spaceVO.getSpaceName());
			pstmt.setInt(3, spaceVO.getSpacePeople());
			pstmt.setDouble(4, spaceVO.getSpaceSize());
			pstmt.setInt(5, spaceVO.getSpaceHourlyFee());
			pstmt.setInt(6, spaceVO.getSpaceDailyFee());
			pstmt.setString(7, spaceVO.getSpaceDesc());
			pstmt.setString(8, spaceVO.getSpaceAlert());
			pstmt.setInt(9, spaceVO.getSpaceStatus());
			pstmt.setString(10, spaceVO.getSpaceAddress());
			pstmt.setDouble(11, spaceVO.getLatitude());
			pstmt.setDouble(12, spaceVO.getLongitude());
			pstmt.setString(13, spaceVO.getSpaceId());

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
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
	public void delete(String spaceId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, spaceId);

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
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
	public SpaceVO findByPrimaryKey(String spaceId) {

		SpaceVO spaceVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, spaceId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				spaceVO = new SpaceVO();
				spaceVO.setSpaceId(rs.getString("space_id"));
				spaceVO.setBranchId(rs.getString("branch_id"));
				spaceVO.setSpaceName(rs.getString("space_name"));
				spaceVO.setSpacePeople(rs.getInt("space_people"));
				spaceVO.setSpaceSize(rs.getDouble("space_size"));
				spaceVO.setSpaceHourlyFee(rs.getInt("space_hourly_fee"));
				spaceVO.setSpaceDailyFee(rs.getInt("space_daily_fee"));
				spaceVO.setSpaceDesc(rs.getString("space_desc"));
				spaceVO.setSpaceRating(rs.getDouble("space_rating"));
				spaceVO.setSpaceAlert(rs.getString("space_alert"));
				spaceVO.setSpaceUsed24hr(rs.getDouble("space_used_24hr"));
				spaceVO.setSpaceUsed7d(rs.getDouble("space_used_7d"));
				spaceVO.setSpaceStatus(rs.getInt("space_status"));
				spaceVO.setSpaceAddress(rs.getString("space_address"));
				spaceVO.setLatitude(rs.getDouble("latitude"));
				spaceVO.setLongitude(rs.getDouble("longitude"));
				spaceVO.setCreatedTime(rs.getTimestamp("created_time"));
			}

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
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
		return spaceVO;
	}

	@Override
	public List<SpaceVO> getAll() {
		List<SpaceVO> list = new ArrayList<SpaceVO>();
		SpaceVO spaceVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				spaceVO = new SpaceVO();
				spaceVO.setSpaceId(rs.getString("space_id"));
				spaceVO.setBranchId(rs.getString("branch_id"));
				spaceVO.setSpaceName(rs.getString("space_name"));
				spaceVO.setSpacePeople(rs.getInt("space_people"));
				spaceVO.setSpaceSize(rs.getDouble("space_size"));
				spaceVO.setSpaceHourlyFee(rs.getInt("space_hourly_fee"));
				spaceVO.setSpaceDailyFee(rs.getInt("space_daily_fee"));
				spaceVO.setSpaceDesc(rs.getString("space_desc"));
				spaceVO.setSpaceRating(rs.getDouble("space_rating"));
				spaceVO.setSpaceAlert(rs.getString("space_alert"));
				spaceVO.setSpaceUsed24hr(rs.getDouble("space_used_24hr"));
				spaceVO.setSpaceUsed7d(rs.getDouble("space_used_7d"));
				spaceVO.setSpaceStatus(rs.getInt("space_status"));
				spaceVO.setSpaceAddress(rs.getString("space_address"));
				spaceVO.setLatitude(rs.getDouble("latitude"));
				spaceVO.setLongitude(rs.getDouble("longitude"));
				spaceVO.setCreatedTime(rs.getTimestamp("created_time"));
				list.add(spaceVO); // Store the row in the list
			}

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
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

	public static void main(String[] args) {

		SpaceJDBCDAO dao = new SpaceJDBCDAO();

		// 新增
//		SpaceVO spaceVO1 = new SpaceVO();
//		spaceVO1.setBranchId("B003");
//		spaceVO1.setSpaceName("Test 101");
//		spaceVO1.setSpacePeople(4);
//		spaceVO1.setSpaceSize(10.0);
//		spaceVO1.setSpaceHourlyFee(200);
//		spaceVO1.setSpaceDailyFee(1200);
//		spaceVO1.setSpaceDesc("該空間寬敞設計現代，提供全面服務，環境安全舒適。");
//		spaceVO1.setSpaceAlert(null);
//		spaceVO1.setSpaceStatus(1);
//		spaceVO1.setSpaceAddress("台北市中正區北平西路12號6樓");
//		spaceVO1.setLatitude(25.044);
//		spaceVO1.setLongitude(121.516);
//		dao.insert(spaceVO1);

		// 修改
//		SpaceVO spaceVO2 = new SpaceVO();
//		spaceVO2.setBranchId("B002");
//		spaceVO2.setSpaceName("Test 101");
//		spaceVO2.setSpacePeople(4);
//		spaceVO2.setSpaceSize(10.0);
//		spaceVO2.setSpaceHourlyFee(200);
//		spaceVO2.setSpaceDailyFee(1200);
//		spaceVO2.setSpaceDesc("aklsdfjas;klfjasl;dfj");
//		spaceVO2.setSpaceAlert(null);
//		spaceVO2.setSpaceStatus(1);
//		spaceVO2.setSpaceAddress("台北市中山區中山北路三段57號9樓");
//		spaceVO2.setLatitude(25.064);
//		spaceVO2.setLongitude(121.522);
//		spaceVO2.setSpaceId("S026");
//		dao.update(spaceVO2);

		// 刪除
//		dao.delete("S023");

		// 查詢
//		SpaceVO spaceVO3 = dao.findByPrimaryKey("S025");
//		System.out.print(spaceVO3.getSpaceId() + ",");
//		System.out.print(spaceVO3.getBranchId() + ",");
//		System.out.print(spaceVO3.getSpaceName() + ",");
//		System.out.print(spaceVO3.getSpacePeople() + ",");
//		System.out.print(spaceVO3.getSpaceSize() + ",");
//		
//		System.out.print(spaceVO3.getSpaceHourlyFee() + ",");
//		System.out.print(spaceVO3.getSpaceDailyFee() + ",");
//		System.out.print(spaceVO3.getSpaceDesc() + ",");
//		System.out.print(spaceVO3.getSpaceRating() + ",");
//		System.out.print(spaceVO3.getSpaceAlert() + ",");
//		System.out.print(spaceVO3.getSpaceUsed24hr() + ",");
//		
//		System.out.print(spaceVO3.getSpaceUsed7d() + ",");
//		System.out.print(spaceVO3.getSpaceStatus() + ",");
//		System.out.print(spaceVO3.getSpaceAddress() + ",");
//		System.out.print(spaceVO3.getLatitude() + ",");
//		System.out.print(spaceVO3.getLongitude() + ",");
//		System.out.print(spaceVO3.getCreatedTime());

//		System.out.println("\n---------------------");

		// 查詢
		List<SpaceVO> list = dao.getAll();
		for (SpaceVO space : list) {
			System.out.print(space.getSpaceId() + ",");
			System.out.print(space.getBranchId() + ",");
			System.out.print(space.getSpaceName() + ",");
			System.out.print(space.getSpacePeople() + ",");
			System.out.print(space.getSpaceSize() + ",");
			
			System.out.print(space.getSpaceHourlyFee() + ",");
			System.out.print(space.getSpaceDailyFee() + ",");
			System.out.print(space.getSpaceDesc() + ",");
			System.out.print(space.getSpaceRating() + ",");
			System.out.print(space.getSpaceAlert() + ",");
			System.out.print(space.getSpaceUsed24hr() + ",");
			
			System.out.print(space.getSpaceUsed7d() + ",");
			System.out.print(space.getSpaceStatus() + ",");
			System.out.print(space.getSpaceAddress() + ",");
			System.out.print(space.getLatitude() + ",");
			System.out.print(space.getLongitude() + ",");
			System.out.print(space.getCreatedTime());
			System.out.println();
		}
	}
}
