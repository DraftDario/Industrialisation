package ch.hearc.ig.odi.epicearc.presentation.managedbean;

import ch.hearc.ig.odi.epicearc.sessionbean.UserSession;
import ch.hearc.ig.odi.epicearc.business.Order;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *  This Managedbean is for orderConfirmation.xhtml
 * 
 * @author dario_zakaria
 */
@Named(value = "orderConfirmation")
@RequestScoped
public class orderConfirmationManagedBean {

    @Inject
    UserSession userSession;

    /**
     * Creates a new instance of orderConfirmation
     */
    public orderConfirmationManagedBean() {
    }

    /**
     * Delete the order in userSession
     * @return in SelectProduct
     */
    public String deleteOrder() {
        userSession.setCurrentOrder(new Order());
        return "next"; //Mettre page 1
    }

}
