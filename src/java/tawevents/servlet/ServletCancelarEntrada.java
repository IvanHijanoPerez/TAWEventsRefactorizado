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
import tawevents.dao.EventoFacade;
import tawevents.dao.PublicoFacade;
import tawevents.dao.UsuarioDeEventosFacade;
import tawevents.entity.Evento;
import tawevents.entity.Publico;
import tawevents.entity.Usuario;
import tawevents.entity.UsuarioDeEventos;

/**
 *
 * @author David
 */
@WebServlet(name = "ServletCancelarEntrada", urlPatterns = {"/ServletCancelarEntrada"})
public class ServletCancelarEntrada extends HttpServlet {
    
    @EJB
    PublicoFacade publicoFacade;
    
    @EJB
    EventoFacade eventoFacade;
    
    @EJB
    UsuarioDeEventosFacade usuarioDeEventosFacade;

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
        
        Publico publico = publicoFacade.findByID(new Integer(request.getParameter("id_entrada")));
        Evento evento = publico.getEvento();
        UsuarioDeEventos usuarioDeEventos = ((Usuario)session.getAttribute("usuario")).getUsuarioDeEventos();
        
        publicoFacade.remove(publico);
        
        List<Publico> listaPublico = evento.getPublicoList();
        listaPublico.remove(publico);
        evento.setPublicoList(listaPublico);
        eventoFacade.edit(evento);
        
        List<Publico> listaAsistencias = usuarioDeEventos.getPublicoList();
        listaAsistencias.remove(publico);
        usuarioDeEventos.setPublicoList(listaAsistencias);
        usuarioDeEventosFacade.edit(usuarioDeEventos);
        
        response.sendRedirect("ServletUnirseEvento?id_evento=" + evento.getId());
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
