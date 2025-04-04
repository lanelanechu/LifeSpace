package com.lifespace.service;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lifespace.constant.EventMemberStatus;
import com.lifespace.constant.EventStatus;
import com.lifespace.dto.EventRequest;
import com.lifespace.dto.EventResponse;
import com.lifespace.entity.Event;
import com.lifespace.entity.EventMember;
import com.lifespace.entity.EventPhoto;
import com.lifespace.entity.Member;
import com.lifespace.exception.ResourceNotFoundException;
import com.lifespace.repository.BranchRepository;
import com.lifespace.repository.EventMemberRepository;
import com.lifespace.repository.EventPhotoRepository;
import com.lifespace.repository.EventRepository;
import com.lifespace.repository.MemberRepository;
import com.lifespace.repository.SpaceRepository;

@Service("eventService")
public class EventService {

	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	EventMemberRepository eventMemberRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	SpaceRepository spaceRepository;
	
	@Autowired
	BranchRepository branchRepository;
	
	@Autowired
    EventPhotoRepository eventPhotoRepository; // 注入 EventPhotoRepository
	
	//新增活動
	//@Transactional，加了會報錯，待解決
	public void addEvent(EventRequest eventRequest, List<MultipartFile> photos) {
		
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		Event event = new Event();
        event.setEventName(eventRequest.getEventName());
        event.setEventStartTime(eventRequest.getEventStartTime());
        event.setEventEndTime(eventRequest.getEventEndTime());
        event.setEventCategory(eventRequest.getEventCategory());
        event.setEventStatus(eventRequest.getEventStatus());
        event.setMaximumOfParticipants(eventRequest.getMaximumOfParticipants());
        event.setEventBriefing(eventRequest.getEventBriefing());
        event.setRemarks(eventRequest.getRemarks());
        event.setHostSpeaking(eventRequest.getHostSpeaking());
        event.setCreatedTime(currentTime);
        event.setNumberOfParticipants(0); // 後續會確保預設為1人(加入舉辦人)

        eventRepository.save(event);
        System.out.println(photos.size());
        // 處理照片上傳
        if (photos != null && !photos.isEmpty()) {
        	System.out.println(photos.size());
            for (MultipartFile photo : photos) {
                try {
                    // 儲存檔案到指定位置，並取得檔案路徑
                    String photoPath = savePhoto(photo);

                    EventPhoto eventPhoto = new EventPhoto();
                    eventPhoto.setEvent(event);
                    eventPhoto.setPhoto(photoPath);
                    eventPhoto.setCreatedTime(currentTime);      
                    eventPhotoRepository.save(eventPhoto);

                } catch (Exception e) {
                    // 處理檔案儲存失敗的例外
                    e.printStackTrace();
                    // 可以選擇拋出例外或記錄錯誤
                 // 重新拋出異常
                    throw new RuntimeException("儲存照片失敗", e);
                }
            }
        }
      //舉辦人為第1個活動參加者//直接將舉辦人加入
//      addMemberToEvent(String memberId, String eventId)//尚無session，先假設為M001
	}

	//更新活動狀態
	@Transactional
	public void updateEventStatus(String eventId, String status) {
			  Event event = eventRepository.findById(eventId)
			            .orElseThrow(() -> new ResourceNotFoundException("找不到活動 ID " + eventId));
			  
			  switch(status) {
			  	case "已取消":
				  event.setEventStatus(EventStatus.CANCELLED);
				  break;
			  	case "已舉辦":
				  event.setEventStatus(EventStatus.HELD);
				  break;
			  	case "尚未舉辦":
				  event.setEventStatus(EventStatus.SCHEDULED);
				  break;
				default:
				  break;
			  }
			    eventRepository.save(event);
		}

	//加入成員到活動
	public void addMemberToEvent(String memberId, String eventId) {
			
			Event event = eventRepository.findById(eventId)
		            .orElseThrow(() -> new ResourceNotFoundException("找不到活動 ID " + eventId));
		 
			Member member = memberRepository.findById(memberId)
		            .orElseThrow(() -> new ResourceNotFoundException("找不到使用者 ID " + eventId));
		 
			EventMember eventMember = eventMemberRepository.findByEventEventIdAndMemberMemberId(eventId, memberId)
														.orElse(null);
			//是否需要驗證已參加再按下參加活動??
	        // 尚未有該使用者對應的活動資料
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			
	        if (eventMember == null) {
	        	
	        	EventMember newEventMember = new EventMember();
	        	newEventMember.setEvent(event);
	            newEventMember.setMember(member);
	            newEventMember.setCreatedTime(currentTime);
	        	//未達人數上限，直接加入
	        	if(event.getNumberOfParticipants() < event.getMaximumOfParticipants()) {
	                eventMemberRepository.save(newEventMember);
	                //活動人數+1
	                event.setNumberOfParticipants(event.getNumberOfParticipants() + 1);
	                eventRepository.save(event);
	                return;
	        	}else {
	        		//已達人數上限，排入候補QUEUED
	        		newEventMember.setParticipateStatus(EventMemberStatus.QUEUED);
	                eventMemberRepository.save(newEventMember);
	                return;
	        	}
	        	 
	        }else{
	        	
	        	//若原本就已參加，不做任何動作
	        	if(eventMember.getParticipateStatus() == EventMemberStatus.ATTENT) {
	        		return;
	        	}
	        	//未達人數上限，更改加入狀態為ATTENT
	        	if(event.getNumberOfParticipants() < event.getMaximumOfParticipants()) {
	        		eventMember.setParticipateStatus(EventMemberStatus.ATTENT);
	        		eventMember.setCreatedTime(currentTime);
	                //活動人數+1
	                event.setNumberOfParticipants(event.getNumberOfParticipants() + 1);
	                eventMemberRepository.save(eventMember);
	                eventRepository.save(event);
	                return;
	        	}else {
	        		//已達人數上限，排入候補QUEUED
	        		eventMember.setParticipateStatus(EventMemberStatus.QUEUED);
	        		eventMember.setCreatedTime(currentTime);
	                eventMemberRepository.save(eventMember);
	                return;
	        	}
	        }

		}


	//將成員移出活動
	@Transactional
	public void removeMemberFromEvent(String memberId, String eventId) {

			 Event event = eventRepository.findById(eventId)
			            .orElseThrow(() -> new ResourceNotFoundException("找不到活動 ID " + eventId));
			 
			 Member member = memberRepository.findById(memberId)
			            .orElseThrow(() -> new ResourceNotFoundException("找不到使用者 ID " + eventId));
			
			  
			 EventMember eventMember = eventMemberRepository.findByEventEventIdAndMemberMemberId(event.getEventId(), member.getMemberId())
					 	.orElseThrow(() -> new ResourceNotFoundException("該會員尚未建立該活動資訊"));
			 
			//若原本就已取消，不做任何動作
	     	if(eventMember.getParticipateStatus() == EventMemberStatus.CANCELLED) {
	     		return;
	     	}
			 //若該筆使用者對應的活動資料存在，且未取消
			 eventMember.setParticipateStatus(EventMemberStatus.CANCELLED);
			
			 eventMemberRepository.save(eventMember);
			 
			 //活動人數-1
			 if(event.getNumberOfParticipants() > 0) {
				 event.setNumberOfParticipants(event.getNumberOfParticipants() - 1);
				 eventRepository.save(event);
			 }
			 
			//舉辦者不能取消參加活動?? 或是直接取消活動?? 待討論
		}

	//根據ID找出單一活動
	public EventResponse getOneEvent(String eventno) {
			Optional<Event> optional = eventRepository.findById(eventno);
//			return optional.get();
			// 將 Event 轉換為 EventResponse
	        EventResponse searchedEvent = eventRepository.getOneEvent(eventno);

			return searchedEvent;  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
		}

	//找出所有活動
	public List<Event> getAll() {
			return eventRepository.findAll();
		}
	
	// 添加搜尋方法
    public Page<EventResponse> searchEvents( String eventName,
            Timestamp startTime,
            Timestamp endTime,
            String category,
            Pageable pageable) {
        // 進行搜尋
        Page<EventResponse> eventPage = eventRepository.findEventsByConditions(
        		eventName,
        		startTime,
        		endTime,
        		category,
                pageable
        );
        
        // 將 Event 轉換為 EventResponse
        List<EventResponse> responseList = eventPage.getContent();
        
        // 創建新的 Page<EventResponse>
        return new PageImpl<>(responseList, pageable, eventPage.getTotalElements());
    }
	 
	
	// 儲存照片並返回檔案路徑
	private String savePhoto(MultipartFile photo) throws Exception {
	    String fileName = photo.getOriginalFilename();
	    String uploadDir = "D://tiba_project//event_images"; // 替換為您的實際儲存目錄

	    // 確保目錄存在
	    File dir = new File(uploadDir);
	    if (!dir.exists()) {
	        if (!dir.mkdirs()) {
	            throw new IOException("無法建立目錄: " + uploadDir);
	        }
	    }

	    String filePath = uploadDir + "/" + fileName;
	    photo.transferTo(new File(filePath));
	    return "/event-images/" + fileName; // 返回可訪問的 URL
	}
	
	// 將 Event 實體轉換為 EventResponse DTO
//    private EventResponse convertToEventResponse(Event event) {
//        EventResponse response = new EventResponse();
//        response.setEventId(event.getEventId());
//        response.setEventName(event.getEventName());
//        response.setEventDate(event.getEventDate());
//        response.setEventStartTime(event.getEventStartTime());
//        response.setEventEndTime(event.getEventEndTime());
//        response.setEventCategory(event.getEventCategory());
//        response.setSpaceAddress(branchRepository.findById(spaceRepository.findById(event.getSpaceId())
//        										.get()
//        										.getBranchId())
//        										.get()
//        										.getBranchAddr());
//        response.setOrganizer(memberRepository.findById(event.getMemberId()).get().getMemberName());
//        response.setNumberOfParticipants(event.getNumberOfParticipants());
//        response.setMaximumOfParticipants(event.getMaximumOfParticipants());
//        response.setEventBriefing(event.getEventBriefing());
//        response.setRemarks(event.getRemarks());
//        response.setHostSpeaking(event.getHostSpeaking());
//        response.setCreatedTime(event.getCreatedTime());
//        
//        // 取得並設置活動照片
//        List<String> photoUrls = event.getPhotoUrls();
//        response.setPhotoUrls(photoUrls != null ? photoUrls : new ArrayList<>());
//        
//        return response;
//    }
}
