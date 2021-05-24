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
import tawevents.dao.UsuarioFacade;
import tawevents.entity.Usuario;

/**
 *
 * @author Ivan
 */
@WebServlet(name = "ServletUsuarioListar", urlPatterns = {"/ServletUsuarioListar"})
public class ServletUsuarioListar extends HttpServlet {
    
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
        
        List<Usuario> lista;
        
        String tipoFiltro = request.getParameter("tipoFiltro");
        String filtro = request.getParameter("filtro");
        if(filtro != null){
            filtro = new String(request.getParameter("filtro").getBytes("ISO-8859-1"), "UTF-8");
        }
        
        if ((filtro != null && filtro.length()>0 )) {
            if(tipoFiltro.equals("fNick")){
                lista = this.usuarioFacade.findBySimilarNick(filtro);
            }else if(tipoFiltro.equals("fContrasena")){
                lista = this.usuarioFacade.findBySimilarContrasena(filtro);
            }else{
                lista = this.usuarioFacade.findBySimilarTipoUsuario(filtro);
            }
            
        }else{
            lista = this.usuarioFacade.findAll();
        }
        
        request.setAttribute("lista", lista);
        
        RequestDispatcher rd = request.getRequestDispatcher("listaUsuarios.jsp");
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
