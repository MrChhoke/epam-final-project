package com.epam.cash.register.entity;

import java.util.Date;
import java.util.List;

public class Receipt {

    private long id;
    private double totalPrice;
    private Date dateCreation;
    private User userCreator;
    private boolean isCanceled;
    private User userCanceler;
    private List<ItemReceipt> items;

    public Receipt() {
    }

    public Receipt(long id,
                   double totalPrice,
                   Date dateCreation,
                   User userCreator,
                   List<ItemReceipt> items) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.dateCreation = dateCreation;
        this.userCreator = userCreator;
        this.items = items;
    }

    public Receipt(long id,
                   double totalPrice,
                   Date dateCreation,
                   User userCreator,
                   boolean isCanceled,
                   User userCanceler,
                   List<ItemReceipt> items) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.dateCreation = dateCreation;
        this.userCreator = userCreator;
        this.isCanceled = isCanceled;
        this.userCanceler = userCanceler;
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
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

    public List<ItemReceipt> getItems() {
        return items;
    }

    public void addItemReceipt(ItemReceipt itemReceipt){
        items.add(itemReceipt);
    }

    public void setItems(List<ItemReceipt> items) {
        this.items = items;
    }
}
