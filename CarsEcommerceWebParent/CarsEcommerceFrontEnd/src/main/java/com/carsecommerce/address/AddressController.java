package com.carsecommerce.address;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.carsecommerce.customer.CustomerController;
import com.carsecommerce.common.entity.Address;
import com.carsecommerce.common.entity.Customer;
import com.carsecommerce.customer.CustomerService;

@Controller
public class AddressController {

	@Autowired private AddressService addressService;
	@Autowired private CustomerService customerService;	
	@Autowired private CustomerController customerController;	

	@GetMapping("/address_book")
	public String showAddressBook(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		List<Address> listAddresses = addressService.listAddressBook(customer);

		boolean usePrimaryAddressAsDefault = true;
		for (Address address : listAddresses) {
			if (address.isDefaultForShipping()) {
				usePrimaryAddressAsDefault = false;
				break;
			}
		}

		model.addAttribute("listAddresses", listAddresses);
		model.addAttribute("customer", customer);
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);

		return "address_book/addresses";
	}
	
	
	@GetMapping("/address_book/new")
	public String newAddress(Model model) {

		model.addAttribute("address", new Address());
		model.addAttribute("pageTitle", "Shto Adresën e re");

		return "address_book/address_form";
	}

	@PostMapping("/address_book/save")
	public String saveAddress(Address address, HttpServletRequest request, RedirectAttributes ra) {
		Customer customer = getAuthenticatedCustomer(request);

		address.setCustomer(customer);
		addressService.save(address);

		ra.addFlashAttribute("message", "Adresa është ruajtur me sukses.");

		return "redirect:/address_book";
	}

	@GetMapping("/address_book/edit/{id}")
	public String editAddress(@PathVariable("id") Integer addressId, Model model,
			HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);

		Address address = addressService.get(addressId, customer.getId());

		model.addAttribute("address", address);
		model.addAttribute("pageTitle", "Ndrysho Adresen (ID: " + addressId + ")");

		return "address_book/address_form";
	}

	@GetMapping("/address_book/delete/{id}")
	public String deleteAddress(@PathVariable("id") Integer addressId, RedirectAttributes ra,
			HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		addressService.delete(addressId, customer.getId());

		ra.addFlashAttribute("message", "Adresa me ID " + addressId + " është fshirë me sukses.");

		return "redirect:/address_book";
	}
	
	@GetMapping("/address_book/default/{id}")
	public String setDefaultAddress(@PathVariable("id") Integer addressId,
			HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		addressService.setDefaultAddress(addressId, customer.getId());

		return "redirect:/address_book"; 
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = customerController.getEmailOfAuthenticatedCustomer(request);				
		return customerService.getCustomerByEmail(email);
	}		
}
