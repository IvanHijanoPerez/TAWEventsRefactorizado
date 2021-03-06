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
import javax.servlet.http.HttpSession;
import tawevents.dto.UsuarioDTO;
import tawevents.service.UsuarioService;

/**
 *
 * @author Ivan
 */
@WebServlet(name = "ServletUsuarioGuardar", urlPatterns = {"/ServletUsuarioGuardar"})
public class ServletUsuarioGuardar extends HttpServlet {

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
        String id, strNick, strClave, tipoUsuario, strClaveConf;
        String strError = "";
        UsuarioDTO usuarioNuevo; 
        
        id = request.getParameter("id");
        strNick = request.getParameter("nick");
        if(strNick != null){
            strNick = new String(request.getParameter("nick").getBytes("ISO-8859-1"), "UTF-8"); 
        }
        strClave = request.getParameter("contrasena");
        strClaveConf = request.getParameter("confirmarContrasena");
        tipoUsuario = request.getParameter("tipoUsuario");
        
        HttpSession session = request.getSession();
        UsuarioDTO usuario = (UsuarioDTO)session.getAttribute("usuario");
        if (usuario == null) {
            request.setAttribute("errorRegistro", "Usuario no autenticado");
            RequestDispatcher rd = request.getRequestDispatcher("inicioSesion.jsp");
            rd.forward(request, response);
        } else {
            
           if (strNick == null || strNick.isEmpty() || 
            strClave == null || strClave.isEmpty()) {  
                                                      
            strError = "Error de creaci??n: alguno de los valores est?? vac??o";
            request.setAttribute("errorRegistro", strError);

            RequestDispatcher rd = request.getRequestDispatcher("crearEditarUsuario.jsp");
            rd.forward(request, response);             
            } else {
                if (id == null || id.isEmpty()) { // Crear nuevo cliente
                    
                    UsuarioDTO i = this.usuarioService.buscarUsuarioNick(strNick);
                    if (i == null) { // No existe usuario con ese nick 
                        if(!strClave.equals(strClaveConf)){
                            strError = "Error de creaci??n: las contrase??as son diferentes";
                            request.setAttribute("errorContra", strError);
                            RequestDispatcher rd = request.getRequestDispatcher("crearEditarUsuario.jsp");
                            rd.forward(request, response); 
                        }else{
                            this.usuarioService.guardarUsuario(id, strNick, strClave, tipoUsuario);  
                            response.sendRedirect("ServletUsuarioListar"); 
                        }
                                           

                    } else { // Existe nick con ese id
                        strError = "Error de creaci??n: este nick est?? cogido";
                        request.setAttribute("errorNick", strError);
                        RequestDispatcher rd = request.getRequestDispatcher("crearEditarUsuario.jsp");
                        rd.forward(request, response); 
                    } 

                } else { // Editar cliente existente
                    usuarioNuevo = this.usuarioService.buscarUsuario(new Integer(id));
                    if(!strClave.equals(strClaveConf)){
                        strError = "Error de creaci??n: las contrase??as son diferentes";
                        request.setAttribute("errorContra", strError);
                        request.setAttribute("usuarioEditar", usuarioNuevo);
                        RequestDispatcher rd = request.getRequestDispatcher("crearEditarUsuario.jsp");
                        rd.forward(request, response); 
                    }else{
                        UsuarioDTO i = this.usuarioService.buscarUsuarioNick(strNick);
                        if(i == null || i.getId().equals(usuarioNuevo.getId())){
                            this.usuarioService.guardarUsuario(id, strNick, strClave, tipoUsuario);  
                            response.sendRedirect("ServletUsuarioListar"); 
                        }else{
                            strError = "Error de creaci??n: este nick est?? cogido";
                            request.setAttribute("usuarioEditar", usuarioNuevo);
                            request.setAttribute("errorNick", strError);
                            RequestDispatcher rd = request.getRequestDispatcher("crearEditarUsuario.jsp");
                            rd.forward(request, response); 
                        }
                    }
                    
                }    
            }  
        }   
    }// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
