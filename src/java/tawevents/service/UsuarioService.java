/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import tawevents.dao.UsuarioFacade;
import tawevents.dto.UsuarioDTO;
import tawevents.entity.Usuario;

/**
 *
 * @author Ivan
 */
@Stateless
public class UsuarioService {

    @EJB
    private UsuarioFacade usuarioFacade;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public UsuarioDTO comprobarCredenciales(String strNick, String strClave){
        Usuario usuario = usuarioFacade.findByNickAndPassword(strNick, strClave);
        if(usuario != null){
            return usuario.getDTO();
        }else{
            return null;
        }
    }
}
