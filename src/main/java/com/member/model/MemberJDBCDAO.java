package com.member.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class MemberJDBCDAO implements MemberDAO_interface {
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/life_space_01?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";

	//-------------------------------------------------------------------------
	private static final String INSERT_STMT = "INSERT INTO member (member_id, member_name, member_image, email, registration_time, phone, account_status, password, birthday) VALUES(?,?,?,?,?,?,?,?,?)";
	private static final String GET_ALL_STMT = "SELECT member_id, member_name, member_image, email, registration_time, phone, account_status, password, birthday FROM member order by member_id";
	private static final String GET_ONE_STMT = "SELECT member_id, member_name, member_image, email, registration_time, phone, account_status, password, birthday FROM member where member_id = ?";
	private static final String DELETE = "DELETE FROM member where member_id = ?";
	private static final String UPDATE = "UPDATE member set member_name=?, member_image=?, email=?, phone=?, account_status=?, password=?, birthday=? where member_id=?";
	private static final String SEARCH_MEMBERS = "SELECT * FROM member WHERE "
			+ "(member_id = COALESCE(?, member_id)) AND " + "(member_name LIKE COALESCE(?, member_name)) AND "
			+ "(phone = COALESCE(?, phone)) AND " + "(email LIKE COALESCE(?, email))";

	private static final String SEARCH_MEMBERS2 = "SELECT * FROM member WHERE "
			+ "(account_status = COALESCE(?, account_status)) AND "
			+ "(DATE(registration_time) = COALESCE(?, DATE(registration_time))) AND "
			+ "(birthday = COALESCE(?, birthday))";
	private static final String INSERT_IMAGE = "UPDATE member SET  member_image = ? WHERE member_id = ?";
	private static final String SHOW_IMAGE = "SELECT member_image FROM member WHERE member_id = ?";
    //------------------------------------------------------------------------------------------------
	
	
	// 獲取下一個流水號
	private String getNextMemberId() throws SQLException {
		String nextId = "M001"; // 預設初始值
		String pref = "M"; // 改成你的表格的流水號開頭

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		}

		String query = "SELECT MAX(member_id) FROM member";
		try (Connection conn = DriverManager.getConnection(url, userid, passwd);
				PreparedStatement pstmt = conn.prepareStatement(query);
				ResultSet rs = pstmt.executeQuery()) {
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
	public void insert(MemberVO memberVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, getNextMemberId());
			pstmt.setString(2, memberVO.getMemberName());
			pstmt.setBytes(3, memberVO.getMemberImage());
			pstmt.setString(4, memberVO.getEmail());
			pstmt.setTimestamp(5, memberVO.getRegistrationTime());
			pstmt.setString(6, memberVO.getPhone());
			pstmt.setInt(7, memberVO.getAccountStatus());
			pstmt.setString(8, memberVO.getPassword());
			pstmt.setDate(9, memberVO.getBirthday());

			pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void update(MemberVO memberVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, memberVO.getMemberName());
			pstmt.setBytes(2, memberVO.getMemberImage());
			pstmt.setString(3, memberVO.getEmail());
//			pstmt.setTimestamp(4, memberVO.getRegistrationTime());
			pstmt.setString(4, memberVO.getPhone());
			pstmt.setInt(5, memberVO.getAccountStatus());
			pstmt.setString(6, memberVO.getPassword());
			pstmt.setDate(7, memberVO.getBirthday());
			pstmt.setString(8, memberVO.getMemberId());
			// 注意!!!!根據SQL語法getMemberId()應該是第 8 個

			// pstmt.executeUpdate();這裡就上傳資料了，只是會傳回筆數(int)
			int rowsUpdated = pstmt.executeUpdate();
			System.out.println("更新筆數: " + rowsUpdated);

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void delete(String memberId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, memberId);
			pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public MemberVO findByPrimaryKey(String memberId) {

		MemberVO memberVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, memberId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				memberVO = new MemberVO();
				memberVO.setMemberId(rs.getString("member_id"));
				memberVO.setMemberName(rs.getString("member_name"));
				memberVO.setMemberImage(rs.getBytes("member_image"));
				memberVO.setEmail(rs.getString("email"));
				memberVO.setRegistrationTime(rs.getTimestamp("registration_time"));
				memberVO.setPhone(rs.getString("phone"));
				memberVO.setAccountStatus(rs.getInt("account_status"));
				memberVO.setPassword(rs.getString("password"));
				memberVO.setBirthday(rs.getDate("birthday"));
			}

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return memberVO;
	}

	@Override
	public List<MemberVO> getAll() {
		List<MemberVO> list = new ArrayList<MemberVO>();
		MemberVO memberVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				memberVO = new MemberVO();
				memberVO.setMemberId(rs.getString("member_id"));
				memberVO.setMemberName(rs.getString("member_name"));
				memberVO.setMemberImage(rs.getBytes("member_image"));
				memberVO.setEmail(rs.getString("email"));
				memberVO.setRegistrationTime(rs.getTimestamp("registration_time"));
				memberVO.setPhone(rs.getString("phone"));
				memberVO.setAccountStatus(rs.getInt("account_status"));
				memberVO.setPassword(rs.getString("password"));
				memberVO.setBirthday(rs.getDate("birthday"));
				list.add(memberVO);
			}

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list;

	}

	@Override
	public List<MemberVO> searchMembers(String memberId, String memberName, String phone, String email) {

		List<MemberVO> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(SEARCH_MEMBERS);

			if (memberId.isEmpty()) {
				pstmt.setNull(1, Types.VARCHAR);
			} else {
				pstmt.setString(1, memberId);
			}

			if (memberName.isEmpty()) {
				pstmt.setNull(2, Types.VARCHAR);
			} else {
				pstmt.setString(2, "%" + memberName + "%");
			}

			if (phone.isEmpty()) {
				pstmt.setNull(3, Types.VARCHAR);
			} else {
				pstmt.setString(3, phone);
			}

			if (email.isEmpty()) {
				pstmt.setNull(4, Types.VARCHAR);
			} else {
				pstmt.setString(4, "%" + email + "%");
			}

			// 執行 SQL 查詢
			System.out.println("執行 SQL 查詢: " + pstmt.toString()); // 確認 SQL
			rs = pstmt.executeQuery();
			System.out.println("SQL 執行完成...");

			while (rs.next()) {
				MemberVO member = new MemberVO();
				member.setMemberId(rs.getString("member_id"));
				member.setMemberName(rs.getString("member_name"));
				member.setPhone(rs.getString("phone"));
				member.setEmail(rs.getString("email"));
				member.setRegistrationTime(rs.getTimestamp("registration_time"));
				member.setAccountStatus(rs.getInt("account_status"));
				member.setPassword(rs.getString("password"));
				member.setBirthday(rs.getDate("birthday"));
				list.add(member);
			}

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			// 關閉資源
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	@Override
	public List<MemberVO> searchMembers2(Integer accountStatus, Date registrationTime, Date birthday) {

		List<MemberVO> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(SEARCH_MEMBERS2);

			if (accountStatus == null) {
				pstmt.setNull(1, Types.INTEGER);
			} else {
				pstmt.setInt(1, accountStatus);
			}

			if (registrationTime == null) {
				pstmt.setNull(2, Types.DATE);
			} else {
				pstmt.setDate(2, registrationTime);
			}

			if (birthday == null) {
				pstmt.setNull(3, Types.DATE);
			} else {
				pstmt.setDate(3, birthday);
			}

			System.out.println("執行 SQL 查詢: " + pstmt.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				MemberVO member = new MemberVO();
				member.setMemberId(rs.getString("member_id"));
				member.setMemberName(rs.getString("member_name"));
				member.setPhone(rs.getString("phone"));
				member.setEmail(rs.getString("email"));
				member.setRegistrationTime(rs.getTimestamp("registration_time"));
				member.setAccountStatus(rs.getInt("account_status"));
				member.setPassword(rs.getString("password"));
				member.setBirthday(rs.getDate("birthday"));
				list.add(member);
			}

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return list;
	}

	// 將圖片存入資料庫
	public void saveMemberImage(String memberId, String imagePath) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_IMAGE);
			FileInputStream fis = new FileInputStream(new File(imagePath));

			pstmt.setBinaryStream(1, fis, (int) new File(imagePath).length());
			pstmt.setString(2, memberId);

			int rowsUpdated = pstmt.executeUpdate();
			System.out.println(rowsUpdated > 0 ? "圖片存入成功！" : "存入失敗！");

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} catch (FileNotFoundException ie) {
			throw new RuntimeException("file not found error occurred. " + ie.getMessage());
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

	}

	// 將圖片從資料庫提取出來
	public byte[] getMemberImage(String memberId) {

		byte[] imageBytes = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(SHOW_IMAGE);
			pstmt.setString(1, memberId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				Blob blob = rs.getBlob("member_image");
				if(blob!=null) {
					imageBytes = blob.getBytes(1, (int) blob.length());
					System.out.println("成功從資料庫獲取圖片：" + memberId);
				} else {
	                System.out.println("沒有找到會員 " + memberId + " 的圖片");
	            }
			} else {
	            System.out.println("資料庫中沒有該會員：" + memberId);
	        } 
			
			//顯示預設照片
//			if (imageBytes == null || imageBytes.length == 0) {
//	            InputStream defaultImage = getClass().getClassLoader().getResourceAsStream("images/G3_img/default_pic/default.jpg");
//	            if (defaultImage != null) {
//	                imageBytes = defaultImage.readAllBytes();
//	                defaultImage.close();
//	            }
//	        }
			

			

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return imageBytes;
	}

//----------------- 執行區 ---------------------------

	public static void main(String[] args) {

		MemberJDBCDAO dao1 = new MemberJDBCDAO();

//		// 新增 時間java這邊還是要寫，照片允許null可以不要
//		MemberVO memberVO1 = new MemberVO();
//		// memberVO1.setMemberId("M007");
//		memberVO1.setMemberName("小南");
//		memberVO1.setEmail("456@gmail.com");
//		memberVO1.setRegistrationTime(Timestamp.valueOf(LocalDateTime.now()));
//		memberVO1.setPhone("0912323464");
//		memberVO1.setAccountStatus(1);
//		memberVO1.setPassword("abaf");
//		memberVO1.setBirthday(java.sql.Date.valueOf("1940-03-12"));
//		dao1.insert(memberVO1);
//		System.out.println("我完成了");

//		//修改
//		MemberVO memberVO2 = new MemberVO();
//		memberVO2.setMemberId("M006");
//		memberVO2.setMemberImage(null); //沒有照片就是null
//		memberVO2.setMemberName("阿修ZZZ");
//		memberVO2.setEmail("123@gmail.com");
//		memberVO2.setRegistrationTime(Timestamp.valueOf(LocalDateTime.now()));
//		memberVO2.setPhone("0912343564");
//		memberVO2.setAccountStatus(1);
//		memberVO2.setPassword("abc");
//		memberVO2.setBirthday(java.sql.Date.valueOf("1980-09-12"));
//		dao1.update(memberVO2);
//		System.out.println("我完成了");
//		
//		//刪除
//		dao1.delete("M006");
//		System.out.println("我完成了");
//		
//		//查詢1
//		MemberVO memberVO3 = dao1.findByPrimaryKey("M005");
//		System.out.println(memberVO3.getMemberId() + ",");
//		System.out.println(memberVO3.getMemberName() + ",");
//		System.out.println(memberVO3.getMemberImage() + ",");
//		System.out.println(memberVO3.getEmail() + ",");
//		System.out.println(memberVO3.getRegistrationTime() + ",");
//		System.out.println(memberVO3.getPhone() + ",");
//		System.out.println(memberVO3.getAccountStatus() + ",");
//		System.out.println(memberVO3.getPassword() + ",");
//		System.out.println(memberVO3.getBirthday());
//		System.out.println("------------------");
//		
//		
//		//查詢2
//		List<MemberVO> list = dao1.getAll();
//		for (MemberVO aMember : list) {
//			System.out.print(aMember.getMemberId() + ",");
//			System.out.print(aMember.getMemberName() + ",");
//			System.out.print(aMember.getMemberImage() + ",");
//			System.out.print(aMember.getEmail() + ",");
//			System.out.print(aMember.getRegistrationTime() + ",");
//			System.out.print(aMember.getPhone() + ",");
//			System.out.print(aMember.getAccountStatus() + ",");
//			System.out.print(aMember.getPassword() + ",");
//			System.out.print(aMember.getBirthday());
//			System.out.println();
//		}
//		System.out.println("我完成了");

		// 放入圖片
		MemberJDBCDAO dao = new MemberJDBCDAO();
//		dao.saveMemberImage("M001", "C:/Users/Tibame/Pictures/M001.jpg");
//	    dao.saveMemberImage("M002", "C:/Users/Tibame/Pictures/M002.jpg");
//	    dao.saveMemberImage("M008", "C:/Users/Tibame/Pictures/M008.jpg");
//	    dao.saveMemberImage("M004", "C:/Users/Tibame/Pictures/M004.jpg");
//	    dao.saveMemberImage("M005", "C:/Users/Tibame/Pictures/M005.jpg");
//		dao.saveMemberImage("M006", "C:/Users/Tibame/Pictures/M006.jpg");
//		dao.saveMemberImage("M007", "C:/Users/Tibame/Pictures/M007.jpg");
//		dao.saveMemberImage("M008", "C:/Users/Tibame/Pictures/M008.jpg");
		dao.saveMemberImage("M009", "C:/Users/Tibame/Pictures/M009.jpg");
		dao.saveMemberImage("M010", "C:/Users/Tibame/Pictures/M010.jpg");
		System.out.println("我完成圖片輸入了!");

	}

}
