package ch.hearc.ig.odi.epicearc.presentation.managedbean;

import java.io.Serializable;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;

import javax.inject.Inject;
import javax.inject.Named;

import ch.hearc.ig.odi.epicearc.exception.IncorrectStateException;
import ch.hearc.ig.odi.epicearc.service.Services;
import ch.hearc.ig.odi.epicearc.sessionbean.UserSession;

/**
 * This Managedbean is for orderRecap.xhtml
 *
 * @author dario_zakaria
 */
@Named(value = "orderRecap")
@RequestScoped
public class orderRecapManagedBean implements Serializable {
    private static final Logger logger = Logger.getLogger(orderRecapManagedBean.class.getName());
    @Inject
    UserSession                 userSession;
    @Inject
    Services                    services;

    /**
     * Creates a new instance of orderConfirmation
     */
    public orderRecapManagedBean() {}

    /**
     * Save the order to Services
     * @return the page orderConfirmation.xhtml
     */
    public String save() {
        try {
            services.saveOrder(userSession.getCurrentOrder());
        } catch (IncorrectStateException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return "next";
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
