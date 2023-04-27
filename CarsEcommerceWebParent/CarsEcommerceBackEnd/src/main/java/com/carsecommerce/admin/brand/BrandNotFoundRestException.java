package com.carsecommerce.admin.brand;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Marka nuk ekziston")
public class BrandNotFoundRestException extends Exception {

}
