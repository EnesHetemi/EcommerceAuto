package com.carsecommerce.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carsecommerce.common.entity.Setting;
import com.carsecommerce.common.entity.SettingCategory;

@Service
public class SettingService {
	@Autowired private SettingRepository repo;


	public List<Setting> getGeneralSettings() {
		return repo.findByOneCategories(SettingCategory.GENERAL);
	}

}
