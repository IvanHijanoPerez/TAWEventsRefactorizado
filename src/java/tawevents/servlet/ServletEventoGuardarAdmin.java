/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import tawevents.dto.EtiquetaDTO;
import tawevents.dto.EventoDTO;
import tawevents.dto.UsuarioDTO;
import tawevents.service.EtiquetaService;
import tawevents.service.EventoService;

/**
 *
 * @author Ivan
 */
@WebServlet(name = "ServletEventoGuardarAdmin", urlPatterns = {"/ServletEventoGuardarAdmin"})
public class ServletEventoGuardarAdmin extends HttpServlet {

    @EJB
    private EtiquetaService etiquetaService;

    @EJB
    private EventoService eventoService;

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
        UsuarioDTO usuario = (UsuarioDTO)session.getAttribute("usuario");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String eventoId = request.getParameter("id");
        String titulo = request.getParameter("titulo");
        if(titulo != null){
            titulo = new String(request.getParameter("titulo").getBytes("ISO-8859-1"), "UTF-8");
        }
        String descripcion = request.getParameter("descripcion");
        if(descripcion != null){
            descripcion = new String(request.getParameter("descripcion").getBytes("ISO-8859-1"), "UTF-8");
        }
        String precio = request.getParameter("precio");
        String imagen = request.getParameter("imagen");
        String etiquetas = request.getParameter("etiquetas");
        if(etiquetas != null){
            etiquetas = new String(request.getParameter("etiquetas").getBytes("ISO-8859-1"), "UTF-8");
        }
        Date fecha = null;
        try {
            fecha = format.parse(request.getParameter("fecha"));
        } catch (ParseException ex) {
            Logger.getLogger(ServletEventoGuardar.class.getName()).log(Level.SEVERE, null, ex);
        }
        Date fecha_limite_entradas = null;
        try {
            fecha_limite_entradas = format.parse(request.getParameter("fecha_limite_entradas"));
        } catch (ParseException ex) {
            Logger.getLogger(ServletEventoGuardar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String aforo_maximo = request.getParameter("aforo_maximo");
        String maximo_entradas_usuario = request.getParameter("maximo_entradas_usuario");      
        String asientos_asignados = request.getParameter("asientos_asignados");
        String numero_filas = request.getParameter("numero_filas");
        String asientos_por_fila = request.getParameter("asientos_por_fila");
        
        if(eventoId != null){
            EventoDTO eventoEditar = eventoService.buscarEvento(new Integer(eventoId));
            if(fecha.before(fecha_limite_entradas)){
                request.setAttribute("eventoEditar", eventoEditar);
                String etiquetass = this.eventoService.getEtiquetasToString(eventoEditar);
                request.setAttribute("etiquetas", etiquetass);
                request.setAttribute("errorRegistro", "Error de edición: fechas no válidas");
                RequestDispatcher rd = request.getRequestDispatcher("editarEventoAdmin.jsp");
                 rd.forward(request, response);
            }else{
                eventoService.editarEvento(Integer.parseInt(eventoId), usuario.getId(), titulo, descripcion, etiquetas, Integer.parseInt(precio), imagen, fecha, fecha_limite_entradas, Integer.parseInt(aforo_maximo), Integer.parseInt(maximo_entradas_usuario), asientos_asignados, numero_filas, asientos_por_fila);                
                
                RequestDispatcher rd = request.getRequestDispatcher("ServletEventoListar");
                rd.forward(request, response);
                //response.sendRedirect("ServletEventoListar");   
            }
                      
        }else{
            
            if(fecha.before(fecha_limite_entradas)){
                request.setAttribute("errorRegistro", "Error de creación: fechas no válidas");
                RequestDispatcher rd = request.getRequestDispatcher("crearEventoAdmin.jsp");
                rd.forward(request, response);
            }else{
                eventoService.crearEvento(usuario.getId(), titulo, descripcion, etiquetas, Integer.parseInt(precio), imagen, fecha, fecha_limite_entradas, Integer.parseInt(aforo_maximo), Integer.parseInt(maximo_entradas_usuario), asientos_asignados, numero_filas, asientos_por_fila);
                RequestDispatcher rd = request.getRequestDispatcher("ServletEventoListar");
                rd.forward(request, response);
                //response.sendRedirect("ServletEventoListar");
            }
              
        }

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
