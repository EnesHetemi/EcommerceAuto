package com.carsecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.carsecommerce.common.entity", "com.carsecommerce.site.customer"})
public class CarsEcommerceFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarsEcommerceFrontEndApplication.class, args);
	}

}
