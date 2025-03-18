package com.comments.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentReportJDBCDAO implements CommentReportDAO_interface {
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/life_space_01?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";

	private static final String INSERT_STMT = 
		"INSERT INTO comment_report (report_id,member_id,admin_id,comment_id,manage_type,close_time,report_message,report_reason,status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT report_id,member_id,admin_id,comment_id,manage_type,close_time,report_message,report_reason,status FROM comment_report order by status ASC";
	private static final String GET_ONE_STMT = 
		"SELECT report_id,member_id,admin_id,comment_id,manage_type,close_time,report_message,report_reason,status FROM comment_report where report_id = ?";
	private static final String DELETE = 
		"DELETE FROM comment_report where report_id = ?";
	private static final String UPDATE = 
		"UPDATE comment_report set member_id=?, admin_id=?, comment_id=?, manage_type=?, close_time=?, report_message=?, report_reason=?, status=? where report_id = ?";
		
	// 獲取下一個流水號
    private String getNextReportId() throws SQLException {
        String nextId = "CR001"; // 預設初始值
        String pref = "CR";  // 改成你的表格的流水號開頭

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
        }

        String query = "SELECT MAX(report_id) FROM comment_report";
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
	public void insert(CommentReportVO commentReportVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, getNextReportId());
			pstmt.setString(2, commentReportVO.getMemberId());
			pstmt.setString(3, commentReportVO.getAdminId());
			pstmt.setString(4, commentReportVO.getCommentId());
			pstmt.setInt(5, commentReportVO.getManageType());
			pstmt.setTimestamp(6, commentReportVO.getCloseTime());
			pstmt.setString(7, commentReportVO.getReportMessage());
			pstmt.setString(8, commentReportVO.getReportReason());
			pstmt.setInt(9, commentReportVO.getStatus());

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
	public void update(CommentReportVO commentReportVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);
			
			pstmt.setString(1, commentReportVO.getMemberId());
			pstmt.setString(2, commentReportVO.getAdminId());
			pstmt.setString(3, commentReportVO.getCommentId());
			pstmt.setInt(4, commentReportVO.getManageType());
			pstmt.setTimestamp(5, commentReportVO.getCloseTime());
			pstmt.setString(6, commentReportVO.getReportMessage());
			pstmt.setString(7, commentReportVO.getReportReason());
			pstmt.setInt(8, commentReportVO.getStatus());
			pstmt.setString(9, commentReportVO.getReportId());

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
	public void delete(String reportId) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setString(1, reportId);

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
	public CommentReportVO findByPrimaryKey(String reportId) {
		CommentReportVO commentReportVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setString(1, reportId);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				commentReportVO = new CommentReportVO();
				commentReportVO.setReportId(rs.getString("report_id"));
				commentReportVO.setMemberId(rs.getString("member_id"));
				commentReportVO.setAdminId(rs.getString("admin_id"));
				commentReportVO.setCommentId(rs.getString("comment_id"));
				commentReportVO.setManageType(rs.getInt("manage_type"));
				commentReportVO.setCloseTime(rs.getTimestamp("close_time"));
				commentReportVO.setReportMessage(rs.getString("report_message"));
				commentReportVO.setReportReason(rs.getString("report_reason"));
				commentReportVO.setStatus(rs.getInt("status"));
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
		return commentReportVO;
	}

	@Override
	public List<CommentReportVO> getAll() {
		List<CommentReportVO> list = new ArrayList<CommentReportVO>();
		CommentReportVO commentReportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				// commentReportVO 也稱為 Domain objects
				commentReportVO = new CommentReportVO();
				commentReportVO.setReportId(rs.getString("report_id"));
				commentReportVO.setMemberId(rs.getString("member_id"));
				commentReportVO.setAdminId(rs.getString("admin_id"));
				commentReportVO.setCommentId(rs.getString("comment_id"));
				commentReportVO.setManageType(rs.getInt("manage_type"));
				commentReportVO.setCloseTime(rs.getTimestamp("close_time"));
				commentReportVO.setReportMessage(rs.getString("report_message"));
				commentReportVO.setReportReason(rs.getString("report_reason"));
				commentReportVO.setStatus(rs.getInt("status"));
				list.add(commentReportVO); // Store the row in the list
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
		
		CommentReportJDBCDAO dao = new CommentReportJDBCDAO();

		// 新增
		CommentReportVO commentReportVO1 = new CommentReportVO();
		commentReportVO1.setReportId("CR011");
		commentReportVO1.setMemberId("M011");
		commentReportVO1.setAdminId("A004");
		commentReportVO1.setCommentId("C011");
		commentReportVO1.setManageType(0);
		commentReportVO1.setCloseTime(java.sql.Timestamp.valueOf("2025-03-14 10:00:00"));
		commentReportVO1.setReportMessage("活動超級爛，主辦人不要再辦活動了!");
		commentReportVO1.setReportReason("涉及人身攻擊，言語不當。");
		commentReportVO1.setStatus(1);
		dao.insert(commentReportVO1);

		// 修改
		CommentReportVO commentReportVO2 = new CommentReportVO();
		commentReportVO2.setMemberId("M003");
		commentReportVO2.setAdminId("A005");
		commentReportVO2.setCommentId("C003");
		commentReportVO2.setManageType(0);
		commentReportVO2.setCloseTime(java.sql.Timestamp.valueOf("2025-03-14 10:20:00"));
		commentReportVO2.setReportMessage("這活動就是給某些特定人參加的吧？其他人滾遠點！");
		commentReportVO2.setReportReason("涉及排他性歧視，違反平等原則。");
		commentReportVO2.setStatus(1);
		dao.update(commentReportVO2);

		// 刪除
//		dao.delete("CR001");

		// 查詢
		CommentReportVO commentReportVO3 = dao.findByPrimaryKey("CR003");
		System.out.print(commentReportVO3.getReportId() + ",");
		System.out.print(commentReportVO3.getMemberId() + ",");
		System.out.print(commentReportVO3.getAdminId() + ",");
		System.out.print(commentReportVO3.getCommentId() + ",");
		System.out.print(commentReportVO3.getManageType() + ",");
		System.out.print(commentReportVO3.getCloseTime() + ",");
		System.out.print(commentReportVO3.getReportMessage() + ",");
		System.out.print(commentReportVO3.getReportReason() + ",");
		System.out.print(commentReportVO3.getStatus() + ",");		
		System.out.println("---------------------");

		// 查詢
		List<CommentReportVO> list = dao.getAll();
		for (CommentReportVO aCommentReport : list) {
			System.out.print(aCommentReport.getReportId() + ",");
			System.out.print(aCommentReport.getMemberId() + ",");
			System.out.print(aCommentReport.getAdminId() + ",");
			System.out.print(aCommentReport.getCommentId() + ",");
			System.out.print(aCommentReport.getManageType() + ",");
			System.out.print(aCommentReport.getCloseTime() + ",");
			System.out.print(aCommentReport.getReportMessage() + ",");
			System.out.print(aCommentReport.getReportReason() + ",");
			System.out.print(aCommentReport.getStatus() + ",");
			System.out.println();
		}		
	}
}
