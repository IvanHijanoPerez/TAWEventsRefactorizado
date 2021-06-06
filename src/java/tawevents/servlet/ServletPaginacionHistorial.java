/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import tawevents.dto.EtiquetaDTO;
import tawevents.dto.EventoDTO;
import tawevents.dto.PublicoDTO;
import tawevents.dto.UsuarioDTO;
import tawevents.dto.UsuarioDeEventosDTO;
import tawevents.service.EtiquetaService;
import tawevents.service.EventoService;
import tawevents.service.UsuarioService;

/**
 *
 * @author David
 */
@WebServlet(name = "ServletPaginacionHistorial", urlPatterns = {"/ServletPaginacionHistorial"})
public class ServletPaginacionHistorial extends HttpServlet {
    
    @EJB
    private EventoService eventoService;

    @EJB
    private EtiquetaService etiquetaService;
    
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
        
        UsuarioDeEventosDTO usuarioDeEventos = usuarioService.getUsuarioDeEventos((UsuarioDTO)session.getAttribute("usuario"));
        Set<EventoDTO> setEventos = new HashSet<>();
        List<EventoDTO> listaEventos;
        

        if (request.getParameter("busqueda") != null) {
            String busqueda = new String(request.getParameter("busqueda").getBytes("ISO-8859-1"), "UTF-8");
            request.setAttribute("ultimaBusqueda", busqueda);
            String palabra;
            EtiquetaDTO etiqueta;
            try (Scanner sc = new Scanner(busqueda)){
                while (sc.hasNext()) {
                    palabra = sc.next();
                    setEventos.addAll(eventoService.findByTituloHistorial(palabra, usuarioDeEventos));
                    etiqueta = etiquetaService.findByNombreExacto(palabra);
                    if (etiqueta != null) {
                        setEventos.addAll(eventoService.findByEtiquetaHistorial(etiqueta, usuarioDeEventos));
                    }
                } 
            }
            listaEventos = new ArrayList<>(setEventos);
        } else {
            listaEventos = this.eventoService.findAllHistorial(usuarioDeEventos);
        }

        Integer pageid;
        if (request.getParameter("pagina") == null) {
            pageid = 1;
        } else {
            pageid = new Integer(request.getParameter("pagina"));
        }

        request.setAttribute("pagina", pageid);
        if ((pageid - 1) * 9 + 9 < listaEventos.size()) {
            List<EventoDTO> listaEventosPagina = listaEventos.subList((pageid - 1) * 9, (pageid - 1) * 9 + 9);
            Map<EventoDTO, List<PublicoDTO>> map = eventoService.construirMap(listaEventosPagina);
            request.setAttribute("listaEventosPagina", map);
            request.setAttribute("pagfinal", false);
        } else {
            List<EventoDTO> listaEventosPagina = listaEventos.subList((pageid - 1) * 9, listaEventos.size());
            Map<EventoDTO, List<PublicoDTO>> map = eventoService.construirMap(listaEventosPagina);
            request.setAttribute("listaEventosPagina", map);
            request.setAttribute("pagfinal", true);
        }

        RequestDispatcher rd = request.getRequestDispatcher("busquedaHistorialUsuarioDeEventos.jsp");
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
