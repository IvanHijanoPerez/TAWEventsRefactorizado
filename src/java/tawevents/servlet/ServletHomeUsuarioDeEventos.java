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
import tawevents.entity.UsuarioDeEventos;
import tawevents.service.EtiquetaService;
import tawevents.service.EventoService;
import tawevents.service.UsuarioDeEventosService;

/**
 *
 * @author David
 */
@WebServlet(name = "ServletHomeUsuarioDeEventos", urlPatterns = {"/ServletHomeUsuarioDeEventos"})
public class ServletHomeUsuarioDeEventos extends HttpServlet {

    @EJB
    private EventoService eventoService;
    
    @EJB
    private EtiquetaService etiquetaService;

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

        Etiqueta etiqueta = etiquetaService.findByNombreExacto(((Usuario)session.getAttribute("usuario")).getUsuarioDeEventos().getCiudad());
        if (etiqueta != null) {
            List<Evento> listaEnCiudad = this.eventoService.findByDisponiblesEtiqueta(etiquetaService.findByNombreExacto(((Usuario)session.getAttribute("usuario")).getUsuarioDeEventos().getCiudad()));
            request.setAttribute("listaEnCiudad", listaEnCiudad);
        } else {
            request.setAttribute("listaEnCiudad", null);
        }
        List<Evento> listaPopulares = this.eventoService.findByDisponiblesMasPopulares();
        request.setAttribute("listaPopulares", listaPopulares);
        List<Evento> listaProximos = this.eventoService.findByDisponiblesMasCercanos();
        request.setAttribute("listaProximos", listaProximos);

        RequestDispatcher rd = request.getRequestDispatcher("homeUsuarioDeEventos.jsp");
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
