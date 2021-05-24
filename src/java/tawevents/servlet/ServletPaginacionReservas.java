/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
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

/**
 *
 * @author David
 */
@WebServlet(name = "ServletPaginacionReservas", urlPatterns = {"/ServletPaginacionReservas"})
public class ServletPaginacionReservas extends HttpServlet {
    
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
        
        HttpSession session = request.getSession();
        
        UsuarioDeEventos usuarioDeEventos = ((Usuario)session.getAttribute("usuario")).getUsuarioDeEventos();
        Set<Evento> setEventos = new HashSet<>();
        List<Evento> listaEventos;

        if (request.getParameter("busqueda") != null) {
            String busqueda = new String(request.getParameter("busqueda").getBytes("ISO-8859-1"), "UTF-8");
            request.setAttribute("ultimaBusqueda", busqueda);
            String palabra;
            Etiqueta etiqueta;
            try (Scanner sc = new Scanner(busqueda)){
                while (sc.hasNext()) {
                    palabra = sc.next();
                    setEventos.addAll(eventoFacade.findByTituloReserva(palabra, usuarioDeEventos));
                    etiqueta = etiquetaFacade.findByNombreExacto(palabra);
                    if (etiqueta != null) {
                        setEventos.addAll(eventoFacade.findByEtiquetaReserva(etiqueta, usuarioDeEventos));
                    }
                } 
            }
            listaEventos = new ArrayList<>(setEventos);
        } else {
            listaEventos = this.eventoFacade.findAllReserva(usuarioDeEventos);
        }

        Integer pageid;
        if (request.getParameter("pagina") == null) {
            pageid = 1;
        } else {
            pageid = new Integer(request.getParameter("pagina"));
        }

        request.setAttribute("pagina", pageid);
        if ((pageid - 1) * 9 + 9 < listaEventos.size()) {
            List<Evento> listaEventosPagina = listaEventos.subList((pageid - 1) * 9, (pageid - 1) * 9 + 9);
            request.setAttribute("listaEventosPagina", listaEventosPagina);
            request.setAttribute("pagfinal", false);
        } else {
            List<Evento> listaEventosPagina = listaEventos.subList((pageid - 1) * 9, listaEventos.size());
            request.setAttribute("listaEventosPagina", listaEventosPagina);
            request.setAttribute("pagfinal", true);
        }

        RequestDispatcher rd = request.getRequestDispatcher("busquedaReservasUsuarioDeEventos.jsp");
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
