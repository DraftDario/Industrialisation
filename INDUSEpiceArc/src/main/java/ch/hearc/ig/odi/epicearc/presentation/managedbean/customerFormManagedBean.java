package ch.hearc.ig.odi.epicearc.presentation.managedbean;

import ch.hearc.ig.odi.epicearc.sessionbean.UserSession;
import ch.hearc.ig.odi.epicearc.business.Customer;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * This Managedbean is for customerForm.xhtml
 * 
 * @author dario_zakaria
 */
@Named(value = "customerForm")
@RequestScoped
public class customerFormManagedBean {

    @Inject
    UserSession userSession;

    private Customer customer;

    /**
     * Set the current customer if exist for a re-order
     */
    @PostConstruct
    public void init() {
        if (userSession.getCustomer() != null) {
            customer = userSession.getCustomer();
        } else {
            customer = new Customer();
        }
    }

    /**
     * Creates a new instance of orderConfirmation
     */
    public customerFormManagedBean() {
    }

    /**
     * Getter userSession
     *
     * @return userSession
     */
    public UserSession getUserSession() {
        return userSession;
    }

    /**
     * Setter userSession
     *
     * @param userSession parameter to set
     */
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    /**
     * Getter customer
     *
     * @return customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Setter customer
     *
     * @param customer parameter to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Save the customer in the UserSession bean
     *
     * @return the page orderRecap.xhtml
     */
    public String saveCustomer() {
        userSession.setCustomer(customer);
        userSession.getCurrentOrder().setCustomer(customer);
        return "next";
    }

}
