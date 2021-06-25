/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.servlet;

import java.io.IOException;
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
import tawevents.service.UsuarioService;

/**
 *
 * @author rafar
 */
@WebServlet(name = "ServletEventoGuardar", urlPatterns = {"/ServletEventoGuardar"})
public class ServletEventoGuardar extends HttpServlet {

    @EJB
    private EventoService eventoService;
    
    @EJB
    private EtiquetaService etiquetaService;
    
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
        UsuarioDTO u = (UsuarioDTO) session.getAttribute("usuario");
        
        String titulo, descripcion, imagen, aforo_maximo, maximo_entradas_usuario, asientos_asignados, numero_filas, asientos_por_fila, precio, etiquetas; 
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        Date fecha_limite_entradas = null;
        EventoDTO evento = new EventoDTO();
        
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
        evento.setUsuario(u.getId());  
        
        List<Integer> etiquetaList = new ArrayList();
        String[] arrayEtiquetas = etiquetas.split(" ");
        for(String item : arrayEtiquetas){
            EtiquetaDTO buscada = etiquetaService.findBySimilarNombre(item);
            if(buscada==null){
                /*   EtiquetaDTO e = new EtiquetaDTO();
                  e.setNombre(item);
                 etiquetaList.add(e);
                 etiquetaService.create(e);  */
                
                int id = etiquetaService.crearEtiqueta(item);
                etiquetaList.add(id);
            }else{
                etiquetaList.add(buscada.getId());
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
               EventoDTO eventoEditar = eventoService.find(idEventoEditar);
               request.setAttribute("evento", eventoEditar);
               request.setAttribute("mensajeError", mensajeError);
               RequestDispatcher rd = request.getRequestDispatcher("editarEventoCreadorDeEventos.jsp");
               rd.forward(request, response); 
            }
        }

        if(request.getParameter("idEventoEditar") == null){ //estamos en guardar
            eventoService.crearEvento(u.getId(), titulo, descripcion, etiquetas, Integer.parseInt(precio), imagen, fecha, fecha_limite_entradas, Integer.parseInt(aforo_maximo), Integer.parseInt(maximo_entradas_usuario), asientos_asignados, numero_filas, asientos_por_fila);
            
            int idMasAlta = eventoService.findIdMasAlta();
            EventoDTO ev = eventoService.find(idMasAlta);
            
            for(EtiquetaDTO e : etiquetaService.convertirAListaDTOdirectamente(ev.getEtiquetaList())){
            List<Integer> eventoList = e.getEventoList();
            eventoList.add(ev.getId());
            e.setEventoList(eventoList);
            etiquetaService.edit(e);
        }
            List <EventoDTO> listaEventos = eventoService.convertirAListaDTOdirectamente(u.getEventoList());
            listaEventos.add(ev);
            u.setEventoList(eventoService.convertirALaInversa(listaEventos));
            usuarioService.edit(u);
        }else{ // estamos en editar
            
        int idEventoEditar = Integer.parseInt(request.getParameter("idEventoEditar"));
        eventoService.editarEvento2(idEventoEditar, u.getId(), titulo, descripcion, Integer.parseInt(precio), imagen, fecha, fecha_limite_entradas, Integer.parseInt(aforo_maximo), Integer.parseInt(maximo_entradas_usuario), asientos_asignados, numero_filas, asientos_por_fila);
        
        /*
        EventoDTO eventoEditar = eventoService.find(idEventoEditar);
        
        List <EventoDTO> listaEventos = eventoService.convertirAListaDTOdirectamente(u.getEventoList());
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
        
        eventoService.editarEvento(eventoEditar);
        listaEventos.add(eventoEditar);
        u.setEventoList(eventoService.convertirALaInversa(listaEventos));
        usuarioService.edit(u); */
        
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
