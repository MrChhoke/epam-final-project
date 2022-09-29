package com.epam.cash.register.service;

import com.epam.cash.register.entity.Product;
import com.epam.cash.register.entity.User;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface ProductService {
    List<Product> findAll() throws IOException;

    Product findById(long id) throws IOException;

    List<Product> findById(long... id) throws IOException;

    List<Product> findById(List<Long> id) throws IOException;

    Product findByCode(String code) throws IOException;

    Product findByUkrainianTitle(String ukrainianTitle) throws IOException;

    Product findByEnglishTitle(String englishTitle) throws IOException;

    List<Product> findAllOutStock() throws IOException;

    List<Product> findAllBetweenDate(Date from, Date to) throws IOException;

    List<Product> findAllBetweenPrice(double from, double to) throws IOException;

    List<Product> findAllAddedByUser(User user) throws IOException;

    List<Product> findAllAddedByUser(String username) throws IOException;

    void insert(Product product) throws IOException;

    void update(Product product) throws IOException;

    void delete(long product_id) throws IOException;

    void delete(String code) throws IOException;
}
