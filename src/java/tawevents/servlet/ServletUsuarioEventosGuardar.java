/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tawevents.dao.UsuarioDeEventosFacade;
import tawevents.dao.UsuarioFacade;
import tawevents.entity.Usuario;
import tawevents.entity.UsuarioDeEventos;

/**
 *
 * @author Ivan
 */
@WebServlet(name = "ServletUsuarioEventosGuardar", urlPatterns = {"/ServletUsuarioEventosGuardar"})
public class ServletUsuarioEventosGuardar extends HttpServlet {
    @EJB
    private UsuarioFacade UsuarioFacade;
    
    @EJB
    private UsuarioDeEventosFacade UsuarioDeEventosFacade;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario)session.getAttribute("usuario");
        if (usuario == null) {
            request.setAttribute("errorRegistro", "Usuario no autenticado");
            RequestDispatcher rd = request.getRequestDispatcher("inicioSesion.jsp");
            rd.forward(request, response);
        } else {
            String id = "",strErrorNick = "", strErrorCorreo = "", strErrorFormato = "", strErrorConfirmar = "", strTo = "";
            String nick, correoElectronico, contrasena, confirmarContrasena, nombre, apellidos, ciudad, sexo;
            Date fechaNacimiento = null;
            UsuarioDeEventos usuarioEventos;
            Usuario usuarioBase;

            // ----------------------

            // Asignamos valores
            id = request.getParameter("id");
            nick = request.getParameter("nick");
            correoElectronico = request.getParameter("correoElectronico");
            contrasena = request.getParameter("contrasena");
            confirmarContrasena = request.getParameter("confirmarContrasena");
            nombre = request.getParameter("nombre");
            apellidos = request.getParameter("apellidos");
            ciudad = request.getParameter("ciudad");
            sexo = request.getParameter("sexo");
            try {
                fechaNacimiento = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("fechaNacimiento"));
            } catch (ParseException ex) {
                Logger.getLogger(ServletCrearUsuarioDeEventos.class.getName()).log(Level.SEVERE, null, ex);
            }

            // ----------------------

            // ----------------------

            Pattern regexPattern = Pattern.compile("^(.+)@(.+)$");
            Matcher regMatcher   = regexPattern.matcher(correoElectronico);

            Boolean nickUnico = UsuarioFacade.esNickUnico(nick);
            Boolean correoUnico = UsuarioDeEventosFacade.esCorreoUnico(correoElectronico);
            Boolean formatoCorreoValido = regMatcher.matches();
            Boolean coincidenContrasenas = contrasena.equals(confirmarContrasena);
            usuarioBase = this.UsuarioFacade.find(new Integer(id));
            if(fechaNacimiento.after(new Date())){
                request.setAttribute("errorFecha", "Error de edición: fecha no válida");
                RequestDispatcher rd = request.getRequestDispatcher("ServletUsuarioEditar?id=" + usuarioBase.getId());
                 rd.forward(request, response);
            }else{
                if ((!nickUnico && !nick.equals(usuarioBase.getNickname())) || (!correoUnico && !correoElectronico.equals(usuarioBase.getUsuarioDeEventos().getCorreo()))  || !formatoCorreoValido || !coincidenContrasenas) { // Si hay algun error en el formulario
                strTo = "ServletUsuarioEditar?id=" + usuarioBase.getId();
                if (!nickUnico && !nick.equals(usuarioBase.getNickname())) { // Si ya hay un usuario con ese nick
                    strErrorNick = "Error de edición: el nick introducido ya está en uso";
                    request.setAttribute("errorNick", strErrorNick);
                }
                if (!correoUnico && !correoElectronico.equals(usuarioBase.getUsuarioDeEventos().getCorreo())) { // Si ya hay un usuario con ese correo
                    strErrorCorreo = "Error de edición: el correo electrónico elegido ya está en uso";
                    request.setAttribute("errorCorreo", strErrorCorreo);
                }
                if (!formatoCorreoValido) { // Si el correo no tiene un formato valido
                    strErrorFormato = "Error de edición: el correo electrónico debe tener un formato válido";
                    request.setAttribute("errorFormato", strErrorFormato);
                }
                if (!coincidenContrasenas) { // Si la confirmacion de contrasena es diferente a la contrasena
                    strErrorConfirmar = "Error de edición: las contraseñas introducidas no coinciden";
                    request.setAttribute("errorConfirmar", strErrorConfirmar);
                }
                RequestDispatcher rd = request.getRequestDispatcher(strTo);
                rd.forward(request, response);
                
                } else { // Si no hay errores
                    usuarioEventos = usuarioBase.getUsuarioDeEventos();
                    usuarioEventos.setNombre(nombre);
                    usuarioEventos.setApellidos(apellidos);
                    usuarioEventos.setCorreo(correoElectronico);
                    usuarioEventos.setCiudad(ciudad);
                    usuarioEventos.setSexo(sexo);
                    usuarioEventos.setFechaNacimiento(fechaNacimiento);
                    usuarioBase.setNickname(nick);
                    usuarioBase.setContrasena(contrasena);
                    this.UsuarioFacade.edit(usuarioBase);
                    response.sendRedirect("ServletUsuarioListar");
                }
            }
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
