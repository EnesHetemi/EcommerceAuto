package com.carsecommerce.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.carsecommerce.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1% " 
			+ "OR p.numberOem LIKE %?1% "
			+ "OR p.otherReference LIKE %?1% "
			+ "OR p.productCode LIKE %?1% "
			+ "OR p.brand.name LIKE %?1% "
			+ "OR p.category.name LIKE %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.brand.id = ?1 ")	
	public Page<Product> findAllInBrand(Integer brandId, 
			Pageable pageable);

	@Query("SELECT p FROM Product p WHERE (p.brand.id = ?1 "
			+ "OR p.category.id LIKE %?2%) AND "
			+ "(p.name LIKE %?3% " 
			+ "OR p.otherReference LIKE %?3% "
			+ "OR p.productCode LIKE %?3% "
			+ "OR p.brand.name LIKE %?3% "
			+ "OR p.category.name LIKE %?3%)")			
	public Page<Product> searchInBrand(Integer brandId, 
			String keyword, Pageable pageable);
	
	public Product findByAlias(String alias);
	
}
