package com.carsecommerce.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.carsecommerce.common.entity.Setting;
import com.carsecommerce.common.entity.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String> {

	public List<Setting> findByCategory(SettingCategory category);
	
}
