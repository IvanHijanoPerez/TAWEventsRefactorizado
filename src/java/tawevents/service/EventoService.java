/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import tawevents.dao.EtiquetaFacade;
import tawevents.dao.EventoFacade;
import tawevents.dao.UsuarioFacade;
import tawevents.dto.EtiquetaDTO;
import tawevents.dto.EventoDTO;
import tawevents.dto.UsuarioDTO;
import tawevents.entity.Etiqueta;
import tawevents.entity.Evento;
import tawevents.entity.Publico;
import tawevents.entity.Usuario;
import tawevents.entity.UsuarioDeEventos;

/**
 *
 * @author rafar
 */
@Stateless
public class EventoService {

    @EJB
    private EtiquetaFacade etiquetaFacade;

    @EJB
    private UsuarioFacade usuarioFacade;

    @EJB
    private EtiquetaService etiquetaService;

     @EJB
     private EventoFacade eventoFacade;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
     
     
     public List<EventoDTO> convertirAListaDTOdirectamente(List<Integer> lista){
         return this.convertirAListaDTO(this.convertirAListaEvento(lista));
     }
     
     public List<EventoDTO> convertirAListaDTO(List<Evento> lista){
         if (lista != null) {
            List<EventoDTO> listaDTO = new ArrayList<>();
            for (Evento e : lista) {
                listaDTO.add(e.getDTO());
            }
            return listaDTO;            
        } else {
            return null;
        } 
     }
     
     public List<Evento> convertirAListaEvento(List<Integer> lista){
         List<Evento> listaEvento = new ArrayList<>();
         if(lista != null ){
            for(int id : lista){
             listaEvento.add(eventoFacade.findById(id));
            }
            return listaEvento;
         }else{
             return null;
         }
        
     }
     
     public List<Integer> convertirALaInversa (List<EventoDTO> lista){
        List<Integer> res = new ArrayList<>();
        for(EventoDTO e : lista){
            res.add(e.getId());
        }
        return res;
    }
     
     public List<EventoDTO> findByTitulo(String titulo){
         List <Evento> lista = eventoFacade.findByTitulo(titulo);
         return convertirAListaDTO(lista);
     }
     
     public List<Evento> findByTituloObjetoCompleto(String titulo){
         return eventoFacade.findByTitulo(titulo);
     }
     
     public EventoDTO find(int id){
         return eventoFacade.findById(id).getDTO();
     }
     
     public Evento findById(int id){
         return eventoFacade.findById(id);
     }
     
     public List<Evento> findAll(){
         return eventoFacade.findAll();
     }
     
     public List<Evento> findByTituloHistorial(String palabra, UsuarioDeEventos usuarioDeEventos){
         return eventoFacade.findByTituloHistorial(palabra, usuarioDeEventos);
     }
     
     public List<Evento> findByEtiquetaHistorial(Etiqueta e, UsuarioDeEventos usuarioDeEventos){
         return eventoFacade.findByEtiquetaHistorial(e, usuarioDeEventos);
     }
     
     public List<Evento> findAllHistorial(UsuarioDeEventos usuarioDeEventos){
         return eventoFacade.findAllHistorial(usuarioDeEventos);
     }
     
     public List<Evento> findByTituloReserva(String palabra, UsuarioDeEventos usuarioDeEventos){
         return eventoFacade.findByTituloReserva(palabra, usuarioDeEventos);
     }
     
     public List<Evento> findByEtiquetaReserva(Etiqueta e, UsuarioDeEventos usuarioDeEventos){
         return eventoFacade.findByEtiquetaReserva(e, usuarioDeEventos);
     }
     
     public List<Evento> findAllReserva(UsuarioDeEventos usuarioDeEventos){
         return eventoFacade.findAllReserva(usuarioDeEventos);
     }
     
     public void remove(EventoDTO e){
         eventoFacade.remove(eventoFacade.find(e.getId()));
     }
     
     public List<EventoDTO> filtroNombre(String nombre){
        List<Evento> eventos = eventoFacade.findBySimilarNombre(nombre);
        return convertirAListaDTO(eventos);
    }
     
     public List<EventoDTO> filtroDescripcion(String descripcion){
        List<Evento> eventos = eventoFacade.findBySimilarDescripcion(descripcion);
        return convertirAListaDTO(eventos);
    }
     
     public List<EventoDTO> filtroFecha(Date fecha){
        List<Evento> eventos = eventoFacade.findBySimilarFecha(fecha);
        return convertirAListaDTO(eventos);
    }
     
     public List<EventoDTO> filtroFechaEntrada(Date fecha){
        List<Evento> eventos = eventoFacade.findBySimilarFechaEntrada(fecha);
        return convertirAListaDTO(eventos);
    }
     
     public List<EventoDTO> filtroPrecio(String precio){
        List<Evento> eventos = eventoFacade.findBySimilarPrecio(precio);
        return convertirAListaDTO(eventos);
    }
     
     public List<EventoDTO> filtroAforo(String aforo){
        List<Evento> eventos = eventoFacade.findBySimilarAforo(aforo);
        return convertirAListaDTO(eventos);
    }
     
     public List<EventoDTO> filtroMaxEntradasUsuario(String entradas){
        List<Evento> eventos = eventoFacade.findBySimilarMaxEntradasUsuario(entradas);
        return convertirAListaDTO(eventos);
    }
     
     public List<EventoDTO> filtroAsientosAsignados(String asiento){
        List<Evento> eventos = eventoFacade.findBySimilarAsientosAsignados(asiento);
        return convertirAListaDTO(eventos);
    }
     
     public List<EventoDTO> filtroNumFilas(String numero){
        List<Evento> eventos = eventoFacade.findBySimilarNumFilas(numero);
        return convertirAListaDTO(eventos);
    }
     
     public List<EventoDTO> filtroAsientosFila(String asientos){
        List<Evento> eventos = eventoFacade.findBySimilarAsientosFila(asientos);
        return convertirAListaDTO(eventos);
    }
     
     public List<EventoDTO> filtroCreador(String creador){
        List<Evento> eventos = eventoFacade.findBySimilarCreador(creador);
        return convertirAListaDTO(eventos);
    }
     
     public List<EventoDTO> filtroImagen(String imagen){
        List<Evento> eventos = eventoFacade.findBySimilarImagen(imagen);
        return convertirAListaDTO(eventos);
    }
     
     public List<EventoDTO> TodosEventos(){
        List<Evento> eventos = eventoFacade.findAll();
        return convertirAListaDTO(eventos);
    }
     
    public List<Evento> findByDisponiblesEtiqueta(Etiqueta e) {
        return eventoFacade.findByDisponiblesEtiqueta(e);
    }
    
    public List<Evento> findByDisponiblesMasPopulares() {
        return eventoFacade.findByDisponiblesMasPopulares();
    }
    
    public List<Evento> findByDisponiblesMasCercanos() {
        return eventoFacade.findByDisponiblesMasCercanos();
    }
     
     public EventoDTO buscarEvento (Integer id) {
        Evento evento = this.eventoFacade.find(id);
        if (evento != null) {
            return evento.getDTO();
        } else {
            return null;
        }
    }
     
     public void borrarEvento (EventoDTO evento) {
        Evento eventoF = this.eventoFacade.find(evento.getId());
        this.eventoFacade.remove(eventoF);
    }
     
     public String getEtiquetasToString(EventoDTO evento) {
        String str = "";
        for(Integer e : evento.getEtiquetaList()){
            str = str + this.etiquetaService.buscarEtiqueta(e).getNombre() + " ";
        }
        return str;
    }
     
     public void editarEvento(EventoDTO e){
         eventoFacade.edit(eventoFacade.find(e.getId()));
     }
     
     public void editarEvento (Integer eventoId, Integer idUsuario, String titulo, String descripcion, String etiquetas, Integer precio, String imagen, Date fecha, Date fecha_limite_entradas, Integer aforo_maximo, Integer maximo_entradas_usuario, String asientos_asignados, String numero_filas, String asientos_por_fila) {
        Evento eventoEditar = eventoFacade.find(eventoId);
        eventoEditar.setTitulo(titulo);
        eventoEditar.setDescripcion(descripcion);
        eventoEditar.setPrecioEntrada(precio); 
        eventoEditar.setImagen(imagen);
        eventoEditar.setFecha(fecha);
        eventoEditar.setFechaLimEntradas(fecha_limite_entradas);
        eventoEditar.setAforoMax(aforo_maximo);
        eventoEditar.setMaxEntradasPorUsuario(maximo_entradas_usuario);   
        if("Si".equals(asientos_asignados)){
            eventoEditar.setAsientosAsignados(true);
            eventoEditar.setNumFilas(Integer.parseInt(numero_filas));
            eventoEditar.setAsientosPorFila(Integer.parseInt(asientos_por_fila)); 
        }else{
            eventoEditar.setAsientosAsignados(false);
            eventoEditar.setNumFilas(null);
            eventoEditar.setAsientosPorFila(null); 
         }
        List<Etiqueta> etiquetaList = new ArrayList(); 
        String[] arrayEtiquetas = etiquetas.split(" ");
        for(String item : arrayEtiquetas){
            Etiqueta buscada = etiquetaFacade.findBySimilarNombreI(item);
            if(buscada==null){
                Etiqueta e = new Etiqueta();
                e.setNombre(item);
                etiquetaList.add(e);
                etiquetaFacade.create(e);  
            }else{
                etiquetaList.add(buscada);
            }
        }
        eventoEditar.setEtiquetaList(etiquetaList);
        eventoFacade.edit(eventoEditar);
     }
     
     public void crearEvento (Integer idUsuario, String titulo, String descripcion, String etiquetas, Integer precio, String imagen, Date fecha, Date fecha_limite_entradas, Integer aforo_maximo, Integer maximo_entradas_usuario, String asientos_asignados, String numero_filas, String asientos_por_fila) {
        Evento eventoEditar = new Evento();
        eventoEditar.setUsuario(usuarioFacade.find(idUsuario));
        eventoEditar.setTitulo(titulo);
        eventoEditar.setDescripcion(descripcion);
        eventoEditar.setPrecioEntrada(precio); 
        eventoEditar.setImagen(imagen);
        eventoEditar.setFecha(fecha);
        eventoEditar.setFechaLimEntradas(fecha_limite_entradas);
        eventoEditar.setAforoMax(aforo_maximo);
        eventoEditar.setMaxEntradasPorUsuario(maximo_entradas_usuario);   
        if("Si".equals(asientos_asignados)){
            eventoEditar.setAsientosAsignados(true);
            eventoEditar.setNumFilas(Integer.parseInt(numero_filas));
            eventoEditar.setAsientosPorFila(Integer.parseInt(asientos_por_fila));
        }else{
            eventoEditar.setAsientosAsignados(false);
            eventoEditar.setNumFilas(null);
            eventoEditar.setAsientosPorFila(null); 
         }
        List<Etiqueta> etiquetaList = new ArrayList(); 
        String[] arrayEtiquetas = etiquetas.split(" ");
        for(String item : arrayEtiquetas){
            Etiqueta buscada = etiquetaFacade.findBySimilarNombreI(item);
            if(buscada==null){
                Etiqueta e = new Etiqueta();
                e.setNombre(item);
                etiquetaList.add(e);
                etiquetaFacade.create(e);  
            }else{
                etiquetaList.add(buscada);
            }
        }
        eventoEditar.setEtiquetaList(etiquetaList);

        eventoFacade.create(eventoEditar);
        
    }
     
     public void addPublico(Evento evento, Publico publico) {
        List<Publico> listaPublico = evento.getPublicoList();
        listaPublico.add(publico);
        evento.setPublicoList(listaPublico);
        eventoFacade.edit(evento);
     }
     
     public void removePublico(Evento evento, Publico publico) {
        List<Publico> listaPublico = evento.getPublicoList();
        listaPublico.remove(publico);
        evento.setPublicoList(listaPublico);
        eventoFacade.edit(evento);
     }
}
