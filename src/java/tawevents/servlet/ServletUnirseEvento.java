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
import tawevents.dao.EventoFacade;
import tawevents.dao.PublicoFacade;
import tawevents.entity.Evento;
import tawevents.entity.Publico;
import tawevents.entity.Usuario;
import tawevents.entity.UsuarioDeEventos;

/**
 *
 * @author David
 */
@WebServlet(name = "ServletUnirseEvento", urlPatterns = {"/ServletUnirseEvento"})
public class ServletUnirseEvento extends HttpServlet {
    
    @EJB
    private EventoFacade eventoFacade;
    
    @EJB
    private PublicoFacade publicoFacade;

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
        
        Evento evento = eventoFacade.findById(new Integer(request.getParameter("id_evento")));
        request.setAttribute("evento", evento);
        List<Publico> publicos = publicoFacade.findByUsuarioYEvento(((Usuario)session.getAttribute("usuario")).getUsuarioDeEventos(), evento);
        request.setAttribute("publicos", publicos);
        
        boolean [][] ocupados = null;
        if (evento.getAsientosAsignados()) {
            ocupados = new boolean[evento.getNumFilas()][evento.getAsientosPorFila()];
            for (int i = 0; i < evento.getNumFilas(); i++) {
                for (int j = 0; j < evento.getAsientosPorFila(); j++) {
                    ocupados[i][j] = buscarOcupado(evento.getPublicoList(), i, j);
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

    private boolean buscarOcupado(List<Publico> publicoList, int i, int j) {
        for (Publico pub : publicoList) {
            if (pub.getFila() == i && pub.getAsiento() == j) {
                return true;
            }
        }
        return false;
    }

}
