package com.epam.cash.register.service;

import com.epam.cash.register.entity.Product;

// TODO FINISH THIS CLASS
public class ProductValidatorServiceImpl implements ProductValidatorService{
    @Override
    public boolean isValid(Product product) {
        if(product.getTitle_ukr().length() < 3 || !product.getTitle_ukr().matches("[\\w]{3,}")){
            return false;
        }

        if(product.getTitle_eng().length() < 3 || !product.getTitle_eng().matches("[\\w]{3,}")){
            return false;
        }

        return product.getQuantity() >= 0 && !(product.getPrice() < 0);
    }
}
