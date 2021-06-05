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
import tawevents.dao.UsuarioDeEventosFacade;
import tawevents.dto.UsuarioDeEventosDTO;
import tawevents.entity.UsuarioDeEventos;

/**
 *
 * @author David
 */
@Stateless
public class UsuarioDeEventosService {
    
    @EJB
    UsuarioDeEventosFacade usuarioDeEventosFacade;
    
    protected List<UsuarioDeEventosDTO> convertirAListaDTO (List<UsuarioDeEventos> lista) {
        if (lista != null) {
            List<UsuarioDeEventosDTO> listaDTO = new ArrayList<UsuarioDeEventosDTO>();
            for (UsuarioDeEventos usuarioDeEventos:lista) {
                listaDTO.add(usuarioDeEventos.getDTO());
            }
            return listaDTO;            
        } else {
            return null;
        }
    }
    
    public UsuarioDeEventos findById(Integer id) {
        return usuarioDeEventosFacade.findById(id);
    }
    
}
