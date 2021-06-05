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
import tawevents.entity.UsuarioDeEventos;

/**
 *
 * @author David
 */
@Stateless
public class UsuarioDeEventosFacade extends AbstractFacade<UsuarioDeEventos> {

    @PersistenceContext(unitName = "TAWEventsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioDeEventosFacade() {
        super(UsuarioDeEventos.class);
    }
    
    public Boolean esCorreoUnico(String correo) {
        Query q;
        List<UsuarioDeEventos> lista;

        q = this.em.createQuery("SELECT a FROM UsuarioDeEventos a WHERE a.correo = :correo");
        q.setParameter("correo", correo);
        lista = q.getResultList();

        return lista == null || lista.isEmpty();
    }
    
    public UsuarioDeEventos findById(Integer id) {
        Query q;
        List<UsuarioDeEventos> lista;

        q = this.em.createQuery("SELECT u FROM UsuarioDeEventos u WHERE u.id = :id");
        q.setParameter("id", id);
        lista = q.getResultList();
        
        return lista.get(0);
    }
}
