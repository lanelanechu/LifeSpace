package com.lifespace.controller;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lifespace.SessionUtils;
import com.lifespace.dto.SpaceResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifespace.dto.SpaceCommentReplyRequestDTO;
import com.lifespace.dto.SpaceCommentResponse;
import com.lifespace.dto.SpaceRequest;
import com.lifespace.entity.Space;
import com.lifespace.exception.ResourceNotFoundException;
import com.lifespace.service.SpaceService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


// 直接回傳 JSON，前端可以用 AJAX 調用
@RestController
public class SpaceController {

	// ======= 空間相關 =======
	@Autowired
	private SpaceService spaceService;

	// 取得所有空間
	@GetMapping("/spaces")
	public ResponseEntity<List<Space>> getAllSpaces() {
		List<Space> allSpaces = spaceService.getAllSpaces();
		return ResponseEntity.ok(allSpaces);
	}

	// 透過ID取得單一空間
	@GetMapping("/spaces/id/{spaceId}")
	public ResponseEntity<Space> getSpaceById(@PathVariable	String spaceId) {
		Space space = spaceService.getSpaceById(spaceId);
		return ResponseEntity.ok(space);
	}

	// 透過關鍵字進行模糊搜尋
	@GetMapping("/spaces/name")
	public ResponseEntity<List<Space>> getSpacesByNameContainingIgnoreCase(@RequestParam String keyword) {  // 用@RequestParam以讓Postman處理空白字元
		List<Space> spaces = spaceService.getSpacesByNameContainingIgnoreCase(keyword);
		return ResponseEntity.ok(spaces);

	}

	// 關鍵字、時間複合搜尋
	@GetMapping("/spaces/available")
	public ResponseEntity<List<Space>> getSpacesAvailable(
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String date,
			@RequestParam(required = false) String startTime,
			@RequestParam(required = false) String endTime) {
		List<Space> availableSpaces = spaceService.getAvailableSpaces(keyword, date, startTime, endTime);
		return ResponseEntity.ok(availableSpaces);
	}

	// 新增空間
	@PostMapping("/spaces")  // 需使用multipart/form-data + JSON + 檔案格式提交
	public ResponseEntity<?> addSpace(@RequestPart("data") @Valid SpaceRequest space,
									  @RequestPart(value = "photos", required = false) List<MultipartFile> photos) {
		Space created = spaceService.addSpace(space, photos);
		URI location = URI.create("/spaces/id/" + created.getSpaceId());
		return ResponseEntity.created(location).body(created); // 201 Created
	}

	// 修改空間
	@PutMapping("/spaces/{spaceId}")  // 需使用multipart/form-data + JSON + 檔案格式提交
	public ResponseEntity<?> updateSpace(@PathVariable String spaceId,
										 @RequestPart("data") @Valid SpaceRequest space,
										 @RequestPart(value = "photos", required = false) List<MultipartFile> files,	// 記錄更新後的照片有哪些
										 @RequestPart(value = "keptPhotoIds", required = false) String keptPhotoIdsJson) throws JsonProcessingException
		{
			List<Integer> keptPhotoIds = new ObjectMapper().readValue(keptPhotoIdsJson, new TypeReference<>() {});	// 記錄更新前現有的照片有哪些(透過存ID: [1, 2, 4, 6, ...])
			// ObjectMapper: 將JSON轉為Java Object
			// readValue: 將JSON解析為指定的Java型別
			// TypeReference: 根據你宣告的型別改成精確的型別: List<Integer> (原本是List<Object>)
			Space updated = spaceService.updateSpace(spaceId, space, files != null ? files : List.of(), keptPhotoIds);
			return ResponseEntity.ok(updated);
	}

	// 上、下架更新狀態（透過上、下架進行篩選）
	@PutMapping("/spaces/status/{spaceId}")
	public ResponseEntity<Space> toggleStatus(@PathVariable String spaceId, @RequestBody Map<String, String> body) {
		Space spaceUpdated = spaceService.toggleStatus(spaceId, body);
		return ResponseEntity.ok(spaceUpdated);
	}


	// ======= 其他表格相關 =======

	// (從micky程式碼複製過來的)檢查是否有登入會員，才可開始預訂
	@GetMapping("/spaces/member/current")
	public ResponseEntity<?> getCurrentMember(HttpSession session) {
		String memberId = SessionUtils.getLoginMemberId(session); // 統一從工具類拿

		if (memberId == null) {
			return ResponseEntity.status(401).body("尚未登入會員");
		}

		Map<String, String> res = new HashMap<>();
		res.put("memberId", memberId);  // key 可以自訂為你前端想用的名字
		return ResponseEntity.ok(res);
	}

	
	
	
	
	//根據空間id查詢評論
	@GetMapping("/spaces/comments/{spaceId}")
	public ResponseEntity<Page<SpaceCommentResponse>> getSpaceCommentsById(
	            @PathVariable String spaceId,
	            @RequestParam(defaultValue = "0") int page, // 預設為第一頁
	            @RequestParam(defaultValue = "10") int size  // 預設每頁 10 筆
	    ) {
			
			Space space = spaceService.getSpaceById(spaceId);

			if (space == null) {   // 若這個spaceId沒有資料
				throw new ResourceNotFoundException("找不到 ID 為「 " + spaceId + " 」的空間");  
			}
	        // 建立分頁物件
	        Pageable pageable = PageRequest.of(page, size);

	        Page<SpaceCommentResponse> commentPage = spaceService.getSpaceCommentsById(spaceId, pageable);

	        return new ResponseEntity<>(commentPage, HttpStatus.OK);
	    }

	//根據條件查詢空間評論
	@GetMapping("/spaces/comments")
	public ResponseEntity<Page<SpaceCommentResponse>> searchSpaceComments( 
	    		@RequestParam(required = false) String spaceId,
	            @RequestParam(required = false) String spaceName,
	            @RequestParam(required = false) String branchId,
	            @RequestParam(defaultValue = "5") @Max(10) @Min(0) Integer size,
	            @RequestParam(defaultValue = "0") @Min(0) Integer page) {
	        // 創建分頁和排序條件
	        Pageable pageable = PageRequest.of( page, size );
	        // 處理空字符串
	        spaceId = (spaceId != null && spaceId.trim().isEmpty()) ? null : spaceId;
	        spaceName = (spaceName != null && spaceName.trim().isEmpty()) ? null : spaceName;
	        branchId = (branchId != null && branchId.trim().isEmpty()) ? null : branchId;

	        Page<SpaceCommentResponse> result = spaceService.getSpaceCommentsByConditions(
	        		spaceId, spaceName, branchId, pageable);
	        
	        return ResponseEntity.ok(result);
	    }
	
		//管理員回覆空間評論
		@PostMapping("/spaces/comments/reply")
		public ResponseEntity<?> replyToComment(@RequestBody @Valid SpaceCommentReplyRequestDTO replyDto) {
		    spaceService.addSpaceCommentReply(replyDto);
		    return ResponseEntity.ok().body(Map.of("message", "回覆成功"));
		}

}