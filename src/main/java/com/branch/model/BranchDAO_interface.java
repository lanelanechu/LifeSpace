package com.branch.model;

import java.util.*;

public interface BranchDAO_interface {
    public void insert(BranchVO branchVO);
    public void update(BranchVO branchVO);
    public void delete(String branch_id);
    public BranchVO findByPrimaryKey(String branch_id);
    public List<BranchVO> getAll();
    // 萬用複合查詢(傳入參數型態Map)(回傳 List)
    // public List<BranchVO> getAll(Map<String, String[]> map);
}