package proyecto.pkg1;

import javax.swing.SwingUtilities;

public class Proyecto1 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Interfaz ui = new Interfaz();
            ui.mostrar();
        });
    }
}