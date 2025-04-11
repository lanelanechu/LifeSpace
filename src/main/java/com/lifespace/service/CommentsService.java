package com.lifespace.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lifespace.dto.CommentsDTO;
import com.lifespace.entity.Comments;
import com.lifespace.repository.CommentsRepository;


@Service("commentsService")
public class CommentsService {

	@Autowired
	private CommentsRepository commentsRepository;
	
	public void addComments(Comments comments) {
		commentsRepository.save(comments);
	}

	public void updateComments(Comments comments) {
		comments.setCommentId(comments.getCommentId()); 
		commentsRepository.save(comments);
	}

	public void deleteComments(String commentId) {
		if (commentsRepository.existsById(commentId))
			commentsRepository.deleteByCommentId(commentId);
//		    commentsRepository.deleteById(commentId);
	}

	public Comments getOneComments(String commentId) {
		Optional<Comments> optional = commentsRepository.findById(commentId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<Comments> getAll() {
//		List<Comments> list = commentsrepository.findAll();
//		return list;
		return commentsRepository.findAll(); //上面兩行簡寫為此行。
	}

	// 前台留言板分頁功能	
	public List<CommentsDTO> getCommentsDTOPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("commentTime").ascending());
        return commentsRepository.findAll(pageable).stream().map(comments -> {
            CommentsDTO dto = new CommentsDTO();
            dto.setCommentId(comments.getCommentId());
            dto.setCommentMessage(comments.getCommentMessage());
            dto.setCommentTime(comments.getCommentTime());
            dto.setMemberName(comments.getEventMember().getMember().getMemberName());
            dto.setEventMemberId(comments.getEventMember().getEventMemberId()); // 搭配JS判斷留言是否屬於本人
            
//            String memberId = comments.getEventMember().getMember().getMemberId();
//            dto.setProfilePictureUrl("/members/" + memberId + "/image");

            return dto;
        }).collect(Collectors.toList());
    }

	// 讓 Controller 拿到儲存後的留言資訊（包含 commentId 與 commentTime）
	public Comments addCommentsReturnSaved(Comments comments) {
		return commentsRepository.save(comments);
	}


	
	
	
}
