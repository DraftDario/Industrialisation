package ch.hearc.ig.odi.epicearc.business;

import java.io.Serializable;

import java.util.Date;

public class Order implements Serializable {
    private Long         id;
    private Date         date;
    private Float        amount;
    private DeliveryDate deliveryDate;
    private PickupDate   pickupDate;
    private Customer     customer;
    private Product      product;

    public Order() {
        product = new Product();

        Customer customer = new Customer();
    }

    public Order(Long id, Date date, Float price, DeliveryDate deliveryDate, PickupDate pickupDate, Product product,
                 Customer customer) {
        this.id           = id;
        this.date         = date;
        this.amount       = price;
        this.deliveryDate = deliveryDate;
        this.pickupDate   = pickupDate;
        this.product      = product;
        this.customer     = customer;
    }

    public Float getAmount() {
        return this.amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public DeliveryDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(DeliveryDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PickupDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(PickupDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
