package com.lifespace.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lifespace.dto.EventMemberResponse;
import com.lifespace.dto.EventRequest;
import com.lifespace.dto.EventResponse;
import com.lifespace.entity.Event;
import com.lifespace.entity.EventCategory;
import com.lifespace.repository.EventRepository;
import com.lifespace.service.EventPhotoService;
import com.lifespace.service.EventService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


@RestController
@CrossOrigin
@RequestMapping("/lifespace/event")
public class EventController {

	@Autowired
	private EventService eventSvc;
	
	@Autowired
	private EventPhotoService eventPhotoSvc;
	
	@Autowired
    private EventRepository eventRepository;
	
	@PostMapping("/add")
    public String insert(
            @RequestPart("eventRequest") EventRequest eventRequest,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos) {

        eventSvc.addEvent(eventRequest, photos);
        return "執行 insert event jpa 方法";
    }
    
	@PutMapping("/updateStatus")
    public String updateStatus(@RequestParam(required = true) String eventId, @RequestParam(required = true) String status) {
    	eventSvc.updateEventStatus(eventId, status);
        return "執行update event status jpa方法";
    }
    
    @PutMapping("/addMemToEvent")
    public String addToEvent(@RequestParam(required = true) String eventId, @RequestParam(required = true) String memberId)
    		throws Exception {
    	
    	eventSvc.addMemberToEvent(memberId, eventId);
        return "執行add event member jpa方法";
    }
    
    @PutMapping("/removeMemFromEvent")
    public String removeFromEvent(@RequestParam(required = true) String eventId, @RequestParam(required = true) String memberId) {
    	eventSvc.removeMemberFromEvent(memberId, eventId);
        return "執行remove event member jpa方法";
    }
    
    @GetMapping("/getAll")
    public List<Event> getAll() {
    	System.out.println("被要求檔案");
    	  List<Event> events = eventSvc.getAll();
          for (Event event : events) {
              event.getPhotoUrls(); // 確保 photoUrls 被填充
          }
        return events;
    }
    
    @GetMapping("/getOne")
    public EventResponse getOneEvent(@RequestParam String eventId) {
    	System.out.println("被要求檔案");
    	EventResponse event = eventSvc.getOneEvent(eventId);
        
        event.getPhotoUrls(); // 確保 photoUrls 被填充
        
        return event;
    }
    
    //獲取所有活動類別
    @GetMapping("/getAllCategories")
    public ResponseEntity<List<EventCategory>> getAllCategories() {
    	System.out.println("被要求檔案");
    	List<EventCategory> categories = eventSvc.findAllEventsCategory();
    	
        return ResponseEntity.ok(categories);
    }
 
    @GetMapping("/search/native")
    public ResponseEntity<Page<EventResponse>> searchEvents( @RequestParam(required = false) String eventName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String branch,
            @RequestParam(defaultValue = "5") @Max(10) @Min(0) Integer size,
            @RequestParam(defaultValue = "0") @Min(0) Integer page) {
        // 創建分頁和排序條件
        Pageable pageable = PageRequest.of( page, size
        			//Sort.by("eventStartTime").descending()
        );
        // 處理空字符串
        eventName = (eventName != null && eventName.trim().isEmpty()) ? null : eventName;
        category = (category != null && category.trim().isEmpty()) ? null : category;
        branch = (branch != null && branch.trim().isEmpty()) ? null : branch;
        
        // 將LocalDateTime轉換為Timestamp
        Timestamp eventStartTime = startTime != null ? Timestamp.valueOf(startTime) : null;
        Timestamp eventEndTime = endTime != null ? Timestamp.valueOf(endTime) : null;
        // 呼叫服務層的搜尋方法
        Page<EventResponse> result = eventSvc.searchEvents(eventName, eventStartTime, eventEndTime, category,
        		branch, pageable);
        
        return ResponseEntity.ok(result);
    }
    
    //使用者根據種類篩選出活動列表 
    @GetMapping("/search/ByMember")
    public ResponseEntity<Page<EventMemberResponse>> filterEventsByUser(
    		@RequestParam(required = true) String userCategory,
    		@RequestParam(required = false) String memberId,
            @RequestParam(required = false) String participateStatus,
            @RequestParam(required = false) String eventStatus,
            @RequestParam(required = false) String organizerId,
            @RequestParam(defaultValue = "5") @Max(10) @Min(0) Integer size,
            @RequestParam(defaultValue = "0") @Min(0) Integer page) {
        // 創建分頁和排序條件
        Pageable pageable = PageRequest.of( page, size
        			//Sort.by("eventStartTime").descending()
        );
        // 處理空字符串
        memberId = (memberId != null && memberId.trim().isEmpty()) ? null : memberId;
        participateStatus = (participateStatus != null && participateStatus.trim().isEmpty()) ? null : participateStatus;
        eventStatus = (eventStatus != null && eventStatus.trim().isEmpty()) ? null : eventStatus;
        organizerId = (organizerId != null && organizerId.trim().isEmpty()) ? null : organizerId;
       
        Page<EventMemberResponse> result = eventSvc.filterEventsByUser(
        		userCategory, memberId, participateStatus, eventStatus, organizerId, pageable);
        
        return ResponseEntity.ok(result);
    }
    
    //舉辦者取消活動
    @PutMapping("/cancell")
    public String cancellEvent(
    		@RequestParam(required = true) String organizerId, 
    		@RequestParam(required = true) String eventId){
    		
    	eventSvc.cancellEvent(organizerId, eventId);
		return "執行cancell event by organizer jpa方法";	
    }
    
}
