package com.lifespace.service;


import com.lifespace.dto.OrdersDTO;
import com.lifespace.dto.RentalItemDetailsDTO;
import com.lifespace.dto.SpaceCommentRequest;
import com.lifespace.entity.*;
import com.lifespace.repository.OrdersRepository;
import com.lifespace.repository.RentalItemRepository;
import com.lifespace.repository.SpaceCommentPhotoRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lifespace.mapper.OrdersMapper;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("ordersService")
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private SpaceCommentPhotoRepository spaceCommentPhotoRepository;

    @Autowired
    private SpaceService spaceService;
    
    public void updateOrderStatusByOrderId(String orderId) {

        Orders orders = ordersRepository.findById(orderId)
                       .orElseThrow(() -> new IllegalArgumentException("訂單編號" + orderId + "不存在"));
        if (orders.getOrderStatus() == 0) {
            throw new IllegalStateException("訂單編號" + orderId + "已取消, 無法再次取消訂單");
        }

        if(orders.getOrderStatus() == 2) {
            throw new IllegalStateException("訂單編號" + orderId + "已完成, 無法取消該訂單");
        }

        orders.setOrderStatus(0);
        ordersRepository.save(orders);
    }

    public Orders getOneOrder(String orderId) {

        Optional<Orders> optional = ordersRepository.findById(orderId);
        return optional.orElse(null);
    }

    public List<Orders> getAllOrders() {
          return ordersRepository.findAll();
        }

    public List<OrdersDTO> getAllOrdersDTOs() {

        return ordersRepository.findAll()
                .stream()
                .map(OrdersMapper::toOrdersDTO)
                .collect(Collectors.toList());
    }

    public List<OrdersDTO> getAllOrdersByMemberId(String memberId) {
        List<Orders> ordersList = ordersRepository.findByMember_MemberIdAndMember_AccountStatus(memberId, 1);
        return  ordersList
                .stream()
                .map(OrdersMapper::toOrdersDTO)
                .collect(Collectors.toList());
    }

    //結束時間到且狀態為已付款訂單 for排程器/啟動spring boot時更新
    public void expiredOrdersToComplete() {
        Timestamp now = Timestamp.from(Instant.now());
        List<Orders> expiredOrdersToComplete = ordersRepository.findByOrderStatusAndOrderEndBefore(1, now);

        if(!expiredOrdersToComplete.isEmpty()){
            for (Orders orders : expiredOrdersToComplete) {
                orders.setOrderStatus(2);
            }
            ordersRepository.saveAll(expiredOrdersToComplete);
            System.out.println("已自動更新" + expiredOrdersToComplete.size() + "筆訂單為已完成");
        } else {
            System.out.println("無訂單需要更新");
        }
    }


    //自動更新排程器
    @Scheduled(fixedRate = 60 * 60 * 1000)
    @Transactional
    public void autoCompleteOrdersByScheduled(){
        expiredOrdersToComplete();
//        System.out.println("排程器自動更新到期訂單");
    }

    @PostConstruct
    @Transactional
    public void autoCompleteOrdersByStartUp(){
        expiredOrdersToComplete();
//        System.out.println("啟動Spring後, 自動更新未更新到的到期訂單");
    }

    
  //訂單完成後，新增空間評論
    public void addSpaceComments(SpaceCommentRequest commentRequest,
    		List<MultipartFile> photos) {
    	
    	String orderId = commentRequest.getOrderId();
    	Orders orders = ordersRepository.findById(orderId)
                 .orElseThrow(() -> new IllegalArgumentException("訂單編號: " + orderId + "不存在"));
    	
    	Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    	
    	orders.setCommentContent(commentRequest.getComments());
    	orders.setCommentTime(currentTime);
    	orders.setSatisfaction(commentRequest.getRating());
    	ordersRepository.save(orders);
    
        // 處理照片上傳
        if (photos != null && !photos.isEmpty()) {
        	System.out.println(photos.size());
            for (MultipartFile photo : photos) {
                try {
                    // 儲存檔案到指定位置，並取得檔案路徑
                    String photoPath = savePhoto(photo);

                    SpaceCommentPhoto spaceCommentPhoto = new SpaceCommentPhoto();
                    spaceCommentPhoto.setOrders(orders);
                    spaceCommentPhoto.setCreatedTime(currentTime);
                    spaceCommentPhoto.setSpacePhoto(photoPath);   
                    spaceCommentPhotoRepository.save(spaceCommentPhoto);

                } catch (Exception e) {
                    // 處理檔案儲存失敗的例外
                    e.printStackTrace();
                    // 可以選擇拋出例外或記錄錯誤
                }
            }
        }

        // for睿寓：更新空間滿意度平均
        String spaceId = orders.getSpaceId(); // ← 這裡取得空間 ID
        spaceService.updateSpaceRating(spaceId);
    }


    private String savePhoto(MultipartFile photo) throws Exception {
	    String fileName = photo.getOriginalFilename();
	    String uploadDir = "D://tiba_project//space_comment_images"; // 替換為您的實際儲存目錄

	    // 確保目錄存在
	    File dir = new File(uploadDir);
	    if (!dir.exists()) {
	        if (!dir.mkdirs()) {
	            throw new IOException("無法建立目錄: " + uploadDir);
	        }
	    }

	    String filePath = uploadDir + "/" + fileName;
	    photo.transferTo(new File(filePath));
	    return "/space-comment-images/" + fileName; // 返回可訪問的 URL
	}










    @Autowired
    private RentalItemRepository rentalItemRepository;

    // 睿寓：新增訂單
    @Transactional
    public OrdersDTO createOrder(OrdersDTO ordersDTO) {
        Orders order = new Orders();

        order.setMemberId(ordersDTO.getMemberId());
        order.setSpaceId(ordersDTO.getSpaceId());
        order.setBranchId(ordersDTO.getBranchId());
        order.setOrderStart(ordersDTO.getOrderStart());
        order.setOrderEnd(ordersDTO.getOrderEnd());
        order.setTotalPrice(ordersDTO.getTotalPrice());
        order.setAccountsPayable(ordersDTO.getAccountsPayable());
        order.setPaymentDatetime(ordersDTO.getPaymentDatetime());

        // 建立Branch與Member的關聯，讓Mapper取得
        Branch branch = new Branch();
        branch.setBranchId(ordersDTO.getBranchId());
        order.setBranch(branch);

        Member member = new Member();
        member.setMemberId(ordersDTO.getMemberId());
        order.setMember(member);

        // 若有加購項目
        List<RentalItemDetailsDTO> rentalItemList = ordersDTO.getRentalItemDetailsDTOList();

        if (rentalItemList != null && !rentalItemList.isEmpty()) {
//            List<RentalItemDetails> rentalItems = rentalItemList.stream().map(dto -> {
//                RentalItem rentalItem = rentalItemRepository.findById(dto.getRentalItemId())
//                        .orElseThrow(() -> new RuntimeException("找不到租借品項"));
//
//                RentalItemDetails item = new RentalItemDetails();
//                item.setRentalItem(rentalItem);
//                item.setRentalItemQuantity(dto.getRentalItemQuantity());
//
//                item.setCreatedTime(new Timestamp(System.currentTimeMillis()));
//                item.setOrders(order); // 關聯訂單
//                return item;
//            }).collect(Collectors.toList());
//
//
//            order.setRentalItemDetails(rentalItems);
            List<RentalItemDetails> rentalItems = new ArrayList<>();

            for (RentalItemDetailsDTO dto : ordersDTO.getRentalItemDetailsDTOList()) {
                // 先檢查租借品項是否可用
                RentalItem rentalItem = rentalItemRepository.findById(dto.getRentalItemId())
                        .orElseThrow(() -> new RuntimeException("找不到租借品項"));

                if (rentalItem.getAvailableRentalQuantity() < dto.getRentalItemQuantity()) {
                    throw new IllegalArgumentException("可租借數量不足：" + rentalItem.getRentalItemName());
                }

                // 更新剩餘可租借數量
                rentalItem.setAvailableRentalQuantity(rentalItem.getAvailableRentalQuantity() - dto.getRentalItemQuantity());
                rentalItemRepository.save(rentalItem);

                // 建立關聯
                RentalItemDetails item = new RentalItemDetails();
                item.setRentalItem(rentalItem);
                item.setRentalItemQuantity(dto.getRentalItemQuantity());

                item.setCreatedTime(new Timestamp(System.currentTimeMillis()));
                item.setOrders(order); // 關聯訂單
                rentalItems.add(item);
            }

            order.setRentalItemDetails(rentalItems);

        }
        ordersRepository.save(order);
        return OrdersMapper.toOrdersDTO(order);

    }

}
