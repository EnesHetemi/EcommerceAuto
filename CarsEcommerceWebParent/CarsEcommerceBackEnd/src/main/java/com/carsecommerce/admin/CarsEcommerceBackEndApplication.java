package com.carsecommerce.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.carsecommerce.common.entity", "com.carsecommerce.admin.user"})
public class CarsEcommerceBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarsEcommerceBackEndApplication.class, args);
	}

}
