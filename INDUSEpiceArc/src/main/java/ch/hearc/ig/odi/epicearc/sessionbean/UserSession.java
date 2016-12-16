
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package ch.hearc.ig.odi.epicearc.sessionbean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import javax.inject.Named;

import ch.hearc.ig.odi.epicearc.business.Customer;
import ch.hearc.ig.odi.epicearc.business.Order;

/**
 * This class is our "Session" bean, which means that she's in charge to keep
 * data between our different pages. As it doesn't share the same role then the
 * Managed beans (which for us only manage the pages values and treatment), we
 * made the choice to don't call it "Managedbeans". Other things are that we
 * separate the customer, which can be kept alive for multiple order (same person
 * want to decorate all his city if we wish to), of the "order. customer" ,which
 * is linked with an order and will be wiped out in the same time as the order
 * (when we save the order). Finally, it's sessionScoped to be alive for the whole
 * user session.
 *
 * @author Zakaria_Dario
 */
@SessionScoped
@Named(value = "userSession")
public class UserSession implements Serializable {
    private Customer customer;
    private Order    currentOrder;

    public UserSession() {
        customer     = new Customer();
        currentOrder = new Order();
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
