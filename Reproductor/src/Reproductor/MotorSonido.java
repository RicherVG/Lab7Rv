/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import java.io.File;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
/**
 *
 * @author riche
 */
public class MotorSonido implements ControlMusical {
    private MediaPlayer reproductor;
    private static boolean iniciado = false;
    public MotorSonido() {
        if (!iniciado) {
            java.io.PrintStream errOriginal = System.err;
            System.setErr(new java.io.PrintStream(new java.io.OutputStream() {
                public void write(int b) {
                }
            }));
            try {
                new JFXPanel();
            } finally {
                System.setErr(errOriginal);
            }
            iniciado = true;
        }
    }

    public void cargarAudio(File archivo) {
        try {
            if (reproductor != null) {
                reproductor.stop();
                reproductor.dispose();
            }
            String ruta = archivo.toURI().toString();
            Media mediaAudio = new Media(ruta);
            reproductor = new MediaPlayer(mediaAudio);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void iniciarSonido() {
        if (reproductor != null) {
            reproductor.play();
        }
    }

    @Override
    public void pausarSonido() {
        if (reproductor != null) {
            reproductor.pause();
        }
    }

    @Override
    public void detenerSonido() {
        if (reproductor != null) {
            reproductor.stop();
        }
    }

    public double getTiempoActual() {
        if (reproductor != null && reproductor.getCurrentTime() != null) {
            return reproductor.getCurrentTime().toSeconds();
        }
        return 0;
    }

    public double getDuracionTotal() {
        if (reproductor != null && reproductor.getTotalDuration() != null) {
            return reproductor.getTotalDuration().toSeconds();
        }
        return 0;
    }

    public void buscarPosicion(double segundos) {
        if (reproductor != null) {
            reproductor.seek(javafx.util.Duration.seconds(segundos));
        }
    }

    public boolean esAudioActual(File archivo) {
        if (reproductor != null && reproductor.getMedia() != null) {
            String rutaTarget = archivo.toURI().toString();
            return reproductor.getMedia().getSource().equals(rutaTarget);
        }
        return false;
    }

    public MediaPlayer getMediaPlayer() {
        return reproductor;
    }
}
