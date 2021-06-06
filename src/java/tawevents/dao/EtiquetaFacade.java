/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import tawevents.entity.Etiqueta;
import tawevents.entity.Evento;

/**
 *
 * @author David
 */
@Stateless
public class EtiquetaFacade extends AbstractFacade<Etiqueta> {

    @PersistenceContext(unitName = "TAWEventsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtiquetaFacade() {
        super(Etiqueta.class);
    }
    
    public Etiqueta findById(Integer id) {
        Query q;
        
        q = em.createNamedQuery("Etiqueta.findById");
        q.setParameter("id", id);
        return (q.getResultList().isEmpty()) ? null : (Etiqueta)q.getResultList().get(0);
    }
    
    public Etiqueta findByNombre(String filtro) {
        Query q;
        
        q = em.createNamedQuery("Etiqueta.findByNombre");
        q.setParameter("nombre", filtro);
        return (q.getResultList().isEmpty()) ? null : (Etiqueta)q.getResultList().get(0);
    }
    
    public Etiqueta findBySimilarNombre (String filtro) {
        Query q;  
        q = em.createQuery("SELECT e FROM Etiqueta e WHERE UPPER(e.nombre) LIKE UPPER(:tit)");
        q.setParameter("tit", "%" + filtro + "%");
    
        return (q.getResultList().isEmpty()) ? null : (Etiqueta)q.getResultList().get(0);        
    }
    
    public Etiqueta findBySimilarNombreI (String filtro) {
        Query q;  
        q = em.createQuery("SELECT e FROM Etiqueta e WHERE UPPER(e.nombre) = UPPER(:tit)");
        q.setParameter("tit",filtro);
        if(q.getResultList() == null || q.getResultList().isEmpty()){
            return null;
        } else {
            return (Etiqueta)q.getResultList().get(0);
        }
    }
    
    public Etiqueta findByNombreExacto (String filtro) {
        Query q;  
        q = em.createQuery("SELECT e FROM Etiqueta e WHERE UPPER(e.nombre) = UPPER(:tit)");
        q.setParameter("tit", filtro);
    
        if(q.getResultList() == null || q.getResultList().isEmpty()){
            return null;
        } else {
            return (Etiqueta)q.getResultList().get(0);
        }  
    }
}
