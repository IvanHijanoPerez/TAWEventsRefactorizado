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
import tawevents.dto.EtiquetaDTO;
import tawevents.dto.EventoDTO;
import tawevents.dto.UsuarioDTO;
import tawevents.service.EtiquetaService;
import tawevents.service.EventoService;
import tawevents.service.UsuarioService;

/**
 *
 * @author Ivan
 */
@WebServlet(name = "ServletEventoListar", urlPatterns = {"/ServletEventoListar"})
public class ServletEventoListar extends HttpServlet {

    @EJB
    private EtiquetaService etiquetaService;

    @EJB
    private EventoService eventoService;

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
        List<EventoDTO> lista;
        List<UsuarioDTO> l = this.usuarioService.TodosUsuarios();
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
                lista = this.eventoService.filtroNombre(filtroT);
            }else if(tipoFiltro.equals("fDescripcion")){
                lista = this.eventoService.filtroDescripcion(filtroT);
            }else if(tipoFiltro.equals("fFecha")){
                Date fecha = null;
                try {
                    fecha = format.parse(request.getParameter("filtroF"));
                } catch (ParseException ex) {
                    Logger.getLogger(ServletEventoGuardar.class.getName()).log(Level.SEVERE, null, ex);
                }
                lista = this.eventoService.filtroFecha(fecha);
            }else if(tipoFiltro.equals("fFechaEntrada")){
                Date fecha = null;
                try {
                    fecha = format.parse(request.getParameter("filtroF"));
                } catch (ParseException ex) {
                    Logger.getLogger(ServletEventoGuardar.class.getName()).log(Level.SEVERE, null, ex);
                }
                lista = this.eventoService.filtroFechaEntrada(fecha);
            }else if(tipoFiltro.equals("fPrecio")){
                lista = this.eventoService.filtroPrecio(filtroN);
            }else if(tipoFiltro.equals("fAforo")){
                lista = this.eventoService.filtroAforo(filtroN);
            }else if(tipoFiltro.equals("fMaxEntradasUsuario")){
                lista = this.eventoService.filtroMaxEntradasUsuario(filtroN);
            }else if(tipoFiltro.equals("fAsientosAsignados")){
                lista = this.eventoService.filtroAsientosAsignados(filtroC);
            }else if(tipoFiltro.equals("fNumFilas")){
                lista = this.eventoService.filtroNumFilas(filtroN);
            }else if(tipoFiltro.equals("fAsientosFila")){
                lista = this.eventoService.filtroAsientosFila(filtroN);
            }else if(tipoFiltro.equals("fCreador")){
                lista = this.eventoService.filtroCreador(filtroT);
            }else if(tipoFiltro.equals("fEtiqueta")){
                EtiquetaDTO e = this.etiquetaService.filtroNombre(filtroT);
                
                if(e == null){
                    lista = new ArrayList();
                }else{
                   lista = this.eventoService.convertirAListaDTOdirectamente(e.getEventoList());
                }       
            }else{
                lista = this.eventoService.filtroImagen(filtroT);
            }
            
        }else{
            lista = this.eventoService.TodosEventos();
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
