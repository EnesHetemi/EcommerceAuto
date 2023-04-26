package com.carsecommerce.admin.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.carsecommerce.common.entity.Category;
import com.carsecommerce.common.entity.User;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
	
	@Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);	
	
	@Query("SELECT c FROM Category c WHERE c.name LIKE %?1%")
	public Page<Category> findAll(String keyword, Pageable pageable);
	
	public Long countById(Integer id);
	
	public Category findByName(String name);

	public Category findByAlias(String alias);

}
