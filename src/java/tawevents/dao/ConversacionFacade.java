/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import tawevents.entity.Conversacion;

/**
 *
 * @author David
 */
@Stateless
public class ConversacionFacade extends AbstractFacade<Conversacion> {

    @PersistenceContext(unitName = "TAWEventsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConversacionFacade() {
        super(Conversacion.class);
    }
    
    
    
}
