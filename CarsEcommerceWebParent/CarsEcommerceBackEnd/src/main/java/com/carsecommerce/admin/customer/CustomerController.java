package com.carsecommerce.admin.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {

	@GetMapping("/customers")
	public String viewHomePage() {
		return "customers/customers";
	}
}
