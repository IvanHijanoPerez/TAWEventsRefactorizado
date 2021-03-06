/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.servlet;

import java.io.IOException;
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
import tawevents.service.UsuarioDeEventosService;
import tawevents.service.UsuarioService;

/**
 *
 * @author david
 */
@WebServlet(name = "ServletCrearUsuarioDeEventos", urlPatterns = {"/ServletCrearUsuarioDeEventos"})
public class ServletCrearUsuarioDeEventos extends HttpServlet {
    
    @EJB
    private UsuarioService usuarioService;
    
    @EJB
    private UsuarioDeEventosService usuarioDeEventosService;

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
        
        // Declaramos variables
        
        HttpSession session = request.getSession();
        Date currentDate = new Date();

        String strErrorNick = "", strErrorCorreo = "", strErrorFormato = "", strErrorConfirmar = "", strErrorNacimiento = "";
        String nick, correoElectronico, contrasena, confirmarContrasena, nombre, apellidos, ciudad, sexo;
        Date fechaNacimiento = null;
        
        // ----------------------
        
        // Asignamos valores
        
        nick = new String(request.getParameter("nick").getBytes("ISO-8859-1"), "UTF-8");
        correoElectronico = request.getParameter("correoElectronico");
        contrasena = request.getParameter("contrasena");
        confirmarContrasena = request.getParameter("confirmarContrasena");
        nombre = new String(request.getParameter("nombre").getBytes("ISO-8859-1"), "UTF-8");
        apellidos = new String(request.getParameter("apellidos").getBytes("ISO-8859-1"), "UTF-8");
        ciudad = new String(request.getParameter("ciudad").getBytes("ISO-8859-1"), "UTF-8");
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
        
        Boolean nickUnico = usuarioService.esNickUnico(nick);
        Boolean correoUnico = usuarioDeEventosService.esCorreoUnico(correoElectronico);
        Boolean formatoCorreoValido = regMatcher.matches();
        Boolean coincidenContrasenas = contrasena.equals(confirmarContrasena);
        Boolean fechaNacimientoCorrecta = currentDate.after(fechaNacimiento);
        
        if (!nickUnico || !correoUnico || !formatoCorreoValido || !coincidenContrasenas || !fechaNacimientoCorrecta) { // Si hay algun error en el formulario
            if (!nickUnico) { // Si ya hay un usuario con ese nick
                strErrorNick = "El nick introducido ya est?? en uso";
                request.setAttribute("errorNick", strErrorNick);
            }
            if (!correoUnico) { // Si ya hay un usuario con ese correo
                strErrorCorreo = "El correo electr??nico elegido ya est?? en uso";
                request.setAttribute("errorCorreo", strErrorCorreo);
            }
            if (!formatoCorreoValido) { // Si el correo no tiene un formato valido
                strErrorFormato = "El correo electr??nico debe tener un formato v??lido";
                request.setAttribute("errorFormato", strErrorFormato);
            }
            if (!coincidenContrasenas) { // Si la confirmacion de contrasena es diferente a la contrasena
                strErrorConfirmar = "La contrase??as introducidas no coinciden";
                request.setAttribute("errorConfirmar", strErrorConfirmar);
            }
            if (!fechaNacimientoCorrecta) {
                strErrorNacimiento = "Fecha de nacimiento inv??lida";
                request.setAttribute("errorNacimiento", strErrorNacimiento);
            }
            
            RequestDispatcher rd = request.getRequestDispatcher("crearEditarUsuarioDeEventos.jsp");
            rd.forward(request, response);
            
        } else { // Si no hay errores
                        
            usuarioService.guardarUsuario(null, nick, contrasena, "usuariodeeventos", nombre, apellidos, correoElectronico, ciudad, sexo, fechaNacimiento);
            
            response.sendRedirect("inicioSesion.jsp");
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
