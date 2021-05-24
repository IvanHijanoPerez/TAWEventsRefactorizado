/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tawevents.dao.UsuarioFacade;
import tawevents.entity.Usuario;

/**
 *
 * @author Ivan
 */
@WebServlet(name = "ServletInicioAutenticar", urlPatterns = {"/ServletInicioAutenticar"})
public class ServletInicioAutenticar extends HttpServlet {

    @EJB
    private UsuarioFacade usuarioFacade;

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
        String strNick = request.getParameter("nick");
        String strClave = request.getParameter("contrasena");
        Usuario usuario;
        String strError = "", strTo = "";
        HttpSession session = request.getSession();

        Usuario usuarioRegistrado = (Usuario) session.getAttribute("usuario");
        if (usuarioRegistrado == null) {
            if (strNick == null || strNick.isEmpty()
                    || strClave == null || strClave.isEmpty()) {  // Error de autenticación por email o clave
                // vacíos o nulos.
                strError = "Error de autenticación: alguno de los valores está vacío";
                request.setAttribute("error", strError);
                strTo = "inicioSesion.jsp";

            } else {
                usuario = this.usuarioFacade.findByNickAndPassword(strNick, strClave);
                if (usuario == null) { // No hay usuario en la BD
                    strError = "Error de autenticación: credenciales incorrectas";
                    request.setAttribute("error", strError);
                    strTo = "inicioSesion.jsp";
                } else { // El usuario está en la BD
                    session.setAttribute("usuario", usuario);
                    if (usuario.getTipoUsuario().equals("administrador")) {
                        strTo = "homeAdmin.jsp";
                    } else if (usuario.getTipoUsuario().equals("creadordeeventos")) {
                        strTo = "ServletCreadorDeEventosListar";
                    } else if (usuario.getTipoUsuario().equals("teleoperador")) {

                    } else if (usuario.getTipoUsuario().equals("analistadeeventos")) {
                        strTo = "homeAnalista.jsp";
                    } else {
                        strTo = "/ServletHomeUsuarioDeEventos";
                    }
                }
            }
        } else {
            if (usuarioRegistrado.getTipoUsuario().equals("administrador")) {
                strTo = "homeAdmin.jsp";
            } else if (usuarioRegistrado.getTipoUsuario().equals("creadordeeventos")) {
                strTo = "ServletCreadorDeEventosListar";
            } else if (usuarioRegistrado.getTipoUsuario().equals("teleoperador")) {

            } else if (usuarioRegistrado.getTipoUsuario().equals("analistadeeventos")) {
                strTo = "homeAnalista.jsp";
            } else {
                strTo = "/ServletHomeUsuarioDeEventos";
            }
        }
        RequestDispatcher rd = request.getRequestDispatcher(strTo);
        rd.forward(request, response);
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
