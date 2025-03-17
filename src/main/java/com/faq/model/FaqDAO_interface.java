package com.faq.model;

import java.util.*;

public interface FaqDAO_interface {
		public void insert(FaqVO faqVO);
		public void update(FaqVO faqVO);
		public void delete(String faqId);
		public FaqVO findByPrimaryKey(String faqId);
		public List<FaqVO> getAll();
//		public List<FaqVO> getAllByQua(String str);
}
