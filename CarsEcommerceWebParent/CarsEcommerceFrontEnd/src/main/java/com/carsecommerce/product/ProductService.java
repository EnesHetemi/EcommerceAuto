package com.carsecommerce.product;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.carsecommerce.common.entity.Product;

@Service
@Transactional
public class ProductService {
	
	public static final int PRODUCTS_PER_PAGE = 12;

	@Autowired private ProductRepository repo;

	public List<Product> listAll() {
		return (List<Product>) repo.findAll();
	}
	
	public Page<Product> listByPage(int pageNum, String keyword) {

		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);

		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}

		return repo.findAll(pageable);		
	}
}
