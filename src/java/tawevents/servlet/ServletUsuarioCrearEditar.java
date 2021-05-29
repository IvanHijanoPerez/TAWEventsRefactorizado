/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tawevents.dto.UsuarioDTO;
import tawevents.service.UsuarioService;


/**
 *
 * @author Ivan
 */
@WebServlet(name = "ServletUsuarioCrearEditar", urlPatterns = {"/ServletUsuarioCrear", "/ServletUsuarioEditar"})
public class ServletUsuarioCrearEditar extends HttpServlet {

    @EJB
    private UsuarioService usuarioService;

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
        String strTo = "crearEditarUsuario.jsp";
        HttpSession session = request.getSession();
        UsuarioDTO usuario = (UsuarioDTO)session.getAttribute("usuario");
        if (usuario == null) {
            request.setAttribute("error", "Usuario no autenticado");
            strTo = "inicioSesion.jsp";
        } else {                                
            String strId = request.getParameter("id");

            if (strId != null) { // Es editar usuario
                UsuarioDTO us = this.usuarioService.buscarUsuario(new Integer(strId));
                if(us.getTipoUsuario().equals("usuariodeeventos")){
                    strTo = "editarUsuarioDeEventos.jsp";
                }
                request.setAttribute("usuarioEditar", us);            
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
