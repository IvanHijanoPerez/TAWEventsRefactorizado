package tawevents.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import tawevents.entity.Estudio;
import tawevents.entity.Evento;
import tawevents.entity.Usuario;

/**
 *
 * @author Iván Hijano Pérez
 */
@Stateless
public class EstudioFacade extends AbstractFacade<Estudio> {

    @PersistenceContext(unitName = "TAWEventsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstudioFacade() {
        super(Estudio.class);
    }

    public List<Estudio> findByDesdeYHasta(int id, Date desdeFecha, Date hastaFecha) {
        Query q;
        q = this.em.createQuery("SELECT e FROM Estudio e WHERE e.analista.id = :idUsuario and e.fecha BETWEEN :desdeFecha AND :hastaFecha");
        q.setParameter("idUsuario", id);
        q.setParameter("desdeFecha", desdeFecha);
        q.setParameter("hastaFecha", hastaFecha);
        return q.getResultList();
    }

    public List<Estudio> findByDesdeYHastaOrdenFechas(Integer id, Date desdeFecha, Date hastaFecha) {
        Query q;
        q = this.em.createQuery("SELECT e FROM Estudio e WHERE e.analista.id = :idUsuario and e.fecha BETWEEN :desdeFecha AND :hastaFecha order by e.fecha desc");
        q.setParameter("idUsuario", id);
        q.setParameter("desdeFecha", desdeFecha);
        q.setParameter("hastaFecha", hastaFecha);
        return q.getResultList();
    }

    public List<Usuario> filtrarUsuarios(List<String> datos) {
        Query q;
        q = this.em.createQuery("SELECT u FROM Usuario u"
                + "    LEFT JOIN u.usuarioDeEventos ude"
                + "    WHERE u.tipoUsuario LIKE :tipousuario"
                + "    AND (ude IS NULL)"
                + "    OR (ude.nombre LIKE :nombre"
                + "    AND ude.apellidos LIKE :apellidos"
                + "    AND ude.ciudad LIKE :ciudad"
                + "    AND ude.sexo LIKE :sexo)");

        q.setParameter("tipousuario", datos.get(14).isEmpty() ? "%" : "%" + datos.get(14) + "%");
        q.setParameter("nombre", datos.get(12).isEmpty() ? "%" : "%" + datos.get(12) + "%");
        q.setParameter("apellidos", datos.get(13).isEmpty() ? "%" : "%" + datos.get(13) + "%");
        q.setParameter("ciudad", datos.get(9).isEmpty() ? "%" : "%" + datos.get(9) + "%");
        q.setParameter("sexo", datos.get(8).isEmpty() ? "%" : "%" + datos.get(8) + "%");
        return q.getResultList();
    }

    public List<Evento> filtrarEventos(String titulo, Date desdefecha,
            Date hastafecha, String aforomin, String aforomax) {
        Query q;

        q = this.em.createQuery("SELECT eve FROM Evento eve "
                + "    WHERE eve.titulo LIKE :titulo"
                + "    AND eve.fecha BETWEEN :desdefecha and :hastafecha"
                + "    AND eve.aforoMax BETWEEN :aforomin and :aforomax");

        q.setParameter("titulo", titulo.isEmpty() ? "%" : "%" + titulo + "%");
        q.setParameter("desdefecha", desdefecha);
        q.setParameter("hastafecha", hastafecha);
        q.setParameter("aforomin", aforomin.isEmpty() ? 0 : new Integer(aforomin));
        q.setParameter("aforomax", aforomax.isEmpty() ? Integer.MAX_VALUE : new Integer(aforomax));

        return q.getResultList();
    }

    public Set<String> findAllCiudades() {
        Query q;
        Set<String> listaSinRepetidos = new HashSet<>();
        q = this.em.createQuery("Select ude.ciudad from UsuarioDeEventos ude");
        List<String> listaConRepetidos = q.getResultList();
        if (listaConRepetidos == null) {
            return listaSinRepetidos;
        } else {
            listaSinRepetidos.addAll(listaConRepetidos);
            return listaSinRepetidos;
        }
    }
}
