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
import tawevents.entity.Usuario;

/**
 *
 * @author David
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "TAWEventsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    public Usuario findByNickAndPassword(String nick, String password) {
        Query q;
        List<Usuario> lista;

        q = this.em.createQuery("SELECT a FROM Usuario a WHERE a.nickname = :nick AND a.contrasena = :password");
        q.setParameter("nick", nick);
        q.setParameter("password", password);
        lista = q.getResultList();
        if (lista == null || lista.isEmpty()) {
            return null;
        } else {
            return lista.get(0);
        }
    }

    public Usuario findByNick(String nick) {
        Query q;
        List<Usuario> lista;

        q = this.em.createQuery("SELECT a FROM Usuario a WHERE a.nickname = :nick");
        q.setParameter("nick", nick);
        lista = q.getResultList();
        if (lista == null || lista.isEmpty()) {
            return null;
        } else {
            return lista.get(0);
        }
    }

    public Boolean esNickUnico(String nick) {
        Query q;
        List<Usuario> lista;

        q = this.em.createQuery("SELECT a FROM Usuario a WHERE a.nickname = :nick");
        q.setParameter("nick", nick);
        lista = q.getResultList();

        return lista == null || lista.isEmpty();
    }

    public List<Usuario> findBySimilarNick(String filtro) {
        Query q;

        q = em.createQuery("SELECT c FROM Usuario c WHERE UPPER(c.nickname) LIKE UPPER(:nick)");
        q.setParameter("nick", "%" + filtro + "%");
        return q.getResultList();
    }

    public List<Usuario> findBySimilarContrasena(String filtro) {
        Query q;

        q = em.createQuery("SELECT c FROM Usuario c WHERE UPPER(c.contrasena) LIKE UPPER(:contra)");
        q.setParameter("contra", "%" + filtro + "%");
        return q.getResultList();
    }

    public List<Usuario> findBySimilarTipoUsuario(String filtro) {
        Query q;

        q = em.createQuery("SELECT c FROM Usuario c WHERE UPPER(c.tipoUsuario) LIKE UPPER(:tipo)");
        q.setParameter("tipo", "%" + filtro + "%");
        return q.getResultList();
    }

}
