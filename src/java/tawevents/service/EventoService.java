/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.service;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import tawevents.dao.EventoFacade;
import tawevents.dto.EtiquetaDTO;
import tawevents.dto.EventoDTO;
import tawevents.dto.UsuarioDTO;
import tawevents.entity.Evento;
import tawevents.entity.Usuario;

/**
 *
 * @author rafar
 */
@Stateless
public class EventoService {
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
     
     public EventoDTO find(int id){
         return eventoFacade.findById(id).getDTO();
     }
     
     public void remove(EventoDTO e){
         eventoFacade.remove(eventoFacade.find(e.getId()));
     }
}
