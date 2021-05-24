/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tawevents.dao.EstudioFacade;
import tawevents.entity.Estudio;

/**
 *
 * @author daniel
 */
@WebServlet(name = "ServletAnalistaEliminar", urlPatterns = {"/ServletAnalistaEliminar"})
public class ServletAnalistaEliminar extends HttpServlet {

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

        String confirmacion = request.getParameter("confirmacion");
        String strID = request.getParameter("id");

        if (confirmacion == null || strID == null || confirmacion.equals("n")) {
            // No elimino nada
            request.setAttribute("confirmacion", null);
            request.setAttribute("id", null);

        } else if (confirmacion.equals("y")) {
            // Elimino estudio
            Integer id = Integer.parseInt(strID);
            Estudio estudio = this.estudioFacade.find(id);
            if (estudio != null) {
                this.estudioFacade.remove(estudio);
            }
            
        } else { //confirmacion = "idk";
            // Hay que confirmar si es seguro eliminar el estudio
            request.setAttribute("confirmacion", confirmacion);
            request.setAttribute("id", strID);
        }

        RequestDispatcher rd = request.getRequestDispatcher("ServletAnalistaListar");
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