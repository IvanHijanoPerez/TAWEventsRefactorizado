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
import tawevents.dto.EventoDTO;
import tawevents.entity.Evento;
import tawevents.dao.EtiquetaFacade;
import tawevents.dto.EtiquetaDTO;
import tawevents.entity.Etiqueta;

/**
 *
 * @author rafar
 */
@Stateless
public class EtiquetaService {
     @EJB
     private EtiquetaFacade etiquetaFacade;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
     
     public List<EtiquetaDTO> convertirAListaDTOdirectamente(List<Integer> lista){
         return this.convertirAListaDTO(this.convertirAListaEtiqueta(lista));
     }
     
     public List<EtiquetaDTO> convertirAListaDTO(List<Etiqueta> lista){
         if (lista != null) {
            List<EtiquetaDTO> listaDTO = new ArrayList<>();
            for (Etiqueta e : lista) {
                listaDTO.add(e.getDTO());
            }
            return listaDTO;            
        } else {
            return null;
        } 
     }
     
     public List<Etiqueta> convertirAListaEtiqueta(List<Integer> lista){
         List<Etiqueta> listaEtiqueta = new ArrayList<>();
         if(lista != null ){
            for(int id : lista){
             listaEtiqueta.add(etiquetaFacade.find(id));
            }
            return listaEtiqueta;
         }else{
             return null;
         }
        
     }
     
     public Etiqueta findByNombreExacto(String nombre) {
         return etiquetaFacade.findByNombreExacto(nombre);
     }
             
             
             
     public EtiquetaDTO findBySimilarNombre(String nombre){
         EtiquetaDTO et = etiquetaFacade.findBySimilarNombre(nombre).getDTO();
         return et;
     }
     
     public void remove(EtiquetaDTO e){
         etiquetaFacade.remove(etiquetaFacade.find(e.getId()));
     }
     
     public void edit(EtiquetaDTO e){
         etiquetaFacade.edit(etiquetaFacade.find(e.getId()));
     }
     
     public EtiquetaDTO filtroNombre(String nombre){
        Etiqueta etiqueta = etiquetaFacade.findBySimilarNombreI(nombre);
        return etiqueta.getDTO();
    }
     
     public EtiquetaDTO buscarEtiqueta (Integer id) {
        Etiqueta etiqueta = this.etiquetaFacade.find(id);
        if (etiqueta != null) {
            return etiqueta.getDTO();
        } else {
            return null;
        }
    }
     
     public int crearEtiqueta (String nombre) {
        Etiqueta etiqueta = new Etiqueta();            
        etiqueta.setNombre(nombre);      
        this.etiquetaFacade.create(etiqueta);   
        return etiqueta.getId();
    }
}
