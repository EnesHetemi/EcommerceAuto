package com.carsecommerce.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carsecommerce.common.entity.Customer;
import com.carsecommerce.customer.CustomerNotFoundException;
import com.carsecommerce.customer.CustomerService;
import com.carsecommerce.customer.CustomerController;

@RestController
public class ShoppingCartRestController {
	@Autowired private ShoppingCartService cartService;
	@Autowired private CustomerService customerService;
	@Autowired private CustomerController customerController;

	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {

		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updatedQuantity = cartService.addProduct(productId, quantity, customer);

			return updatedQuantity + " artikujt e këtij produkti janë shtuar në shportën tuaj të blerjeve.";
		} catch (CustomerNotFoundException ex) {
			return "Ju duhet të identifikoheni për të shtuar këtë produkt në shportë.";
		} catch (ShoppingCartException ex) {
			return ex.getMessage();
		}

	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) 
			throws CustomerNotFoundException {
		String email = customerController.getEmailOfAuthenticatedCustomer(request);
		if (email == null) {
			throw new CustomerNotFoundException("Nuk jeni te vertetuar");
		}

		return customerService.getCustomerByEmail(email);
	}
}
