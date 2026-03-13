package Reproductor;


import java.io.File;
import java.io.Serializable;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author riche
 */
public abstract class RecursoSon implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String tituloTema;
    protected String autorTema;
    protected String tiempoTexto;
    protected String direccionAudio;
    protected String direccionPortada;
    protected TipoGenero tipoGenero;
    public RecursoSon(String tituloTema, String autorTema,String tiempoTexto,String direccionAudio,String direccionPortada,TipoGenero tipoGenero) {
        this.tituloTema = tituloTema;
        this.autorTema = autorTema;
        this.tiempoTexto = tiempoTexto;
        this.direccionAudio = direccionAudio;
        this.direccionPortada = direccionPortada;
        this.tipoGenero = tipoGenero;
    }

    public abstract String obtenerCategoria();
    public String getTituloTema() {
        return tituloTema;
    }
    public String getAutorTema() {
        return autorTema;
    }
    public String getTiempoTexto() {
        return tiempoTexto;
    }
    public String getDireccionAudio() {
        return direccionAudio;
    }
    public String getDireccionPortada() {
        return direccionPortada;
    }
    public TipoGenero getTipoGenero() {
        return tipoGenero;
    }
    public File getArchivoAudio() {
        return new File(direccionAudio);
    }
    @Override
    public String toString() {
        return tituloTema + " - " + autorTema;
    }

}
