package com.carsecommerce.admin.customer;

import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.carsecommerce.common.entity.Customer;

@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMERS_PER_PAGE = 4;

	@Autowired private CustomerRepository customerRepo;	

	public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, CUSTOMERS_PER_PAGE, sort);

		if (keyword != null) {
			return customerRepo.findAll(keyword, pageable);
		}

		return customerRepo.findAll(pageable);
	}

	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepo.updateEnabledStatus(id, enabled);
	}

	public Customer get(Integer id) throws CustomerNotFoundException {
		try {
			return customerRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CustomerNotFoundException("Nuk mund të gjendet asnjë konsumator me ID " + id);
		}
	}	

	public void delete(Integer id) throws CustomerNotFoundException {
		Long count = customerRepo.countById(id);
		if (count == null || count == 0) {
			throw new CustomerNotFoundException("Nuk mund të gjendet asnjë konsumator me ID " + id);
		}

		customerRepo.deleteById(id);
	}

}
