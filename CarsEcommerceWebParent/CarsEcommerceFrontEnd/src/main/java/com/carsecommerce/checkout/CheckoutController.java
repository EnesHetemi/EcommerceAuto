package com.carsecommerce.checkout;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.carsecommerce.customer.CustomerController;
import com.carsecommerce.address.AddressService;
import com.carsecommerce.common.entity.Address;
import com.carsecommerce.common.entity.CartItem;
import com.carsecommerce.common.entity.Customer;
import com.carsecommerce.order.OrderService;
import com.carsecommerce.common.entity.order.PaymentMethod;
import com.carsecommerce.customer.CustomerService;
import com.carsecommerce.shoppingcart.ShoppingCartService;

@Controller
public class CheckoutController {

	@Autowired private CheckoutService checkoutService;
	@Autowired private CustomerService customerService;
	@Autowired private AddressService addressService;
	@Autowired private ShoppingCartService cartService;
	@Autowired private CustomerController customerController;
	@Autowired private OrderService orderService;

	@GetMapping("/checkout")
	public String showCheckoutPage(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);

		Address defaultAddress = addressService.getDefaultAddress(customer);

		if (defaultAddress != null) {
			model.addAttribute("address", defaultAddress);
		} else {
			model.addAttribute("address", customer.getAddress());
		}

		List<CartItem> cartItems = cartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems);

		model.addAttribute("checkoutInfo", checkoutInfo);
		model.addAttribute("cartItems", cartItems);

		return "checkout/checkout";
	}
	
	
	@PostMapping("/place_order")
	public String placeOrder(HttpServletRequest request) {
		String paymentType = request.getParameter("paymentMethod");
		PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

		Customer customer = getAuthenticatedCustomer(request);

		Address defaultAddress = addressService.getDefaultAddress(customer);

		List<CartItem> cartItems = cartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems);

		orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
		cartService.deleteByCustomer(customer);

		return "checkout/order_completed";
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = customerController.getEmailOfAuthenticatedCustomer(request);				
		return customerService.getCustomerByEmail(email);
	}	
}
