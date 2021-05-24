/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tawevents.dao.EstudioFacade;
import tawevents.entity.Estudio;
import tawevents.entity.Usuario;

/**
 *
 * @author daniel
 */
@WebServlet(name = "ServletAnalistaListar", urlPatterns = {"/ServletAnalistaListar"})
public class ServletAnalistaListar extends HttpServlet {

    @EJB
    private EstudioFacade estudioFacade;

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
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        List<Estudio> lista = null;
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        String strDesdeFecha = request.getParameter("desdeFecha");
        String strHastaFecha = request.getParameter("hastaFecha");
        Date desdeFecha = null;
        Date hastaFecha = null;

        String ordenarporfecha = request.getParameter("ordenporfecha");

        try {
            desdeFecha = (strDesdeFecha != null && !strDesdeFecha.isEmpty()) ? formatoFecha.parse(strDesdeFecha) : formatoFecha.parse("2000-01-01");
            hastaFecha = (strHastaFecha != null && !strHastaFecha.isEmpty()) ? formatoFecha.parse(strHastaFecha) : new Date();
        } catch (ParseException ex) {
            Logger.getLogger(ServletAnalistaListar.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (ordenarporfecha == null || ordenarporfecha.equals("desdeprimeros")) {
            lista = this.estudioFacade.findByDesdeYHasta(usuario.getId(), desdeFecha, hastaFecha);
        } else {
            lista = this.estudioFacade.findByDesdeYHastaOrdenFechas(usuario.getId(), desdeFecha, hastaFecha);
        }

        //request.setAttribute("actualizarTabla", null);
        request.setAttribute("desdeFecha", desdeFecha);
        request.setAttribute("hastaFecha", hastaFecha);
        request.setAttribute("lista", lista);

        RequestDispatcher rd = request.getRequestDispatcher("analistaVerEstudios.jsp");
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
