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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tawevents.dao.EtiquetaFacade;
import tawevents.dao.EventoFacade;
import tawevents.dao.UsuarioFacade;
import tawevents.entity.Etiqueta;
import tawevents.entity.Evento;
import tawevents.entity.Usuario;

/**
 *
 * @author rafar
 */
@WebServlet(name = "ServletCreadorDeEventosBorrar", urlPatterns = {"/ServletCreadorDeEventosBorrar"})
public class ServletCreadorDeEventosBorrar extends HttpServlet {

    @EJB
    private EventoFacade eventoFacade;
    
    @EJB
    private UsuarioFacade usuarioFacade;
    
     @EJB
    private EtiquetaFacade etiquetaFacade;
    
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

            Evento eventoABorrar = this.eventoFacade.find(Integer.parseInt(request.getParameter("idE")));
            
            for(Etiqueta e : eventoABorrar.getEtiquetaList()){
                List<Evento> eventoList = e.getEventoList();
                eventoList.remove(eventoABorrar);
                if(eventoList.isEmpty()){
                    etiquetaFacade.remove(e);
                }else{
                    e.setEventoList(eventoList);
                     etiquetaFacade.edit(e);
                }
            }
            
            this.eventoFacade.remove(eventoABorrar);
            
            List eventosList = usuario.getEventoList();
            eventosList.remove(eventoABorrar);
            usuarioFacade.edit(usuario); 
            
            response.sendRedirect("ServletCreadorDeEventosListar"); 
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
