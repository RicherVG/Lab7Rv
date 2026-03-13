/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author riche
 */

public class ColeccionTemas implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<TemaMusical> bibliotecaTemas;
    public ColeccionTemas() {
        bibliotecaTemas = new ArrayList<>();
    }

    public void registrarTema(TemaMusical tema) {
        bibliotecaTemas.add(tema);
    }

    public void quitarTema(int posicionTema) {
        if (posicionTema >= 0 && posicionTema < bibliotecaTemas.size()) {
            bibliotecaTemas.remove(posicionTema);
        }
    }
    public TemaMusical buscarTemaEn(int posicionTema) {
        if (posicionTema >= 0 && posicionTema < bibliotecaTemas.size()) {
            return bibliotecaTemas.get(posicionTema);
        }
        return null;
    }
    public ArrayList<TemaMusical> getBibliotecaTemas() {
        return bibliotecaTemas;
    }
}
