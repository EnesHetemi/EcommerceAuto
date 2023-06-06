package com.carsecommerce.checkout;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class CheckoutInfo {
	private float productTotal;
	private float paymentTotal;
	private int deliverDays;

	public float getProductTotal() {
		return productTotal;
	}

	public void setProductTotal(float productTotal) {
		this.productTotal = productTotal;
	}

	public float getPaymentTotal() {
		return paymentTotal;
	}

	public void setPaymentTotal(float paymentTotal) {
		this.paymentTotal = paymentTotal;
	}

	public int getDeliverDays() {
		return deliverDays;
	}

	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}

	public Date getDeliverDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, deliverDays);

		return calendar.getTime();
	}
	
	public String getPaymentTotal4PayPal() {
		DecimalFormat formatter = new DecimalFormat("###,###.##");
		return formatter.format(paymentTotal);
	}

}
