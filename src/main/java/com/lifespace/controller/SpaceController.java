package com.lifespace.controller;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	@Autowired
	private SpaceService spaceService;

	@GetMapping("/spaces")
	public ResponseEntity<List<Space>> getAllSpaces() {
		List<Space> allSpaces = spaceService.getAllSpaces();
		if (allSpaces.isEmpty()) {    // 如果沒有資料時
		    return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allSpaces);
	}

	@GetMapping("/spaces/id/{spaceId}")
	public ResponseEntity<Space> getSpaceById(@PathVariable	String spaceId) {
		Space space = spaceService.getSpaceById(spaceId);

		if (space == null) {   // 若這個spaceId沒有資料
			throw new ResourceNotFoundException("找不到 ID 為「 " + spaceId + " 」的空間");   // ResourceNotFoundException為我自己定義的Exception
		}
		return ResponseEntity.ok(space);
	}
	
	@GetMapping("/spaces/name")
	public ResponseEntity<List<Space>> getSpacesByNameContainingIgnoreCase(@RequestParam String keyword) {  // 用@RequestParam以讓Postman處理空白字元
		List<Space> spaces = spaceService.getSpacesByNameContainingIgnoreCase(keyword);

		if (spaces.isEmpty()) {
			throw new ResourceNotFoundException("找不到包含「" + keyword + "」的空間");
		}

		return ResponseEntity.ok(spaces);

	}
	
	@PostMapping("/spaces")  // 需使用multipart/form-data + JSON + 檔案格式提交
	public ResponseEntity<?> addSpace(@RequestPart("data") @Valid SpaceRequest space,
									  @RequestPart(value = "photos", required = false) List<MultipartFile> photos) {
	    try {
	        Space created = spaceService.addSpace(space, photos);
	        URI location = URI.create("/spaces/id/" + created.getSpaceId());
	        return ResponseEntity.created(location).body(created); // 201 Created
	    } catch (DataIntegrityViolationException e) {
	        // 回傳 409 Conflict，並可加入錯誤訊息
			Map<String, String> errorBody = new HashMap<>();
			errorBody.put("message", "此空間名稱已經被使用過");
			return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON).body(errorBody);  // .contentType(MediaType.APPLICATION_JSON):確保該回傳型別不論為何，都是指定為application/json，
	    } catch (IOException e) {
			Map<String, String> errorBody = new HashMap<>();
			errorBody.put("message", "空間照片新增失敗：" + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(errorBody);
		} catch (IllegalArgumentException e) {
			Map<String, String> errorBody = new HashMap<>();
			errorBody.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(errorBody);
		}
	}
	
	@PutMapping("/spaces/{spaceId}")  // 需使用multipart/form-data + JSON + 檔案格式提交
	public ResponseEntity<?> updateSpace(@PathVariable String spaceId,
										 @RequestPart("data") @Valid SpaceRequest space,
										 @RequestPart(value = "photos", required = false) List<MultipartFile> files,	// 記錄更新後的照片有哪些
										 @RequestPart(value = "keptPhotoIds", required = false) String keptPhotoIdsJson) 	// 記錄更新前現有的照片有哪些(透過存ID: [1, 2, 4, 6, ...])
		{
		try {
			List<Integer> keptPhotoIds = new ObjectMapper().readValue(keptPhotoIdsJson, new TypeReference<>() {});
			// ObjectMapper: 將JSON轉為Java Object
			// readValue: 將JSON解析為指定的Java型別
			// TypeReference: 根據你宣告的型別改成精確的型別: List<Integer> (原本是List<Object>)
			Space updated = spaceService.updateSpace(spaceId, space, files != null ? files : List.of(), keptPhotoIds);
	        return ResponseEntity.ok(updated);
		} catch (DataIntegrityViolationException e) {
			// 回傳 409 Conflict，並可加入錯誤訊息
			Map<String, String> errorBody = new HashMap<>();
			errorBody.put("message", "此空間名稱已經被使用過");
			return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON).body(errorBody);
		} catch (IOException e) {
			Map<String, String> errorBody = new HashMap<>();
			errorBody.put("message", "空間照片新增失敗：" + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(errorBody);
		}
	}

	// 上、下架更新狀態
	@PutMapping("/spaces/status/{spaceId}")
	public ResponseEntity<Space> toggleStatus(@PathVariable String spaceId, @RequestBody Map<String, String> body) {
		Space spaceUpdated = spaceService.toggleStatus(spaceId, body);
		return ResponseEntity.ok(spaceUpdated);
	}

	// 透過上、下架進行篩選
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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

}