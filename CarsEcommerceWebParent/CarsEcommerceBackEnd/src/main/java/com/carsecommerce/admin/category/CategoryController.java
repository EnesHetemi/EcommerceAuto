package com.carsecommerce.admin.category;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {

	@GetMapping("/categories")
	public String viewHomePage() {
		return "categories/categories";
	}
}