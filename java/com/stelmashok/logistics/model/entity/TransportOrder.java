package com.stelmashok.logistics.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class TransportOrder extends AbstractEntity {
    User user;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    Product product;
    Unloading unloading;
    Carrier carrier;

    public User getUser() {
        return user;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public Product getProduct() {
        return product;
    }

    public Unloading getUnloading() {
        return unloading;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setUnloading(Unloading unloading) {
        this.unloading = unloading;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TransportOrder that = (TransportOrder) o;
        return Objects.equals(user, that.user) && Objects.equals(orderDate, that.orderDate) && Objects.equals(deliveryDate, that.deliveryDate) && Objects.equals(product, that.product) && Objects.equals(unloading, that.unloading) && Objects.equals(carrier, that.carrier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, orderDate, deliveryDate, product, unloading, carrier);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TransportOrder{");
        sb.append("user=").append(user);
        sb.append(", orderDate=").append(orderDate);
        sb.append(", deliveryDate=").append(deliveryDate);
        sb.append(", product=").append(product);
        sb.append(", unloading=").append(unloading);
        sb.append(", carrier=").append(carrier);
        sb.append('}');
        return sb.toString();
    }
}
