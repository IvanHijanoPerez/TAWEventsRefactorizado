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
import tawevents.dto.EventoDTO;
import tawevents.dto.PublicoDTO;
import tawevents.dto.UsuarioDTO;
import tawevents.service.EventoService;
import tawevents.service.PublicoService;
import tawevents.service.UsuarioService;

/**
 *
 * @author David
 */
@WebServlet(name = "ServletUnirseEvento", urlPatterns = {"/ServletUnirseEvento"})
public class ServletUnirseEvento extends HttpServlet {
    
    @EJB
    private EventoService eventoService;
    
    @EJB
    private PublicoService publicoService;
    
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
        
        EventoDTO evento = eventoService.find(new Integer(request.getParameter("id_evento")));
        request.setAttribute("evento", evento);
        List<PublicoDTO> publicos = publicoService.findByUsuario(usuarioService.getUsuarioDeEventos((UsuarioDTO)session.getAttribute("usuario")));
        request.setAttribute("publicosUs", publicos);
        List<PublicoDTO> publicoDeEvento = publicoService.findByEvento(evento);
        request.setAttribute("publicosEv", publicoDeEvento);
        
        
        boolean [][] ocupados = null;
        if (evento.getAsientosAsignados()) {
            ocupados = new boolean[evento.getNumFilas()][evento.getAsientosPorFila()];
            for (int i = 0; i < evento.getNumFilas(); i++) {
                for (int j = 0; j < evento.getAsientosPorFila(); j++) {
                    ocupados[i][j] = buscarOcupado(publicoDeEvento, i, j);
                }
            }
        }
        request.setAttribute("ocupados", ocupados);
        
        
        RequestDispatcher rd = request.getRequestDispatcher("reservaEvento.jsp");
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

    private boolean buscarOcupado(List<PublicoDTO> publicoList, int i, int j) {
        for (PublicoDTO pub : publicoList) {
            if (pub.getFila() == i && pub.getAsiento() == j) {
                return true;
            }
        }
        return false;
    }

}
