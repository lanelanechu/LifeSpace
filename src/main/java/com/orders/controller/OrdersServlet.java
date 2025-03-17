package com.orders.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.orders.model.OrdersService;
import com.orders.model.OrdersVO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/OrdersServlet")
public class OrdersServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);

	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		String action = req.getParameter("action");
		
		List<String> errorMsgs = new ArrayList<>();
		
         //=========修改(delete)訂單狀態=========

		//System.out.println("收到的參數：" + action);
		if ("delete".equals(action)) {

			String orderId = req.getParameter("orderId"); // 取得orderId的參數

			OrdersVO ordersVO = new OrdersVO();

			if (orderId == null || orderId.trim().isEmpty()) {
				res.sendError(HttpServletResponse.SC_BAD_REQUEST, "orderId錯誤");
				return;
			}

			OrdersService ordersService = new OrdersService();
			ordersService.deleteOrders(orderId);

			req.setAttribute("ordersVO", ordersVO);

			String url = "/back-end/orders/order.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功轉交order.jsp
			successView.forward(req, res);
		}
		
		//=========搜尋訂單編號=========
		
		//System.out.println("收到：" + action);
		if ("getOneOrders".equals(action)) {
            String orderId = req.getParameter("orderId");
            
            // 驗證空字串
            if (orderId == null || orderId.trim().isEmpty()) {
                errorMsgs.add("你忘記打上訂單編號囉!");
            }
            
            // 驗證訂單流水號 是否正確
            String orderIdRegExp = "^OR\\d{3}$";
            if (orderId != null && !orderId.matches(orderIdRegExp)) {
                errorMsgs.add("請輸入正確的訂單編號");
            }
            
            List<OrdersVO> searchResult = new ArrayList<>();
            OrdersService ordersService = new OrdersService();
            
            // 如果沒有錯誤
            if (errorMsgs.isEmpty()) {
                try {
                    OrdersVO order = ordersService.getOneOrders(orderId);
                    
                    if (order != null) {
                        searchResult.add(order);
                    } else {
                        errorMsgs.add("查無此訂單");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsgs.add("查詢訂單時發生系統錯誤");
                }
            }
            
            if (!errorMsgs.isEmpty()) {
                req.setAttribute("errorMsgs", errorMsgs);
            } else {
                req.setAttribute("ordersList", searchResult);
            }
            
            String url = "/back-end/orders/listOneOrders.jsp";
            RequestDispatcher dispatcher = req.getRequestDispatcher(url); //查詢成功轉交 listOneOrder.jsp
            dispatcher.forward(req, res);
        }
    }

	}


