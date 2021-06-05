/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.dto;

import tawevents.entity.Evento;
import tawevents.entity.UsuarioDeEventos;

/**
 *
 * @author David
 */
public class PublicoDTO {
    private Integer id;
    private Integer fila;
    private Integer asiento;
    private Integer evento;
    private Integer usuarioDeEventos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFila() {
        return fila;
    }

    public void setFila(Integer fila) {
        this.fila = fila;
    }

    public Integer getAsiento() {
        return asiento;
    }

    public void setAsiento(Integer asiento) {
        this.asiento = asiento;
    }

    public Integer getEvento() {
        return evento;
    }

    public void setEvento(Integer evento) {
        this.evento = evento;
    }

    public Integer getUsuarioDeEventos() {
        return usuarioDeEventos;
    }

    public void setUsuarioDeEventos(Integer usuarioDeEventos) {
        this.usuarioDeEventos = usuarioDeEventos;
    }
}
