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
import tawevents.service.EventoService;
import tawevents.service.PublicoService;
import tawevents.service.UsuarioDeEventosService;

/**
 *
 * @author David
 */
@WebServlet(name = "ServletSolicitarEntrada", urlPatterns = {"/ServletSolicitarEntrada"})
public class ServletSolicitarEntrada extends HttpServlet {

    @EJB
    PublicoService publicoService;

    @EJB
    EventoService eventoService;

    @EJB
    UsuarioDeEventosService usuarioDeEventosService;

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
        Evento evento = eventoService.findById(new Integer(request.getParameter("id_evento")));
        UsuarioDeEventos usuarioDeEventos = ((Usuario) session.getAttribute("usuario")).getUsuarioDeEventos();
        Publico publico = new Publico();

        if (evento.getAsientosAsignados()) {
            publicoService.createAsientosAsignados(publico, evento, usuarioDeEventos, new Integer(request.getParameter("fila")) - 1, new Integer(request.getParameter("asiento")) - 1);

        } else {
            publicoService.createSinAsientos(publico, evento, usuarioDeEventos);
        }
        eventoService.addPublico(evento, publico);
        usuarioDeEventosService.addPublico(usuarioDeEventos, publico);
        

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
