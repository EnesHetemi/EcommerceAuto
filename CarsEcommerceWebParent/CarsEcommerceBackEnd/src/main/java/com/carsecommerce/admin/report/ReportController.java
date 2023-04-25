package com.carsecommerce.admin.report;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

	@GetMapping("/report")
	public String viewHomePage() {
		return "reports/reports";
	}
}
