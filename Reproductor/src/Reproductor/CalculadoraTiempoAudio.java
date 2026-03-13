package Reproductor;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.embed.swing.JFXPanel;

public class CalculadoraTiempoAudio {
    private static boolean jfxIniciado = false;

    private static void iniciarJFX() {
        if (!jfxIniciado) {
            try {
                new JFXPanel();
                jfxIniciado = true;
            } catch (Exception e) {}
        }
    }

    public static String calcularDuracion(File archivoAudio) {
        iniciarJFX();
        final String[] resultado = {"00:00"};
        CountDownLatch latch = new CountDownLatch(1);

        try {
            Media media = new Media(archivoAudio.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnReady(() -> {
                Duration duration = media.getDuration();
                if (duration != null) {
                    int segundosTotales = (int) duration.toSeconds();
                    int minutos = segundosTotales / 60;
                    int segundos = segundosTotales % 60;
                    resultado[0] = String.format("%02d:%02d", minutos, segundos);
                }
                mediaPlayer.dispose();
                latch.countDown();
            });

            mediaPlayer.setOnError(() -> {
                mediaPlayer.dispose();
                latch.countDown();
            });

            if (!latch.await(2, TimeUnit.SECONDS)) {
                mediaPlayer.dispose();
            }
        } catch (Exception e) {
            return "00:00";
        }
        
        return resultado[0];
    }
}
