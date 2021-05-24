/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import tawevents.dao.EstudioFacade;
import tawevents.dto.EstudioDTO;
import tawevents.entity.Estudio;

/**
 *
 * @author danie
 */
@Stateless
public class EstudioService {

    @EJB
    private EstudioFacade estudioFacade;

    public List<EstudioDTO> getListaEstudios(Integer usuarioID, Date desdeFecha, Date hastaFecha, String ordenarporfecha) {
        List<Estudio> lista;

        if (ordenarporfecha == null || ordenarporfecha.equals("desdeprimeros")) {
            lista = this.estudioFacade.findByDesdeYHasta(usuarioID, desdeFecha, hastaFecha);
        } else {
            lista = this.estudioFacade.findByDesdeYHastaOrdenFechas(usuarioID, desdeFecha, hastaFecha);
        }

        return convertirAListaDTO(lista);
    }

    protected List<EstudioDTO> convertirAListaDTO(List<Estudio> lista) {
        if (lista != null) {
            List<EstudioDTO> listaDTO = new ArrayList<EstudioDTO>();
            for (Estudio estudio : lista) {
                listaDTO.add(estudio.getDTO());
            }
            return listaDTO;
        } else {
            return null;
        }
    }

    public Date[] parseFiltroEstudioFechas(String strDesdeFecha, String strHastaFecha) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date[] fechas = new Date[2];
        fechas[0] = null;
        fechas[1] = null;
        try {
            fechas[0] = (strDesdeFecha != null && !strDesdeFecha.isEmpty()) ? formatoFecha.parse(strDesdeFecha) : formatoFecha.parse("2000-01-01");
            fechas[1] = (strHastaFecha != null && !strHastaFecha.isEmpty()) ? formatoFecha.parse(strHastaFecha) : new Date();
        } catch (ParseException ex) {
            Logger.getLogger(EstudioService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fechas;
    }
}
