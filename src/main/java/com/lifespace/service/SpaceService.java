package com.lifespace.service;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.lifespace.dto.SpaceEquipmentRequest;
import com.lifespace.dto.SpaceRequest;
import com.lifespace.entity.*;
import com.lifespace.repository.SpaceUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifespace.exception.ResourceNotFoundException;
import com.lifespace.repository.SpaceRepository;
import org.springframework.web.multipart.MultipartFile;

@Service	
public class SpaceService {
	
	@Autowired
	private SpaceRepository spaceRepository;

    @Autowired
    private SpaceUsageRepository spaceUsageRepository;

	public List<Space> getAllSpaces() {  // 取得所有空間
		return spaceRepository.findAll();
	}

	public Space getSpaceById(String spaceId) {  // 透過id取得單一空間
		return spaceRepository.findById(spaceId).orElse(null);
	}

	public Space getSpaceBySpaceName(String spaceName) {  // 透過空間名稱取得單一空間
		return spaceRepository.findBySpaceName(spaceName).orElse(null);
	}
	
	// 含有巢狀關聯資料(space equipment, space photos)的新增做法
	public Space addSpace(SpaceRequest space, List<MultipartFile> files) throws IOException {

		Space s = new Space();
		s.setBranchId(space.getBranchId());
		s.setSpaceName(space.getSpaceName());
		s.setSpacePeople(space.getSpacePeople());
		s.setSpaceSize(space.getSpaceSize());
		s.setSpaceHourlyFee(space.getSpaceHourlyFee());
		s.setSpaceDailyFee(space.getSpaceDailyFee());
		s.setSpaceDesc(space.getSpaceDesc());
		s.setSpaceAlert(space.getSpaceAlert());
		s.setSpaceStatus(space.getSpaceStatus());
		s.setSpaceFloor(space.getSpaceFloor());

		// ============= 新增Space Equipments =============
		Set<SpaceEquipment> equips = space.getSpaceEquipments().stream().map(se -> {
			SpaceEquipment equip = new SpaceEquipment();
			equip.setSpaceEquipName(se.getSpaceEquipName());
			equip.setSpaceEquipComment(se.getSpaceEquipComment());
			equip.setSpace(s);  // 建立關聯，沒寫會報錯
			return equip;
		}).collect(Collectors.toSet());
        s.setSpaceEquipments(equips);

		// ============= 新增Space Photos =============
 		Set<SpacePhoto> photos = new LinkedHashSet<>();
		for (MultipartFile file : files) {
			SpacePhoto photo = new SpacePhoto();
			photo.setPhoto(file.getBytes());
			photo.setSpace(s); // 關聯回 Space
			photos.add(photo);
		}
		s.setSpacePhotos(photos);

		// ============= 新增Space Usage maps =============
		List<SpaceUsage> usages = spaceUsageRepository.findAllById(space.getSpaceUsageIds());

		// 3. 建立對應的 SpaceUsageMap
		Set<SpaceUsageMap> usageMaps = usages.stream().map(usage -> {
			SpaceUsageMap map = new SpaceUsageMap();
			map.setSpace(s);           // 關聯到 Space（必填）
			map.setSpaceUsage(usage);      // 關聯到 Usage（必填）
			return map;
		}).collect(Collectors.toSet());

		// 4. 把 usageMap 塞進 Space
		s.setSpaceUsageMaps(usageMaps);

		// 新增空間
		return spaceRepository.save(s);  // CascadeType.ALL 會自動幫你存子表格的項目
	}

	public Space updateSpace(String spaceId, SpaceRequest space, List<MultipartFile> files) throws IOException {  // 修改空間
		Space s = spaceRepository.findById(spaceId).orElse(null);   // 檢查資料是否存在
		if (s == null) {
			throw new ResourceNotFoundException("找不到ID 為「 " + spaceId + " 」的空間");
		}
		s.setBranchId(space.getBranchId());
		s.setSpaceName(space.getSpaceName());
		s.setSpacePeople(space.getSpacePeople());
		s.setSpaceSize(space.getSpaceSize());
		s.setSpaceHourlyFee(space.getSpaceHourlyFee());
		s.setSpaceDailyFee(space.getSpaceDailyFee());
		s.setSpaceDesc(space.getSpaceDesc());
		s.setSpaceAlert(space.getSpaceAlert());
		s.setSpaceStatus(space.getSpaceStatus());
		s.setSpaceFloor(space.getSpaceFloor());

		// ============= 修改Space Equipments =============
		Set<SpaceEquipment> targetEquipments = s.getSpaceEquipments();
		targetEquipments.clear();
		for (SpaceEquipmentRequest se : space.getSpaceEquipments()) {
			System.out.println(s.getSpaceEquipments());
			SpaceEquipment equip = new SpaceEquipment();
			equip.setSpaceEquipName(se.getSpaceEquipName());
			equip.setSpaceEquipComment(se.getSpaceEquipComment());
			equip.setSpace(s);
			targetEquipments.add(equip);
		}

		// ============= 修改Space Photos =============
		Set<SpacePhoto> targetPhotos = s.getSpacePhotos();
		targetPhotos.clear();
		if (files != null && !files.isEmpty()) {
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					SpacePhoto photo = new SpacePhoto();
					photo.setPhoto(file.getBytes());
					photo.setSpace(s);
					targetPhotos.add(photo);
				}
			}
		}

		// ============= 修改Space Usage Maps =============
		Set<SpaceUsageMap> targetUsageMaps = s.getSpaceUsageMaps();
		targetUsageMaps.clear();
		List<SpaceUsage> usages = spaceUsageRepository.findAllById(space.getSpaceUsageIds());
		for (SpaceUsage usage : usages) {
			SpaceUsageMap map = new SpaceUsageMap();
			map.setSpace(s);
			map.setSpaceUsage(usage);
			targetUsageMaps.add(map);
		}

		return spaceRepository.save(s);   // CascadeType.ALL + orphanRemoval = true
	}
}
