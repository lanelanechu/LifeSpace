package com.comments.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.comments.model.CommentsService;
import com.comments.model.CommentsVO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/backend/comments/comments.do")
public class CommentsServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");

		if (action.equals("getOne_For_Display")) { // 來自comments_select_page.jsp的請求
			List<String> errorMsgs = new LinkedList<String>(); // 用來存所有錯誤資料
			request.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			String str = request.getParameter("commentId");
			if (str == null || (str.trim()).length() == 0) {
				errorMsgs.add("留言編號不得為空");
			}
			// 若errorMsgs已經有錯誤資料
			if (!errorMsgs.isEmpty()) {	
				RequestDispatcher failureView = request.getRequestDispatcher("/backend/comments/comments_select_page.jsp"); // 轉交回主頁，並顯示錯誤資訊
				failureView.forward(request, response);
				return; // 程式中斷
			}

			String commentId = str;
			System.out.println(commentId);
			if (!commentId.trim().matches("^C\\d{3}$")) { // 格式必須為"C001, C002, ..."
				errorMsgs.add("留言編號格式不正確");
			}
			// 若errorMsgs已經有錯誤資料
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher failureView = request.getRequestDispatcher("/backend/comments/comments_select_page.jsp");
				failureView.forward(request, response);
				return; // 程式中斷
			}

			/*************************** 2.開始查詢資料 *****************************************/
			CommentsService commentsSvc = new CommentsService();
			CommentsVO commentsVO = commentsSvc.getOneComments(commentId);
			if (commentsVO == null) {
				errorMsgs.add("查無資料");
			}

			// 若errorMsgs已經有錯誤資料
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher failureView = request.getRequestDispatcher("/backend/comments/comments_select_page.jsp");
				failureView.forward(request, response);
				return;// 程式中斷
			}

			/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
			request.setAttribute("commentsVO", commentsVO); // 自資料庫中取出commentsVO物件，並存入request
			String url = "/backend/comments/listOneComments.jsp";
			RequestDispatcher successView = request.getRequestDispatcher(url);
			successView.forward(request, response);

		}
		
		if (action.equals("getOne_For_Update")) { // 來自listAllComments.jsp的請求
			List<String> errorMsgs = new LinkedList<String>(); // 用來存所有錯誤資料
			request.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 ****************************************/
			String commentId = request.getParameter("commentId");

			/*************************** 2.開始查詢資料 ****************************************/
			CommentsService commentsSvc = new CommentsService();
			CommentsVO commentsVO = commentsSvc.getOneComments(commentId);

			/*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
			request.setAttribute("commentsVO", commentsVO); // 自資料庫中取出commentsVO物件，並存入request
			String url = "/backend/comments/update_comments_input.jsp";
			RequestDispatcher successView = request.getRequestDispatcher(url);
			successView.forward(request, response);
		}
		
		if (action.equals("update")) { // 來自update_comments_input.jsp的請求
			List<String> errorMsgs = new LinkedList<String>(); // 用來存所有錯誤資料
			request.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/

			String commentId = request.getParameter("commentId").trim();

			Integer commentHide = null;
			try {
				commentHide = Integer.valueOf(request.getParameter("commentHide").trim());
				if (commentHide != 1 && commentHide != 0) {
					commentHide = 0;
					errorMsgs.add("留言隱藏：數字需為0或1");
				}
			} catch (NumberFormatException e) {
//				commentHide = 0;
				errorMsgs.add("留言隱藏：請填數字");
			}
			
			String commentMessage = request.getParameter("commentMessage");
			if (commentMessage == null || commentMessage.trim().length() == 0) {
				errorMsgs.add("留言內容：不得空白");
			}
			
			String eventMemberId = request.getParameter("eventMemberId");

			CommentsVO commentsVO = new CommentsVO();
			commentsVO.setCommentId(commentId);
			commentsVO.setCommentHide(commentHide);
			commentsVO.setCommentMessage(commentMessage);
			commentsVO.setEventMemberId(eventMemberId);

			if (!errorMsgs.isEmpty()) {
				request.setAttribute("commentsVO", commentsVO); // 含有輸入格式錯誤的commentsVO物件,也存入request
				RequestDispatcher failureView = request.getRequestDispatcher("/backend/comments/update_comments_input.jsp");
				failureView.forward(request, response);
				return;// 程式中斷
			}

			/*************************** 2.開始修改資料 *****************************************/
			CommentsService commentsSvc = new CommentsService();
			commentsVO = commentsSvc.updateComments(commentId, eventMemberId, commentHide, commentMessage);

			/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
			request.setAttribute("commentsVO", commentsVO); // 自資料庫中取出commentsVO物件，並存入request
			String url = "/backend/comments/listAllComments.jsp";
			RequestDispatcher successView = request.getRequestDispatcher(url);
			successView.forward(request, response);
		}

		if (action.equals("insert")) { // 來自addComments.jsp的請求
			List<String> errorMsgs = new LinkedList<String>(); // 用來存所有錯誤資料
			request.setAttribute("errorMsgs", errorMsgs);
			
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/

			String eventMemberId = request.getParameter("eventMemberId");
			if (eventMemberId == null || eventMemberId.trim().length() == 0) {
				errorMsgs.add("活動參與會員：不得空白");
			} else if (!eventMemberId.trim().matches("^EM\\d{3}$")) { // 格式必須為"EM001, EM002, ..."
				errorMsgs.add("活動參與會員：格式錯誤 (需為 EM001, EM002, ...)");
			}

			
			Integer commentHide = null;
			try {
				commentHide = Integer.valueOf(request.getParameter("commentHide").trim());
				if (commentHide != 1 && commentHide != 0) {
					commentHide = 0;
					errorMsgs.add("留言隱藏：數字需為0或1");
				}
			} catch (NumberFormatException e) {
//				commentHide = 0;
				errorMsgs.add("留言隱藏：請填數字");
			}

			String commentMessage = request.getParameter("commentMessage");
			if (commentMessage == null || commentMessage.trim().length() == 0) {
				errorMsgs.add("留言內容：不得空白");
			}
			
			CommentsVO commentsVO = new CommentsVO();
			commentsVO.setEventMemberId(eventMemberId);
			commentsVO.setCommentHide(commentHide);
			commentsVO.setCommentMessage(commentMessage);

			if (!errorMsgs.isEmpty()) {
				request.setAttribute("commentsVO", commentsVO); // 含有輸入格式錯誤的commentsVO物件,也存入request
				RequestDispatcher failureView = request.getRequestDispatcher("/backend/comments/addComments.jsp");
				failureView.forward(request, response);
				return;// 程式中斷
			}

			/*************************** 2.開始新增資料 *****************************************/
			CommentsService commentsSvc = new CommentsService();
			commentsVO = commentsSvc.addComments(eventMemberId, commentHide, commentMessage);

			/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
			String url = "/backend/comments/listAllComments.jsp";
			RequestDispatcher successView = request.getRequestDispatcher(url);
			successView.forward(request, response);

		}
		
		if (action.equals("delete")) { // 來自listAllComments.jsp的請求
			List<String> errorMsgs = new LinkedList<String>(); // 用來存所有錯誤資料
			request.setAttribute("errorMsgs", errorMsgs);
			
			/*************************** 1.接收請求參數 ****************************************/
			String commentId = request.getParameter("commentId");

			/*************************** 2.開始查詢資料 ****************************************/
			CommentsService commentsSvc = new CommentsService();
			commentsSvc.deleteComments(commentId);

			/*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
			String url = "/backend/comments/listAllComments.jsp";
			RequestDispatcher successView = request.getRequestDispatcher(url);
			successView.forward(request, response);
		}
	}
}
