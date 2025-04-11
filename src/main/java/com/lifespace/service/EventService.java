package com.lifespace.service;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lifespace.constant.EventMemberStatus;
import com.lifespace.constant.EventStatus;
import com.lifespace.dto.EventMemberResponse;
import com.lifespace.dto.EventRequest;
import com.lifespace.dto.EventResponse;
import com.lifespace.entity.Event;
import com.lifespace.entity.EventCategory;
import com.lifespace.entity.EventMember;
import com.lifespace.entity.EventPhoto;
import com.lifespace.entity.Member;
import com.lifespace.entity.Orders;
import com.lifespace.exception.ResourceNotFoundException;
import com.lifespace.repository.BranchRepository;
import com.lifespace.repository.EventCategoryRepository;
import com.lifespace.repository.EventMemberRepository;
import com.lifespace.repository.EventPhotoRepository;
import com.lifespace.repository.EventRepository;
import com.lifespace.repository.MemberRepository;
import com.lifespace.repository.OrdersRepository;
import com.lifespace.repository.SpaceRepository;

import jakarta.annotation.PostConstruct;

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
	
	@Autowired
	OrdersRepository ordersRepository;
	
	@Autowired
	EventCategoryRepository eventCategoryRepository;
	
	@Autowired
	MailService mailService;
	
	//新增活動
	//@Transactional
	public void addEvent(EventRequest eventRequest, List<MultipartFile> photos) {
		
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		
		Event event = new Event();
        event.setEventName(eventRequest.getEventName());
        event.setEventStartTime(eventRequest.getEventStartTime());
        event.setEventEndTime(eventRequest.getEventEndTime());
        event.setEventCategory(eventCategoryRepository.findById(
        		eventRequest.getEventCategory()).get());
        event.setEventStatus(eventRequest.getEventStatus());
        event.setMaximumOfParticipants(eventRequest.getMaximumOfParticipants());
        event.setEventBriefing(eventRequest.getEventBriefing());
        event.setRemarks(eventRequest.getRemarks());
        event.setHostSpeaking(eventRequest.getHostSpeaking());
        event.setCreatedTime(currentTime);
        event.setNumberOfParticipants(0); // 後續會確保預設為1人(加入舉辦人)

        // 儲存 event 物件
        Event savedEvent = eventRepository.save(event);
        
        // 從儲存後的 event 物件中取得自增主鍵 ID
        String eventId = savedEvent.getEventId();
        System.out.println("新活動的 ID: " + eventId);
        
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
                }
            }
        }
        //舉辦人為第1個活動參加者，直接將舉辦人加入
        //舉辦人id暫時放在EventRequest裡，之後可能會從session拿??  
        try {
			addMemberToEvent(eventRequest.getOrganizerId(), eventId);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        //因為是一筆訂單對應活動，該筆訂單(EventRequest是否要加入order_id?)也要加上新建的event_id
	}

	//獲取所有活動類別
	public List<EventCategory> findAllEventsCategory() {
		
		return eventCategoryRepository.findAll();
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
	public void addMemberToEvent(String memberId, String eventId) throws Exception {
			
			Event event = eventRepository.findById(eventId)
		            .orElseThrow(() -> new ResourceNotFoundException("找不到活動 ID " + eventId));
		 
			Member member = memberRepository.findById(memberId)
		            .orElseThrow(() -> new ResourceNotFoundException("找不到使用者 ID " + eventId));
		 
			EventMember eventMember = eventMemberRepository.findByEventEventIdAndMemberMemberId(eventId, memberId)
														.orElse(null);
			
			//若雖有該活動但狀態為已取消或已舉辦，則無法加入
			if (event.getEventStatus() == EventStatus.CANCELLED 
					|| event.getEventStatus() == EventStatus.HELD  ) {
				throw new IllegalStateException("該活動已取消或已舉辦，不可參加該活動。");
			}
			
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
	     	
	     	//若原本排候補的取消候補，QUEUED改為CANCELLED??
	     	if(eventMember.getParticipateStatus() == EventMemberStatus.QUEUED) {
	     		eventMember.setParticipateStatus(EventMemberStatus.CANCELLED);
	     		return;
	     	}
	     	
			 //若該筆使用者對應的活動資料存在，且未取消
			 eventMember.setParticipateStatus(EventMemberStatus.CANCELLED);
			
			 eventMemberRepository.save(eventMember);
			 
			//查詢是否有候補人選，並依時間從早到晚排序
			 List<EventMember> queuedMembers = eventMemberRepository
					 .findByEvent_EventIdAndParticipateStatusOrderByCreatedTimeAsc(eventId, EventMemberStatus.QUEUED);
			 
			 System.out.println(queuedMembers);
			 
			 
			if( event.getNumberOfParticipants() == event.getMaximumOfParticipants()
					 && !queuedMembers.isEmpty() ) {
				//若人數已滿的狀態下有人取消，自動遞補候補為正取(篩選時間最前面的候補者)，同時寄email給該位使用者
				//若活動已經額滿，且有候補人選，篩選最早排入候補的人選，變更其狀態為ATTENT，活動參加人數不變
				EventMember firstQueuedMemebr = queuedMembers.get(0);
				firstQueuedMemebr.setParticipateStatus(EventMemberStatus.ATTENT);
				eventMemberRepository.save(firstQueuedMemebr);
				
				System.out.println("使用者 ID: " + firstQueuedMemebr.getMember().getMemberId() +
						" 已成功候補到活動: " + event.getEventName() );
				//之後加上寄email給該位成功候補的使用者
				String memberName = firstQueuedMemebr.getMember().getMemberName();
				String eventName = event.getEventName();
				String toEmail = firstQueuedMemebr.getMember().getEmail();
						
				mailService.eventMemberNotification("成功候補", memberName, eventName, toEmail);
				 
			 }else {
				//若活動尚未額滿，或已額滿但無候補人選，活動人數 -1
				 event.setNumberOfParticipants(event.getNumberOfParticipants() - 1);
				 eventRepository.save(event); 
			 }
			 
			//舉辦者不能取消參加活動?? 或是直接取消活動?? 待討論
		}

	//根據ID找出單一活動  (getOneEvent跟findEventsByConditions可以寫在一起...邏輯同空間評論...後改)
	public EventResponse getOneEvent(String eventno) {
		
			Optional<Event> optional = eventRepository.findById(eventno);
	        EventResponse searchedEvent = eventRepository.getOneEvent(eventno);

			return searchedEvent;  
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
            String branch,
            Pageable pageable) {
        // 進行搜尋
        Page<EventResponse> eventPage = eventRepository.findEventsByConditions(
        		eventName,
        		startTime,
        		endTime,
        		category,
        		branch,
                pageable
        );
        
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
	
	
	//隨時間自動更新活動狀態 ( 主要是 SCHEDULED 時間過了event_start_time後變成 HELD )，
	//要用到Thread ( 多執行續 ) ?
    public void UpdateHeldEvents() {
        Timestamp now = Timestamp.from(Instant.now());
        //找出狀態為SCHEDULED 且目前時間已經超過event_start_time
        List<Event> scheduledEvents = eventRepository.findByEventStatusAndEventStartTimeAfter( EventStatus.SCHEDULED, now);
        
        //將狀態改為HELD
        if(!scheduledEvents.isEmpty()){
            for (Event event : scheduledEvents) {
            	event.setEventStatus(EventStatus.HELD);
            }
            eventRepository.saveAll(scheduledEvents);
            System.out.println("已自動更新" + scheduledEvents.size() + "個活動為已舉辦");
        } else {
            System.out.println("無活動需要更新");
        }
    }
    
	//自動更新排程器
    @Scheduled(fixedRate = 60 * 60 * 1000)
    @Transactional
    public void autoUpdateEvents(){
    	UpdateHeldEvents();
        //System.out.println("排程器自動更新已舉辦的活動");
    }

    @PostConstruct
    @Transactional
    public void updateEventsByStartUp(){
    	UpdateHeldEvents();
    	notifyHeldEventsMembers();
        System.out.println("啟動Spring後, 自動更新已舉辦的活動");
    }
    
    //先在排程器中添加新方法調用
    @Scheduled(cron = "0 0 10 * * ?") // 每天上午10點執行
    @Transactional
    public void scheduledNotificationCheck() {
    	notifyHeldEventsMembers();
        System.out.println("排程器檢查需要發送提醒的活動");
    }
    
    
	//活動正式舉辦前一天(? 寄email給所有參加者以及舉辦者，用Thread(多執行續?)
    @Transactional(readOnly = true) // 只讀事務，因為我們只是查詢並發送郵件
    public void notifyHeldEventsMembers() {
    	
    	 // 取得明天的日期（從當前時間加上24小時）
        Timestamp tomorrow = new Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        // 取得後天的日期（當前時間加上48小時）
        Timestamp dayAfterTomorrow = new Timestamp(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000);
        
        // 找出明天開始的活動（event_start_time 介於明天和後天之間）
        List<Event> eventsStartingTomorrow = eventRepository.findByEventStartTimeBetweenAndEventStatus(
            tomorrow, dayAfterTomorrow, EventStatus.SCHEDULED);
        
        if (!eventsStartingTomorrow.isEmpty()) {
            System.out.println("找到 " + eventsStartingTomorrow.size() + " 個明天開始的活動，準備發送提醒郵件");
            
            // 使用非同步方式發送郵件
            CompletableFuture.runAsync(() -> {
                for (Event event : eventsStartingTomorrow) {
                	String eventName = event.getEventName();
                    try {
                        // 取得參加者名單
                        List<EventMember> participants = eventMemberRepository.findByEvent_EventId(event.getEventId());
                        
                        // 發送郵件給參加者
                        for (EventMember participant : participants) {
                        	String member = participant.getMember().getEmail();
                        	String mail = participant.getMember().getMemberName();
                        	mailService.eventMemberNotification("活動開始通知", member, eventName, mail );
                        }
                   
                        System.out.println("活動 '" + eventName + "' 的提醒郵件已發送");
                    } catch (Exception e) {
                        System.err.println("發送活動 '" + eventName + "' 的提醒郵件時出錯: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
            
            System.out.println("提醒郵件發送請求已提交");
        } else {
            System.out.println("沒有需要發送提醒的活動");
        }
       //寄email通知所有被設為取消的成員
//         String memberName = firstQueuedMemebr.getMember().getMemberName();
//		 String eventName = event.getEventName();
//		 String toEmail = firstQueuedMemebr.getMember().getEmail();
//					
//		mailService.eventMemberNotification("活動取消", memberName, eventName, toEmail);
    
    }
    
    
    
	//活動內容是否可以修改?? 改人數上限(只能調高不能調低)、活動類別、活動標題、注意事項、使用者的話...?
	

	//(使用者方的活動頁面) 根據種類篩選出活動列表 ( 已報名ATTENT、替補狀態QUEUED、已參與的活動歷史、自己建立的活動 )
	public Page<EventMemberResponse> filterEventsByUser(
			String userCategory, 
			String memberId,
			String participateStatus,
	        String eventStatus,
	        String organizerId,
			Pageable pageable){
		
		Page<EventMemberResponse> memberResponsePage = null;
		
		//初始化搜尋條件 
		switch(userCategory) {
	  		case "已報名但尚未舉辦":
	  			participateStatus = "ATTENT";
	  			eventStatus = "SCHEDULED";
	  			memberResponsePage = eventMemberRepository.getEventByMemberConditions( memberId, participateStatus, eventStatus, organizerId, pageable );
	  			break;
	  		case "候補活動":
	  			participateStatus = "QUEUED";
	  			eventStatus = "SCHEDULED";
	  			memberResponsePage = eventMemberRepository.getEventByMemberConditions( memberId, participateStatus, eventStatus, organizerId, pageable );
	  			break;
	  		case "已舉辦且已參加":
	  			participateStatus = "ATTENT";
	  			eventStatus = "HELD";
	  			memberResponsePage = eventMemberRepository.getEventByMemberConditions( memberId, participateStatus, eventStatus, organizerId, pageable );
	  			break;
	  		case "自己建立的活動":
	  			memberResponsePage = eventMemberRepository.getEventByMemberConditions( memberId, participateStatus, eventStatus, organizerId, pageable );
	  			break;
	  		default:
	  			break;
	  }
		
		 List<EventMemberResponse> responseList = memberResponsePage.getContent();
	        
	     //回傳的 Page<EventMemberResponse>
	     return new PageImpl<>(responseList, pageable, memberResponsePage.getTotalElements());
		
	}
	
	//舉辦者取消活動的Service(update活動狀態為CANCELLED、所有參加以及候補的成員狀態改為CANCELLED)
	//並寄email通知所有參加者以及候補者
	@Transactional
	public void cancellEvent(String organizerId, String eventId) {
		Event event = eventRepository.findById(eventId)
	            .orElseThrow(() -> new ResourceNotFoundException("找不到活動 ID " + eventId));
	 
		Member member = memberRepository.findById(organizerId)
	            .orElseThrow(() -> new ResourceNotFoundException("找不到使用者 ID " + eventId));
	 
		//用舉辦人id以及活動id查詢訂單，作為舉辦者取消活動用
		Orders cancelledEventOrder = ordersRepository.findByEventEventIdAndMemberMemberId(eventId, organizerId)
													.orElse(null);
		
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		
		if( cancelledEventOrder != null ) {
			//找到該活動id，取消活動
			updateEventStatus(eventId, "已取消");
			
			//將所有活動參加以及候補的成員狀態改為CANCELLED
			List<EventMember> cancellMembers = eventMemberRepository.findByEvent_EventId(eventId);
			//儲存被設為取消的成員們
			List<Member> cancelledMembers = new ArrayList<Member>();
			
			for(EventMember cancellMember : cancellMembers) {
				if( cancellMember.getParticipateStatus() == EventMemberStatus.ATTENT 
						|| cancellMember.getParticipateStatus() == EventMemberStatus.QUEUED ) {
					
					cancellMember.setParticipateStatus(EventMemberStatus.CANCELLED);
					cancellMember.setCreatedTime(currentTime);
					
					eventMemberRepository.save(cancellMember);
					
					cancelledMembers.add(cancellMember.getMember());
					
					System.out.println("活動成員: " + cancellMember.getMember().getMemberId() 
							+ "已被取消參加活動: " + eventId);
					
					//寄email通知所有被設為取消的成員
					String memberName = cancellMember.getMember().getMemberName();
					String eventName = event.getEventName();
					String toEmail = cancellMember.getMember().getEmail();
							
					mailService.eventMemberNotification("活動取消", memberName, eventName, toEmail);
				}
				
			}
			//之後加上寄email通知所有被設為取消的成員
		}
		
		//若活動開始時間已超過，則不能CANCELLED !! 要加判斷式
		
	
	}
	
}
