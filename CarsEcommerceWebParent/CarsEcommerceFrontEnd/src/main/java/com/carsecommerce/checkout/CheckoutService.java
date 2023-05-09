package com.carsecommerce.checkout;

import java.util.List;

import org.springframework.stereotype.Service;

import com.carsecommerce.common.entity.CartItem;

@Service
public class CheckoutService {

	public CheckoutInfo prepareCheckout(List<CartItem> cartItems) {
		CheckoutInfo checkoutInfo = new CheckoutInfo();

		float productTotal = calculateProductTotal(cartItems);
		float paymentTotal = productTotal;

		checkoutInfo.setProductTotal(productTotal);
		checkoutInfo.setPaymentTotal(paymentTotal);

		return checkoutInfo;
	}

	private float calculateProductTotal(List<CartItem> cartItems) {
		float total = 0.0f;

		for (CartItem item : cartItems) {
			total += item.getSubtotal();
		}

		return total;
	}
}
