package com.epam.cash.register.entity;

import java.util.Date;
import java.util.Objects;

public class Product {

    private long id;
    private String code;
    private String title_ukr;
    private String title_eng;
    private long quantity;
    private double price;
    private Date dateCreation;
    private User userCreator;

    public Product(){}

    public Product(long id,
                   String code,
                   String title_ukr,
                   String title_eng,
                   long quantity,
                   double price) {
        this.id = id;
        this.code = code;
        this.title_ukr = title_ukr;
        this.title_eng = title_eng;
        this.quantity = quantity;
        this.price = price;
    }

    public Product(long id,
                   String code,
                   String title_ukr,
                   String title_eng,
                   long quantity,
                   double price,
                   Date dateCreation,
                   User userCreator) {
        this.id = id;
        this.code = code;
        this.title_ukr = title_ukr;
        this.title_eng = title_eng;
        this.quantity = quantity;
        this.price = price;
        this.dateCreation = dateCreation;
        this.userCreator = userCreator;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle_ukr() {
        return title_ukr;
    }

    public void setTitle_ukr(String title_ukr) {
        this.title_ukr = title_ukr;
    }

    public String getTitle_eng() {
        return title_eng;
    }

    public void setTitle_eng(String title_eng) {
        this.title_eng = title_eng;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public User getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(User userCreator) {
        this.userCreator = userCreator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != product.id) return false;
        if (quantity != product.quantity) return false;
        if (Double.compare(product.price, price) != 0) return false;
        if (!Objects.equals(code, product.code)) return false;
        if (!Objects.equals(title_ukr, product.title_ukr)) return false;
        return Objects.equals(title_eng, product.title_eng);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", title_ukr='" + title_ukr + '\'' +
                ", title_eng='" + title_eng + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", dateCreation=" + dateCreation +
                ", userCreator=" + userCreator +
                '}';
    }
}
