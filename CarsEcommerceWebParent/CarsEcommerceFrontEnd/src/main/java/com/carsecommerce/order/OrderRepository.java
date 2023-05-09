package com.carsecommerce.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carsecommerce.common.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
