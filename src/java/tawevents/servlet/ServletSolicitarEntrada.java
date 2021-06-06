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
import tawevents.dto.EventoDTO;
import tawevents.dto.UsuarioDTO;
import tawevents.dto.UsuarioDeEventosDTO;
import tawevents.service.EventoService;
import tawevents.service.PublicoService;
import tawevents.service.UsuarioDeEventosService;
import tawevents.service.UsuarioService;

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
    
    @EJB
    UsuarioService usuarioService;

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
        EventoDTO evento = eventoService.find(new Integer(request.getParameter("id_evento")));
        UsuarioDeEventosDTO usuarioDeEventos = usuarioService.getUsuarioDeEventos((UsuarioDTO)session.getAttribute("usuario"));

        if (evento.getAsientosAsignados()) {
            publicoService.createAsientosAsignados(evento, usuarioDeEventos, new Integer(request.getParameter("fila")) - 1, new Integer(request.getParameter("asiento")) - 1);

        } else {
            publicoService.createSinAsientos(evento, usuarioDeEventos);
        }

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
