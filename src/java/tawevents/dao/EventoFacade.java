/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import tawevents.entity.Etiqueta;
import tawevents.entity.Evento;
import tawevents.entity.UsuarioDeEventos;

/**
 *
 * @author David
 */
@Stateless
public class EventoFacade extends AbstractFacade<Evento> {

    @PersistenceContext(unitName = "TAWEventsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventoFacade() {
        super(Evento.class);
    }
    
    public Evento findById (Integer id) {
        Query q;
        
        q = em.createNamedQuery("Evento.findById");
        q.setParameter("id", id);
        return (Evento)q.getResultList().get(0);
    }
    
    public int findIdMasAlta () {
        Query q;
        
        q = em.createQuery("SELECT max(e.id) FROM Evento e");
        return (int)q.getResultList().get(0);
    }
    
    public List<Evento> findByTitulo (String filtro) {
        Query q;
        
        q = em.createQuery("SELECT c FROM Evento c WHERE UPPER(c.titulo) LIKE UPPER(:tit)");
        q.setParameter("tit", "%" + filtro + "%");
        return q.getResultList();        
    }
    
    public List<Evento> findBySimilarNombre (String filtro) {
        Query q;
        
        q = em.createQuery("SELECT c FROM Evento c WHERE UPPER(c.titulo) LIKE UPPER(:tit)");
        q.setParameter("tit", "%" + filtro + "%");
        return q.getResultList();        
    }

    public List<Evento> findBySimilarDescripcion (String filtro) {
        Query q;
        
        q = em.createQuery("SELECT c FROM Evento c WHERE UPPER(c.descripcion) LIKE UPPER(:tit)");
        q.setParameter("tit", "%" + filtro + "%");
        return q.getResultList();        
    }
    
    public List<Evento> findBySimilarImagen (String filtro) {
        Query q;
        
        q = em.createQuery("SELECT c FROM Evento c WHERE UPPER(c.imagen) LIKE UPPER(:tit)");
        q.setParameter("tit", "%" + filtro + "%");
        return q.getResultList();        
    }
    
    public List<Evento> findBySimilarFecha (Date filtro) {
        Query q;
        q = em.createQuery("SELECT c FROM Evento c WHERE c.fecha = :tit");
        q.setParameter("tit", filtro);
        return q.getResultList();        
    }
    
    public List<Evento> findBySimilarFechaEntrada (Date filtro) {
        Query q;
        q = em.createQuery("SELECT c FROM Evento c WHERE c.fechaLimEntradas = :tit");
        q.setParameter("tit", filtro);
        return q.getResultList();        
    }
    
    public List<Evento> findBySimilarPrecio (String filtro) {
        Query q;
        
        q = em.createQuery("SELECT c FROM Evento c WHERE c.precioEntrada = :tit");
        q.setParameter("tit", new Integer(filtro));
        return q.getResultList();        
    }
    
    public List<Evento> findBySimilarAforo (String filtro) {
        Query q;
        
        q = em.createQuery("SELECT c FROM Evento c WHERE c.aforoMax = :tit");
        q.setParameter("tit", new Integer(filtro));
        return q.getResultList();        
    }
    
    public List<Evento> findBySimilarMaxEntradasUsuario (String filtro) {
        Query q;
        
        q = em.createQuery("SELECT c FROM Evento c WHERE c.maxEntradasPorUsuario = :tit");
        q.setParameter("tit", new Integer(filtro));
        return q.getResultList();        
    }
    
    public List<Evento> findBySimilarAsientosAsignados (String filtro) {
        Query q;
        boolean asignados = filtro.equals("Si");
        
        q = em.createQuery("SELECT c FROM Evento c WHERE c.asientosAsignados = :tit");
        q.setParameter("tit", asignados);
        return q.getResultList();        
    }
    
    public List<Evento> findBySimilarNumFilas (String filtro) {
        Query q;
        
        q = em.createQuery("SELECT c FROM Evento c WHERE c.numFilas = :tit");
        q.setParameter("tit", new Integer(filtro));
        return q.getResultList();        
    }
    
    public List<Evento> findBySimilarAsientosFila (String filtro) {
        Query q;
        
        q = em.createQuery("SELECT c FROM Evento c WHERE c.asientosPorFila = :tit");
        q.setParameter("tit", new Integer(filtro));
        return q.getResultList();        
    }
    
    public List<Evento> findBySimilarCreador (String filtro) {
        Query q;
        q = em.createQuery("SELECT c FROM Evento c WHERE UPPER(c.usuario.nickname) LIKE UPPER(:tit)");
        q.setParameter("tit", "%" + filtro + "%");
        return q.getResultList();        
    }
    
    public List<Evento> findByEtiqueta(Etiqueta etiqueta) {
        Query q;
        q = this.em.createQuery("SELECT e FROM Evento e WHERE :etiqueta MEMBER OF e.etiquetaList");
        q.setParameter("etiqueta", etiqueta);
        return q.getResultList();
    }
    
    public List<Evento> findByDisponiblesEtiqueta(Etiqueta etiqueta) {
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = new Date(System.currentTimeMillis());
        Date date = new Date();
        Query q;
        q = this.em.createQuery("SELECT e FROM Evento e WHERE :date < e.fechaLimEntradas AND size(e.publicoList) < e.aforoMax AND :etiqueta MEMBER OF e.etiquetaList");
        q.setMaxResults(7);
        q.setParameter("date",date,TemporalType.DATE);
        q.setParameter("etiqueta", etiqueta);
        return q.getResultList();
    }
    
    public List<Evento> findByDisponiblesMasPopulares() {
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = new Date(System.currentTimeMillis());
        Date date = new Date();
        Query q;
        q = this.em.createQuery("SELECT e FROM Evento e WHERE :date < e.fechaLimEntradas AND size(e.publicoList) < e.aforoMax ORDER BY size(e.publicoList) DESC");
        q.setMaxResults(7);
        q.setParameter("date",date,TemporalType.DATE);
        return q.getResultList();
    }
    
    public List<Evento> findByDisponiblesMasCercanos() {
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = new Date(System.currentTimeMillis());
        Date date = new Date();
        Query q;
        q = this.em.createQuery("SELECT e FROM Evento e WHERE :date < e.fechaLimEntradas AND size(e.publicoList) < e.aforoMax ORDER BY e.fechaLimEntradas ASC");
        q.setMaxResults(7);
        q.setParameter("date",date,TemporalType.DATE);
        return q.getResultList();
    }
    
    // ======= FILTROS EXPLORAR =========
    
    public List<Evento> findByTituloHistorial(String filtro, UsuarioDeEventos user) {
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = new Date(System.currentTimeMillis());
        Date date = new Date();
        Query q;       
        q = em.createQuery("SELECT e FROM Evento e WHERE UPPER(e.titulo) LIKE UPPER(:tit) AND :date > e.fechaLimEntradas AND :user IN (SELECT p.usuarioDeEventos.id FROM Publico p WHERE p.evento = e)");
        q.setParameter("tit", "%" + filtro + "%");
        q.setParameter("user", user.getId());
        q.setParameter("date",date,TemporalType.DATE);
        return q.getResultList();        
    }
    
    public List<Evento> findByTituloReserva(String filtro, UsuarioDeEventos user) {
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = new Date(System.currentTimeMillis());
        Date date = new Date();
        Query q;       
        q = em.createQuery("SELECT e FROM Evento e WHERE UPPER(e.titulo) LIKE UPPER(:tit) AND :date < e.fechaLimEntradas AND :user IN (SELECT p.usuarioDeEventos.id FROM Publico p WHERE p.evento = e)");
        q.setParameter("tit", "%" + filtro + "%");
        q.setParameter("user", user.getId());
        q.setParameter("date",date,TemporalType.DATE);
        return q.getResultList();        
    }
    
    public List<Evento> findByEtiquetaHistorial(Etiqueta etiqueta, UsuarioDeEventos user) {
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = new Date(System.currentTimeMillis());
        Date date = new Date();
        Query q;
        q = this.em.createQuery("SELECT e FROM Evento e WHERE :etiqueta MEMBER OF e.etiquetaList AND :date > e.fechaLimEntradas AND :user IN (SELECT p.usuarioDeEventos.id FROM Publico p WHERE p.evento = e)");
        q.setParameter("etiqueta", etiqueta);
        q.setParameter("user", user.getId());
        q.setParameter("date",date,TemporalType.DATE);
        return q.getResultList();
    }
    
    public List<Evento> findByEtiquetaReserva(Etiqueta etiqueta, UsuarioDeEventos user) {
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = new Date(System.currentTimeMillis());
        Date date = new Date();
        Query q;
        q = this.em.createQuery("SELECT e FROM Evento e WHERE :etiqueta MEMBER OF e.etiquetaList AND :date < e.fechaLimEntradas AND :user IN (SELECT p.usuarioDeEventos.id FROM Publico p WHERE p.evento = e)");
        q.setParameter("etiqueta", etiqueta);
        q.setParameter("user", user.getId());
        q.setParameter("date",date,TemporalType.DATE);
        return q.getResultList();
    }
    
    public List<Evento> findAllHistorial(UsuarioDeEventos user) {
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = new Date(System.currentTimeMillis());
        Date date = new Date();
        Query q;
        q = this.em.createQuery("SELECT e FROM Evento e WHERE :date > e.fechaLimEntradas AND :user IN (SELECT p.usuarioDeEventos.id FROM Publico p WHERE p.evento = e)");
        q.setParameter("user", user.getId());
        q.setParameter("date",date,TemporalType.DATE);
        return q.getResultList();
    }
    
    public List<Evento> findAllReserva(UsuarioDeEventos user) {
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = new Date(System.currentTimeMillis());
        Date date = new Date();
        Query q;
        q = this.em.createQuery("SELECT e FROM Evento e WHERE :date < e.fechaLimEntradas AND :user IN (SELECT p.usuarioDeEventos.id FROM Publico p WHERE p.evento = e)");
        q.setParameter("user", user.getId());
        q.setParameter("date",date,TemporalType.DATE);
        return q.getResultList();
    }
}
