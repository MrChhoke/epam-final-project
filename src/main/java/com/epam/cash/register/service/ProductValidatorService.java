package com.epam.cash.register.service;

import com.epam.cash.register.entity.Product;

public interface ProductValidatorService {

    boolean isValid(Product product);

}
