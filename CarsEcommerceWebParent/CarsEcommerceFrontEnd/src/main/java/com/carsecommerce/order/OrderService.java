package com.carsecommerce.order;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.carsecommerce.checkout.CheckoutInfo;
import com.carsecommerce.common.entity.Address;
import com.carsecommerce.common.entity.CartItem;
import com.carsecommerce.common.entity.Customer;
import com.carsecommerce.common.entity.order.Order;
import com.carsecommerce.common.entity.order.OrderDetail;
import com.carsecommerce.common.entity.order.OrderStatus;
import com.carsecommerce.common.entity.order.PaymentMethod;
import com.carsecommerce.common.entity.Product;

@Service
@Transactional
public class OrderService {
	
	public static final int ORDERS_PER_PAGE = 1;

	@Autowired private OrderRepository repo;

	public Order createOrder(Customer customer, Address address, List<CartItem> cartItems,
			PaymentMethod paymentMethod, CheckoutInfo checkoutInfo) {
		Order newOrder = new Order();
		newOrder.setOrderTime(new Date());
		if (paymentMethod.equals(PaymentMethod.PAYPAL)) {
			newOrder.setStatus(OrderStatus.PAID);
		} else {
			newOrder.setStatus(OrderStatus.NEW);
		}
		newOrder.setCustomer(customer);
		newOrder.setTotal(checkoutInfo.getProductTotal());
		newOrder.setPaymentMethod(paymentMethod);
		newOrder.setDeliverDays(checkoutInfo.getDeliverDays());
		newOrder.setDeliverDate(checkoutInfo.getDeliverDate());

		if (address == null) {
			newOrder.copyAddressFromCustomer();
		} else {
			newOrder.copyShippingAddress(address);
		}

		Set<OrderDetail> orderDetails = newOrder.getOrderDetails();

		for (CartItem cartItem : cartItems) {
			Product product = cartItem.getProduct();

			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrder(newOrder);
			orderDetail.setProduct(product);
			orderDetail.setQuantity(cartItem.getQuantity());
			orderDetail.setUnitPrice(product.getDiscountPrice());
			orderDetail.setTotal(cartItem.getSubtotal());
			orderDetails.add(orderDetail);
		}


		return repo.save(newOrder);
	}
	
	public Page<Order> listForCustomerByPage(Customer customer, int pageNum, 
			String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

		if (keyword != null) {
			return repo.findAll(keyword, customer.getId(), pageable);
		}

		return repo.findAll(customer.getId(), pageable);

	}
	
	public Order getOrder(Integer id, Customer customer) {
		return repo.findByIdAndCustomer(id, customer);
	}
}
