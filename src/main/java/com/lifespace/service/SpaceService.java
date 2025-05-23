package com.lifespace.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import com.lifespace.dto.*;
import com.lifespace.entity.*;
import com.lifespace.exception.DuplicatedSpaceNameException;
import com.lifespace.exception.PhotoIOException;
import com.lifespace.repository.OrdersRepository;
import com.lifespace.repository.SpaceCommentReplyRepository;
import com.lifespace.repository.SpaceUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lifespace.exception.ResourceNotFoundException;
import com.lifespace.repository.SpaceRepository;
import org.springframework.web.multipart.MultipartFile;

@Service	
public class SpaceService {
	
	@Autowired
	private SpaceRepository spaceRepository;

    @Autowired
    private SpaceUsageRepository spaceUsageRepository;

    @Autowired
    private OrdersRepository ordersRepository;
    
    @Autowired
    private SpaceCommentReplyRepository spaceCommentReplyRepo;

	public List<Space> getAllSpaces() {  // 取得所有空間
		List<Space> allSpaces = spaceRepository.findAll();

		recalculateAllSpaceRatings(allSpaces);
		return allSpaces;
	}

	public Space getSpaceById(String spaceId) {  // 透過id取得單一空間
		Space space = spaceRepository.findById(spaceId).orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為「 " + spaceId + " 」的空間"));

		return space;
	}

	public List<Space> getSpacesByNameContainingIgnoreCase(String keyword) {  // 透過空間名稱取得單一空間
		List<Space> spaces = spaceRepository.findBySpaceNameContainingIgnoreCase(keyword);
		if (spaces.isEmpty()) {
			throw new ResourceNotFoundException("找不到包含「" + keyword + "」的空間");
		}
		return spaces;
	}

	// 關鍵字、開始結束日間的複合查詢
	public List<Space> getAvailableSpaces(String keyword, String dateStr, String startTimeStr, String endTimeStr) {
		LocalTime startTime = LocalTime.parse(startTimeStr);
		LocalTime endTime = LocalTime.parse(endTimeStr);

		List<Space> targetSpaces;

		// 先對關鍵字模糊搜尋
		if (keyword != null && !keyword.isBlank()) {
			targetSpaces = spaceRepository.findBySpaceNameContainingIgnoreCase(keyword);
		} else {
			targetSpaces = spaceRepository.findAll(); // 若沒關鍵字則撈全部
		}

		if (targetSpaces == null || targetSpaces.isEmpty()) {
			throw new ResourceNotFoundException("查無空間");
		}

		if (dateStr == null || dateStr.isBlank()) {
			return targetSpaces; // 沒日期就不篩預約
		}

		LocalDate date = LocalDate.parse(dateStr);
		LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
		LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

		// 再過濾出沒被預約的空間
		return targetSpaces.stream()
				.filter(space -> !spaceRepository.existsBySpaceIdAndOrderOverlap(
						space.getSpaceId(), startDateTime, endDateTime))
				.collect(Collectors.toList());
	}

	// 含有巢狀關聯資料(space equipment, space photos)的新增做法
	public Space addSpace(SpaceRequest space, List<MultipartFile> files) {
		try {
			Space s = new Space();
			// 設定分點ID與其他欄位
			s.setBranchId(space.getBranchId());
			s.setSpaceName(space.getSpaceName());
			s.setSpacePeople(space.getSpacePeople());
			s.setSpaceSize(space.getSpaceSize());
			s.setSpaceHourlyFee(space.getSpaceHourlyFee());
			s.setSpaceDailyFee(space.getSpaceDailyFee());
			s.setSpaceDesc(space.getSpaceDesc());
			s.setSpaceAlert(space.getSpaceAlert());
			s.setSpaceStatus(space.getSpaceStatus());
			s.setSpaceFloor(space.getSpaceFloor());

			// 設定關聯分點
			Branch branch = new Branch();
			branch.setBranchId(space.getBranchId());
			s.setBranch(branch);

			// ============= 新增Space Equipments =============
			Set<SpaceEquipment> equips = space.getSpaceEquipments().stream().map(se -> {
				SpaceEquipment equip = new SpaceEquipment();
				equip.setSpaceEquipName(se.getSpaceEquipName());
				equip.setSpace(s);  // 建立關聯，沒寫會報錯
				return equip;
			}).collect(Collectors.toSet());
			s.setSpaceEquipments(equips);

			// ============= 新增Space Photos =============
			Set<SpacePhoto> photos = new LinkedHashSet<>();

			if (files == null || files.isEmpty()) {
				throw new IllegalArgumentException("請至少上傳一張照片");
			}

			for (MultipartFile file : files) {
				SpacePhoto photo = new SpacePhoto();
				photo.setPhoto(file.getBytes());
				photo.setFilename(file.getOriginalFilename());   // 用來比對同一個spaceId下的同一張圖是否已經存入 (originalFilename: 原始檔名 (xxx.png, xxx.jpg, ......))
				photo.setSpace(s); // 關聯回 Space
				photos.add(photo);
			}
			s.setSpacePhotos(photos);

			// ============= 新增Space Usage maps =============

			List<SpaceUsage> usages = spaceUsageRepository.findAllById(space.getSpaceUsageIds());

			// 3. 建立對應的 SpaceUsageMap
			Set<SpaceUsageMap> usageMaps = usages.stream().map(usage -> {
				SpaceUsageMap map = new SpaceUsageMap();
				map.setSpace(s);           // 關聯到 Space（必填）
				map.setSpaceUsage(usage);      // 關聯到 Usage（必填）
				return map;
			}).collect(Collectors.toSet());

			// 4. 把 usageMap 塞進 Space
			s.setSpaceUsageMaps(usageMaps);

			// 新增空間
			return spaceRepository.save(s);  // CascadeType.ALL 會自動幫你存子表格的項目
		} catch (DataIntegrityViolationException de) {
			throw new DuplicatedSpaceNameException("此空間名稱已經被使用過");
		} catch (IOException ie) {
			throw new PhotoIOException("空間照片新增失敗：" + ie.getMessage());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public Space updateSpace(String spaceId, SpaceRequest space, List<MultipartFile> files, List<Integer> keptPhotoIds) {  // 修改空間
		try {
			Space s = spaceRepository.findById(spaceId).orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為「 " + spaceId + " 」的空間"));

			// 要跟 branch 關聯，才會更新branch的相關資料
			Branch branch = new Branch();
			branch.setBranchId(space.getBranchId());
			s.setBranch(branch);

			s.setSpaceName(space.getSpaceName());
			s.setSpacePeople(space.getSpacePeople());
			s.setSpaceSize(space.getSpaceSize());
			s.setSpaceHourlyFee(space.getSpaceHourlyFee());
			s.setSpaceDailyFee(space.getSpaceDailyFee());
			s.setSpaceDesc(space.getSpaceDesc());
			s.setSpaceAlert(space.getSpaceAlert());
			s.setSpaceStatus(space.getSpaceStatus());
			s.setSpaceFloor(space.getSpaceFloor());

			// ============= 修改Space Equipments =============
			Set<SpaceEquipment> targetEquipments = s.getSpaceEquipments();
			targetEquipments.clear();

			for (SpaceEquipmentRequest se : space.getSpaceEquipments()) {
				SpaceEquipment equip = new SpaceEquipment();
				equip.setSpaceEquipName(se.getSpaceEquipName());
				equip.setSpace(s);
				targetEquipments.add(equip);
			}

			// ============= 修改Space Photos =============

			// 取得現有照片且未被刪除的
			Set<SpacePhoto> originalPhotos = s.getSpacePhotos();
			originalPhotos.removeIf(photo -> !keptPhotoIds.contains(photo.getSpacePhotoId()));

			// 加入新照片
			if (files != null) {   // 不要加files.isEmpty() 因為可能照片沒有新增
				for (MultipartFile file : files) {
					if (!file.isEmpty()) {
						SpacePhoto photo = new SpacePhoto();
						photo.setPhoto(file.getBytes());
						photo.setFilename(file.getOriginalFilename());	// 用來比對同一個spaceId下的同一張圖是否已經存入
						photo.setSpace(s);
						originalPhotos.add(photo);		// 將新加入的photo直接丟進originalPhoto中，作為修改後的照片陣列
					}
				}
			}

			if (originalPhotos.isEmpty()) {		// 修改後若為空
				throw new IllegalArgumentException("請至少保留或上傳一張照片");
			}

			// ============= 修改Space Usage Maps =============
			Set<SpaceUsageMap> targetUsageMaps = s.getSpaceUsageMaps();
			targetUsageMaps.clear();
			List<SpaceUsage> usages = spaceUsageRepository.findAllById(space.getSpaceUsageIds());
			for (SpaceUsage usage : usages) {
				SpaceUsageMap map = new SpaceUsageMap();
				map.setSpace(s);
				map.setSpaceUsage(usage);
				targetUsageMaps.add(map);
			}

			return spaceRepository.save(s);   // CascadeType.ALL + orphanRemoval = true
		} catch (DataIntegrityViolationException de) {
			throw new DuplicatedSpaceNameException("此空間名稱已經被使用過");
		} catch (IOException ie) {
			throw new PhotoIOException("空間照片新增失敗：" + ie.getMessage());
		}
	}

	// 切換上、下架狀態
	public Space toggleStatus(String spaceId, Map<String, String> body) {
		String newStatus = body.get("status");
		Space s = spaceRepository.findById(spaceId).orElse(null);

		if (s == null) {
			throw new ResourceNotFoundException("找不到ID 為「 " + spaceId + " 」的空間");
		}

		// 調整空間狀態
		System.out.println(newStatus);
		s.setSpaceStatus(newStatus.equals("1") ? 1 : 0);
		s.setSpaceStatusText(newStatus.equals("1") ? "上架中" : "未上架");

		return spaceRepository.save(s);
	}

	// 計算個別空間的平均滿意度
	public void updateSpaceRating(String spaceId) {
		Double avgRating = spaceRepository.findAverageSatisfactionBySpaceId(spaceId);

		if (avgRating == null) {
			avgRating = 0.0;
		}

		Space s = spaceRepository.findById(spaceId).orElse(null);

		if (s == null) {
			throw new ResourceNotFoundException("找不到ID 為「 " + spaceId + " 」的空間");
		}

		s.setSpaceRating(avgRating);
		spaceRepository.save(s);
	}

	// 取得所有空間時，同時計算空間滿意度
	public void recalculateAllSpaceRatings(List<Space> allSpaces) {
		for (Space space : allSpaces) {
			String spaceId = space.getSpaceId();
			updateSpaceRating(spaceId);
		}
	}



















	//找特定空間id的評論
	public Page<SpaceCommentResponse> getSpaceCommentsById(String spaceId,Pageable pageable) {
			
			// 進行搜尋，不搜尋空間名稱以及分店
			String spaceName = null;
	        String branchId = null;
	        Page<SpaceCommentResponse> commentPage = spaceRepository.findSpaceCommentsByConditions(
	        		spaceId,
	                spaceName,
	                branchId,
	                pageable
	        );
	        
	        List<SpaceCommentResponse> responseList = commentPage.getContent();
	        
	        //回傳response
	        return new PageImpl<>(responseList, pageable, commentPage.getTotalElements());
			
		}

	//依照查詢條件查看空間評論
	public Page<SpaceCommentResponse> getSpaceCommentsByConditions( 
				String spaceId,
	            String spaceName,
	            String branchId,
	            Pageable pageable) {
			
			// 進行搜尋
	        Page<SpaceCommentResponse> commentPage = spaceRepository.findSpaceCommentsByConditions(
	        		spaceId,
	                spaceName,
	                branchId,
	                pageable
	        );
	        
	        List<SpaceCommentResponse> responseList = commentPage.getContent();
	        
	        //回傳response
	        return new PageImpl<>(responseList, pageable, commentPage.getTotalElements());
			
			}

	
	//管理員新增回覆空間評論
    public void addSpaceCommentReply(SpaceCommentReplyRequestDTO replyRequest) {
    	
    	Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        String orderId = replyRequest.getOrderId();
        String content = replyRequest.getCommentReplyContent();

        // 嘗試取得已存在的評論回覆
        SpaceCommentReply existingReply = spaceCommentReplyRepo.findByOrdersOrderId(orderId).orElse(null);

        if (existingReply == null) {
            // 新增評論回覆
            SpaceCommentReply newReply = new SpaceCommentReply();
            newReply.setOrders(ordersRepository.findById(orderId).orElseThrow(() -> 
                new ResourceNotFoundException("找不到訂單 ID：" + orderId)));
            newReply.setCommentReplyContent(content);
            newReply.setCreatedTime(currentTime);
            spaceCommentReplyRepo.save(newReply);
        } else {
            // 更新評論回覆
            existingReply.setCommentReplyContent(content);
            existingReply.setCreatedTime(currentTime);
            spaceCommentReplyRepo.save(existingReply);
        }
    }

}
