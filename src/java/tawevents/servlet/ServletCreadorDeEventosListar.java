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
import tawevents.dao.EtiquetaFacade;
import tawevents.dao.EventoFacade;
import tawevents.entity.Etiqueta;
import tawevents.entity.Evento;
import tawevents.entity.Usuario;

/**
 *
 * @author rafar
 */
@WebServlet(name = "ServletCreadorDeEventosListar", urlPatterns = {"/ServletCreadorDeEventosListar"})
public class ServletCreadorDeEventosListar extends HttpServlet {
    
    @EJB
    private EventoFacade eventoFacade;
    
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
        Usuario usuario;
        HttpSession session = request.getSession();
        usuario = (Usuario) session.getAttribute("usuario");
        
        String filtro = request.getParameter("filtro");
        if(filtro != null && filtro.length() > 0){
            List <Evento> lista = eventoFacade.findByTitulo(filtro);
            lista.retainAll(usuario.getEventoList());
            
            Etiqueta e = etiquetaFacade.findBySimilarNombre(filtro);
            if(e != null){
                for(Evento even : e.getEventoList()){
                    if(!lista.contains(even) && usuario.getEventoList().contains(even)){
                        lista.add(even);
                    }
                }
            }
                
            request.setAttribute("listaEventos", lista);
        }else{ 
            request.setAttribute("listaEventos", usuario.getEventoList());
        }
        request.setAttribute("usuario", usuario);
        
        request.setAttribute("filtro", filtro);
                
        RequestDispatcher rd = request.getRequestDispatcher("homeCreadorDeEventos.jsp");
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
