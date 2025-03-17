package com.branch.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BranchJDBCDAO implements BranchDAO_interface {
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/life_space_01?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";

	private static final String INSERT_STMT = 
		"INSERT INTO branch (branch_id, branch_name, branch_addr, space_qty, latitude, longitude, branchStatus) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT branch_id, branch_name, branch_addr, space_qty, latitude, longitude, branchStatus, created_time FROM branch order by branch_id";
	private static final String GET_ONE_STMT = 
		"SELECT branch_id, branch_name, branch_addr, space_qty, latitude, longitude, branchStatus, created_time FROM branch where branch_id = ?";
	private static final String DELETE = 
		"DELETE FROM branch where branch_id = ?";
	private static final String UPDATE = 
		"UPDATE branch set branch_name=?, branch_addr=?, space_qty=?, latitude=?, longitude=?, branchStatus=? where branch_id = ?";

	// 獲取下一個流水號
		
		private String getNextBranchId() throws SQLException {	
		    String nextId = "B001"; // 預設初始值
		    String pref = "B";  // 改成你的表格的流水號開頭

		    try {
		        Class.forName(driver);
		    } catch (ClassNotFoundException e) {
		        throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		    }
		    
		    String query = "SELECT MAX(branch_id) FROM branch";
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
	public void insert(BranchVO branchVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, getNextBranchId());
			pstmt.setString(2, branchVO.getBranchName());
			pstmt.setString(3, branchVO.getBranchAddr());
			pstmt.setInt(4, branchVO.getSpaceQty());
			pstmt.setDouble(5, branchVO.getLatitude());
			pstmt.setDouble(6, branchVO.getLongitude());
			pstmt.setInt(7, branchVO.getBranchStatus());
			
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
	public void update(BranchVO branchVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, branchVO.getBranchName());
			pstmt.setString(2, branchVO.getBranchAddr());
			pstmt.setInt(3, branchVO.getSpaceQty());
			pstmt.setDouble(4, branchVO.getLatitude());
			pstmt.setDouble(5, branchVO.getLongitude());
			pstmt.setInt(6, branchVO.getBranchStatus());
			pstmt.setString(7, branchVO.getBranchId());

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
	public void delete(String branchId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, branchId);

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
	public BranchVO findByPrimaryKey(String branchId) {

		BranchVO branchVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, branchId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// branchVO 也稱為 Domain objects
				branchVO = new BranchVO();
				branchVO.setBranchId(rs.getString("branch_id"));
				branchVO.setBranchName(rs.getString("branch_name"));
				branchVO.setBranchAddr(rs.getString("branch_addr"));
				branchVO.setSpaceQty(rs.getInt("space_qty"));
				branchVO.setLatitude(rs.getDouble("latitude"));
				branchVO.setLongitude(rs.getDouble("longitude"));
				branchVO.setBranchStatus(rs.getInt("branchstatus"));
				branchVO.setCreatedTime(rs.getTimestamp("created_time"));
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
		return branchVO;
	}

	@Override
	public List<BranchVO> getAll() {
		List<BranchVO> list = new ArrayList<BranchVO>();
		BranchVO branchVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// branchVO 也稱為 Domain objects
				branchVO = new BranchVO();
				branchVO.setBranchId(rs.getString("branch_id"));
				branchVO.setBranchName(rs.getString("branch_name"));
				branchVO.setBranchAddr(rs.getString("branch_addr"));
				branchVO.setSpaceQty(rs.getInt("space_qty"));
				branchVO.setLatitude(rs.getDouble("latitude"));
				branchVO.setLongitude(rs.getDouble("longitude"));
				branchVO.setBranchStatus(rs.getInt("branchstatus"));
				branchVO.setCreatedTime(rs.getTimestamp("created_time"));
				list.add(branchVO); // Store the row in the list
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

		BranchJDBCDAO dao = new BranchJDBCDAO();

//		 新增
//		BranchVO branchVO1 = new BranchVO();
//		branchVO1.setBranchName("台北分店");
//		branchVO1.setBranchAddr("台北市信義區信義路5段7號");
//		branchVO1.setSpaceQty(50);
//		branchVO1.setLatitude(25.033611);
//		branchVO1.setLongitude(121.564444);
//		branchVO1.setBranchStatus(1);
//		dao.insert(branchVO1);

		// 修改
//		BranchVO branchVO2 = new BranchVO();
//		branchVO2.setBranchId("B001");
//		branchVO2.setBranchName("台北總店");
//		branchVO2.setBranchAddr("台北市信義區信義路5段6號");
//		branchVO2.setSpaceQty(60);
//		branchVO2.setLatitude(25.222222);
//		branchVO2.setLongitude(121.222222);
//		branchVO2.setBranchStatus(1);
//		dao.update(branchVO2);

		// 刪除
//		dao.delete("B001");

		// 查詢
//		BranchVO branchVO3 = dao.findByPrimaryKey("B002");
//		System.out.print(branchVO3.getBranchId() + ",");
//		System.out.print(branchVO3.getBranchName() + ",");
//		System.out.print(branchVO3.getBranchAddr() + ",");
//		System.out.print(branchVO3.getSpaceQty() + ",");
//		System.out.print(branchVO3.getLatitude() + ",");
//		System.out.print(branchVO3.getLongitude() + ",");
//		System.out.print(branchVO3.getBranchStatus() + ",");
//		System.out.print(branchVO3.getCreatedTime() + ",");
//		System.out.println("---------------------");

		// 查詢
		List<BranchVO> list = dao.getAll();
		for (BranchVO aBranch : list) {
			System.out.print(aBranch.getBranchId() + ",");
			System.out.print(aBranch.getBranchName() + ",");
			System.out.print(aBranch.getBranchAddr() + ",");
			System.out.print(aBranch.getSpaceQty() + ",");
			System.out.print(aBranch.getLatitude() + ",");
			System.out.print(aBranch.getLongitude() + ",");
			System.out.print(aBranch.getBranchStatus() + ",");
			System.out.print(aBranch.getCreatedTime() + ",");
			System.out.println();
		}
	}
}