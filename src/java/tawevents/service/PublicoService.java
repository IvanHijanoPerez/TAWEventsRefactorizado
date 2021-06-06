/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.service;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import tawevents.dao.PublicoFacade;
import tawevents.dto.PublicoDTO;
import tawevents.entity.Evento;
import tawevents.entity.Publico;
import tawevents.entity.UsuarioDeEventos;

/**
 *
 * @author David
 */
@Stateless
public class PublicoService {

    @EJB
    PublicoFacade publicoFacade;

    protected List<PublicoDTO> convertirAListaDTO(List<Publico> lista) {
        if (lista != null) {
            List<PublicoDTO> listaDTO = new ArrayList<>();
            for (Publico publico : lista) {
                listaDTO.add(publico.getDTO());
            }
            return listaDTO;
        } else {
            return null;
        }
    }
    
    public Publico findByID(Integer id) {
        return publicoFacade.findByID(id);
    }

    public List<Publico> findByUsuarioYEvento(UsuarioDeEventos usuarioDeEventos, Evento evento) {
        List<Publico> publicos = publicoFacade.findByUsuarioYEvento(usuarioDeEventos, evento);
        return publicos;
    }

    public void createAsientosAsignados(Publico publico, Evento evento, UsuarioDeEventos usuarioDeEventos, int fila, int asiento) {
        publico.setEvento(evento);
        publico.setUsuarioDeEventos(usuarioDeEventos);
        publico.setFila(fila);
        publico.setAsiento(asiento);
        publicoFacade.create(publico);
    }

    public void createSinAsientos(Publico publico, Evento evento, UsuarioDeEventos usuarioDeEventos) {
        publico.setEvento(evento);
        publico.setUsuarioDeEventos(usuarioDeEventos);
        publicoFacade.create(publico);
    }
    
    public void borrar(Publico publico) {
        publicoFacade.remove(publico);
    }
}
