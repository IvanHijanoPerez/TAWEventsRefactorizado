/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import tawevents.dao.UsuarioDeEventosFacade;
import tawevents.dao.UsuarioFacade;
import tawevents.dto.UsuarioDTO;
import tawevents.dto.UsuarioDeEventosDTO;
import tawevents.entity.Usuario;
import tawevents.entity.UsuarioDeEventos;

/**
 *
 * @author Ivan
 */
@Stateless
public class UsuarioService {

    @EJB
    private UsuarioFacade usuarioFacade;
    
    @EJB
    private UsuarioDeEventosFacade usuarioDeEventosFacade;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public UsuarioDTO comprobarCredenciales(String strNick, String strClave) {
        Usuario usuario = usuarioFacade.findByNickAndPassword(strNick, strClave);
        if (usuario != null) {
            return usuario.getDTO();
        } else {
            return null;
        }
    }

    public Usuario comprobarCredencialesUsuario(String strNick, String strClave) {
        Usuario usuario = usuarioFacade.findByNickAndPassword(strNick, strClave);
        if (usuario != null) {
            return usuario;
        } else {
            return null;
        }
    }

    protected List<UsuarioDTO> convertirAListaDTO(List<Usuario> lista) {
        if (lista != null) {
            List<UsuarioDTO> listaDTO = new ArrayList<UsuarioDTO>();
            for (Usuario usuario : lista) {
                listaDTO.add(usuario.getDTO());
            }
            return listaDTO;
        } else {
            return null;
        }
    }

    public Boolean esNickUnico(String nick) {
        return usuarioFacade.esNickUnico(nick);
    }

    public List<UsuarioDTO> filtroNick(String nick) {
        List<Usuario> usuarios = usuarioFacade.findBySimilarNick(nick);
        return convertirAListaDTO(usuarios);
    }

    public List<UsuarioDTO> filtroContrasena(String contra) {
        List<Usuario> usuarios = usuarioFacade.findBySimilarContrasena(contra);
        return convertirAListaDTO(usuarios);
    }

    public List<UsuarioDTO> filtroTipoUsuario(String tipo) {
        List<Usuario> usuarios = usuarioFacade.findBySimilarTipoUsuario(tipo);
        return convertirAListaDTO(usuarios);
    }

    public List<UsuarioDTO> TodosUsuarios() {
        List<Usuario> usuarios = usuarioFacade.findAll();
        return convertirAListaDTO(usuarios);
    }

    public UsuarioDTO buscarUsuario(Integer id) {
        Usuario usuario = this.usuarioFacade.find(id);
        if (usuario != null) {
            return usuario.getDTO();
        } else {
            return null;
        }
    }

    public UsuarioDTO buscarUsuarioNick(String nick) {
        Usuario usuario = this.usuarioFacade.findByNick(nick);
        if (usuario != null) {
            return usuario.getDTO();
        } else {
            return null;
        }
    }

    public UsuarioDeEventosDTO getUsuarioDeEventos(UsuarioDTO user) {
        Usuario usuario = usuarioFacade.find(user.getId());
        return usuario.getUsuarioDeEventos().getDTO();
    }

    public void borrarUsuario(Integer id) {
        Usuario usuario = this.usuarioFacade.find(id);
        this.usuarioFacade.remove(usuario);
    }

    public void edit(UsuarioDTO e) {
        usuarioFacade.edit(usuarioFacade.find(e.getId()));
    }

    public void guardarUsuario(String id, String nickname, String contrasena,
            String tipoUsuario) {
        Usuario usuario;

        if (id == null || id.isEmpty()) { // Crear nuevo cliente
            usuario = new Usuario();
        } else { // Editar cliente existente
            usuario = this.usuarioFacade.find(new Integer(id));
        }
        usuario.setNickname(nickname);
        usuario.setContrasena(contrasena);
        usuario.setTipoUsuario(tipoUsuario);

        if (id == null || id.isEmpty()) { // Crear nuevo cliente        
            this.usuarioFacade.create(usuario);
        } else { // Editar cliente existente
            this.usuarioFacade.edit(usuario);
        }
    }

    public void guardarUsuario(String id, String nickname, String contrasena,
            String tipoUsuario, String nombre, String apellidos, String correoElectronico, String ciudad, String sexo, Date fechaNacimiento) {

        UsuarioDeEventos usuarioEventos = new UsuarioDeEventos();
        usuarioEventos.setNombre(nombre);
        usuarioEventos.setApellidos(apellidos);
        usuarioEventos.setCorreo(correoElectronico);
        usuarioEventos.setCiudad(ciudad);
        usuarioEventos.setSexo(sexo);
        usuarioEventos.setFechaNacimiento(fechaNacimiento);
        usuarioDeEventosFacade.create(usuarioEventos);
        
        Usuario usuario;

        if (id == null || id.isEmpty()) { // Crear nuevo cliente
            usuario = new Usuario();
        } else { // Editar cliente existente
            usuario = this.usuarioFacade.find(new Integer(id));
        }
        usuario.setNickname(nickname);
        usuario.setContrasena(contrasena);
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setUsuarioDeEventos(usuarioEventos);
        
        

        if (id == null || id.isEmpty()) { // Crear nuevo cliente        
            this.usuarioFacade.create(usuario);
        } else { // Editar cliente existente
            this.usuarioFacade.edit(usuario);
        }
    }
}
