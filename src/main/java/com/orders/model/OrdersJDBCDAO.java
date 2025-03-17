package com.orders.model;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import com.event.model.*;

public class OrdersJDBCDAO implements OrdersDAO_interface{
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/life_space_01?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";
	
	private static final String INSERT_STMT = 
			"INSERT INTO orders (order_id,space_id,member_id,branch_id,event_id,total_price,payment_datetime,order_start,order_end,comment_time,comment_contect,satisfaction,accounts_payable) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		private static final String GET_ALL_STMT = 
			"SELECT o.order_id, o.space_id, o.member_id, o.branch_id, o.event_id, o.total_price, o.payment_datetime, o.order_start, o.order_end, o.comment_time, o.comment_contect, o.satisfaction, o.accounts_payable, o.order_status, o.created_time, e.event_name FROM orders o LEFT JOIN event e ON o.event_id = e.event_id ORDER BY o.order_id";
		private static final String GET_ONE_STMT = 
			"SELECT order_id, space_id, member_id, branch_id, event_id, total_price, payment_datetime, order_start, order_end, comment_time, comment_contect, satisfaction, accounts_payable, order_status, created_time FROM orders WHERE order_id = ?";
		private static final String DELETE = 
			"UPDATE orders set order_status = 0 where order_id = ?";
		private static final String UPDATE = 
			"UPDATE orders set space_id=?, member_id=?, branch_id=?, event_id=? ,total_price=?, payment_datetime=?, order_start=?, order_end=?, comment_time=?, comment_contect=?, satisfaction=?, accounts_payable=? ,order_status=? WHERE order_id = ?";
	
		 private String getNextOrderId() throws SQLException {
		        String nextId = "OR001"; // 預設初始值
		        String pref = "OR";  // 改成你的表格的流水號開頭

		        try {
		            Class.forName(driver);
		        } catch (ClassNotFoundException e) {
		            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		        }

		        String query = "SELECT MAX(order_id) FROM orders";
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
		public void insert(OrdersVO ordersVO) {

			Connection con = null;
			PreparedStatement pstmt = null;

			try {

				Class.forName(driver);
				con = DriverManager.getConnection(url, userid, passwd);
				pstmt = con.prepareStatement(INSERT_STMT);

				pstmt.setString(1, getNextOrderId());
				pstmt.setString(2, ordersVO.getSpaceId());
	            pstmt.setString(3, ordersVO.getMemberId());
	            pstmt.setString(4, ordersVO.getBranchId());
	            pstmt.setString(5, ordersVO.getEventId());
	            pstmt.setInt(6, ordersVO.getTotalPrice());
	            pstmt.setTimestamp(7, ordersVO.getPaymentDatetime());
	            pstmt.setTimestamp(8, ordersVO.getOrderStart());
	            pstmt.setTimestamp(9, ordersVO.getOrderEnd());
	            pstmt.setTimestamp(10, ordersVO.getCommentTime());
	            pstmt.setString(11, ordersVO.getCommentContect());
	            pstmt.setInt(12, ordersVO.getSatisfaction());
	            pstmt.setInt(13, ordersVO.getAccountsPayable());
	     
	            
				

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
		public void update(OrdersVO ordersVO) {

			Connection con = null;
			PreparedStatement pstmt = null;

			try {

				Class.forName(driver);
				con = DriverManager.getConnection(url, userid, passwd);
				pstmt = con.prepareStatement(UPDATE);
				
				pstmt.setString(1, ordersVO.getSpaceId());
	            pstmt.setString(2, ordersVO.getMemberId());
	            pstmt.setString(3, ordersVO.getBranchId());
	            pstmt.setString(4, ordersVO.getEventId());
	            pstmt.setInt(5, ordersVO.getTotalPrice());
	            pstmt.setTimestamp(6, ordersVO.getPaymentDatetime());
	            pstmt.setTimestamp(7, ordersVO.getOrderStart());
	            pstmt.setTimestamp(8, ordersVO.getOrderEnd());
	            pstmt.setTimestamp(9, ordersVO.getCommentTime());
	            pstmt.setString(10, ordersVO.getCommentContect());
	            pstmt.setInt(11, ordersVO.getSatisfaction());
	            pstmt.setInt(12, ordersVO.getAccountsPayable());
	            pstmt.setInt(13, ordersVO.getOrderStatus());
	            pstmt.setString(14, ordersVO.getOrderId());
	            
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
		public void delete(String orderId) {

			Connection con = null;
			PreparedStatement pstmt = null;

			try {

				Class.forName(driver);
				con = DriverManager.getConnection(url, userid, passwd);
				pstmt = con.prepareStatement(DELETE);

				pstmt.setString(1, orderId);

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
		public OrdersVO findByPrimaryKey(String orderId) {

			OrdersVO ordersVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {

				Class.forName(driver);
				con = DriverManager.getConnection(url, userid, passwd);
				pstmt = con.prepareStatement(GET_ONE_STMT);

				pstmt.setString(1, orderId);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					// empVo 也稱為 Domain objects
					ordersVO = new OrdersVO();
					ordersVO.setOrderId(rs.getString("order_id"));
					ordersVO.setSpaceId(rs.getString("space_id"));
		            ordersVO.setMemberId(rs.getString("member_id"));
		            ordersVO.setBranchId(rs.getString("branch_id"));
		            ordersVO.setEventId(rs.getString("event_id"));
		            ordersVO.setTotalPrice(rs.getInt("total_price"));
		            ordersVO.setPaymentDatetime(rs.getTimestamp("payment_datetime"));
		            ordersVO.setOrderStart(rs.getTimestamp("order_start"));
		            ordersVO.setOrderEnd(rs.getTimestamp("order_end"));
		            ordersVO.setCommentTime(rs.getTimestamp("comment_time"));
		            ordersVO.setCommentContect(rs.getString("comment_contect"));
		            ordersVO.setSatisfaction(rs.getInt("satisfaction"));
		            ordersVO.setAccountsPayable(rs.getInt("accounts_payable"));
		            ordersVO.setOrderStatus(rs.getInt("order_status"));
		            ordersVO.setCreatedTime(rs.getTimestamp("created_time"));

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

				Class.forName(driver);
				con = DriverManager.getConnection(url, userid, passwd);
				pstmt = con.prepareStatement(GET_ALL_STMT);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					// empVO 也稱為 Domain objects
					ordersVO = new OrdersVO();
					ordersVO.setOrderId(rs.getString("order_id"));
					ordersVO.setSpaceId(rs.getString("space_id"));
		            ordersVO.setMemberId(rs.getString("member_id"));
		            ordersVO.setBranchId(rs.getString("branch_id"));
		            ordersVO.setEventId(rs.getString("event_id"));
		            ordersVO.setTotalPrice(rs.getInt("total_price"));
		            ordersVO.setPaymentDatetime(rs.getTimestamp("payment_datetime"));
		            ordersVO.setOrderStart(rs.getTimestamp("order_start"));
		            ordersVO.setOrderEnd(rs.getTimestamp("order_end"));
		            ordersVO.setCommentTime(rs.getTimestamp("comment_time"));
		            ordersVO.setCommentContect(rs.getString("comment_contect"));
		            ordersVO.setSatisfaction(rs.getInt("satisfaction"));
		            ordersVO.setAccountsPayable(rs.getInt("accounts_payable"));
		            ordersVO.setOrderStatus(rs.getInt("order_status"));
		            ordersVO.setCreatedTime(rs.getTimestamp("created_time"));
		            
		            EventVO eventVO = new EventVO();
		            
		            String eventId = rs.getString("event_id");
		            eventVO.setEventName(rs.getString("event_name"));

		            if (eventId != null) { 
		                eventVO.setEventId(eventId);
		                eventVO.setEventName(rs.getString("event_name"));
		            } else {
		                eventVO.setEventId("-");
		                eventVO.setEventName("尚無建立揪團活動");
		            }
		            
		            eventVO.setEventId(rs.getString("event_id"));
	   
	                ordersVO.setEventVO(eventVO);

					list.add(ordersVO); // Store the row in the list
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

			OrdersJDBCDAO dao = new OrdersJDBCDAO();
			
////			 新增
//			OrdersVO ordersVO1 = new OrdersVO();
//			
//			ordersVO1.setSpaceId("S001");
//            ordersVO1.setMemberId("M001");
//            ordersVO1.setBranchId("B001");
//            ordersVO1.setEventId("E003");
//            ordersVO1.setTotalPrice(2000);
//            ordersVO1.setPaymentDatetime(Timestamp.valueOf("2025-02-28 15:00:00"));
//            ordersVO1.setOrderStart(Timestamp.valueOf("2025-02-28 18:00:00"));
//            ordersVO1.setOrderEnd(Timestamp.valueOf("2025-02-28 19:00:00"));
//            ordersVO1.setCommentTime(Timestamp.valueOf("2025-02-28 21:00:00"));
//            ordersVO1.setCommentContect("2遊戲非常有趣，活動氣氛熱烈，大家玩得很開心！");
//            ordersVO1.setSatisfaction(5);
//            ordersVO1.setAccountsPayable(2000);
//			dao.insert(ordersVO1);
//			
//			System.out.println("新增成功");
//
//			// 修改
//			OrdersVO ordersVO2 = new OrdersVO();
//			
//			ordersVO2.setOrderId("OR001");
//			ordersVO2.setSpaceId("S001");
//            ordersVO2.setMemberId("M001");
//            ordersVO2.setBranchId("B001");
//            ordersVO2.setEventId("E001");
//            ordersVO2.setTotalPrice(2000);
//            ordersVO2.setPaymentDatetime(Timestamp.valueOf("2025-02-28 15:00:00"));
//            ordersVO2.setOrderStart(Timestamp.valueOf("2025-02-28 17:00:00"));
//            ordersVO2.setOrderEnd(Timestamp.valueOf("2025-02-28 19:00:00"));
//            ordersVO2.setCommentTime(Timestamp.valueOf("2025-02-28 21:00:00"));
//            ordersVO2.setCommentContect("2遊戲非常有趣，活動氣氛熱烈，大家玩得很開心！");
//            ordersVO2.setSatisfaction(5);
//            ordersVO2.setAccountsPayable(3000);
//			ordersVO2.setOrderStatus(1);
//			dao.update(ordersVO2);
//			System.out.println("更新成功");
			
//			 刪除(更新成0)
//			OrdersVO ordersVO4 = new OrdersVO();
//
//			ordersVO4.setOrderStatus(0);
//			dao.update(ordersVO4);

//			 查詢
//			OrdersVO ordersVO3 = dao.findByPrimaryKey("OR001");
//			System.out.print(ordersVO3.getOrderId() + ",");
//			System.out.print(ordersVO3.getSpaceId() + ",");
//			System.out.print(ordersVO3.getMemberId() + ",");
//			System.out.print(ordersVO3.getBranchId() + ",");
//			System.out.print(ordersVO3.getEventId() + ",");
//			System.out.print(ordersVO3.getTotalPrice() + ",");
//			System.out.print(ordersVO3.getOrderStart() + ",");
//			System.out.print(ordersVO3.getOrderEnd() + ",");
//			System.out.print(ordersVO3.getCommentTime() + ",");
//			System.out.print(ordersVO3.getCommentContect() + ",");
//			System.out.print(ordersVO3.getSatisfaction() + ",");
//			System.out.print(ordersVO3.getAccountsPayable() + ",");
//			System.out.print(ordersVO3.getOrderStatus() + ",");
//			System.out.println(ordersVO3.getCreatedTime());
//			System.out.println("---------------------");

//			 查詢
			List<OrdersVO> list = dao.getAll();
			for (OrdersVO aOrders : list) {
				System.out.print(aOrders.getOrderId() + ",");
				System.out.print(aOrders.getSpaceId() + ",");
				System.out.print(aOrders.getMemberId() + ",");
				System.out.print(aOrders.getBranchId() + ",");
				System.out.print(aOrders.getEventId() + ",");
				System.out.print(aOrders.getTotalPrice() + ",");
				System.out.print(aOrders.getPaymentDatetime() + ",");
				System.out.print(aOrders.getOrderStart() + ",");
				System.out.print(aOrders.getOrderEnd() + ",");
				System.out.print(aOrders.getCommentTime() + ",");
				System.out.print(aOrders.getCommentContect() + ",");
				System.out.print(aOrders.getSatisfaction() + ",");
				System.out.print(aOrders.getAccountsPayable() + ",");
				System.out.print(aOrders.getOrderStatus() + ",");
				System.out.print(aOrders.getCreatedTime());
				System.out.print(aOrders.getEventVO()); // join EventVO
				System.out.println();
			}
		}
	}