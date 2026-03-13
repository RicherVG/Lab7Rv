/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author riche
 */

public class VentanaReproductor extends JFrame {

    private DefaultListModel<TemaMusical> modeloLista;
    private JList<TemaMusical> listaVisual;
    private MotorSonido motor;
    private ColeccionTemas coleccion;
    private ArchivoPlayList archivo;
    private JSlider sliderProgreso;
    private JLabel lblTiempoActual;
    private JLabel lblTiempoTotal;
    private Timer timerProgreso;
    private boolean isDragging = false;
    private final Color COLOR_FONDO = new Color(12, 12, 12);
    private final Color COLOR_PANEL_REPRODUCTOR = new Color(18, 18, 18);
    private final Color COLOR_VERDE = new Color(30, 215, 96);
    private final Color COLOR_TEXTO = new Color(255, 255, 255);
    private final Color COLOR_TEXTO_SECUNDARIO = new Color(167, 167, 167);
    private final Color COLOR_HOVER = new Color(43, 43, 43);

    public VentanaReproductor() {
        setTitle("Music Player");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());
        motor = new MotorSonido();
        archivo = new ArchivoPlayList("playlist.dat");
        try {
            coleccion = archivo.leerRespaldo();
        } catch (Exception e) {
            coleccion = new ColeccionTemas();
        }
        modeloLista = new DefaultListModel<>();
        for (TemaMusical t : coleccion.getBibliotecaTemas()) {
            modeloLista.addElement(t);
        }

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_FONDO);
        header.setBorder(new EmptyBorder(20, 30, 10, 30));
        JLabel title = new JLabel("RichMusic");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(COLOR_TEXTO);
        header.add(title, BorderLayout.WEST);
        JPanel btnHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnHeaderPanel.setOpaque(false);
        JButton btnAgregar = crearBotonTexto("Agregar", COLOR_VERDE, Color.BLACK);
        JButton btnRemove = crearBotonTexto("Eliminar", new Color(50, 50, 50), COLOR_TEXTO);
        btnHeaderPanel.add(btnAgregar);
        btnHeaderPanel.add(btnRemove);
        header.add(btnHeaderPanel, BorderLayout.EAST);
        listaVisual = new JList<>(modeloLista);
        listaVisual.setBackground(COLOR_FONDO);
        listaVisual.setForeground(COLOR_TEXTO);
        listaVisual.setSelectionBackground(COLOR_HOVER);
        listaVisual.setSelectionForeground(COLOR_TEXTO);
        listaVisual.setCellRenderer(new TemaMusicalRenderer());
        listaVisual.setFixedCellHeight(70);
        JScrollPane scrollPane = new JScrollPane(listaVisual);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(COLOR_FONDO);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(COLOR_PANEL_REPRODUCTOR);
        panelInferior.setPreferredSize(new Dimension(0, 100));
        panelInferior.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(40, 40, 40)));
        JPanel panelProgreso = new JPanel(new BorderLayout(10, 0));
        panelProgreso.setOpaque(false);
        panelProgreso.setBorder(new EmptyBorder(5, 50, 5, 50));
        lblTiempoActual = new JLabel("0:00");
        lblTiempoActual.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblTiempoActual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTiempoTotal = new JLabel("0:00");
        lblTiempoTotal.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblTiempoTotal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sliderProgreso = new JSlider(0, 100, 0);
        sliderProgreso.setBackground(COLOR_PANEL_REPRODUCTOR);
        sliderProgreso.setFocusable(false);
        sliderProgreso.setUI(new CustomSliderUI(sliderProgreso));
        panelProgreso.add(lblTiempoActual, BorderLayout.WEST);
        panelProgreso.add(sliderProgreso, BorderLayout.CENTER);
        panelProgreso.add(lblTiempoTotal, BorderLayout.EAST);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        panelBotones.setOpaque(false);
        JButton btnPlay = crearBotonImagen("/Icons/play.png", 55, true, "play");
        JButton btnPause = crearBotonImagen("/Icons/pause.png", 45, false, "pausa");
        JButton btnStop = crearBotonImagen("/Icons/stop.png", 45, false, "stop");

        panelBotones.add(btnStop);
        panelBotones.add(btnPlay);
        panelBotones.add(btnPause);
        panelInferior.add(panelProgreso, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
        timerProgreso = new Timer(500, e -> actualizarTick());
        btnAgregar.addActionListener(e -> agregarTema());
        btnRemove.addActionListener(e -> eliminarTema());
        btnPlay.addActionListener(e -> reproducir());
        btnPause.addActionListener(e -> motor.pausarSonido());
        btnStop.addActionListener(e -> {
            motor.detenerSonido();
            actualizarTick();
        });

        sliderProgreso.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                isDragging = true;
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (motor != null && motor.getDuracionTotal() > 0) {
                    double pct = sliderProgreso.getValue() / 100.0;
                    motor.buscarPosicion(motor.getDuracionTotal() * pct);
                }
                isDragging = false;
            }
        });
    }

    private void actualizarTick() {
        if (!isDragging) {
            double actual = motor.getTiempoActual();
            double total = motor.getDuracionTotal();
            if (total > 0) {
                int pos = (int) ((actual / total) * 100);
                sliderProgreso.setValue(pos);
                lblTiempoActual.setText(formatearTiempo(actual));
                lblTiempoTotal.setText(formatearTiempo(total));
            } else {
                sliderProgreso.setValue(0);
                lblTiempoActual.setText("0:00");
            }
        }
    }

    private String formatearTiempo(double segundos) {
        int m = (int) segundos / 60;
        int s = (int) segundos % 60;
        return String.format("%d:%02d", m, s);
    }

    private JButton crearBotonTexto(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);
                super.paint(g2, c);
                g2.dispose();
            }
        });
        return btn;
    }

    private JButton crearBotonImagen(String rutaIcono, int size, boolean main, String fallbackText) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(size, size));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            java.net.URL imgURL = getClass().getResource(rutaIcono);
            if (imgURL != null) {
                ImageIcon icono = new ImageIcon(imgURL);
                int imgSize = (int) (size * 0.7);
                Image img = icono.getImage().getScaledInstance(imgSize, imgSize, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(img));
            } else {
                btn.setText(fallbackText);
                btn.setForeground(main ? Color.BLACK : COLOR_TEXTO_SECUNDARIO);
                btn.setFont(new Font("Segoe UI", Font.PLAIN, main ? 24 : 18));
            }
        } catch (Exception e) {
            btn.setText(fallbackText);
        }

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
            }
        });
        return btn;
    }

    private void agregarTema() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de audio", "mp3", "wav", "m4a");
        chooser.setFileFilter(filtro);
        int r = chooser.showOpenDialog(this);

        if (r == JFileChooser.APPROVE_OPTION) {
            File archivoAudio = chooser.getSelectedFile();

            // Lógica de detección automática
            String duracionAuto = CalculadoraTiempoAudio.calcularDuracion(archivoAudio);
            File imgAuto = BuscadorPortadas.buscarImagen(archivoAudio.getParentFile());
            String portadaRuta = imgAuto != null ? imgAuto.getAbsolutePath() : "";

            // Formulario Moderno
            JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
            panel.setBackground(COLOR_PANEL_REPRODUCTOR);
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));

            JTextField txtNombre = new JTextField(archivoAudio.getName().replaceFirst("[.][^.]+$", ""));
            JTextField txtArtista = new JTextField();
            JTextField txtDuracion = new JTextField(duracionAuto);
            JTextField txtPortada = new JTextField(portadaRuta);
            JButton btnBuscarPortada = new JButton("...");
            btnBuscarPortada.addActionListener(ev -> {
                JFileChooser imgChooser = new JFileChooser();
                imgChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));
                if (imgChooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
                    txtPortada.setText(imgChooser.getSelectedFile().getAbsolutePath());
                }
            });

            JComboBox<TipoGenero> cbGenero = new JComboBox<>(TipoGenero.values());

            JLabel[] labels = {
                    new JLabel("Título:"), new JLabel("Artista:"),
                    new JLabel("Duración (m:ss):"), new JLabel("Imagen:"), new JLabel("Género:")
            };
            for (JLabel l : labels)
                l.setForeground(COLOR_TEXTO_SECUNDARIO);

            panel.add(labels[0]);
            panel.add(txtNombre);
            panel.add(labels[1]);
            panel.add(txtArtista);
            panel.add(labels[2]);
            panel.add(txtDuracion);

            JPanel portPanel = new JPanel(new BorderLayout(5, 0));
            portPanel.setOpaque(false);
            portPanel.add(txtPortada, BorderLayout.CENTER);
            portPanel.add(btnBuscarPortada, BorderLayout.EAST);

            panel.add(labels[3]);
            panel.add(portPanel);
            panel.add(labels[4]);
            panel.add(cbGenero);

            int result = JOptionPane.showConfirmDialog(this, panel, "Nueva Canción",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String nombre = txtNombre.getText().trim().isEmpty() ? "Desconocido" : txtNombre.getText();
                String artista = txtArtista.getText().trim().isEmpty() ? "Desconocido" : txtArtista.getText();
                String duracion = txtDuracion.getText().trim().isEmpty() ? "3:00" : txtDuracion.getText();
                String portada = txtPortada.getText().trim();
                TipoGenero genero = (TipoGenero) cbGenero.getSelectedItem();

                TemaMusical tema = new TemaMusical(nombre, artista, duracion, archivoAudio.getAbsolutePath(), portada,
                        genero);
                coleccion.registrarTema(tema);
                modeloLista.addElement(tema);
                guardarInfo();
            }
        }
    }

    private void guardarInfo() {
        try {
            archivo.guardarRespaldo(coleccion);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void reproducir() {
        TemaMusical tema = listaVisual.getSelectedValue();
        if (tema == null) {
            if (modeloLista.getSize() > 0) {
                listaVisual.setSelectedIndex(0);
                tema = listaVisual.getSelectedValue();
            } else
                return;
        }

        if (motor.esAudioActual(tema.getArchivoAudio())) {
            motor.iniciarSonido();
        } else {
            motor.cargarAudio(tema.getArchivoAudio());
            motor.iniciarSonido();
        }
        timerProgreso.start();
    }

    private void eliminarTema() {
        int pos = listaVisual.getSelectedIndex();
        if (pos != -1) {
            TemaMusical temaAEliminar = modeloLista.get(pos);
            if (motor.esAudioActual(temaAEliminar.getArchivoAudio())) {
                motor.detenerSonido();
                actualizarTick(); 
            }
            coleccion.quitarTema(pos);
            modeloLista.remove(pos);
            guardarInfo();
        }
    }

    private class TemaMusicalRenderer extends JPanel implements ListCellRenderer<TemaMusical> {
        private JLabel lblTitulo, lblInfo, lblDuracion, lblIcono;

        public TemaMusicalRenderer() {
            setLayout(new BorderLayout(15, 0));
            setBorder(new EmptyBorder(10, 20, 10, 20));
            setOpaque(true);

            lblIcono = new JLabel();
            lblIcono.setPreferredSize(new Dimension(50, 50));
            lblIcono.setOpaque(true);
            lblIcono.setBackground(new Color(30, 30, 30));
            lblIcono.setHorizontalAlignment(JLabel.CENTER);

            JPanel textPanel = new JPanel(new GridLayout(2, 1));
            textPanel.setOpaque(false);

            lblTitulo = new JLabel();
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblTitulo.setForeground(COLOR_TEXTO);

            lblInfo = new JLabel();
            lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblInfo.setForeground(COLOR_TEXTO_SECUNDARIO);

            textPanel.add(lblTitulo);
            textPanel.add(lblInfo);

            lblDuracion = new JLabel();
            lblDuracion.setForeground(COLOR_TEXTO_SECUNDARIO);

            add(lblIcono, BorderLayout.WEST);
            add(textPanel, BorderLayout.CENTER);
            add(lblDuracion, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends TemaMusical> list, TemaMusical value, int index,
                boolean isSelected, boolean cellHasFocus) {
            lblTitulo.setText(value.getTituloTema());
            lblInfo.setText(value.getAutorTema() + " • " + value.getTipoGenero());
            lblDuracion.setText(value.getTiempoTexto());

            if (value.getDireccionPortada() != null && !value.getDireccionPortada().isEmpty()) {
                try {
                    ImageIcon icono = new ImageIcon(value.getDireccionPortada());
                    Image img = icono.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    lblIcono.setIcon(new ImageIcon(img));
                    lblIcono.setText("");
                } catch (Exception e) {
                    lblIcono.setIcon(null);
                    lblIcono.setText("🎵");
                }
            } else {
                lblIcono.setIcon(null);
                lblIcono.setText("🎵");
                lblIcono.setForeground(COLOR_TEXTO_SECUNDARIO);
                lblIcono.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            }

            setBackground(isSelected ? COLOR_HOVER : COLOR_FONDO);
            lblTitulo.setForeground(isSelected ? COLOR_VERDE : COLOR_TEXTO);
            return this;
        }
    }

    private class CustomSliderUI extends javax.swing.plaf.basic.BasicSliderUI {
        public CustomSliderUI(JSlider b) {
            super(b);
        }

        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(60, 60, 60));
            g2.fillRoundRect(trackRect.x, trackRect.y + trackRect.height / 2 - 2, trackRect.width, 4, 4, 4);
            int thumbPos = thumbRect.x + thumbRect.width / 2;
            g2.setColor(COLOR_VERDE);
            g2.fillRoundRect(trackRect.x, trackRect.y + trackRect.height / 2 - 2, thumbPos - trackRect.x, 4, 4, 4);
        }

        @Override
        public void paintThumb(Graphics g) {
            if (isDragging) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillOval(thumbRect.x, thumbRect.y + 2, 12, 12);
            }
        }
    }

    private class CustomScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(70, 70, 70);
            this.trackColor = COLOR_FONDO;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            return b;
        }
    }
}
