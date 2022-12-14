package com.epam.cash.register.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Receipt {

    private long id;
    private double totalPrice;
    private Date dateCreation;
    private String receiptCode;
    private User userCreator;
    private boolean isCanceled;
    private boolean isDone;
    private User userCanceler;
    private List<ItemReceipt> items;

    public Receipt() {
    }

    public Receipt(long id,
                   String receiptCode,
                   double totalPrice,
                   Date dateCreation,
                   User userCreator,
                   List<ItemReceipt> items) {
        this.id = id;
        this.receiptCode = receiptCode;
        this.totalPrice = totalPrice;
        this.dateCreation = dateCreation;
        this.userCreator = userCreator;
        this.items = items;
    }

    public Receipt(long id,
                   String receiptCode,
                   double totalPrice,
                   Date dateCreation,
                   boolean isDone,
                   User userCreator,
                   List<ItemReceipt> items) {
        this.id = id;
        this.receiptCode = receiptCode;
        this.totalPrice = totalPrice;
        this.isDone = isDone;
        this.dateCreation = dateCreation;
        this.userCreator = userCreator;
        this.items = items;
    }

    public Receipt(long id,
                   String receiptCode,
                   double totalPrice,
                   Date dateCreation,
                   User userCreator,
                   boolean isCanceled,
                   User userCanceler,
                   List<ItemReceipt> items) {
        this.id = id;
        this.receiptCode = receiptCode;
        this.totalPrice = totalPrice;
        this.dateCreation = dateCreation;
        this.userCreator = userCreator;
        this.isCanceled = isCanceled;
        this.userCanceler = userCanceler;
        this.items = items;
    }

    public Receipt(String receiptCode,
                   User userCreator,
                   List<ItemReceipt> items) {
        this.receiptCode = receiptCode;
        this.userCreator = userCreator;
        totalPrice = items.stream().
                mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
        dateCreation = new Date();
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

    public void addItemReceipt(ItemReceipt itemReceipt) {
        if (items == null) {
            items = new ArrayList<>(20);
        }
        if (itemReceipt == null) {
            return;
        }
        items.removeIf(item -> item.getProduct().getId() == itemReceipt.getProduct().getId());

        items.add(itemReceipt);

        totalPrice = items.stream().
                mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
    }

    public String getReceiptCode() {
        return receiptCode;
    }

    public void setReceiptCode(String receiptCode) {
        this.receiptCode = receiptCode;
    }

    public void setItems(List<ItemReceipt> items) {
        this.items = items;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receipt receipt = (Receipt) o;

        if (id != receipt.id) return false;
        if (Double.compare(receipt.totalPrice, totalPrice) != 0) return false;
        if (isCanceled != receipt.isCanceled) return false;
        if (isDone != receipt.isDone) return false;
        if (!Objects.equals(dateCreation, receipt.dateCreation))
            return false;
        if (!Objects.equals(receiptCode, receipt.receiptCode)) return false;
        if (!Objects.equals(userCreator, receipt.userCreator)) return false;
        if (!Objects.equals(userCanceler, receipt.userCanceler))
            return false;
        return Objects.equals(items, receipt.items);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        temp = Double.doubleToLongBits(totalPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (dateCreation != null ? dateCreation.hashCode() : 0);
        result = 31 * result + (receiptCode != null ? receiptCode.hashCode() : 0);
        result = 31 * result + (userCreator != null ? userCreator.hashCode() : 0);
        result = 31 * result + (isCanceled ? 1 : 0);
        result = 31 * result + (isDone ? 1 : 0);
        result = 31 * result + (userCanceler != null ? userCanceler.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "id=" + id +
                ", totalPrice=" + totalPrice +
                ", dateCreation=" + dateCreation +
                ", receiptCode='" + receiptCode + '\'' +
                ", userCreator=" + userCreator +
                ", isCanceled=" + isCanceled +
                ", isDone=" + isDone +
                ", userCanceler=" + userCanceler +
                ", items=" + items +
                '}';
    }
}
