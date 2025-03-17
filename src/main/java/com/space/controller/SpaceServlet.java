package com.space.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.branch.model.BranchService;
import com.branch.model.BranchVO;
import com.space.model.SpaceService;
import com.space.model.SpaceVO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/backend/space/space.do")
public class SpaceServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");

		if (action.equals("getOne_For_Display")) { // 來自space_select_page.jsp的請求
			List<String> errorMsgs = new LinkedList<String>(); // 用來存所有錯誤資料
			request.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			String str = request.getParameter("spaceId");
			if (str == null || (str.trim()).length() == 0) {
				errorMsgs.add("空間編號不得為空");
			}
			// 若errorMsgs已經有錯誤資料
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher failureView = request.getRequestDispatcher("/backend/space/listAllSpace.jsp"); // 轉交回主頁，並顯示錯誤資訊
				failureView.forward(request, response);
				return; // 程式中斷
			}

			String spaceId = str;
			if (!spaceId.trim().matches("^S\\d{3}$")) { // 格式必須為"S001, S002, ..."
				errorMsgs.add("空間編號格式不正確");
			}
			// 若errorMsgs已經有錯誤資料
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher failureView = request.getRequestDispatcher("/backend/space/listAllSpace.jsp");
				failureView.forward(request, response);
				return; // 程式中斷
			}

			/*************************** 2.開始查詢資料 *****************************************/
			SpaceService spaceSvc = new SpaceService();
			SpaceVO spaceVO = spaceSvc.getOneSpace(spaceId);
			if (spaceVO == null) {
				errorMsgs.add("查無資料");
			}

			// 若errorMsgs已經有錯誤資料
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher failureView = request.getRequestDispatcher("/backend/space/listAllSpace.jsp");
				failureView.forward(request, response);
				return;// 程式中斷
			}

			/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
			request.setAttribute("spaceVO", spaceVO); // 自資料庫中取出spaceVO物件，並存入request
			String url = "/backend/space/listOneSpace.jsp";
			RequestDispatcher successView = request.getRequestDispatcher(url);
			successView.forward(request, response);

		}

		if (action.equals("getOne_For_Update")) { // 來自listAllSpace.jsp的請求
			List<String> errorMsgs = new LinkedList<String>(); // 用來存所有錯誤資料
			request.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 ****************************************/
			String spaceId = request.getParameter("spaceId");

			/*************************** 2.開始查詢資料 ****************************************/
			SpaceService spaceSvc = new SpaceService();
			SpaceVO spaceVO = spaceSvc.getOneSpace(spaceId);

			/*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
			request.setAttribute("spaceVO", spaceVO); // 自資料庫中取出spaceVO物件，並存入request
			String url = "/backend/space/update_space_input.jsp";
			RequestDispatcher successView = request.getRequestDispatcher(url);
			successView.forward(request, response);
		}

		if (action.equals("update")) { // 來自update_space_input.jsp的請求
			List<String> errorMsgs = new LinkedList<String>(); // 用來存所有錯誤資料
			request.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			String spaceId = request.getParameter("spaceId");

			// 會改成下拉式清單
			String branchId = request.getParameter("branchId").trim();

			String spaceName = request.getParameter("spaceName");
			if (spaceName == null || spaceName.trim().length() == 0) {
				errorMsgs.add("空間名稱：不得空白");
			}

			Integer spacePeople = null;
			try {
				spacePeople = Integer.valueOf(request.getParameter("spacePeople").trim());
				if (spacePeople < 1 || spacePeople > 100) {
					spacePeople = 0;
					errorMsgs.add("空間人數：數字需介於1~100之間");
				}
			} catch (NumberFormatException e) {
//				spacePeople = 0;
				errorMsgs.add("空間人數：請填數字");
			}

			Double spaceSize = null;
			try {
				spaceSize = Double.valueOf(request.getParameter("spaceSize").trim());
				if (spaceSize <= 0.0) {
//					spaceSize = 1.0;
					errorMsgs.add("空間大小：需為正數");
				}
			} catch (NumberFormatException e) {
//				spaceSize = 1.0;
				errorMsgs.add("空間大小：請填數字");
			}

			Integer spaceHourlyFee = null;
			try {
				spaceHourlyFee = Integer.valueOf(request.getParameter("spaceHourlyFee").trim());
				if (spaceHourlyFee < 0) {
//					spaceHourlyFee = 0;
					errorMsgs.add("時租費率：請填0以上的整數");
				}
			} catch (NumberFormatException e) {
//				spaceHourlyFee = 0;
				errorMsgs.add("時租費率：請填數字");
			}

			Integer spaceDailyFee = null;
			try {
				spaceDailyFee = Integer.valueOf(request.getParameter("spaceDailyFee").trim());
				if (spaceDailyFee < 0) {
//					spaceDailyFee = 0;
					errorMsgs.add("日租費率：請填0以上的整數");
				}
			} catch (NumberFormatException e) {
//				spaceDailyFee = 0;
				errorMsgs.add("日租費率：請填數字");
			}

			String spaceDesc = request.getParameter("spaceDesc").trim();

			String spaceAlert = request.getParameter("spaceAlert").trim();

			// 會改成下拉式清單
			Integer spaceStatus = Integer.valueOf(request.getParameter("spaceStatus").trim());

			String spaceAddress = request.getParameter("spaceAddress").trim();
			if (spaceAddress == null || spaceAddress.trim().length() == 0) {
				errorMsgs.add("空間地址：請勿空白");
			}

			// 經緯度以後會套Google Maps API取得
			Double latitude = null;
			try {
				latitude = Double.valueOf(request.getParameter("latitude").trim());
				if (latitude < -90 || latitude > 90) {
//					latitude = 0.0;
					errorMsgs.add("緯度：已超出範圍");
				}
			} catch (NumberFormatException e) {
//				latitude = 0.0;
				errorMsgs.add("緯度：請填數字");
			}

			Double longitude = null;
			try {
				longitude = Double.valueOf(request.getParameter("longitude").trim());
				if (longitude < -180 || longitude > 180) {
//					longitude = 0.0;
					errorMsgs.add("經度：已超出範圍");
				}
			} catch (NumberFormatException e) {
//				longitude = 0.0;
				errorMsgs.add("經度：請填數字");
			}

			SpaceVO spaceVO = new SpaceVO();
			spaceVO.setSpaceId(spaceId);
			spaceVO.setBranchId(branchId);
			spaceVO.setSpaceName(spaceName);
			spaceVO.setSpacePeople(spacePeople);
			spaceVO.setSpaceSize(spaceSize);
			spaceVO.setSpaceHourlyFee(spaceHourlyFee);
			spaceVO.setSpaceDailyFee(spaceDailyFee);
			spaceVO.setSpaceDesc(spaceDesc);
			spaceVO.setSpaceAlert(spaceAlert);
			spaceVO.setSpaceStatus(spaceStatus);
			spaceVO.setSpaceAddress(spaceAddress);
			spaceVO.setLatitude(latitude);
			spaceVO.setLongitude(longitude);

			if (!errorMsgs.isEmpty()) {
				request.setAttribute("spaceVO", spaceVO); // 含有輸入格式錯誤的spaceVO物件,也存入request
				RequestDispatcher failureView = request.getRequestDispatcher("/backend/space/update_space_input.jsp");
				failureView.forward(request, response);
				return;// 程式中斷
			}

			/*************************** 2.開始修改資料 *****************************************/
			SpaceService spaceSvc = new SpaceService();
			spaceVO = spaceSvc.updateSpace(spaceId, branchId, spaceName, spacePeople, spaceSize, spaceHourlyFee,
					spaceDailyFee, spaceDesc, spaceAlert, spaceStatus, spaceAddress, latitude, longitude);

			/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
			request.setAttribute("spaceVO", spaceVO); // 自資料庫中取出spaceVO物件，並存入request
			String url = "/backend/space/listAllSpace.jsp";
			RequestDispatcher successView = request.getRequestDispatcher(url);
			successView.forward(request, response);
		}

		if (action.equals("insert")) { // 來自addSpace.jsp的請求
			List<String> errorMsgs = new LinkedList<String>(); // 用來存所有錯誤資料
			request.setAttribute("errorMsgs", errorMsgs);
			
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/

			// 會改成下拉式清單
			String branchId = request.getParameter("branchId").trim();
//			if (branchId == null || branchId.trim().length() == 0) {
//				errorMsgs.add("所屬分店編號：不得空白");
//			} else if (!branchId.trim().matches("^B\\d{3}$")) { // 格式必須為"B001, B002, ..."
//				errorMsgs.add("所屬分店編號：格式錯誤 (需為 B001, B002, ...)");
//			}

			String spaceName = request.getParameter("spaceName");
			if (spaceName == null || spaceName.trim().length() == 0) {
				errorMsgs.add("空間名稱：不得空白");
			}

			Integer spacePeople = null;
			try {
				spacePeople = Integer.valueOf(request.getParameter("spacePeople").trim());
				if (spacePeople < 1 || spacePeople > 100) {
//					spacePeople = 0;
					errorMsgs.add("空間人數：數字需介於1~100之間");
				}
			} catch (NumberFormatException e) {
//				spacePeople = 0;
				errorMsgs.add("空間人數：請填數字");
			}

			Double spaceSize = null;
			try {
				spaceSize = Double.valueOf(request.getParameter("spaceSize").trim());
				if (spaceSize <= 0.0) {
//					spaceSize = 1.0;
					errorMsgs.add("空間大小：需為正數");
				}
			} catch (NumberFormatException e) {
//				spaceSize = 1.0;
				errorMsgs.add("空間大小：請填數字");
			}

			Integer spaceHourlyFee = null;
			try {
				spaceHourlyFee = Integer.valueOf(request.getParameter("spaceHourlyFee").trim());
				if (spaceHourlyFee < 0) {
//					spaceHourlyFee = 0;
					errorMsgs.add("時租費率：請填0以上的整數");
				}
			} catch (NumberFormatException e) {
//				spaceHourlyFee = 0;
				errorMsgs.add("時租費率：請填數字");
			}

			Integer spaceDailyFee = null;
			try {
				spaceDailyFee = Integer.valueOf(request.getParameter("spaceDailyFee").trim());
				if (spaceDailyFee < 0) {
//					spaceDailyFee = 0;
					errorMsgs.add("日租費率：請填0以上的整數");
				}
			} catch (NumberFormatException e) {
//				spaceDailyFee = 0;
				errorMsgs.add("日租費率：請填數字");
			}

			String spaceDesc = request.getParameter("spaceDesc").trim();

			String spaceAlert = request.getParameter("spaceAlert").trim();

			// 會改成下拉式清單
			Integer spaceStatus = Integer.valueOf(request.getParameter("spaceStatus").trim());

			String spaceAddress = request.getParameter("spaceAddress").trim();
			if (spaceAddress == null || spaceAddress.trim().length() == 0) {
				errorMsgs.add("空間地址：請勿空白");
			}

			// 經緯度以後會套Google Maps API取得
			Double latitude = null;
			try {
				latitude = Double.valueOf(request.getParameter("latitude").trim());
				if (latitude < -90 || latitude > 90) {
//					latitude = 0.0;
					errorMsgs.add("緯度：已超出範圍");
				}
			} catch (NumberFormatException e) {
//				latitude = 0.0;
				errorMsgs.add("緯度：請填數字");
			}

			Double longitude = null;
			try {
				longitude = Double.valueOf(request.getParameter("longitude").trim());
				if (longitude < -180 || longitude > 180) {
//					longitude = 0.0;
					errorMsgs.add("經度：已超出範圍");
				}
			} catch (NumberFormatException e) {
//				longitude = 0.0;
				errorMsgs.add("經度：請填數字");
			}

			SpaceVO spaceVO = new SpaceVO();
			spaceVO.setBranchId(branchId);
			spaceVO.setSpaceName(spaceName);
			spaceVO.setSpacePeople(spacePeople);
			spaceVO.setSpaceSize(spaceSize);
			spaceVO.setSpaceHourlyFee(spaceHourlyFee);
			spaceVO.setSpaceDailyFee(spaceDailyFee);
			spaceVO.setSpaceDesc(spaceDesc);
			spaceVO.setSpaceAlert(spaceAlert);
			spaceVO.setSpaceStatus(spaceStatus);
			spaceVO.setSpaceAddress(spaceAddress);
			spaceVO.setLatitude(latitude);
			spaceVO.setLongitude(longitude);

			if (!errorMsgs.isEmpty()) {
				request.setAttribute("spaceVO", spaceVO); // 含有輸入格式錯誤的spaceVO物件,也存入request
				RequestDispatcher failureView = request.getRequestDispatcher("/backend/space/addSpace.jsp");
				failureView.forward(request, response);
				return;// 程式中斷
			}

			/*************************** 2.開始新增資料 *****************************************/
			SpaceService spaceSvc = new SpaceService();
			spaceVO = spaceSvc.addSpace(branchId, spaceName, spacePeople, spaceSize, spaceHourlyFee, spaceDailyFee,
					spaceDesc, spaceAlert, spaceStatus, spaceAddress, latitude, longitude);

			/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
			String url = "/backend/space/listAllSpace.jsp";
			RequestDispatcher successView = request.getRequestDispatcher(url);
			successView.forward(request, response);

		}

		if (action.equals("delete")) { // 來自listAllSpace.jsp的請求
			List<String> errorMsgs = new LinkedList<String>(); // 用來存所有錯誤資料
			request.setAttribute("errorMsgs", errorMsgs);
			
			/*************************** 1.接收請求參數 ****************************************/
			String spaceId = request.getParameter("spaceId");

			/*************************** 2.開始查詢資料 ****************************************/
			SpaceService spaceSvc = new SpaceService();
			spaceSvc.deleteSpace(spaceId);

			/*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
			String url = "/backend/space/listAllSpace.jsp";
			RequestDispatcher successView = request.getRequestDispatcher(url);
			successView.forward(request, response);
		}
	}
}