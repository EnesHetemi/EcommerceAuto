package com.carsecommerce.admin.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

	@GetMapping("/orders")
	public String viewHomePage() {
		return "orders/orders";
	}
}