
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package ch.hearc.ig.odi.epicearc.presentation.converter;

import javax.enterprise.context.RequestScoped;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import javax.inject.Inject;
import javax.inject.Named;

import ch.hearc.ig.odi.epicearc.business.AbstractDate;
import ch.hearc.ig.odi.epicearc.service.Services;

/**
 * Custom converter for the Deliveries and Pick ups Drop-down list. Since the both
 * of them are AbstractDate , we can use only one class for the both of them.
 *
 * This class is resquestScoped since there is no need to keep it alive more
 * than that
 *
 * @author zakariae_dario
 */
@RequestScoped
@Named(value = "abstracdateconverter")
public class AbstracDateLOVConverter implements Converter {
    @Inject
    Services se;

    /**
     * Receive the id of an abstract date and use the service class to get the
     * matched AbstractDate
     *
     * @param context a FacesContext
     * @param component an UiComponent
     * @param value the ID of  the abstract date
     * @return the targeted abstract date
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Object r = null;

        if (!value.equals("")) {
            r = se.getAbstractDateWithId(Long.valueOf(value));
        }

        return r;
    }

    /**
     * Returning the id of the passsing abstract date
     *
     * @param context a FacesContext
     * @param component an UIComponent
     * @param value Abstract date
     * @return the abstract date's ID
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = "";

        if (value != null) {
            AbstractDate d = (AbstractDate) value;

            r = String.valueOf((d.getId()));
        }

        return r;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
