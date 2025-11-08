package proyecto.pkg1;

import javax.swing.SwingUtilities;

/**
 * Clase principal del programa.
 * Inicia la interfaz gráfica y muestra la ventana principal.
 */
public class Proyecto1 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Interfaz ui = new Interfaz();
            ui.mostrar();
        });
    }
}