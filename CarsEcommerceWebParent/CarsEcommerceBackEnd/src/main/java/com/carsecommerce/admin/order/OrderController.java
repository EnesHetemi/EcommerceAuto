package com.carsecommerce.admin.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.carsecommerce.admin.security.CarsEcommerceUserDetails;
import com.carsecommerce.common.entity.order.Order;

@Controller
public class OrderController {

	@Autowired
	private OrderService service;
	
	@GetMapping("/orders")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "name", "asc", null, null);
	}

	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword, @AuthenticationPrincipal CarsEcommerceUserDetails loggedUser
			) {
		Page<Order> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Order> listOrders = page.getContent();

		long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
		long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);	
		model.addAttribute("moduleURL", "/orders");

		model.addAttribute("listOrders", listOrders);

		return "orders/orders";
	}
	
	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails(@PathVariable("id") Integer id, Model model, 
			RedirectAttributes ra, HttpServletRequest request) {
		try {
			Order order = service.get(id);			
			model.addAttribute("order", order);

			return "orders/order_details_modal";
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/orders";
		}

	}
	
	@GetMapping("/orders/delete/{id}")
	public String deleteOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			service.delete(id);;
			ra.addFlashAttribute("message", "Porosia me ID " + id + " është fshirë.");
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/orders";
	}
}