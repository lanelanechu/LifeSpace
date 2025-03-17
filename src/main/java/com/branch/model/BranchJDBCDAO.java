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
	String url = "jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";

	private static final String INSERT_STMT = 
		"INSERT INTO branch (branch_id, branch_name, branch_addr, space_qty, latitude, longitude, branchStatus, created_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT branch_id, branch_name, branch_addr, space_qty, latitude, longitude, branchStatus, created_time FROM branch order by branch_id";
	private static final String GET_ONE_STMT = 
		"SELECT branch_id, branch_name, branch_addr, space_qty, latitude, longitude, branchStatus, created_time FROM branch where branch_id = ?";
	private static final String DELETE = 
		"DELETE FROM branch where branch_id = ?";
	private static final String UPDATE = 
		"UPDATE branch set branch_name=?, branch_addr=?, space_qty=?, latitude=?, longitude=?, branchStatus=? where branch_id = ?";

	// 獲取下一個流水號
		
		private String getNextBranch_id() throws SQLException {
			// 
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

			pstmt.setString(1, getNextBranch_id());
			pstmt.setString(2, branchVO.getBranch_name());
			pstmt.setString(3, branchVO.getBranch_addr());
			pstmt.setInt(4, branchVO.getSpace_qty());
			pstmt.setDouble(5, branchVO.getLatitude());
			pstmt.setDouble(6, branchVO.getLongitude());
			pstmt.setInt(7, branchVO.getBranchStatus());
			pstmt.setTimestamp(8, branchVO.getCreated_time());

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

			pstmt.setString(1, branchVO.getBranch_name());
			pstmt.setString(2, branchVO.getBranch_addr());
			pstmt.setInt(3, branchVO.getSpace_qty());
			pstmt.setDouble(4, branchVO.getLatitude());
			pstmt.setDouble(5, branchVO.getLongitude());
			pstmt.setInt(6, branchVO.getBranchStatus());
			pstmt.setString(7, branchVO.getBranch_id());

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
	public void delete(String branch_id) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, branch_id);

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
	public BranchVO findByPrimaryKey(String branch_id) {

		BranchVO branchVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, branch_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// branchVO 也稱為 Domain objects
				branchVO = new BranchVO();
				branchVO.setBranch_id(rs.getString("branch_id"));
				branchVO.setBranch_name(rs.getString("branch_name"));
				branchVO.setBranch_addr(rs.getString("branch_addr"));
				branchVO.setSpace_qty(rs.getInt("space_qty"));
				branchVO.setLatitude(rs.getDouble("latitude"));
				branchVO.setLongitude(rs.getDouble("longitude"));
				branchVO.setBranchStatus(rs.getInt("branchstatus"));
				branchVO.setCreated_time(rs.getTimestamp("created_time"));
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
				branchVO.setBranch_id(rs.getString("branch_id"));
				branchVO.setBranch_name(rs.getString("branch_name"));
				branchVO.setBranch_addr(rs.getString("branch_addr"));
				branchVO.setSpace_qty(rs.getInt("space_qty"));
				branchVO.setLatitude(rs.getDouble("latitude"));
				branchVO.setLongitude(rs.getDouble("longitude"));
				branchVO.setBranchStatus(rs.getInt("branchstatus"));
				branchVO.setCreated_time(rs.getTimestamp("created_time"));
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
//		branchVO1.setBranch_name("台北分店");
//		branchVO1.setBranch_addr("台北市信義區信義路5段7號");
//		branchVO1.setSpace_qty(50);
//		branchVO1.setLatitude(25.033611);
//		branchVO1.setLongitude(121.564444);
//		branchVO1.setBranchStatus(1);
//		dao.insert(branchVO1);

		// 修改
//		BranchVO branchVO2 = new BranchVO();
//		branchVO2.setBranch_id("B001");
//		branchVO2.setBranch_name("台北總店");
//		branchVO2.setBranch_addr("台北市信義區信義路5段6號");
//		branchVO2.setSpace_qty(60);
//		branchVO2.setLatitude(25.222222);
//		branchVO2.setLongitude(121.222222);
//		branchVO2.setBranchStatus(1);
//		dao.update(branchVO2);

		// 刪除
//		dao.delete("B001");

		// 查詢
//		BranchVO branchVO3 = dao.findByPrimaryKey("B002");
//		System.out.print(branchVO3.getBranch_id() + ",");
//		System.out.print(branchVO3.getBranch_name() + ",");
//		System.out.print(branchVO3.getBranch_addr() + ",");
//		System.out.print(branchVO3.getSpace_qty() + ",");
//		System.out.print(branchVO3.getLatitude() + ",");
//		System.out.print(branchVO3.getLongitude() + ",");
//		System.out.print(branchVO3.getBranchStatus() + ",");
//		System.out.print(branchVO3.getCreated_time() + ",");
//		System.out.println("---------------------");

		// 查詢
		List<BranchVO> list = dao.getAll();
		for (BranchVO aBranch : list) {
			System.out.print(aBranch.getBranch_id() + ",");
			System.out.print(aBranch.getBranch_name() + ",");
			System.out.print(aBranch.getBranch_addr() + ",");
			System.out.print(aBranch.getSpace_qty() + ",");
			System.out.print(aBranch.getLatitude() + ",");
			System.out.print(aBranch.getLongitude() + ",");
			System.out.print(aBranch.getBranchStatus() + ",");
			System.out.print(aBranch.getCreated_time() + ",");
			System.out.println();
		}
	}
}