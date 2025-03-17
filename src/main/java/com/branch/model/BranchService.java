package com.branch.model;

import java.util.List;


public class BranchService {

    private BranchDAO_interface dao;

    public BranchService() {
        dao = new BranchJDBCDAO();
    }

    public BranchVO addBranch( String branchName, String branchAddr, 
            Integer spaceQty, Double latitude, Double longitude, 
            Integer branchStatus) {

        BranchVO branchVO = new BranchVO();

        branchVO.setBranchName(branchName);
        branchVO.setBranchAddr(branchAddr);
        branchVO.setSpaceQty(spaceQty);
        branchVO.setLatitude(latitude);
        branchVO.setLongitude(longitude);
        branchVO.setBranchStatus(branchStatus);
        
        dao.insert(branchVO);

        return branchVO;
    }

    public BranchVO updateBranch(String branchId, String branchName, String branchAddr, 
            Integer spaceQty, Double latitude, Double longitude, 
            Integer branchStatus) {

        BranchVO branchVO = new BranchVO();

        branchVO.setBranchId(branchId);
        branchVO.setBranchName(branchName);
        branchVO.setBranchAddr(branchAddr);
        branchVO.setSpaceQty(spaceQty);
        branchVO.setLatitude(latitude);
        branchVO.setLongitude(longitude);
        branchVO.setBranchStatus(branchStatus);
        
        dao.update(branchVO);

        return branchVO;
    }

    public void deleteBranch(String branchId) {
        dao.delete(branchId);
    }

    public BranchVO getOneBranch(String branchId) {
        return dao.findByPrimaryKey(branchId);
    }

    public List<BranchVO> getAll() {
        return dao.getAll();
    }
}