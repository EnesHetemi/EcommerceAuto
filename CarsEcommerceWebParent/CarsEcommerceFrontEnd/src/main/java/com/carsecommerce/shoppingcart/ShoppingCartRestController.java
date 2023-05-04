package com.carsecommerce.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			float subtotal = cartService.updateQuantity(productId, quantity, customer);

			return String.valueOf(subtotal);
		} catch (CustomerNotFoundException ex) {
			return "Ju duhet të identifikoheni për të ndryshuar sasinë e produktit.";
		}	
	}
	
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable("productId") Integer productId,
			HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			cartService.removeProduct(productId, customer);

			return "Produkti është hequr nga karroca juaj e blerjeve.";

		} catch (CustomerNotFoundException e) {
			return "Ju duhet të identifikoheni për të hequr produktin.";
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
