package com.lifespace.controller;


import com.lifespace.dto.OrdersDTO;
import com.lifespace.dto.SpaceCommentRequest;
import com.lifespace.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersSvc;
    @Autowired
    private OrdersService ordersService;

    public OrdersController(OrdersService ordersSvc) {

        this.ordersSvc = ordersSvc;
    }


    @GetMapping("/getAll")
    public List<OrdersDTO> getAllOrders() {

        return ordersSvc.getAllOrdersDTOs();
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrders(@PathVariable String orderId) {

        try {
            ordersSvc.updateOrderStatusByOrderId(orderId); // 改變訂單狀態
            return ResponseEntity.ok("已成功取消訂單" + orderId);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //會員查詢訂單session
//     @GetMapping("/member/orders")
//    public List<OrdersDTO> getOrdersByLonginMember(HttpSession session) {
//        Member member = (Member) session.getAttribute("member");
//        if(member == null) {
//            throw new IllegalStateException("請登入會員");
//        }
//        return ordersSvc.getAllOrdersDTOsByMemberId(member.getMemberId());
//    }

    //會員查詢訂單測試
    @GetMapping("/member/{memberId}")
    public List<OrdersDTO> getOrdersByMemberId(@PathVariable String memberId) {

        return ordersSvc.getAllOrdersByMemberId(memberId);
    }
    
    @PostMapping("/addComment")
    public String addSpaceComments(
            @RequestPart("eventRequest") SpaceCommentRequest commentRequest,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos) {

    	ordersSvc.addSpaceComments(commentRequest, photos);
        return "執行 insert sapce comment jpa 方法";
    }


    // 睿寓：新增訂單
    @PostMapping
    public ResponseEntity<OrdersDTO> createOrder(@RequestBody OrdersDTO ordersDTO) {
        OrdersDTO newOrder = ordersService.createOrder(ordersDTO);
        return ResponseEntity.ok(newOrder);
    }
    
}


