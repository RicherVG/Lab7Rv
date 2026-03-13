/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import java.io.File;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

/**
 *
 * @author riche
 */
public class CalculadoraTiempoAudio {
    public static String calcularDuracion(File archivoAudio) {
        try {
            AudioFileFormat formato = AudioSystem.getAudioFileFormat(archivoAudio);
            long frames = formato.getFrameLength();
            float frameRate = formato.getFormat().getFrameRate();
            if (frames > 0 && frameRate > 0) {
                int segundosTotales = (int) (frames / frameRate);
                int minutos = segundosTotales / 60;
                int segundos = segundosTotales % 60;
                return String.format("%02d:%02d", minutos, segundos);
            }
        } catch (Exception e) {
        }
        return "00:00"; 
    }
}
