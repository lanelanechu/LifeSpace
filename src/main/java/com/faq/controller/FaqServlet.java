package com.faq.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.faq.model.FaqService;
import com.faq.model.FaqVO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/backend/faq/faq.do")
public class FaqServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action"); // 接收action參數
//			System.out.print(action); 

		// -------刪除---------
		if ("delete".equals(action)) {
			String str = request.getParameter("faqId"); //從請求中獲取要刪除的faqId
			FaqService faqSvc = new FaqService();
			faqSvc.deleteFaq(str); // 呼叫FaqService的deleteFaq方法
			String url = "/backend/faq/listAllFaq.jsp";
			RequestDispatcher successView = request.getRequestDispatcher(url);
			successView.forward(request, response);// 導回listAllFaq.jsp
		}

		// -------修改-----------

		if ("update".equals(action)) {
			String str = request.getParameter("faqId"); 
			FaqService faqSvc = new FaqService();
			FaqVO faqVO = faqSvc.getOneFaq(str); // 回傳單筆FAQ
			request.setAttribute("faqVO", faqVO); // 存FAQ的資料
			String url = "/backend/faq/update_faq_input.jsp";
			RequestDispatcher successView = request.getRequestDispatcher(url);
			successView.forward(request, response); // 跳轉修改頁面update_faq_input.jsp
//				System.out.print(faqVO.getFaqId()); updateOk

		}
		// 現在修改好資料按下確定
		if ("updateOk".equals(action)) {
			String faqId = request.getParameter("faqId");
			String faqAsk = request.getParameter("faqAsk");
			String faqAnswer = request.getParameter("faqAnswer");
//			System.out.println(faqId + faqAsk +faqAnswer); 

			// 如果faqAsk或faqAnswer都為空值或空字串
			if (faqAsk == null || faqAsk.trim().isEmpty()) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少faqAsk參數或輸入空白，請確認");
				return;
			}
			if (faqAnswer == null || faqAnswer.trim().isEmpty()) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少faqAnswer參數或輸入空白，請確認");
				return;
			}
//		   
			FaqService faqSvc = new FaqService();
			faqSvc.updateFaq(faqId, faqAsk, faqAnswer); // 呼叫SVC執行修改
			String url = "/backend/faq/listAllFaq.jsp";
			RequestDispatcher successView = request.getRequestDispatcher(url);
			successView.forward(request, response);// 導回listAllFaq.jsp

		}

		// ------ 查詢單筆 FAQ -------
		if ("getOne_For_Display".equals(action)) {
		    List<String> errorMsgs = new LinkedList<String>();
		    request.setAttribute("errorMsgs", errorMsgs);

		    try {
		        // 取得要查詢的 FAQ ID
		        String faqId = request.getParameter("faqId");

		        // 驗證 FAQ ID 格式是否正確
		        if (faqId == null || (faqId = faqId.trim()).isEmpty()) {
		            errorMsgs.add("請輸入常見問題編號");
		        } 
		        // 增加 FAQ ID 格式驗證
		        else if (!faqId.matches("^FAQ\\d{2}$")) {
		            errorMsgs.add("常見問題編號格式不正確。請使用 FAQXX 格式，如 FAQ01");
		        }

		        // 如果有錯誤，轉回原頁面
		        if (!errorMsgs.isEmpty()) {
		            RequestDispatcher failureView = request.getRequestDispatcher("/backend/faq/faq_select_page.jsp");
		            failureView.forward(request, response);
		            return;
		        }

		        // 執行查詢
		        FaqService faqSvc = new FaqService();
		        FaqVO faqVO = faqSvc.getOneFaq(faqId);

		        // 如果找不到該 FAQ
		        if (faqVO == null) {
		            errorMsgs.add("查無此常見問題");
		        }

		        // 如果有錯誤，轉回原頁面
		        if (!errorMsgs.isEmpty()) {
		            RequestDispatcher failureView = request.getRequestDispatcher("/backend/faq/faq_select_page.jsp");
		            failureView.forward(request, response);
		            return;
		        }

		        // 查詢成功，將結果存入 request
		        request.setAttribute("faqVO", faqVO);

		        // 轉發到顯示單一常見問題的頁面
		        String url = "/backend/faq/listOneFaq.jsp";
		        RequestDispatcher successView = request.getRequestDispatcher(url);
		        successView.forward(request, response);

		    } catch (Exception e) {
		        // 發生例外錯誤時
		        errorMsgs.add("處理查詢時發生錯誤: " + e.getMessage());
		        RequestDispatcher failureView = request.getRequestDispatcher("/backend/faq/faq_select_page.jsp");
		        failureView.forward(request, response);
		    }
		}
		// ------ 新增FAQ的邏輯 ------
		if ("insert".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			request.setAttribute("errorMsgs", errorMsgs);

			try {
				String adminId = "A001"; // 固定管理員ID
				String faqAsk = request.getParameter("faqAsk");
				String faqAnswer = request.getParameter("faqAnswer");

				// 創建 FaqVO 並設置現有值
				FaqVO faqVO = new FaqVO();
				faqVO.setAdminId(adminId);
				faqVO.setFaqAsk(faqAsk);
				faqVO.setFaqAnswer(faqAnswer);

				// 驗證輸入值
				if (faqAsk == null || faqAsk.trim().isEmpty()) {
					errorMsgs.add("問題標題不能為空");
				}

				if (faqAnswer == null || faqAnswer.trim().isEmpty()) {
					errorMsgs.add("問題回答不能為空");
				}

				// 如果有錯誤，將faqVO存回request並轉回addFaq
				if (!errorMsgs.isEmpty()) {
					request.setAttribute("faqVO", faqVO);
					RequestDispatcher failureView = request.getRequestDispatcher("/backend/faq/addFaq.jsp");
					failureView.forward(request, response);
					return;
				}

				// 執行新增操作(呼叫Service)
				FaqService faqSvc = new FaqService();
				faqVO = faqSvc.addFaq(adminId, faqAsk, faqAnswer, 1); // 預設狀態為1(已上線)

				// 查詢所有 FAQ 列表並存入 request
				List<FaqVO> faqList = faqSvc.getAll();
				request.setAttribute("faqList", faqList);

				// 新增成功後導回常見問題首頁listAllFaq
				String url = "/backend/faq/listAllFaq.jsp";
				RequestDispatcher successView = request.getRequestDispatcher(url);
				successView.forward(request, response);

			} catch (Exception e) {
				errorMsgs.add("新增資料失敗:" + e.getMessage());
				RequestDispatcher failureView = request.getRequestDispatcher("/backend/faq/addFaq.jsp");
				failureView.forward(request, response);
			}
		}
	}
}