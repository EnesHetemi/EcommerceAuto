package com.carsecommerce.admin.brand;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BrandController {

	@GetMapping("/brands")
	public String viewHomePage() {
		return "brands/brands";
	}
}
