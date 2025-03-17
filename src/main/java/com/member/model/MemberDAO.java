package com.member.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.*;

public class MemberDAO implements MemberDAO_interface {

	// 一個應用程式中,針對一個資料庫 ,共用一個DataSource即可
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/life_space_01");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private static final String INSERT_STMT = "INSERT INTO member (member_id, member_name, member_image, email, registration_time, phone, account_status, password, birthday) VALUES(?,?,?,?,?,?,?,?,?)";
	private static final String GET_ALL_STMT = "SELECT member_id, member_name, member_image, email, registration_time, phone, account_status, password, birthday FROM member order by member_id";
	private static final String GET_ONE_STMT = "SELECT member_id, member_name, member_image, email, registration_time, phone, account_status, password, birthday FROM member where member_id = ?";
	private static final String DELETE = "DELETE FROM member where member_id = ?";
	private static final String UPDATE = "UPDATE member set member_name=?, member_image=?, email=?, registration_time=?, phone=?, account_status=?, password=?, birthday=? where member_id=?";
	private static final String SEARCH_MEMBERS = "SELECT * FROM member WHERE "
			+ "(member_id = COALESCE(?, member_id)) AND " + "(member_name LIKE COALESCE(?, member_name)) AND "
			+ "(phone = COALESCE(?, phone)) AND " + "(email LIKE COALESCE(?, email))";
	private static final String IMG_UPDATE = "UPDATE member SET profile_pic = ? WHERE member_id = ?";
	private static final String SEARCH_MEMBERS2 = "SELECT * FROM member WHERE "
			+ "(account_status = COALESCE(?, account_status)) AND "
			+ "(DATE(registration_time) = COALESCE(?, DATE(registration_time))) AND "
			+ "(birthday = COALESCE(?, birthday))";
	
	private static final String SHOW_IMAGE = "SELECT member_image FROM member WHERE member_id = ?";
	
	//-----------------------------------------------------

	@Override
	public void insert(MemberVO memberVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, memberVO.getMemberId());
			pstmt.setString(2, memberVO.getMemberName());
			pstmt.setBytes(3, memberVO.getMemberImage());
			pstmt.setString(4, memberVO.getEmail());
//			pstmt.setTimestamp(5, memberVO.getRegistrationTime());
			pstmt.setString(6, memberVO.getPhone());
			pstmt.setInt(7, memberVO.getAccountStatus());
			pstmt.setString(8, memberVO.getPassword());
			pstmt.setDate(9, memberVO.getBirthday());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
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
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, memberVO.getMemberId());
			pstmt.setString(2, memberVO.getMemberName());
//			照片啦QQ pstmt.setbyte[](3, memberVO.getMemberImage());
			pstmt.setString(4, memberVO.getEmail());
			pstmt.setTimestamp(5, memberVO.getRegistrationTime());
			pstmt.setString(6, memberVO.getPhone());
			pstmt.setInt(7, memberVO.getAccountStatus());
			pstmt.setString(8, memberVO.getPassword());
			pstmt.setDate(9, memberVO.getBirthday());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
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
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, memberId);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
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
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);
			;

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

		} catch (SQLException e) {
			e.printStackTrace();
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
			con = ds.getConnection();
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

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
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
	public List<MemberVO> searchMembers(String memberId, String memberName, String phone, String email) {

		List<MemberVO> list = new ArrayList<MemberVO>();
		MemberVO memberVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			// 設定參數，避免空值導致 SQL 錯誤
			pstmt.setString(1, memberId.isEmpty() ? null : memberId);
			pstmt.setString(2, memberId.isEmpty() ? null : memberId);

			pstmt.setString(3, memberName.isEmpty() ? null : "%" + memberName + "%");
			pstmt.setString(4, memberName.isEmpty() ? null : "%" + memberName + "%");

			pstmt.setString(5, phone.isEmpty() ? null : phone);
			pstmt.setString(6, phone.isEmpty() ? null : phone);

			pstmt.setString(7, email.isEmpty() ? null : "%" + email + "%");
			pstmt.setString(8, email.isEmpty() ? null : "%" + email + "%");

			while (rs.next()) {
				MemberVO member = new MemberVO();
				member.setMemberId(rs.getString("memberId"));
				member.setMemberName(rs.getString("memberName"));
				member.setPhone(rs.getString("phone"));
				member.setEmail(rs.getString("email"));
				// 其他欄位
				list.add(member);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
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
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

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

	public void saveMemberImage(String memberId, String imagePath) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(IMG_UPDATE);
			FileInputStream fis = new FileInputStream(new File(imagePath));
			rs = pstmt.executeQuery();
			

			pstmt.setBinaryStream(1, fis, (int) new File(imagePath).length());
			pstmt.setString(2, memberId);

			int rowsUpdated = pstmt.executeUpdate();
			System.out.println(rowsUpdated > 0 ? "圖片存入成功！" : "存入失敗！");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
	
	
	// 將圖片從資料庫提取出來
		public byte[] getMemberImage(String memberId) {

			byte[] imageBytes = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				
				con = ds.getConnection();
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
//				if (imageBytes == null || imageBytes.length == 0) {
//		            InputStream defaultImage = getClass().getClassLoader().getResourceAsStream("images/G3_img/default_pic/default.jpg");
//		            if (defaultImage != null) {
//		                imageBytes = defaultImage.readAllBytes();
//		                defaultImage.close();
//		            }
//		        }
				

				

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
	
	

}
