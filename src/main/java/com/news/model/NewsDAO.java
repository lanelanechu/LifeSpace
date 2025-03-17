package com.news.model;

import java.util.*;
import java.sql.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class NewsDAO implements NewsDAO_interface {

	// 一個應用程式中,針對一個資料庫 ,共用一個DataSource即可...
	private static DataSource ds = null;
	static {
		try {
			InitialContext ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TestDB2");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private static final String GET_NEWS_BY_PAGE = "SELECT news_id, news_title, news_content, news_start_date, news_end_date, news_img, created_time, news_category_id_fk, admin_id_fk, news_status_id_fk "
			+ "FROM news " + "WHERE news_title LIKE ? " + "AND (news_category_id_fk = ? OR ? IS NULL) "
			+ "AND (news_status_id_fk = ? OR ? IS NULL) " + "ORDER BY news_id " + "LIMIT ?, ?";

	private static final String GET_NEWS_COUNT = "SELECT COUNT(*) FROM news " + "WHERE news_title LIKE ? "
			+ "AND (news_category_id_fk = ? OR ? IS NULL) " + "AND (news_status_id_fk = ? OR ? IS NULL)";

	
	private static final String INSERT_STMT = "INSERT INTO news ( news_id, news_title, news_content, news_start_date, news_end_date, news_img, created_time, news_category_id_fk, admin_id_fk, news_status_id_fk )"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = "SELECT news_id, news_title, news_content, news_start_date, news_end_date, news_img, created_time, news_category_id_fk, admin_id_fk, news_status_id_fk"
			+ " FROM news order by news_id";
	private static final String GET_ONE_STMT = "SELECT news_id, news_title, news_content, news_start_date, news_end_date, news_img, created_time, news_category_id_fk, admin_id_fk, news_status_id_fk"
			+ " FROM news where news_id = ?";
	private static final String DELETE = "DELETE FROM news where news_id = ?";
	private static final String UPDATE = "UPDATE news set news_title=?, news_content=?, news_start_date=?, news_end_date=?, news_img=?, created_time=?, news_category_id_fk=?, admin_id_fk=?, news_status_id_fk=?"
			+ " where news_id = ?";

	@Override
	public void insert(NewsVO newsVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, newsVO.getNewsId());
			pstmt.setString(2, newsVO.getNewsTitle());
			pstmt.setString(3, newsVO.getNewsContent());
			pstmt.setTimestamp(4, newsVO.getNewsStartDate());
			pstmt.setTimestamp(5, newsVO.getNewsEndDate());
			pstmt.setBytes(6, newsVO.getNewsImg());
			pstmt.setTimestamp(7, newsVO.getCreatedTime());
			pstmt.setString(8, newsVO.getNewsCategoryId());
			pstmt.setString(9, newsVO.getAdminId());
			pstmt.setInt(10, newsVO.getNewsStatusId());

			pstmt.executeUpdate();

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
	public void update(NewsVO newsVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
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
	public void delete(String newsId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, newsId);

			pstmt.executeUpdate();

			// Handle any driver errors
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
	public NewsVO findByPrimaryKey(String newsId) {

		NewsVO newsVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, newsId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
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
			}

			// Handle any driver errors
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

			con = ds.getConnection();
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

	@Override
	public String getNextNewsId() throws SQLException {
		String nextId = "N001"; // 預設初始值
		String pref = "N"; // 改成你的表格的流水號開頭

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String query = "SELECT MAX(news_id) FROM news";

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();

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
	public List<NewsVO> getNewsByPage(String title, Integer categoryId, Integer statusId, int page,
			int recordsPerPage) {
		 List<NewsVO> list = new ArrayList<>();
	        NewsVO newsVO = null;
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;

	        try {
	            con = ds.getConnection();
	            pstmt = con.prepareStatement(GET_NEWS_BY_PAGE);

	            int startIndex = (page - 1) * recordsPerPage;

	            pstmt.setString(1, "%" + title + "%");
	            if (categoryId != null) {
	                pstmt.setInt(2, categoryId);
	            } else {
	                pstmt.setString(2, null);
	            }
	            pstmt.setObject(3, categoryId);

	            if (statusId != null) {
	                pstmt.setInt(4, statusId);
	            } else {
	                pstmt.setString(4, null);
	            }
	            pstmt.setObject(5, statusId);

	            pstmt.setInt(6, startIndex);
	            pstmt.setInt(7, recordsPerPage);

	            rs = pstmt.executeQuery();

	            while (rs.next()) {
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
	                list.add(newsVO);
	            }

	        } catch (SQLException se) {
	            throw new RuntimeException("A database error occurred. " + se.getMessage());
	        } finally {
	            // ... (關閉資源的程式碼)
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

	@Override
	public int getNewsCount(String title, Integer categoryId, Integer statusId) {
		   int count = 0;
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;

	        try {
	            con = ds.getConnection();
	            pstmt = con.prepareStatement(GET_NEWS_COUNT);

	            pstmt.setString(1, "%" + title + "%");
	            if (categoryId != null) {
	                pstmt.setInt(2, categoryId);
	            } else {
	                pstmt.setString(2, null);
	            }
	            pstmt.setObject(3, categoryId);

	            if (statusId != null) {
	                pstmt.setInt(4, statusId);
	            } else {
	                pstmt.setString(4, null);
	            }
	            pstmt.setObject(5, statusId);

	            rs = pstmt.executeQuery();

	            if (rs.next()) {
	                count = rs.getInt(1);
	            }

	        } catch (SQLException se) {
	            throw new RuntimeException("A database error occurred. " + se.getMessage());
	        } finally {
	            // ... (關閉資源的程式碼)
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
	        return count;
	    }
	}