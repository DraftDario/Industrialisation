/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.ig.odi.epicearc.presentation.converter;

import ch.hearc.ig.odi.epicearc.business.Product;
import ch.hearc.ig.odi.epicearc.service.Services;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Custom converter for the Product Drop-down list.
 *
 * This class is resquestScoped since there is no need to keep it alive more
 * than that
 *
 * @author zakariae_dario
 */
@RequestScoped
@Named(value = "productconverter")
public class ProductLOVConverter implements Converter {

    @Inject
    Services se;

    /**
     * Receive the id of product and use the service class to get the matched
     * Product
     *
     * @param context a FacesContext
     * @param component an UIComponent
     * @param value the ID of the product
     * @return the targeted product
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Object r = null;
        if (!value.equals("")) {
            r = se.getProductsByID(Long.valueOf(value));
        }
        return r;
    }

    /**
     * Returning the id of the passing product
     *
     * @param context a FacesContext
     * @param component an UIComponent
     * @param value Product
     * @return the product's ID
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = "";
        if (value != null) {
            Product p = (Product) value;
            r = String.valueOf((p.getId()));
        }
        return r;
    }

}
