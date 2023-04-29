package com.carsecommerce.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.carsecommerce.common.entity.Product;

@Controller
public class ProductController {
	@Autowired private ProductService productService;

	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return listByPage(1, model, null);
	}

	@GetMapping("/products/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("keyword") String keyword
			) {
		Page<Product> page = productService.listByPage(pageNum, keyword);
		List<Product> listProducts = page.getContent();

		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("keyword", keyword);	
		model.addAttribute("moduleURL", "/products");

		model.addAttribute("listProducts", listProducts);

		return "products/product";
	}
}
