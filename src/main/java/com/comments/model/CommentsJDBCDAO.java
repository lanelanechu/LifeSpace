package com.comments.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentsJDBCDAO implements CommentsDAO_interface {
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/life_space_01?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";

	private static final String INSERT_STMT = 
		"INSERT INTO comments (comment_id, event_member_id, comment_hide, comment_message) VALUES (?, ?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT comment_id,event_member_id,comment_hide,comment_message, comment_time FROM comments order by comment_time desc";
	private static final String GET_ONE_STMT = 
		"SELECT comment_id,event_member_id,comment_hide,comment_message, comment_time FROM comments where comment_id = ?";
	private static final String DELETE = 
		"DELETE FROM comments where comment_id = ?";
	private static final String UPDATE = 
		"UPDATE comments set event_member_id=?, comment_hide=?, comment_message=? where comment_id = ?";
	//private static final String GET_MEMBERID_STMT = 
		//"SELECT comment_id,event_member_id,comment_hide,comment_message, comment_time FROM comments where comment_id = ?";
	
	// 獲取下一個流水號
    private String getNextCommentId() throws SQLException {
        String nextId = "C001"; // 預設初始值
        String pref = "C";  // 改成你的表格的流水號開頭

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
        }

        String query = "SELECT MAX(comment_id) FROM comments";
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
	public void insert(CommentsVO commentsVO) {

		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, getNextCommentId());
			pstmt.setString(2, commentsVO.getEventMemberId());
			pstmt.setInt(3, commentsVO.getCommentHide());
			pstmt.setString(4, commentsVO.getCommentMessage());
//			pstmt.setTimestamp(5, commentsVO.getCommentTime());
//			pstmt.setTimestamp(6, commentsVO.getCreatedTime());

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
	public void update(CommentsVO commentsVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);
			
			pstmt.setString(1, commentsVO.getEventMemberId());
			pstmt.setInt(2, commentsVO.getCommentHide());
			pstmt.setString(3, commentsVO.getCommentMessage());
			pstmt.setString(4, commentsVO.getCommentId());

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
	public void delete(String commentId) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setString(1, commentId);

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
	public CommentsVO findByPrimaryKey(String commentId) {
		
		CommentsVO commentsVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setString(1, commentId);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				commentsVO = new CommentsVO();
				commentsVO.setCommentId(rs.getString("comment_id"));
				commentsVO.setEventMemberId(rs.getString("event_member_id"));
				commentsVO.setCommentHide(rs.getInt("comment_hide"));
				commentsVO.setCommentMessage(rs.getString("comment_message"));
				commentsVO.setCommentTime(rs.getTimestamp("comment_time"));
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
		return commentsVO;
	}
	@Override
	public List<CommentsVO> getAll() {
		List<CommentsVO> list = new ArrayList<CommentsVO>();
		CommentsVO commentsVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				// commentsVO 也稱為 Domain objects
				commentsVO = new CommentsVO();
				commentsVO.setCommentId(rs.getString("comment_id"));
				commentsVO.setEventMemberId(rs.getString("event_member_id"));
				commentsVO.setCommentHide(rs.getInt("comment_hide"));
				commentsVO.setCommentMessage(rs.getString("comment_message"));
				commentsVO.setCommentTime(rs.getTimestamp("comment_time"));
				list.add(commentsVO); // Store the row in the list
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
	
//	@Override
//	public List<CommentsVO> findByMemberId(String eventMemberId) {
//		List<CommentsVO> list = new ArrayList<CommentsVO>();
//		CommentsVO commentsVO = null;
//
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(GET_ALL_STMT);
//			rs = pstmt.executeQuery();
//			
//			while (rs.next()) {
//				// commentsVO 也稱為 Domain objects
//				commentsVO = new CommentsVO();
//				commentsVO.setCommentId(rs.getString("comment_id"));
//				commentsVO.setEventMemberId(rs.getString("event_member_id"));
//				commentsVO.setCommentHide(rs.getInt("comment_hide"));
//				commentsVO.setCommentMessage(rs.getString("comment_message"));
//				commentsVO.setCommentTime(rs.getTimestamp("comment_time"));
//				list.add(commentsVO); // Store the row in the list
//			}
//		
//			// Handle any driver errors
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. "
//					+ e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. "
//					+ se.getMessage());
//			// Clean up JDBC resources
//		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//		return list;
//	}

	public static void main(String[] args) {
		
		CommentsJDBCDAO dao = new CommentsJDBCDAO();

		// 新增
		CommentsVO commentsVO1 = new CommentsVO();
		commentsVO1.setCommentId("C005");
		commentsVO1.setEventMemberId("EM005");
		commentsVO1.setCommentHide(0);
		commentsVO1.setCommentMessage("活動好棒!");
		dao.insert(commentsVO1);

		// 修改
		CommentsVO commentsVO2 = new CommentsVO();
		commentsVO2.setEventMemberId("EM003");
		commentsVO2.setCommentHide(0);
		commentsVO2.setCommentMessage("活動好棒!!!!!!!");
		commentsVO2.setCommentTime(java.sql.Timestamp.valueOf("2025-03-14 10:20:00"));
		dao.update(commentsVO2);

		// 刪除
//		dao.delete("C001");

		// 查詢
		CommentsVO commentsVO3 = dao.findByPrimaryKey("C003");
		System.out.print(commentsVO3.getCommentId() + ",");
		System.out.print(commentsVO3.getEventMemberId() + ",");
		System.out.print(commentsVO3.getCommentHide() + ",");
		System.out.print(commentsVO3.getCommentMessage() + ",");
		System.out.println("---------------------");

		// 查詢
		List<CommentsVO> list = dao.getAll();
		for (CommentsVO aComments : list) {
			System.out.print(aComments.getCommentId() + ",");
			System.out.print(aComments.getEventMemberId() + ",");
			System.out.print(aComments.getCommentHide() + ",");
			System.out.print(aComments.getCommentMessage() + ",");
			System.out.println();
		}		
	}
}
