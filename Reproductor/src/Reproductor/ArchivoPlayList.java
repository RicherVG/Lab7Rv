/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author riche
 */
public class ArchivoPlayList {
    private File respaldoPlaylist;
    public ArchivoPlayList(String ruta) {
        respaldoPlaylist = new File(ruta);
    }
    public void guardarRespaldo(ColeccionTemas coleccion) throws IOException {
        ObjectOutputStream salida =
                new ObjectOutputStream(new FileOutputStream(respaldoPlaylist));
        salida.writeObject(coleccion);
        salida.close();
    }
    public ColeccionTemas leerRespaldo() throws Exception {
        if (!respaldoPlaylist.exists()) {
            return new ColeccionTemas();
        }
        ObjectInputStream entrada =
                new ObjectInputStream(new FileInputStream(respaldoPlaylist));
        ColeccionTemas coleccion = (ColeccionTemas) entrada.readObject();
        entrada.close();
        return coleccion;
    }
}
