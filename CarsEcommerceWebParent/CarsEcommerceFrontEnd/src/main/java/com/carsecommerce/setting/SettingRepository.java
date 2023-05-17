package com.carsecommerce.setting;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.carsecommerce.common.entity.Setting;
import com.carsecommerce.common.entity.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String> {
	public List<Setting> findByCategory(SettingCategory category);

	@Query("SELECT s FROM Setting s WHERE s.category = ?1")
	public List<Setting> findByOneCategories(SettingCategory catOne);
}
