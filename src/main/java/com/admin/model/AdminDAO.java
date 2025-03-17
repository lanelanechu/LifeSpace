package com.admin.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AdminDAO implements AdminDAO_interface {
	
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TestDB2");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private static final String INSERT_STMT = "INSERT INTO admin (admin_id, admin_name, password, email, account_status, registration_time ) VALUES(?,?,?,?,?,?)";
	private static final String GET_ALL_STMT = "SELECT admin_id, admin_name, password, email, account_status, registration_time FROM admin order by admin_id";
	private static final String GET_ONE_STMT = "SELECT admin_id, admin_name, password, email, account_status, registration_time FROM admin where admin_id = ?";
	private static final String DELETE = "DELETE FROM admin where admin_id = ?";
	private static final String UPDATE = "UPDATE admin set admin_name=?, password=?, email=?, account_status=?, registration_time=? where admin_id=?";

	@Override
	public void insert(AdminVO adminVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, adminVO.getAdminId());
			pstmt.setString(2, adminVO.getAdminName());
			pstmt.setString(3, adminVO.getPassword());
			pstmt.setString(4, adminVO.getEmail());
			pstmt.setInt(5, adminVO.getAccountStatus());
			pstmt.setTimestamp(6, adminVO.getRegistrationTime());

			pstmt.executeUpdate();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	public void update(AdminVO adminVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, adminVO.getAdminName());
			pstmt.setString(2, adminVO.getPassword());
			pstmt.setString(3, adminVO.getEmail());
			pstmt.setInt(4, adminVO.getAccountStatus());
			pstmt.setTimestamp(5, adminVO.getRegistrationTime());
			pstmt.setString(6, adminVO.getAdminId());

			pstmt.executeUpdate();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	public void delete(String adminId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, adminId);

			pstmt.executeUpdate();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	public AdminVO findByPrimaryKey(String adminId) {

		AdminVO adminVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, adminId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				adminVO = new AdminVO();
				adminVO.setAdminId(rs.getString("admin_id"));
				adminVO.setAdminName(rs.getString("admin_name"));
				adminVO.setPassword(rs.getString("password"));
				adminVO.setEmail(rs.getString("email"));
				adminVO.setAccountStatus(rs.getInt("account_status"));
				adminVO.setRegistrationTime(rs.getTimestamp("registration_time"));
			}

		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return adminVO;

	}

	@Override
	public List<AdminVO> getAll() {

		List<AdminVO> list = new ArrayList<AdminVO>();
		AdminVO adminVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				adminVO = new AdminVO();
				adminVO.setAdminId(rs.getString("admin_id"));
				adminVO.setAdminName(rs.getString("admin_name"));
				adminVO.setPassword(rs.getString("password"));
				adminVO.setEmail(rs.getString("email"));
				adminVO.setAccountStatus(rs.getInt("account_status"));
				adminVO.setRegistrationTime(rs.getTimestamp("registration_time"));
				list.add(adminVO);
			}

		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
