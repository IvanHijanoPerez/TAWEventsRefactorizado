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
import tawevents.dao.EtiquetaFacade;
import tawevents.dao.EventoFacade;
import tawevents.dao.UsuarioFacade;
import tawevents.entity.Etiqueta;
import tawevents.entity.Evento;
import tawevents.entity.Usuario;

/**
 *
 * @author rafar
 */
@WebServlet(name = "ServletEventoGuardar", urlPatterns = {"/ServletEventoGuardar"})
public class ServletEventoGuardar extends HttpServlet {

    @EJB
    private EventoFacade eventoFacade;
    
    @EJB
    private UsuarioFacade usuarioFacade;
    
    @EJB
    private EtiquetaFacade etiquetaFacade;
    
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
        Usuario u = (Usuario) session.getAttribute("usuario");    
        
        String titulo, descripcion, imagen, aforo_maximo, maximo_entradas_usuario, asientos_asignados, numero_filas, asientos_por_fila, precio, etiquetas;
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        Date fecha_limite_entradas = null;
        Evento evento = new Evento();
        
        titulo = request.getParameter("titulo");
        descripcion = request.getParameter("descripcion");
        imagen = request.getParameter("imagen");
        precio = request.getParameter("precio");
        try {
            fecha = format.parse(request.getParameter("fecha"));
        } catch (ParseException ex) {
            Logger.getLogger(ServletEventoGuardar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            fecha_limite_entradas = format.parse(request.getParameter("fecha_limite_entradas"));
        } catch (ParseException ex) {
            Logger.getLogger(ServletEventoGuardar.class.getName()).log(Level.SEVERE, null, ex);
        }

        aforo_maximo = request.getParameter("aforo_maximo");
        maximo_entradas_usuario = request.getParameter("maximo_entradas_usuario");
        etiquetas = request.getParameter("etiquetas");
        asientos_asignados = request.getParameter("asientos_asignados");
        numero_filas = request.getParameter("numero_filas");
        asientos_por_fila = request.getParameter("asientos_por_fila");
        
        try{
        //evento.setId(0);
        evento.setTitulo(titulo);
        evento.setDescripcion(descripcion);
        evento.setImagen(imagen);
        evento.setPrecioEntrada(Integer.parseInt(precio)); 
        evento.setFecha(fecha);
        evento.setFechaLimEntradas(fecha_limite_entradas);
        evento.setAforoMax(Integer.parseInt(aforo_maximo));
        evento.setMaxEntradasPorUsuario(Integer.parseInt(maximo_entradas_usuario));
        
        if("Si".equals(asientos_asignados)){
            evento.setAsientosAsignados(true);
            if(Integer.parseInt(numero_filas) <= 0 || Integer.parseInt(numero_filas) > 10){
                throw new Exception("Numero de filas no válido");
            }
            evento.setNumFilas(Integer.parseInt(numero_filas));
             if(Integer.parseInt(asientos_por_fila) <= 0 || Integer.parseInt(asientos_por_fila) > 10){
                throw new Exception("Asientos por fila no válido");
            }
            evento.setAsientosPorFila(Integer.parseInt(asientos_por_fila)); 
        }else{
            evento.setAsientosAsignados(false);
        } 
        evento.setUsuario(u);  
        
        List<Etiqueta> etiquetaList = new ArrayList(); 
        String[] arrayEtiquetas = etiquetas.split(" ");
        for(String item : arrayEtiquetas){
            Etiqueta buscada = etiquetaFacade.findBySimilarNombre(item);
            if(buscada==null){
                   Etiqueta e = new Etiqueta();
                  e.setNombre(item);
                 etiquetaList.add(e);
                etiquetaFacade.create(e);  
            }else{
                etiquetaList.add(buscada);
            }
        }
        evento.setEtiquetaList(etiquetaList);
        
        }catch(Exception e){
            
            String mensajeError = "Error rellenando los datos del formulario. Intentelo de nuevo.";
            
            if(request.getParameter("idEventoEditar") == null){ //estamos en guardar
                
            request.setAttribute("mensajeError", mensajeError);
            RequestDispatcher rd = request.getRequestDispatcher("crearEvento.jsp");
            rd.forward(request, response); 
            
            }else{ //estamos en editar
                int idEventoEditar = Integer.parseInt(request.getParameter("idEventoEditar"));
                Evento eventoEditar = eventoFacade.find(idEventoEditar);
               request.setAttribute("evento", eventoEditar);
               request.setAttribute("mensajeError", mensajeError);
               RequestDispatcher rd = request.getRequestDispatcher("editarEventoCreadorDeEventos.jsp");
               rd.forward(request, response); 
            }
        }

        if(request.getParameter("idEventoEditar") == null){ //estamos en guardar
            eventoFacade.create(evento);
            
            int idMasAlta = eventoFacade.findIdMasAlta();
            Evento ev = eventoFacade.find(idMasAlta);
            
            for(Etiqueta e : ev.getEtiquetaList()){
            List<Evento> eventoList = e.getEventoList(); 
            eventoList.add(ev);
            e.setEventoList(eventoList);
            // aqui se haria el edit?
            etiquetaFacade.edit(e);
        }
            
            List <Evento> listaEventos = u.getEventoList();
            listaEventos.add(evento);
            u.setEventoList(listaEventos);
            usuarioFacade.edit(u);
        }else{ // estamos en editar
        int idEventoEditar = Integer.parseInt(request.getParameter("idEventoEditar"));
        Evento eventoEditar = eventoFacade.find(idEventoEditar);
        
        List <Evento> listaEventos = u.getEventoList();
        listaEventos.remove(eventoEditar);
        
        eventoEditar.setTitulo(titulo);
        eventoEditar.setDescripcion(descripcion);
        eventoEditar.setImagen(imagen);
        eventoEditar.setPrecioEntrada(Integer.parseInt(precio)); 
        eventoEditar.setFecha(fecha);
        eventoEditar.setFechaLimEntradas(fecha_limite_entradas);
        eventoEditar.setAforoMax(Integer.parseInt(aforo_maximo));
        eventoEditar.setMaxEntradasPorUsuario(Integer.parseInt(maximo_entradas_usuario));   
        
        //eventoEditar.setEtiquetaList(evento.getEtiquetaList());
        
        if("Si".equals(asientos_asignados)){
            eventoEditar.setAsientosAsignados(true);
            eventoEditar.setNumFilas(Integer.parseInt(numero_filas));
            eventoEditar.setAsientosPorFila(Integer.parseInt(asientos_por_fila)); 
        }else{
            eventoEditar.setAsientosAsignados(false);
         }
        
        eventoFacade.edit(eventoEditar);
        listaEventos.add(eventoEditar);
        u.setEventoList(listaEventos);
        usuarioFacade.edit(u);
        }
     
        response.sendRedirect("ServletCreadorDeEventosListar");
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
