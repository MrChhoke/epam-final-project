package com.epam.cash.register.entity;

public class ItemReceipt {

    private long id;
    private Product product;
    private boolean isCanceled;
    private User userCanceler;

    public ItemReceipt(){
    }

    public ItemReceipt(long id,
                       Product product) {
        this.id = id;
        this.product = product;
    }

    public ItemReceipt(long id,
                       Product product,
                       boolean isCanceled,
                       User userCanceler) {
        this.id = id;
        this.product = product;
        this.isCanceled = isCanceled;
        this.userCanceler = userCanceler;
    }
}
