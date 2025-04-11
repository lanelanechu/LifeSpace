package com.lifespace.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lifespace.SessionUtils;
import com.lifespace.dto.CommentsDTO;
import com.lifespace.dto.OrdersDTO;
import com.lifespace.entity.Comments;
import com.lifespace.entity.EventMember;
import com.lifespace.entity.Member;
import com.lifespace.repository.EventMemberRepository;
import com.lifespace.service.CommentsService;
import com.lifespace.service.OrdersService;
//import com.lifespace.util.SessionUtils;

import jakarta.servlet.http.HttpSession;


@RestController
public class CommentsController {

	@Autowired
	CommentsService commentsService;
	
	@Autowired
    private OrdersService ordersSvc;
	
	@Autowired
	private EventMemberRepository eventMemberRepository;
	
//	// 沒驗證登入者是本人
//	@PostMapping("/comments")
//	public String insert(@RequestBody Comments comments) {
//		commentsService.addComments(comments);
//		return "執行資料庫的 Create 操作";
//	}
	
//	// 沒驗證登入者是本人
//	@PutMapping("/comments/{commentId}")
//	public String update(@PathVariable String commentId,
//						 @RequestBody Comments comments) {
//		comments.setCommentId(commentId); //這樣就可以設定commentsVO裡面的id的值
//		commentsService.updateComments(comments);
//		return "執行資料庫的 Update 操作";
//	}
	
//	// 沒驗證登入者是本人
//	@DeleteMapping("/comments/{commentId}")
//	public String delete(@PathVariable String commentId) {
//		commentsService.deleteComments(commentId);
//		return "執行資料庫的 Delete 操作";
//	}
	
	@GetMapping("/comments/{commentId}")
	public Comments read(@PathVariable String commentId) {
		Comments comments = commentsService.getOneComments(commentId);
		return comments;
	}
	
	@GetMapping("/comments")
	public List<Comments> read() {
		List<Comments> comments = commentsService.getAll();
		return comments;
	}
	
	// 後台中心活動留言板管理查詢功能
	@GetMapping("/comments/getAll")
    public List<OrdersDTO> getAllOrders() {
        return ordersSvc.getAllOrdersDTOs();
    }
	
	
	
	/**
	 * 前台取得留言分頁資料（RESTful 路由）
	 *
	 * 使用 infinite scroll 時呼叫此方法，每次回傳指定頁數的留言清單。
	 *
	 * @param page 當前頁數（從 0 開始）
	 * @param size 每頁筆數
	 * @return 留言資料的 List<CommentsDTO>
	 */	
	@GetMapping("/comments/page/{page}/{size}")
	public List<CommentsDTO> getCommentsPage(@PathVariable int page, @PathVariable int size) {
	    return commentsService.getCommentsDTOPage(page, size);
	}
	
	// 有參加活動的會員才能 新增 本人的留言
	@PostMapping("/comments")
	public CommentsDTO insertComments(@RequestBody Comments comments, HttpSession session) {
	    Object obj = session.getAttribute("eventMember");
	    EventMember eventMember = (obj instanceof EventMember) ? (EventMember) obj : null;

	    if (eventMember == null) {
	        throw new RuntimeException("尚未登入或未參加活動");
	    }

	    
	    
	    comments.setEventMember(eventMember);
	    Comments saved = commentsService.addCommentsReturnSaved(comments);

	    CommentsDTO dto = new CommentsDTO();
	    dto.setCommentId(saved.getCommentId());
	    dto.setCommentMessage(saved.getCommentMessage());
	    dto.setCommentTime(saved.getCommentTime());
	    dto.setEventMemberId(eventMember.getEventMemberId());
	    String memberId = eventMember.getMember().getMemberId();
	String memberName = eventMember.getMember().getMemberName();
	dto.setMemberName("<a href=\"/members/" + memberId + "/profile\">" + memberName + "</a>");

	    return dto;
	}

	// 有參加活動的會員才能 編輯 本人的留言
	@PutMapping("/comments/{commentId}")
	public ResponseEntity<?> updateComment(@PathVariable String commentId,
	                                        @RequestBody Comments updatedComment,
	                                        HttpSession session) {
	    Object obj = session.getAttribute("eventMember");
	    EventMember eventMember = (obj instanceof EventMember) ? (EventMember) obj : null;
	    if (eventMember == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登入或未參加活動");
	    }

	    Comments original = commentsService.getOneComments(commentId);
	    if (original == null || !original.getEventMember().getEventMemberId().equals(eventMember.getEventMemberId())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("無權限編輯他人留言");
	    }

	    original.setCommentMessage(updatedComment.getCommentMessage());
	    commentsService.updateComments(original);
	    return ResponseEntity.ok("留言已更新");
	}

	// 有參加活動的會員才能 刪除 本人的留言
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable String commentId, HttpSession session) {
	    Object obj = session.getAttribute("eventMember");
	    EventMember eventMember = (obj instanceof EventMember) ? (EventMember) obj : null;
	    if (eventMember == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登入或未參加活動");
	    }

	    Comments original = commentsService.getOneComments(commentId);
	    if (original == null || !original.getEventMember().getEventMemberId().equals(eventMember.getEventMemberId())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("無權限刪除他人留言");
	    }

	    commentsService.deleteComments(commentId);
	    return ResponseEntity.ok("留言已刪除");
	}

	// 查詢會員資料
	@GetMapping("/members/{eventMemberId}/profile")
	public ResponseEntity<?> getMemberProfileFromEventMember(@PathVariable String eventMemberId) {
	    Optional<EventMember> emOpt = eventMemberRepository.findById(eventMemberId);
	    if (emOpt.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到活動會員：" + eventMemberId);
	    }

	    Member member = emOpt.get().getMember();

	    Map<String, Object> data = new HashMap<>();
	    data.put("memberId", member.getMemberId());
	    data.put("memberName", member.getMemberName());
	    data.put("email", member.getEmail());
	    data.put("phone", member.getPhone());
	    data.put("birthday", member.getBirthday());

	    return ResponseEntity.ok(data);
	}

	
	
	
//	// 新增留言（從 session 拿 EventMember 與 Member，不用修改 SessionUtils）
//	@PostMapping("/comments")
//	public CommentsDTO insertComment(@RequestBody Comments comments, HttpSession session) {
//	    Object obj = session.getAttribute("eventMember");
//	    EventMember eventMember = (obj instanceof EventMember) ? (EventMember) obj : null;
//
//	    if (eventMember == null) {
//	        throw new RuntimeException("未登入或未參加活動");
//	    }
//
//	    Member member = eventMember.getMember();
//
//	    comments.setEventMember(eventMember);
//	    comments.setEvent(eventMember.getEvent());
//	    Comments saved = commentsService.addCommentsReturnSaved(comments);
//
//	    CommentsDTO dto = new CommentsDTO();
//	    dto.setCommentId(saved.getCommentId());
//	    dto.setCommentMessage(saved.getCommentMessage());
//	    dto.setCommentTime(saved.getCommentTime());
//	    dto.setEventMemberId(eventMember.getEventMemberId());
//	    dto.setMemberName("<a href=\"/members/" + eventMember.getEventMemberId() + "/profile\">" + member.getMemberName() + "</a>");
//	    dto.setProfilePictureUrl(member.getImageUrl());
//
//	    return dto;
//	}

//	// 查詢留言（只查該活動的留言）
//	@GetMapping("/comments/page/{page}/{size}")
//	public List<CommentsDTO> getCommentsPageForCurrentEvent(@PathVariable int page,
//	                                                         @PathVariable int size,
//	                                                         HttpSession session) {
//	    Object obj = session.getAttribute("eventMember");
//	    EventMember eventMember = (obj instanceof EventMember) ? (EventMember) obj : null;
//
//	    if (eventMember == null) {
//	        throw new RuntimeException("未登入或未參加活動");
//	    }
//
//	    String eventId = eventMember.getEvent().getEventId();
//	    return commentsService.getCommentsDTOPageByEvent(eventId, page, size);
//	}


	
	
	// 判斷是否為活動參加者，限制留言與查詢，檢查 session
//	@PostMapping("/comments")
//	public ResponseEntity<String> insert(@RequestBody Comments comments, HttpSession session) {
//	    EventMember eventMember = SessionUtils.getCurrentEventMember(session);
//	    if (eventMember == null) {
//	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("尚未參加活動，無法留言");
//	    }
//	    comments.setEventMember(eventMember);
//	    commentsService.addComments(comments);
//	    return ResponseEntity.ok("留言成功");
//	}

//	@GetMapping("/comments/page")
//	public ResponseEntity<?> getCommentsPage(@RequestParam int page, @RequestParam int size, HttpSession session) {
//	    EventMember eventMember = SessionUtils.getCurrentEventMember(session);
//	    if (eventMember == null) {
//	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("尚未參加活動，無法查看留言");
//	    }
//	    return ResponseEntity.ok(commentsService.getCommentsDTOPage(page, size));
//	}

	
	
}
