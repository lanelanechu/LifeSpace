package com.news.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class NewsJDBCDAO implements NewsDAO_interface{

	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/db01?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";
	
	private static final String INSERT_STMT = 
		"INSERT INTO news ( news_id, news_title, news_content, news_start_date, news_end_date, news_img, created_time, news_category_id_fk, admin_id_fk, news_status_id_fk )" 
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT news_id, news_title, news_content, news_start_date, news_end_date, news_img, created_time, news_category_id_fk, admin_id_fk, news_status_id_fk"
		+ " FROM news order by news_id";
	private static final String GET_ONE_STMT = 
		"SELECT news_id, news_title, news_content, news_start_date, news_end_date, news_img, created_time, news_category_id_fk, admin_id_fk, news_status_id_fk"
		+ " FROM news where news_id = ?";
	private static final String DELETE = 
		"DELETE FROM news where news_id = ?";
	private static final String UPDATE = 
		"UPDATE news set news_title=?, news_content=?, news_start_date=?, news_end_date=?, news_img=?, created_time=?, news_category_id_fk=?, admin_id_fk=?, news_status_id_fk=?"
		+ " where news_id = ?";

	@Override
	 public String getNextNewsId() throws SQLException {
	        String nextId = "N001"; // 預設初始值
	        String pref = "N";  // 改成你的表格的流水號開頭

	        try {
	            Class.forName(driver);
	        } catch (ClassNotFoundException e) {
	            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
	        }

	        String query = "SELECT MAX(news_id) FROM news";
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
	public void insert(NewsVO newsVO) {


		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, getNextNewsId());
			pstmt.setString(2 , newsVO.getNewsTitle());
			pstmt.setString(3, newsVO.getNewsContent());
			pstmt.setTimestamp(4, newsVO.getNewsStartDate());
			pstmt.setTimestamp(5, newsVO.getNewsEndDate());
			pstmt.setBytes(6, newsVO.getNewsImg());
			pstmt.setTimestamp(7, newsVO.getCreatedTime());
			pstmt.setString(8, newsVO.getNewsCategoryId());
			pstmt.setString(9, newsVO.getAdminId());
			pstmt.setInt(10, newsVO.getNewsStatusId());
			

			pstmt.executeUpdate();

			System.out.print("新增成功");
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
	public void update(NewsVO newsVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			
			pstmt.setString(1, newsVO.getNewsTitle());
			pstmt.setString(2, newsVO.getNewsContent());
			pstmt.setTimestamp(3, newsVO.getNewsStartDate());
			pstmt.setTimestamp(4, newsVO.getNewsEndDate());
			pstmt.setBytes(5, newsVO.getNewsImg());
			pstmt.setTimestamp(6, newsVO.getCreatedTime());
			pstmt.setString(7, newsVO.getNewsCategoryId());
			pstmt.setString(8, newsVO.getAdminId());
			pstmt.setInt(9, newsVO.getNewsStatusId());
			pstmt.setString(10, newsVO.getNewsId());

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
	public void delete(String newsId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, newsId);

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
	public NewsVO findByPrimaryKey(String newsId) {

		NewsVO newsVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, newsId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				newsVO = new NewsVO();
			//news_title, news_content, news_start_date, news_end_date, news_img, created_time, news_category_id_fk, admin_id_fk, news_status_id_fk	
				newsVO.setNewsId(rs.getString("news_id"));
				newsVO.setNewsTitle(rs.getString("news_title"));
				newsVO.setNewsContent(rs.getString("news_content"));
				newsVO.setNewsStartDate(rs.getTimestamp("news_start_date"));
				newsVO.setNewsEndDate(rs.getTimestamp("news_end_date"));
				newsVO.setNewsImg(rs.getBytes("news_img"));
				newsVO.setCreatedTime(rs.getTimestamp("created_time"));
				newsVO.setNewsCategoryId(rs.getString("news_category_id_fk"));
				newsVO.setAdminId(rs.getString("admin_id_fk"));
				newsVO.setNewsStatusId(rs.getInt("news_status_id_fk"));
				

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
		return newsVO;
	}

	@Override
	public List<NewsVO> getAll() {

		List<NewsVO> list = new ArrayList<NewsVO>();
		NewsVO newsVO = null;

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
				newsVO = new NewsVO();
				newsVO.setNewsId(rs.getString("news_id"));
				newsVO.setNewsTitle(rs.getString("news_title"));
				newsVO.setNewsContent(rs.getString("news_content"));
				newsVO.setNewsStartDate(rs.getTimestamp("news_start_date"));
				newsVO.setNewsEndDate(rs.getTimestamp("news_end_date"));
				newsVO.setNewsImg(rs.getBytes("news_img"));
				newsVO.setCreatedTime(rs.getTimestamp("created_time"));
				newsVO.setNewsCategoryId(rs.getString("news_category_id_fk"));
				newsVO.setAdminId(rs.getString("admin_id_fk"));
				newsVO.setNewsStatusId(rs.getInt("news_status_id_fk"));
				list.add(newsVO); // Store the row in the list
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

		NewsJDBCDAO dao = new NewsJDBCDAO();

		// 新增
		NewsVO empVO1 = new NewsVO();
		
		Timestamp in1 = new Timestamp(System.currentTimeMillis()); 
		
		empVO1.setNewsId("N007");
		empVO1.setNewsTitle("測試中22");
		empVO1.setNewsContent("測試內容22");
		empVO1.setNewsStartDate(in1);
		empVO1.setNewsEndDate(in1);
		empVO1.setNewsImg(null);
		empVO1.setCreatedTime(in1);
		empVO1.setNewsCategoryId("NC001");
		empVO1.setAdminId("A001");
		empVO1.setNewsStatusId(0);
		
		dao.insert(empVO1);

		// 修改
//		NewsVO empVO2 = new NewsVO();
//		
//		Timestamp in1 = new Timestamp(System.currentTimeMillis()); 
//		
//		empVO2.setNewsId("N007");
//		empVO2.setNewsTitle("修改中");
//		empVO2.setNewsContent("修改內容");
//		empVO2.setNewsStartDate(in1);
//		empVO2.setNewsEndDate(in1);
//		empVO2.setNewsImg(null);
//		empVO2.setCreatedTime(in1);
//		empVO2.setNewsCategoryId("NC001");
//		empVO2.setAdminId("A001");
//		empVO2.setNewsStatusId(0);
//		
//		dao.update(empVO2);

		// 刪除
//		dao.delete("N007");

		// 查詢
//		NewsVO empVO3 = dao.findByPrimaryKey("N001");
//	
//		System.out.print(empVO3.getNewsId() + ",");
//		System.out.print(empVO3.getNewsTitle() + ",");
//		System.out.print(empVO3.getNewsContent() + ",");
//		System.out.print(empVO3.getNewsStartDate() + ",");
//		System.out.print(empVO3.getNewsEndDate() + ",");
//		System.out.print(empVO3.getNewsImg() + ",");
//		System.out.print(empVO3.getCreatedTime() + ",");
//		System.out.print(empVO3.getNewsCategoryId() + ",");
//		System.out.print(empVO3.getAdminId() + ",");
//		System.out.print(empVO3.getNewsStatusId() + ",");
//
//		System.out.println("---------------------");

		// 查詢
//		List<NewsVO> list = dao.getAll();
//		for (NewsVO aEmp : list) {
//		System.out.print(aEmp.getNewsId() + ",");
//		System.out.print(aEmp.getNewsTitle() + ",");
//		System.out.print(aEmp.getNewsContent() + ",");
//		System.out.print(aEmp.getNewsStartDate() + ",");
//		System.out.print(aEmp.getNewsEndDate() + ",");
//		System.out.print(aEmp.getNewsImg() + ",");
//		System.out.print(aEmp.getCreatedTime() + ",");
//		System.out.print(aEmp.getNewsCategoryId() + ",");
//		System.out.print(aEmp.getAdminId() + ",");
//		System.out.print(aEmp.getNewsStatusId() + ",");
//			System.out.println();
//		}
		
	}


	@Override
	public List<NewsVO> getNewsByPage(String title, Integer categoryId, Integer statusId, int page,
			int recordsPerPage) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getNewsCount(String title, Integer categoryId, Integer statusId) {
		// TODO Auto-generated method stub
		return 0;
	}
	

	
}
