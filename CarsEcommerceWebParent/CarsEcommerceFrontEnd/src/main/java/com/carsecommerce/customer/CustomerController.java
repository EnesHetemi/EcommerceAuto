package com.carsecommerce.customer;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.carsecommerce.common.entity.Customer;
import com.carsecommerce.security.CustomerUserDetails;
import com.carsecommerce.security.oauth.CustomerOAuth2User;

@Controller
public class CustomerController {
	@Autowired private CustomerService customerService;

	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		
		model.addAttribute("pageTitle", "Regjistrimi i Konsumatorit");
		model.addAttribute("customer", new Customer());

		return "register/register_form";
	}
	
	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, Model model,
			HttpServletRequest request) throws UnsupportedEncodingException {
		customerService.registerCustomer(customer);

		model.addAttribute("pageTitle", "Regjistrimi u krye me sukses!");

		return "/register/register_success";
	}
	
	@GetMapping("/account_details")
	public String viewAccountDetails(Model model, HttpServletRequest request) {
		String email = getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCustomerByEmail(email);

		model.addAttribute("customer", customer);
		model.addAttribute("pageTitle", "Ndrysho te dhenat tuaja");

		return "customer/account_form";
	}

	private String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
		String customerEmail = null;

		if (principal instanceof UsernamePasswordAuthenticationToken 
				|| principal instanceof RememberMeAuthenticationToken) {
			customerEmail = request.getUserPrincipal().getName();
		} else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
			customerEmail = oauth2User.getEmail();
		}

		return customerEmail;
	}

	@PostMapping("/update_account_details")
	public String updateAccountDetails(Model model, Customer customer, RedirectAttributes ra,
			HttpServletRequest request) {
		customerService.update(customer);
		ra.addFlashAttribute("message", "Të dhënat e llogarisë suaj janë ndryshuar.");

		updateNameForAuthenticatedCustomer(customer, request);

		return "redirect:/account_details";
	}

	private void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
		Object principal = request.getUserPrincipal();

		if (principal instanceof UsernamePasswordAuthenticationToken 
				|| principal instanceof RememberMeAuthenticationToken) {
			CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);
			Customer authenticatedCustomer = userDetails.getCustomer();
			authenticatedCustomer.setFirstName(customer.getFirstName());
			authenticatedCustomer.setLastName(customer.getLastName());

		} else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
			String fullName = customer.getFirstName() + " " + customer.getLastName();
			oauth2User.setFullName(fullName);
		}		
	}

	private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
		CustomerUserDetails userDetails = null;
		if (principal instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		} else if (principal instanceof RememberMeAuthenticationToken) {
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		}

		return userDetails;
	}
}
