/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import tawevents.dto.EtiquetaDTO;
import tawevents.dto.EventoDTO;
import tawevents.dto.UsuarioDTO;
import tawevents.entity.Etiqueta;
import tawevents.entity.Evento;
import tawevents.entity.Usuario;
import tawevents.service.EtiquetaService;
import tawevents.service.EventoService;
import tawevents.service.UsuarioService;

/**
 *
 * @author rafar
 */
@WebServlet(name = "ServletCreadorDeEventosListar", urlPatterns = {"/ServletCreadorDeEventosListar"})
public class ServletCreadorDeEventosListar extends HttpServlet {
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
        UsuarioDTO usuario;
        HttpSession session = request.getSession();
        usuario = (UsuarioDTO) session.getAttribute("usuario");
        
        String filtro = request.getParameter("filtro");
        if(filtro != null && filtro.length() > 0){
            
           List <Integer> lista = eventoService.convertirALaInversa(eventoService.findByTitulo(filtro));
           lista.retainAll(usuario.getEventoList());
            // falta filtrar por etiquetas:  (+ el borrado bien hecho)
            List<EtiquetaDTO> etList = etiquetaService.findBySimilarNombreMuchas(filtro);
            if(!etList.isEmpty()){
            for(EtiquetaDTO e : etList){
            
                System.out.println("Etiquetaaaa: " + e.getNombre());
                for(int even : e.getEventoList()){
                    if(!lista.contains(even) && usuario.getEventoList().contains(even)){
                        lista.add(even);
                    } 
                }
            }
            }
                
            request.setAttribute("listaEventos", eventoService.convertirAListaDTOdirectamente(lista));
            
        }else{ 
            List <EventoDTO> lEventos = eventoService.convertirAListaDTOdirectamente(usuario.getEventoList());
            request.setAttribute("listaEventos", lEventos);
        }
        request.setAttribute("usuario", usuario);
        List<EtiquetaDTO> todasLasEtiquetas = etiquetaService.findAll();
        request.setAttribute("todasLasEtiquetas", todasLasEtiquetas);
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
