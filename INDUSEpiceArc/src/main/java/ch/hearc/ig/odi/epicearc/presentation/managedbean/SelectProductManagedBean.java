
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package ch.hearc.ig.odi.epicearc.presentation.managedbean;

import java.io.Serializable;

import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.inject.Inject;
import javax.inject.Named;

import ch.hearc.ig.odi.epicearc.business.ConiferType;
import ch.hearc.ig.odi.epicearc.business.DeliveryDate;
import ch.hearc.ig.odi.epicearc.business.Order;
import ch.hearc.ig.odi.epicearc.business.PickupDate;
import ch.hearc.ig.odi.epicearc.business.Product;
import ch.hearc.ig.odi.epicearc.service.Services;
import ch.hearc.ig.odi.epicearc.sessionbean.UserSession;

/**
 * This Managedbean is for selectProduct.xhtml
 *
 * This class manages all things link to the selection of the product Depending
 * on what the user Select , it will load the right things in the right moment.
 * I am playing white the null values to choose what to display. This class is
 * also "ConversationScoped" with the life cycle of : Select a tree -- Begin
 * conversation Click on final button -- end conversation Like that I can keep
 * my data between the submits.
 *
 * @author Zakaria_Dario
 */
@ConversationScoped
@Named(value = "selectProduct")
public class SelectProductManagedBean implements Serializable {

    /**
     * Creates a new instance of SelectProductManagedBean
     */
    @Inject
    UserSession                us;
    @Inject
    Services                   se;
    private ConiferType        ct;
    private Product            prod;
    private DeliveryDate       selectDeliveryDate;
    private PickupDate         selectPickUpDate;
    private List<Product>      products;
    private List<DeliveryDate> deliveries;
    private List<PickupDate>   pickups;
    private @Inject
    Conversation               conversation;

    /**
     * Explicit constructor for JSF
     */
    public SelectProductManagedBean() {}

    /**
     * This method allow us to begin a new instance of the conversation if : -
     * It's not a postback AND - the conversation is transient and not long time
     * run
     */
    public void beginConversation() {
        if (FacesContext.getCurrentInstance().isPostback() && conversation.isTransient()) {
            conversation.begin();
        }
    }

    /**
     * That event happened when the user changes the coniferous type. First
     * things first, it begins the conversation, then it sets the coniferous
     * type from the passing value by the event and load the product size list.
     * Finally reset the two other lists and reset the next values to hide the
     * drop-down list in the form if the customers have already made all choices
     * but change his mind.
     *
     * @param e the passing value of the event which contains the selected
     * coniferous
     */
    public void coniferChange(ValueChangeEvent e) {
        beginConversation();
        this.ct  = (ConiferType) e.getNewValue();
        products = se.getProductsForConiferType(ct);
        prod     = null;
        resetDelivries();
        resetPickups();
    }

    /**
     * That event happened when the user changes the selected delivery date. It
     * sets the delivery date from the passing value by the event and load the
     * available pick up dates list.Finally reset the pick up lists if the
     * selected delivery date is null (first choice in the drop-down list) and
     * selected value in order to hide the drop-down list in the form if the
     * customers have already made all choices but change his mind.
     *
     * @param e the passing value of the event which contains the selected
     * delivery date
     */
    public void deliveryChange(ValueChangeEvent e) {
        this.selectDeliveryDate = (DeliveryDate) e.getNewValue();

        if (selectDeliveryDate != null) {
            selectPickUpDate = null;
            pickups          = se.getPickupDatesForProduct(prod);
        } else {
            resetPickups();
        }
    }

    /**
     * Saving method into our UserSession bean. Normally the user should not be
     * able to launch it if he did not select all the previous values. This
     * method, create an order and pass all the selected values within,
     * calculate the final price for next steps and set within the session.
     * Reaching that point means also there is no need to keep the conversation,
     * so we end it here. Finally, we are redirecting to the next step.
     *
     * @return the next pages
     */
    public String nextStep() {
        Order o = new Order();

        o.setDeliveryDate(selectDeliveryDate);
        o.setPickupDate(selectPickUpDate);
        o.setAmount(prod.getPrice() + se.getTransportCosts());
        o.setProduct(prod);
        us.setCurrentOrder(o);
        conversation.end();

        return "customerForm";
    }

    /**
     * That event happened when the user changes the selected pick up date. It
     * sets the pick up from the passing value by the event.
     *
     * @param e the passing value of the event which contains the selected pick
     * up date
     */
    public void pickupChange(ValueChangeEvent e) {
        this.selectPickUpDate = (PickupDate) e.getNewValue();
    }

    // reseting the diplay lists
    public void resetDelivries() {
        deliveries         = null;
        selectDeliveryDate = null;
    }

    public void resetPickups() {
        pickups          = null;
        selectPickUpDate = null;
    }

    /**
     * That event happened when the user changes the selected size. It sets the
     * coniferous size from the passing value by the event and load the
     * available delivery dates list.Finally reset the delivery lists if the
     * selected product is null (first choice in the drop-down list), in any
     * case the pickups list and selected value in order to hide the drop-down
     * list in the form if the customers have already made all choices but
     * change his mind.
     *
     * @param e the passing value of the event which contains the selected
     * coniferous size
     */
    public void sizeChange(ValueChangeEvent e) {
        this.prod = (Product) e.getNewValue();

        if (prod != null) {
            selectDeliveryDate = null;
            deliveries         = se.getDeliveryDatesForProduct(prod);
        } else {
            resetDelivries();
        }

        resetPickups();
    }

    public ConiferType getCt() {
        return ct;
    }

    public void setCt(ConiferType ct) {
        this.ct = ct;
    }

    public List<DeliveryDate> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<DeliveryDate> deliveries) {
        this.deliveries = deliveries;
    }

    /**
     * Get the enum coniferous type.
     *
     * @return An array of avaible coniferous type
     */
    public ConiferType[] getEnumerateType() {
        return (ConiferType[]) se.getConiferTypes().toArray();
    }

    public List<PickupDate> getPickups() {
        return pickups;
    }

    public void setPickups(List<PickupDate> pickups) {
        this.pickups = pickups;
    }

    public Product getProd() {
        return prod;
    }

    public void setProd(Product prod) {
        this.prod = prod;
    }

    // From here , there is only the getter and setter of the values
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Services getSe() {
        return se;
    }

    public void setSe(Services se) {
        this.se = se;
    }

    public DeliveryDate getSelectDeliveryDate() {
        return selectDeliveryDate;
    }

    public void setSelectDeliveryDate(DeliveryDate selectDeliveryDate) {
        this.selectDeliveryDate = selectDeliveryDate;
    }

    public PickupDate getSelectPickUpDate() {
        return selectPickUpDate;
    }

    public void setSelectPickUpDate(PickupDate selectPickUpDate) {
        this.selectPickUpDate = selectPickUpDate;
    }

    public UserSession getUs() {
        return us;
    }

    public void setUs(UserSession us) {
        this.us = us;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
