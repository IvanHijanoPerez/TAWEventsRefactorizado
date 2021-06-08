/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.servlet;

import java.io.IOException;
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
import tawevents.dto.UsuarioDTO;
import tawevents.service.EstudioService;

/**
 *
 * @author daniel
 */
@WebServlet(name = "ServletAnalistaCrearEstudio", urlPatterns = {"/ServletAnalistaCrearEstudio"})
public class ServletAnalistaCrearEstudio extends HttpServlet {

    @EJB
    private EstudioService estudioService;

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

        // Comprobar que el usuario está conectado y es analita de eventos
        HttpSession session = request.getSession();
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null || !usuario.getTipoUsuario().equals("analistadeeventos")) {
            RequestDispatcher rd = request.getRequestDispatcher("inicioSesion.jsp");
            rd.forward(request, response);
        }
        List<String> datos = new ArrayList<>();
        
        datos.add(request.getParameter("titulo")); // 0
        datos.add(request.getParameter("desdeFecha")); // 1
        datos.add(request.getParameter("hastaFecha")); // 2
        datos.add(request.getParameter("event_fin")); // 3
        datos.add(request.getParameter("asient_asig")); // 4
        datos.add(""); // 5 Hueco para etiquetas
        datos.add(request.getParameter("aforo_min")); // 6
        datos.add(request.getParameter("aforo_max")); // 7   
        datos.add(request.getParameter("sexo")); // 8
        datos.add(request.getParameter("ciudad")); // 9
        datos.add(request.getParameter("edad_min")); // 10
        datos.add(request.getParameter("edad_max")); // 11
        datos.add(request.getParameter("nombre")); // 12
        datos.add(request.getParameter("apellidos")); // 13
        datos.add(request.getParameter("tipousuario")); // 14

        //DESCRIPCION NO PUEDE ESTAR VACIA
        String descripcion = request.getParameter("descripcion"); // 15
        if (descripcion == null) {
            RequestDispatcher rd = request.getRequestDispatcher("analistaEstudio.jsp");
            rd.forward(request, response);
        }
        datos.add(descripcion); // 15

        this.estudioService.arreglarDatosYGuardarEstudio(usuario.getId(), datos);

        response.sendRedirect("ServletAnalistaListar");
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
