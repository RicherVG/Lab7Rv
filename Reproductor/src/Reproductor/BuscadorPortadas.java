/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import java.io.File;

/**
 *
 * @author riche
 */
public class BuscadorPortadas {
    public static File buscarImagen(File carpeta) {
        if (carpeta == null || !carpeta.exists()) {
            return null;
        }
        File[] archivos = carpeta.listFiles();
        if (archivos == null) {
            return null;
        }
        for (File f : archivos) {
            if (f.isDirectory()) {

                File resultado = buscarImagen(f);

                if (resultado != null) {
                    return resultado;
                }
            } else {
                String nombre = f.getName().toLowerCase();
                if (nombre.endsWith(".jpg") ||
                        nombre.endsWith(".png") ||
                        nombre.endsWith(".jpeg")) {
                    return f;
                }
            }
        }
        return null;
    }
}
