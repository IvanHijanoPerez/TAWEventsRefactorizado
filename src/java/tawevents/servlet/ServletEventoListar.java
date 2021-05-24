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
import tawevents.dao.EtiquetaFacade;
import tawevents.dao.EventoFacade;
import tawevents.dao.UsuarioFacade;
import tawevents.entity.Etiqueta;
import tawevents.entity.Evento;
import tawevents.entity.Usuario;

/**
 *
 * @author Ivan
 */
@WebServlet(name = "ServletEventoListar", urlPatterns = {"/ServletEventoListar"})
public class ServletEventoListar extends HttpServlet {

    @EJB
    private EtiquetaFacade etiquetaFacade;

    @EJB
    private UsuarioFacade usuarioFacade;

    @EJB
    private EventoFacade eventoFacade;

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
        List<Evento> lista;
        List<Usuario> l = this.usuarioFacade.findAll();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String tipoFiltro = request.getParameter("tipoFiltro");
        String filtroT = request.getParameter("filtroT");
        String filtroN = request.getParameter("filtroN");
        if(filtroT != null){
            filtroT = new String(request.getParameter("filtroT").getBytes("ISO-8859-1"), "UTF-8");
        }
        String filtroF = request.getParameter("filtroF");
        String filtroC = request.getParameter("filtroC");
        if ((filtroT != null && filtroT.length()>0 ) || (filtroN != null && filtroN.length()>0 ) || (filtroF != null && filtroF.length()>0 ) || (filtroC != null && filtroC.length()>0 )) {
            if(tipoFiltro.equals("fNombre")){
                lista = this.eventoFacade.findBySimilarNombre(filtroT);
            }else if(tipoFiltro.equals("fDescripcion")){
                lista = this.eventoFacade.findBySimilarDescripcion(filtroT);
            }else if(tipoFiltro.equals("fFecha")){
                Date fecha = null;
                try {
                    fecha = format.parse(request.getParameter("filtroF"));
                } catch (ParseException ex) {
                    Logger.getLogger(ServletEventoGuardar.class.getName()).log(Level.SEVERE, null, ex);
                }
                lista = this.eventoFacade.findBySimilarFecha(fecha);
            }else if(tipoFiltro.equals("fFechaEntrada")){
                Date fecha = null;
                try {
                    fecha = format.parse(request.getParameter("filtroF"));
                } catch (ParseException ex) {
                    Logger.getLogger(ServletEventoGuardar.class.getName()).log(Level.SEVERE, null, ex);
                }
                lista = this.eventoFacade.findBySimilarFechaEntrada(fecha);
            }else if(tipoFiltro.equals("fPrecio")){
                lista = this.eventoFacade.findBySimilarPrecio(filtroN);
            }else if(tipoFiltro.equals("fAforo")){
                lista = this.eventoFacade.findBySimilarAforo(filtroN);
            }else if(tipoFiltro.equals("fMaxEntradasUsuario")){
                lista = this.eventoFacade.findBySimilarMaxEntradasUsuario(filtroN);
            }else if(tipoFiltro.equals("fAsientosAsignados")){
                lista = this.eventoFacade.findBySimilarAsientosAsignados(filtroC);
            }else if(tipoFiltro.equals("fNumFilas")){
                lista = this.eventoFacade.findBySimilarNumFilas(filtroN);
            }else if(tipoFiltro.equals("fAsientosFila")){
                lista = this.eventoFacade.findBySimilarAsientosFila(filtroN);
            }else if(tipoFiltro.equals("fCreador")){
                lista = this.eventoFacade.findBySimilarCreador(filtroT);
            }else if(tipoFiltro.equals("fEtiqueta")){
                Etiqueta e = etiquetaFacade.findBySimilarNombreI(filtroT);
                
                if(e == null){
                    lista = new ArrayList();
                }else{
                   lista = e.getEventoList();
                }       
            }else{
                lista = this.eventoFacade.findBySimilarImagen(filtroT);
            }
            
        }else{
            lista = this.eventoFacade.findAll();
        }
        
        request.setAttribute("listaEv", lista);
        request.setAttribute("listaUs", l);
        RequestDispatcher rd = request.getRequestDispatcher("listaEventos.jsp");
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
