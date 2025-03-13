package com.branch.model;

import java.util.List;


public class BranchService {

    private BranchDAO_interface dao;

    public BranchService() {
        dao = new BranchJDBCDAO();
    }

    public BranchVO addBranch( String branch_name, String branch_addr, 
            Integer space_qty, Double latitude, Double longitude, 
            Integer branchStatus) {

        BranchVO branchVO = new BranchVO();

        branchVO.setBranch_name(branch_name);
        branchVO.setBranch_addr(branch_addr);
        branchVO.setSpace_qty(space_qty);
        branchVO.setLatitude(latitude);
        branchVO.setLongitude(longitude);
        branchVO.setBranchStatus(branchStatus);
        
        dao.insert(branchVO);

        return branchVO;
    }

    public BranchVO updateBranch(String branch_id, String branch_name, String branch_addr, 
            Integer space_qty, Double latitude, Double longitude, 
            Integer branchStatus) {

        BranchVO branchVO = new BranchVO();

        branchVO.setBranch_id(branch_id);
        branchVO.setBranch_name(branch_name);
        branchVO.setBranch_addr(branch_addr);
        branchVO.setSpace_qty(space_qty);
        branchVO.setLatitude(latitude);
        branchVO.setLongitude(longitude);
        branchVO.setBranchStatus(branchStatus);
        
        dao.update(branchVO);

        return branchVO;
    }

    public void deleteBranch(String branch_id) {
        dao.delete(branch_id);
    }

    public BranchVO getOneBranch(String branch_id) {
        return dao.findByPrimaryKey(branch_id);
    }

    public List<BranchVO> getAll() {
        return dao.getAll();
    }
}