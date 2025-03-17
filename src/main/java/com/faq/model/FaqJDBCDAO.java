package com.faq.model;

import java.util.*;

import com.faq.model.FaqJDBCDAO;
import com.faq.model.FaqVO;

import java.sql.*;

public class FaqJDBCDAO implements FaqDAO_interface {
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/life_space_01?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";
	
	private static final String INSERT_STMT = 
		"INSERT INTO faq (faq_id, admin_id, faq_ask, faq_answer, faq_status) VALUES (?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT faq_id, admin_id, faq_ask, faq_answer, faq_status, created_time FROM faq order by faq_id";
	private static final String GET_ONE_STMT = 
		"SELECT faq_id, admin_id, faq_ask, faq_answer, faq_status, created_time FROM faq where faq_id = ?";
	private static final String DELETE = 
		"UPDATE faq set faq_status= 0 where faq_id = ?";
	private static final String UPDATE = 
		"UPDATE faq set faq_ask=?, faq_answer=? where faq_id = ?";
	
	
	// 獲取下一個流水號(特殊格式自增主鍵值)
    private String getNextFaqId() throws SQLException {
        String nextId = "FAQ01"; // 預設初始值
        String pref = "FAQ";  // 表格的流水號開頭

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
        }
        //查詢資料表中的最大 faq_id
        String query = "SELECT MAX(faq_id) FROM faq";
        try (
            Connection conn = DriverManager.getConnection(url, userid, passwd);
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()
        ) {
            if (rs.next() && rs.getString(1) != null) {
                String currentId = rs.getString(1);
                //從索引 3 開始擷取字串剩餘部分,並將字串轉換成整數
                int numericPart = Integer.parseInt(currentId.substring(3));
                numericPart++; //數字+1產生新ID
                // pre+格式化三位數(%d:整數，02: 寬度2不足補0)
                nextId = pref + String.format("%02d", numericPart);
            }
        } catch (SQLException se) {
            throw new RuntimeException("A database error occurred. " + se.getMessage());
        }
        return nextId;
    }
    
	@Override
	public void insert(FaqVO faqVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, getNextFaqId());
			pstmt.setString(2, faqVO.getAdminId());
			pstmt.setString(3, faqVO.getFaqAsk());
			pstmt.setString(4, faqVO.getFaqAnswer());
			pstmt.setInt(5, faqVO.getFaqStatus());


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
	public void update(FaqVO faqVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			
			pstmt.setString(1, faqVO.getFaqAsk());
			pstmt.setString(2, faqVO.getFaqAnswer());
			pstmt.setString(3, faqVO.getFaqId());
			

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
	public void delete(String faqId) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, faqId);

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
	public FaqVO findByPrimaryKey(String faqId) {
		
		FaqVO faqVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, faqId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// faqVO 也稱為 Domain objects
				faqVO = new FaqVO();
				faqVO.setFaqId(rs.getString("faq_id")); 
				faqVO.setAdminId(rs.getString("admin_id"));
				faqVO.setFaqAsk(rs.getString("faq_ask"));
				faqVO.setFaqAnswer(rs.getString("faq_answer"));
				faqVO.setFaqStatus(rs.getInt("faq_status"));
				faqVO.setCreateTime(rs.getTimestamp("created_time"));
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
		return faqVO;
	}
	
	@Override
	public List<FaqVO> getAll() {
		List<FaqVO> list = new ArrayList<FaqVO>();
		FaqVO faqVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// faqVO 也稱為 Domain objects
				faqVO = new FaqVO();
				faqVO.setFaqId(rs.getString("faq_id")); 
				faqVO.setAdminId(rs.getString("admin_id"));
				faqVO.setFaqAsk(rs.getString("faq_ask"));
				faqVO.setFaqAnswer(rs.getString("faq_answer"));
				faqVO.setFaqStatus(rs.getInt("faq_status"));
				faqVO.setCreateTime(rs.getTimestamp("created_time"));
				list.add(faqVO); // Store the row in the list
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

		FaqJDBCDAO dao = new FaqJDBCDAO();
		
		// 新增
//		FaqVO faqVO1 = new FaqVO();		
//		faqVO1.setAdminId("A002");
//		faqVO1.setFaqAsk("有注意到房間、公共走道都有監視器，請問用途是什麼？");
//		faqVO1.setFaqAnswer("為提供您更好的人身安全及法律保障，我們會在有消費爭議、調查案件等情況時調閱監視器的影片。關於監視器的資訊蒐集與利用請參考「隱私權保護政策」，也可以參考「我們對監視攝影機的考量」以瞭解我們的政策方向、為什麼需要這項設置。");
//		faqVO1.setFaqStatus(1);
//		dao.insert(faqVO1);

		// 修改
//		FaqVO faqVO2 = new FaqVO();
//		faqVO2.setFaqId("FAQ02");
//		faqVO2.setFaqAsk("LifeSpace 空間介紹");
//		faqVO2.setFaqAnswer("LifeSpace任你玩！任你high翻天!別等了!動動手指，24小時任你預訂!");
//		dao.update(faqVO2);
//		System.out.println("---------------------");

		// 刪除(狀態0時)
//		dao.delete("faq01");

		// 查詢
		FaqVO faqVO3 = dao.findByPrimaryKey("FAQ03");
		System.out.print(faqVO3.getFaqId() + ",");
		System.out.print(faqVO3.getAdminId() + ",");
		System.out.print(faqVO3.getFaqAsk() + ",");
		System.out.print(faqVO3.getFaqAnswer() + ",");
		System.out.print(faqVO3.getFaqStatus() + ",");
		System.out.print(faqVO3.getCreateTime() + ",");
		System.out.println("---------------------");

		// 查詢
//		List<FaqVO> list = dao.getAll();
//		for (FaqVO aFaq : list) {
//			System.out.print(aFaq.getFaqId() + ",");
//			System.out.print(aFaq.getAdminId() + ",");
//			System.out.print(aFaq.getFaqAsk() + ",");
//			System.out.print(aFaq.getFaqAnswer() + ",");
//			System.out.print(aFaq.getFaqStatus() + ",");
//			System.out.print(aFaq.getCreateTime() + ",");
//			System.out.println(list);
//		}
	}
}
