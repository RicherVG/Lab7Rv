/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

/**
 *
 * @author riche
 */
public class TemaMusical extends RecursoSon {
    private static final long serialVersionUID = 1L;
    public TemaMusical(String tituloTema,String autorTema,String tiempoTexto,String direccionAudio,String direccionPortada,TipoGenero tipoGenero) {
        super(tituloTema, autorTema, tiempoTexto,
                direccionAudio, direccionPortada, tipoGenero);
    }
    @Override
    public String obtenerCategoria() {
        return "Tema Musical";
    }

}
