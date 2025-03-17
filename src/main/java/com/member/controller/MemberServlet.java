package com.member.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.member.model.MemberService;
import com.member.model.MemberVO;

//@WebServlet("/MemberServlet")
@WebServlet("/member/member.do")
public class MemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MemberServlet() {
		super();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//		doGet(request, response);

// ------------------------------------------------------------------------		
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
// ------------------------------------------------------------------------	

// -------------------------------------- 修改 ------------------------------------------------

		if ("getOne_For_Update".equals(action)) { // 來自member.jsp的請求

			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 ****************************************/
			String memberId = req.getParameter("memberId");

			/*************************** 2.開始查詢資料 ****************************************/
			MemberService memberSvc = new MemberService();
			MemberVO memberVO = memberSvc.getOneEmp(memberId);

			/*************************** 3.查詢完成,準備轉交(Send the Success view) ************/

			req.setAttribute("memberVO", memberVO); // 自資料庫中取出spaceVO物件，並存入request
			String url = "/backend/member/member_update.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 member_update.jsp
			successView.forward(req, res);
		}

//----------------------------------------- 送出修改 ----------------------------------------------		

		if ("update".equals(action)) { // 來自member_update.jsp的請求

			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			// 會員編號
			String memberId = req.getParameter("memberId").trim();
			
			// 會員照片
			// 讀取原本的會員圖片
			MemberService memberSvc = new MemberService();
			byte[] memberImage = memberSvc.getMemberImage(memberId);
			// 嘗試讀取新圖片（如果有上傳）
//			Part filePart = req.getPart("memberImage");
//			if (filePart != null && filePart.getSize() > 0) {
//				InputStream fileContent = filePart.getInputStream();
//				memberImage = fileContent.readAllBytes(); //如果有新圖片，則覆蓋原來的
//				fileContent.close();
//			}

			// 會員名字除錯
			String memberName = req.getParameter("memberName");
			String memberNameReg = "^[\u4e00-\u9fa5a-zA-Z]{2,10}$";
			if (memberName == null || memberName.trim().length() == 0) {
				errorMsgs.put("memberName", "會員姓名: 請勿空白");
			} else if (!memberName.trim().matches(memberNameReg)) { // 以下練習正則(規)表示式(regular-expression)
				errorMsgs.put("memberNameReg", "會員姓名: 只能是中、英文字母,且長度必需在2到10之間");
			}

			// email除錯
			String email = req.getParameter("email").trim();
			if (email == null || email.trim().length() == 0 || !email.contains("@gmail.com")) {
				errorMsgs.put("email", "格式不對，請重新輸入");
			}

			// phone除錯
			String phone = req.getParameter("phone").trim();
			if (phone == null || phone.trim().length() == 0) {
				errorMsgs.put("phone", "格式不對，請重新輸入");
			} else if (phone.trim().length() != 10) {
				errorMsgs.put("phone", "電話長度應為10，請重新輸入");
			}

			// accountStatus除錯(要將String格式轉成Int)
			String accountStatusStr = req.getParameter("accountStatus");
			Integer accountStatus = null;
			String accountStatusReg = "^[0-3]$";
			if (accountStatusStr == null || accountStatusStr.trim().length() == 0) {
				errorMsgs.put("accountStatus", "會員狀態: 請勿空白");
			} else if (!accountStatusStr.trim().matches(accountStatusReg)) { // 以下練習正則(規)表示式(regular-expression)
				errorMsgs.put("accountStatus", "會員狀態: 只能是0~3的阿拉伯數字");
			} else {
				try {
					accountStatus = Integer.parseInt(accountStatusStr);
				} catch (NumberFormatException e) {
					errorMsgs.put("accountStatus", "會員狀態: 格式錯誤，請輸入 0~3 的數字");
				}
			}

			// password除錯
			String password = req.getParameter("password");
			String passwordReg = "^[0-9]+$";
			if (password == null || password.trim().length() == 0) {
				errorMsgs.put("password", "密碼: 請勿空白");
			} else if (!password.trim().matches(passwordReg)) {
				errorMsgs.put("password", "密碼: 格式錯誤");
			} else if (!phone.trim().startsWith("0")) {
				errorMsgs.put("phone", "開頭要是0喔~ 請重新輸入");
			}

			// birthday除錯
			java.sql.Date birthday = null;
			try {
				birthday = java.sql.Date.valueOf(req.getParameter("birthday").trim());
			} catch (IllegalArgumentException e) {
				errorMsgs.put("birthday", "請輸入正確日期格式");
			}

			// Send the use back to the form, if there were errors
			if (!errorMsgs.isEmpty()) {
				req.setAttribute("errorMsgs", errorMsgs);
				req.setAttribute("memberName", memberName);
				req.setAttribute("email", email);
				req.setAttribute("phone", phone);
				req.setAttribute("accountStatus", accountStatus);
				req.setAttribute("password", password);
				req.setAttribute("birthday", birthday);
				req.setAttribute("memberImage",memberImage);
				RequestDispatcher failureView = req.getRequestDispatcher("/backend/member/member_update.jsp");
				failureView.forward(req, res);
				return; // 程式中斷
			}

			/*************************** 2.開始修改資料 *****************************************/

			memberSvc = new MemberService();
			MemberVO memberVO = memberSvc.updateMember(memberId, memberName, memberImage, email, birthday, phone,
					accountStatus, password);

			/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
			req.setAttribute("memberVO", memberVO); // 資料庫update成功後,正確的的memberVO物件,存入req

			// 修改成功後, 重新導向回正確的首頁 URL
			// 用sendRedirect，才會讓瀏覽器重新發送一個GET請求，瀏覽器的 URL 也會跟著更新
			String url = req.getContextPath() + "/backend/member/member.jsp";
			res.sendRedirect(url);

//			【原本的寫法不行】forward(req, res) 不會改變 URL，只是在伺服器端進行內部轉發
//			String url = "../backend/member/member.jsp";
//			RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交member.jsp
//			successView.forward(req, res);

		}

		// --------------------------- 新增 ----------------------------------------

		if ("insert".equals(action)) { // 來自member.jsp的請求

			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);

			/*********************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/
			// 會員名字除錯
			String memberName = req.getParameter("memberName");
			String memberNameReg = "^[\u4e00-\u9fa5a-zA-Z]{2,10}$";
			if (memberName == null || memberName.trim().length() == 0) {
				errorMsgs.put("memberName", "會員姓名: 請勿空白");
			} else if (!memberName.trim().matches(memberNameReg)) { // 以下練習正則(規)表示式(regular-expression)
				errorMsgs.put("memberNameReg", "會員姓名: 只能是中、英文字母,且長度必需在2到10之間");
			}

			// email除錯
			String email = req.getParameter("email").trim();
			if (email == null || email.trim().length() == 0 || !email.contains("@gmail.com")) {
				errorMsgs.put("email", "格式不對，請重新輸入");
			}

			// phone除錯
			String phone = req.getParameter("phone").trim();
			if (phone == null || phone.trim().length() == 0) {
				errorMsgs.put("phone", "格式不對，請重新輸入");
			} else if (phone.trim().length() != 10) {
				errorMsgs.put("phone", "電話長度應為10，請重新輸入");
			} else if (!phone.trim().startsWith("0")) {
				errorMsgs.put("phone", "開頭要是0喔~ 請重新輸入");
			}

			// accountStatus除錯(要將String格式轉成Int)
			String accountStatusStr = req.getParameter("accountStatus");
			Integer accountStatus = null;
			String accountStatusReg = "^[0-3]$";
			if (accountStatusStr == null || accountStatusStr.trim().length() == 0) {
				errorMsgs.put("accountStatus", "會員狀態: 請勿空白");
			} else if (!accountStatusStr.trim().matches(accountStatusReg)) { // 以下練習正則(規)表示式(regular-expression)
				errorMsgs.put("accountStatus", "會員狀態: 只能是0~3的阿拉伯數字");
			} else {
				try {
					accountStatus = Integer.parseInt(accountStatusStr);
				} catch (NumberFormatException e) {
					errorMsgs.put("accountStatus", "會員狀態: 格式錯誤，請輸入 0~3 的數字");
				}
			}

			// password除錯
			String password = req.getParameter("password");
			String passwordReg = "^[0-9]+$";
			if (password == null || password.trim().length() == 0) {
				errorMsgs.put("password", "密碼: 請勿空白");
			} else if (!password.trim().matches(passwordReg)) {
				errorMsgs.put("password", "密碼: 格式錯誤");
			}

			// birthday除錯
			java.sql.Date birthday = null;
			try {
				birthday = java.sql.Date.valueOf(req.getParameter("birthday").trim());
			} catch (IllegalArgumentException e) {
				errorMsgs.put("birthday", "請輸入正確日期格式");
			}

			// Send the use back to the form, if there were errors
			if (!errorMsgs.isEmpty()) {
				req.setAttribute("errorMsgs", errorMsgs);
				req.setAttribute("memberName", memberName);
				req.setAttribute("email", email);
				req.setAttribute("phone", phone);
				req.setAttribute("accountStatus", accountStatus);
				req.setAttribute("password", password);
				req.setAttribute("birthday", birthday);
				RequestDispatcher failureView = req.getRequestDispatcher("/backend/member/member_add.jsp");
				failureView.forward(req, res);
				return; // 程式中斷
			}

			/*************************** 2.開始新增資料 ***************************************/

			MemberService memberSvc = new MemberService();
			// String registrationTime = Timestamp.valueOf(LocalDateTime.now());
			memberSvc.addMember(memberName, email, birthday, phone, accountStatus, password);

			/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/

			// 修改成功後, 重新導向回正確的首頁 URL
			// 用sendRedirect，才會讓瀏覽器重新發送一個GET請求，瀏覽器的 URL 也會跟著更新
			String url = req.getContextPath() + "/backend/member/member.jsp";
			res.sendRedirect(url);

		}

		// --------------------------- 單一搜尋 ----------------------------------------

		if ("getOne_For_Display".equals(action)) { // 來自member.jsp的請求

			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/

			// 取得使用者輸入的搜尋條件
			String memberId = req.getParameter("memberId").trim();
			String memberName = req.getParameter("memberName").trim();
			String phone = req.getParameter("phone").trim();
			String email = req.getParameter("email").trim();

//		    如果 req.getParameter("memberId") 為 null，直接 .trim() 
//		    會導致 NullPointerException，然後程式可能會跳過 if (!errorMsgs.isEmpty()) 這部分
			memberId = (memberId == null) ? "" : memberId.trim();
			memberName = (memberName == null) ? "" : memberName.trim();
			phone = (phone == null) ? "" : phone.trim();
			email = (email == null) ? "" : email.trim();

			// 檢查是否至少有一個欄位被填寫
			if (memberId.isEmpty() && memberName.isEmpty() && phone.isEmpty() && email.isEmpty()) {
				errorMsgs.put("searchError", "請至少輸入一個搜尋條件！");
			}

			// 會員流水號除錯
			memberId = req.getParameter("memberId");
			if (!memberId.isEmpty() && !memberId.startsWith("M")) {
				errorMsgs.put("memberId", "會員流水號: 開頭要是M喔");
			}

			// 會員名字除錯
			memberName = req.getParameter("memberName");
			String memberNameReg = "^[\u4e00-\u9fa5a-zA-Z]{2,10}$";
			if (!memberName.isEmpty() && !memberName.matches(memberNameReg)) { // 以下練習正則(規)表示式(regular-expression)
				errorMsgs.put("memberNameReg", "會員姓名: 只能是中、英文字母,且長度必需在2到10之間");
			}

			// email除錯
			email = req.getParameter("email").trim();
			if (!email.isEmpty() && !email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
				errorMsgs.put("email", "信箱格式錯誤，請輸入正確的 Gmail");
			}

			// phone除錯
			phone = req.getParameter("phone").trim();
			if (!phone.isEmpty()) {
				if (!phone.matches("^[0-9]{10}$")) {
					errorMsgs.put("phone", "電話號碼必須是 10 位數字");
				} else if (!phone.startsWith("0")) {
					errorMsgs.put("phone", "電話號碼開頭必須為 0");
				}
			}

			// Send the use back to the form, if there were errors
			if (!errorMsgs.isEmpty()) {
				req.setAttribute("errorMsgs", errorMsgs);
				req.setAttribute("memberName", memberName);
				req.setAttribute("email", email);
				req.setAttribute("phone", phone);
				RequestDispatcher failureView = req.getRequestDispatcher("/backend/member/member.jsp");
				failureView.forward(req, res);
				return; // 程式中斷

			}

			/*************************** 2.開始查詢資料 *****************************************/

			// 執行查詢
			MemberService memberSvc = new MemberService();
			List<MemberVO> resultList = memberSvc.searchMembers(memberId, memberName, phone, email);

			// 若查無資料
			if (resultList.isEmpty()) {
				errorMsgs.put("searchError", "查無符合條件的會員");
				RequestDispatcher failureView = req.getRequestDispatcher("/backend/member/member.jsp");
				failureView.forward(req, res);

			}

			/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
			// 查詢成功，轉交結果
			req.setAttribute("resultList", resultList);
			RequestDispatcher successView = req.getRequestDispatcher("../backend/member/member_search_one.jsp");
			successView.forward(req, res);

		}

		// ----------------------------- 多樣查詢 --------------------------------------

		if ("getMore_For_Display".equals(action)) {

			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/

			// 取得使用者輸入的搜尋條件
			String accountStatusStr = req.getParameter("accountStatus").trim();
			String registrationTimeStr = req.getParameter("registrationTime").trim();
			String birthdayStr = req.getParameter("birthday").trim();

			if (accountStatusStr.isEmpty() && registrationTimeStr.isEmpty() && birthdayStr.isEmpty()) {
				errorMsgs.put("searchError", "請至少輸入一個搜尋條件！");
			}

			// 要將form表單中String型別轉成它本來的型別
			Integer accountStatus = null;
			if (accountStatusStr != null && !accountStatusStr.trim().isEmpty()) {
				try {
					accountStatus = Integer.parseInt(accountStatusStr.trim()); // 🔹 轉換 `String` → `Integer`
				} catch (NumberFormatException e) {
					errorMsgs.put("accountStatus", "帳號狀態必須是數字");
				}
			}

			Date registrationTime = null;
			if (registrationTimeStr != null && !registrationTimeStr.trim().isEmpty()) {
				try {
					registrationTime = Date.valueOf(registrationTimeStr.trim()); // 🔹 `String` →
																					// `Timestamp`
				} catch (IllegalArgumentException e) {
					errorMsgs.put("registrationTime", "註冊日期格式錯誤");
				}
			}

			Date birthday = null;
			if (birthdayStr != null && !birthdayStr.trim().isEmpty()) {
				try {
					birthday = Date.valueOf(birthdayStr.trim()); // 🔹 `String` → `Date`
				} catch (IllegalArgumentException e) {
					errorMsgs.put("birthday", "生日格式錯誤");
				}
			}

			// Send the use back to the form, if there were errors
			if (!errorMsgs.isEmpty()) {
				req.setAttribute("errorMsgs", errorMsgs);
				RequestDispatcher failureView = req.getRequestDispatcher("/backend/member/member.jsp");
				failureView.forward(req, res);
				return; // 程式中斷

			}

			/*************************** 2.開始查詢資料 *****************************************/

			// 執行查詢
			MemberService memberSvc = new MemberService();
			List<MemberVO> resultList = memberSvc.searchMembers2(accountStatus, registrationTime, birthday);
			System.out.println("執行查詢成功");

			// 若查無資料，回傳錯誤訊息
			if (resultList.isEmpty()) {
				errorMsgs.put("searchError", "查無符合條件的會員");
				req.setAttribute("errorMsgs", errorMsgs);
				RequestDispatcher failureView = req.getRequestDispatcher("/backend/member/member.jsp");
				failureView.forward(req, res);
				return;
			}

			/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
			// 查詢成功，轉交結果
			req.setAttribute("resultList", resultList);
			RequestDispatcher successView = req.getRequestDispatcher("../backend/member/member_search_one.jsp");
			successView.forward(req, res);
			System.out.println("查詢成功，轉交結果");

		}

	}

}
