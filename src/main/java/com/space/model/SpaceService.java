package com.space.model;

import java.sql.Timestamp;
import java.util.List;

public class SpaceService {
	private SpaceDAO_interface dao;

	public SpaceService() {
		dao = new SpaceJDBCDAO();
	}

	public SpaceVO addSpace(String branchId, String spaceName, Integer spacePeople,
			Double spaceSize, Integer spaceHourlyFee, Integer spaceDailyFee, String spaceDesc,
			String spaceAlert, Integer spaceStatus, String spaceAddress,
			Double latitude, Double longitude) {
		System.out.println(spaceStatus);
		SpaceVO spaceVO = new SpaceVO();
		spaceVO.setBranchId(branchId);
		spaceVO.setSpaceName(spaceName);
		spaceVO.setSpacePeople(spacePeople);
		spaceVO.setSpaceSize(spaceSize);
		spaceVO.setSpaceHourlyFee(spaceHourlyFee);
		spaceVO.setSpaceDailyFee(spaceDailyFee);
		spaceVO.setSpaceDesc(spaceDesc);
		spaceVO.setSpaceAlert(spaceAlert);
		spaceVO.setSpaceStatus(spaceStatus);
		spaceVO.setSpaceAddress(spaceAddress);
		spaceVO.setLatitude(latitude);
		spaceVO.setLongitude(longitude);
		dao.insert(spaceVO);
		return spaceVO;
	}
	
	public SpaceVO updateSpace(String spaceId, String branchId, String spaceName, Integer spacePeople,
			Double spaceSize, Integer spaceHourlyFee, Integer spaceDailyFee, String spaceDesc,
			String spaceAlert, Integer spaceStatus, String spaceAddress,
			Double latitude, Double longitude) {
		SpaceVO spaceVO = new SpaceVO();
		spaceVO.setSpaceId(spaceId);
		spaceVO.setBranchId(branchId);
		spaceVO.setSpaceName(spaceName);
		spaceVO.setSpacePeople(spacePeople);
		spaceVO.setSpaceSize(spaceSize);
		spaceVO.setSpaceHourlyFee(spaceHourlyFee);
		spaceVO.setSpaceDailyFee(spaceDailyFee);
		spaceVO.setSpaceDesc(spaceDesc);
		spaceVO.setSpaceAlert(spaceAlert);
		spaceVO.setSpaceStatus(spaceStatus);
		spaceVO.setSpaceAddress(spaceAddress);
		spaceVO.setLatitude(latitude);
		spaceVO.setLongitude(longitude);
		dao.update(spaceVO);
		return spaceVO;
	}
	
	public void deleteSpace(String spaceId) {
		dao.delete(spaceId);
	}
	
	public SpaceVO getOneSpace(String spaceId) {
		return dao.findByPrimaryKey(spaceId);
	}
	
	public List<SpaceVO> getAll() {
		return dao.getAll();
	}
}