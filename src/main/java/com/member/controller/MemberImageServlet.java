package com.member.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.member.model.MemberJDBCDAO;

@WebServlet("/MemberImageServlet")
public class MemberImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberJDBCDAO daoImage = new MemberJDBCDAO();

	public MemberImageServlet() {
		super();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("image/jpeg"); // 設定回應類型為圖片
		ServletOutputStream out = res.getOutputStream();
		String memberId = req.getParameter("memberId");
		
		if (memberId == null || memberId.trim().isEmpty()) {
			System.out.println("memberId 為空，無法載入圖片！");
			return;
		}

		try {
			//讀取 BLOB
			byte[] imageBytes = daoImage.getMemberImage(memberId);

			if (imageBytes != null) {
				out.write(imageBytes);
				System.out.println("成功讀取圖片：" + memberId);
			} else {
				System.out.println("member_image 為 null");
			}

//	        
//			if (rs.next()) {
//				
//                Blob blob = rs.getBlob("member_image");
//                if (blob != null) {
//                    byte[] imageBytes = blob.getBytes(1, (int) blob.length());
//                    res.getOutputStream().write(imageBytes);
//                    System.out.println("成功讀取圖片：" + memberId);
//                } else {
//                    System.out.println("member_image 為 null");
//                }

//				BufferedInputStream in = new BufferedInputStream(rs.getBinaryStream("member_image"));
//				byte[] buf = new byte[4 * 1024]; // 4K buffer
//				int len;
//				while ((len = in.read(buf)) != -1) {
//					out.write(buf, 0, len);
//				}
//				in.close();

		} catch (Exception e) {
			e.printStackTrace();
		} 

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
