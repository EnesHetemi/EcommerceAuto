package com.carsecommerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.carsecommerce.common.entity.Customer;
import com.carsecommerce.customer.CustomerRepository;

public class CustomerUserDetailsService implements UserDetailsService {

	@Autowired private CustomerRepository repo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Customer customer = repo.findByEmail(email);
		if (customer == null) 
			throw new UsernameNotFoundException("Asnjë klient nuk u gjet me email " + email);

		return new CustomerUserDetails(customer);
	}

}
