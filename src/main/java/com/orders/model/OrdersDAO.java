package com.orders.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class OrdersDAO implements OrdersDAO_interface {

	// 一個應用程式中,針對一個資料庫 ,共用一個DataSource即可
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TestDB2");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private static final String INSERT_STMT = "INSERT INTO orders (order_id, space_id, member_id, branch_id, total_price, payment_datetime, "
			+ "order_start, order_end, comment_time, comment_contect, satisfaction, accounts_payable) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String GET_ALL_STMT = "SELECT order_id, space_id, member_id, branch_id, total_price, payment_datetime, "
			+ "order_start, order_end, comment_time, comment_contect, satisfaction, "
			+ "accounts_payable, created_time FROM orders ORDER BY order_id";

	private static final String GET_ONE_STMT = "SELECT order_id, space_id, member_id, branch_id, total_price, payment_datetime, "
			+ "order_start, order_end, comment_time, comment_contect, satisfaction, "
			+ "accounts_payable, created_time FROM orders WHERE order_id = ?";

	private static final String DELETE = "DELETE FROM orders WHERE order_id = ?";

	private static final String UPDATE = "UPDATE orders SET space_id=?, member_id=?, branch_id=?, total_price=?, payment_datetime=?, "
			+ "order_start=?, order_end=?, comment_time=?, comment_contect=?, satisfaction=?, "
			+ "accounts_payable=? WHERE order_id = ?";

	@Override
	public void insert(OrdersVO ordersVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, ordersVO.getOrderId());
			pstmt.setString(2, ordersVO.getSpaceId());
			pstmt.setString(3, ordersVO.getMemberId());
			pstmt.setString(4, ordersVO.getBranchId());
			pstmt.setInt(5, ordersVO.getTotalPrice());
			pstmt.setTimestamp(6, ordersVO.getPaymentDatetime());
			pstmt.setTimestamp(7, ordersVO.getOrderStart());
			pstmt.setTimestamp(8, ordersVO.getOrderEnd());
			pstmt.setTimestamp(9, ordersVO.getCommentTime());
			pstmt.setString(10, ordersVO.getCommentContect());
			pstmt.setInt(11, ordersVO.getSatisfaction());
			pstmt.setInt(12, ordersVO.getAccountsPayable());

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
	public void update(OrdersVO ordersVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, ordersVO.getSpaceId());
			pstmt.setString(2, ordersVO.getMemberId());
			pstmt.setString(3, ordersVO.getBranchId());
			pstmt.setInt(4, ordersVO.getTotalPrice());
			pstmt.setTimestamp(5, ordersVO.getPaymentDatetime());
			pstmt.setTimestamp(6, ordersVO.getOrderStart());
			pstmt.setTimestamp(7, ordersVO.getOrderEnd());
			pstmt.setTimestamp(8, ordersVO.getCommentTime());
			pstmt.setString(9, ordersVO.getCommentContect());
			pstmt.setInt(10, ordersVO.getSatisfaction());
			pstmt.setInt(11, ordersVO.getAccountsPayable());
			pstmt.setString(12, ordersVO.getOrderId());

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
	public void delete(String orderId) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, orderId);

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
	public OrdersVO findByPrimaryKey(String order_id) {
		OrdersVO ordersVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, order_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// ordersVO 也稱為 Domain objects
				ordersVO = new OrdersVO();
				ordersVO.setOrderId(rs.getString("order_id"));
				ordersVO.setSpaceId(rs.getString("space_id"));
				ordersVO.setMemberId(rs.getString("member_id"));
				ordersVO.setBranchId(rs.getString("branch_id"));
				ordersVO.setTotalPrice(rs.getInt("total_price"));
				ordersVO.setPaymentDatetime(rs.getTimestamp("payment_datetime"));
				ordersVO.setOrderStart(rs.getTimestamp("order_start"));
				ordersVO.setOrderEnd(rs.getTimestamp("order_end"));
				ordersVO.setCommentTime(rs.getTimestamp("comment_time"));
				ordersVO.setCommentContect(rs.getString("comment_contect"));
				ordersVO.setSatisfaction(rs.getInt("satisfaction"));
				ordersVO.setAccountsPayable(rs.getInt("accounts_payable"));
				ordersVO.setCreatedTime(rs.getTimestamp("created_time"));
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
		return ordersVO;
	}

	@Override
	public List<OrdersVO> getAll() {
		List<OrdersVO> list = new ArrayList<OrdersVO>();
		OrdersVO ordersVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// ordersVO 也稱為 Domain objects
				ordersVO = new OrdersVO();
				ordersVO.setOrderId(rs.getString("order_id"));
				ordersVO.setSpaceId(rs.getString("space_id"));
				ordersVO.setMemberId(rs.getString("member_id"));
				ordersVO.setBranchId(rs.getString("branch_id"));
				ordersVO.setTotalPrice(rs.getInt("total_price"));
				ordersVO.setPaymentDatetime(rs.getTimestamp("payment_datetime"));
				ordersVO.setOrderStart(rs.getTimestamp("order_start"));
				ordersVO.setOrderEnd(rs.getTimestamp("order_end"));
				ordersVO.setCommentTime(rs.getTimestamp("comment_time"));
				ordersVO.setCommentContect(rs.getString("comment_contect"));
				ordersVO.setSatisfaction(rs.getInt("satisfaction"));
				ordersVO.setAccountsPayable(rs.getInt("accounts_payable"));
				ordersVO.setCreatedTime(rs.getTimestamp("created_time"));
				list.add(ordersVO); // Store the row in the list
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
}
