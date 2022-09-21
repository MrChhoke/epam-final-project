package com.epam.cash.register.entity;

public class ItemReceipt {

    private long id;
    private Product product;
    private boolean isCanceled;
    private long idReceipt;
    private User userCanceler;

    public ItemReceipt(){
    }

    public ItemReceipt(long id,
                       Product product) {
        this.id = id;
        this.product = product;
    }

    public ItemReceipt(long id,
                       long idReceipt,
                       Product product) {
        this.id = id;
        this.idReceipt = idReceipt;
        this.product = product;
    }

    public ItemReceipt(long id,
                       Product product,
                       long idReceipt,
                       boolean isCanceled,
                       User userCanceler) {
        this.id = id;
        this.idReceipt = idReceipt;
        this.product = product;
        this.isCanceled = isCanceled;
        this.userCanceler = userCanceler;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public User getUserCanceler() {
        return userCanceler;
    }

    public void setUserCanceler(User userCanceler) {
        this.userCanceler = userCanceler;
    }

    public long getIdReceipt() {
        return idReceipt;
    }

    public void setIdReceipt(long idReceipt) {
        this.idReceipt = idReceipt;
    }
}
