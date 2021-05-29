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
import tawevents.dto.UsuarioDTO;
import tawevents.service.UsuarioService;

/**
 *
 * @author Ivan
 */
@WebServlet(name = "ServletUsuarioBorrar", urlPatterns = {"/ServletUsuarioBorrar"})
public class ServletUsuarioBorrar extends HttpServlet {

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
        HttpSession session = request.getSession();
        UsuarioDTO usuario = (UsuarioDTO)session.getAttribute("usuario");
        if (usuario == null) {
            request.setAttribute("errorRegistro", "Usuario no autenticado");
            RequestDispatcher rd = request.getRequestDispatcher("inicioSesion.jsp");
            rd.forward(request, response);
        } else {                
            String strId = request.getParameter("id");

            UsuarioDTO elUsuario = this.usuarioService.buscarUsuario(new Integer(strId));
               if(usuario.getNickname().equals(elUsuario.getNickname())){
                   this.usuarioService.borrarUsuario(new Integer(strId));
                   session.invalidate();
                   response.sendRedirect("inicioSesion.jsp");
               }else{
                   this.usuarioService.borrarUsuario(new Integer(strId));
                   response.sendRedirect("ServletUsuarioListar"); 
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
