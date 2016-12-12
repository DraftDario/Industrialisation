/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.ig.il.test;

import ch.hearc.ig.odi.epicearc.business.Customer;
import ch.hearc.ig.odi.epicearc.service.Services;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dario.mosca
 */
public class Tests {

    private final Services services = new Services();
    private Customer c;

    @Test
    public final void testTransportCosts() {
        Float costs = 30.0f;
        Float f = services.getTransportCosts();

        assertEquals(costs, f);
    }

    @Test
    public final void testValidCustomer() {
        Long id = new Long(1);
        c = new Customer(id, "Dario", "Mosca", "chemin", "1373", "Chavo",
                "1234", "a@mail.ch");
        assertTrue(services.isCustomerValid(c));
    }

}
