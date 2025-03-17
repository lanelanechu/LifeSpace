package com.admin.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.member.model.MemberVO;

public class AdminJDBCDAO implements AdminDAO_interface {
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/life_space_01?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";

	private static final String INSERT_STMT = "INSERT INTO admin (admin_id, admin_name, password, email, account_status, registration_time ) VALUES(?,?,?,?,?,?)";
	private static final String GET_ALL_STMT = "SELECT admin_id, admin_name, password, email, account_status, registration_time FROM admin order by admin_id";
	private static final String GET_ONE_STMT = "SELECT admin_id, admin_name, password, email, account_status, registration_time FROM admin where admin_id = ?";
	private static final String DELETE = "DELETE FROM admin where admin_id = ?";
	private static final String UPDATE = "UPDATE admin set admin_name=?, password=?, email=?, account_status=?, registration_time=? where admin_id=?";

	// 獲取下一個流水號
    private String getNextAdminId() throws SQLException {
        String nextId = "A001"; // 預設初始值
        String pref = "A";  // 改成你的表格的流水號開頭

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
        }

        String query = "SELECT MAX(admin_id) FROM admin";
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
	public void insert(AdminVO adminVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);
		
			pstmt.setString(1, getNextAdminId());
			pstmt.setString(2, adminVO.getAdminName());
			pstmt.setString(3, adminVO.getPassword());
			pstmt.setString(4, adminVO.getEmail());
			pstmt.setInt(5, adminVO.getAccountStatus());
			pstmt.setTimestamp(6, adminVO.getRegistrationTime());

			pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
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
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, adminVO.getAdminName());
			pstmt.setString(2, adminVO.getPassword());
			pstmt.setString(3, adminVO.getEmail());
			pstmt.setInt(4, adminVO.getAccountStatus());
			pstmt.setTimestamp(5, adminVO.getRegistrationTime());
			pstmt.setString(6, adminVO.getAdminId());

			pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
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
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, adminId);

			pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
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
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
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

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
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
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
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

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
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

	public static void main(String[] args) {

		AdminJDBCDAO dao = new AdminJDBCDAO();

		// 新增
		AdminVO adminVO1 = new AdminVO();
		//adminVO1.setAdminId("A006");
		adminVO1.setAdminName("超派");
		adminVO1.setPassword("888");
		adminVO1.setEmail("8888888@gmail.com");
		adminVO1.setAccountStatus(1);
		adminVO1.setRegistrationTime(Timestamp.valueOf(LocalDateTime.now()));
		dao.insert(adminVO1);
		System.out.println("完成!");
		
//		//修改
//		AdminVO adminVO2 = new AdminVO();
//		adminVO2.setAdminId("A006");
//		adminVO2.setAdminName("大南XX");
//		adminVO2.setPassword("543");
//		adminVO2.setEmail("543@gmail.com");
//		adminVO2.setAccountStatus(0);
//		adminVO2.setRegistrationTime(Timestamp.valueOf(LocalDateTime.now()));
//		dao.update(adminVO2);
//		System.out.println("我完成了");
		
//		//刪除
//		dao.delete("A006");
//		System.out.println("我完成了");
		
//		//查詢1
//		AdminVO adminVO3 = dao.findByPrimaryKey("A005");
//		System.out.println(adminVO3.getAdminId() + ",");
//		System.out.println(adminVO3.getAdminName() + ",");
//		System.out.println(adminVO3.getPassword() + ",");
//		System.out.println(adminVO3.getEmail() + ",");
//		System.out.println(adminVO3.getAccountStatus() + ",");
//		System.out.println(adminVO3.getRegistrationTime());
//		System.out.println("------------------");
		
		
//		//查詢2
//		List<AdminVO> list = dao.getAll();
//		for (AdminVO aMember : list) {
//			System.out.print(aMember.getAdminId() + ",");
//			System.out.print(aMember.getAdminName() + ",");
//			System.out.print(aMember.getPassword() + ",");
//			System.out.print(aMember.getEmail() + ",");
//			System.out.print(aMember.getAccountStatus() + ",");
//			System.out.print(aMember.getRegistrationTime() );
//			System.out.println();
//		}
//		System.out.println("我完成了");
		

	}

}
