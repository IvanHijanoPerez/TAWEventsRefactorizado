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
import tawevents.entity.Evento;
import tawevents.entity.Publico;
import tawevents.entity.UsuarioDeEventos;

/**
 *
 * @author David
 */
@Stateless
public class PublicoFacade extends AbstractFacade<Publico> {

    @PersistenceContext(unitName = "TAWEventsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PublicoFacade() {
        super(Publico.class);
    }

    public List<Publico> findByUsuarioYEvento(UsuarioDeEventos usuario, Evento evento) {
        Query q;
        q = this.em.createQuery("SELECT p FROM Publico p WHERE p.usuarioDeEventos = :usuario AND p.evento = :evento");
        q.setParameter("usuario", usuario);
        q.setParameter("evento", evento);
        return q.getResultList();
    }

    public Publico findByID(Integer id) {
        Query q;
        q = this.em.createNamedQuery("Publico.findById");
        q.setParameter("id", id);
        return (q.getResultList().isEmpty()) ? null : (Publico)q.getResultList().get(0);
    }
    
    public Publico findByFilaAsientoEvento(Integer fila, Integer asiento, Evento evento) {
        Query q;
        q = this.em.createNamedQuery("SELECT p FROM Publico p WHERE p.evento = :evento AND p.fila = :fila AND p.asiento = :asiento");
        q.setParameter("fila", fila);
        q.setParameter("asiento", asiento);
        q.setParameter("evento", evento);
        return (q.getResultList().isEmpty()) ? null : (Publico)q.getResultList().get(0);
    }
    
}
