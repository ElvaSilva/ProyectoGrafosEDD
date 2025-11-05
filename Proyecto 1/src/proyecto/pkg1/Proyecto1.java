package proyecto.pkg1;

import javax.swing.*;
import java.awt.*;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;

public class Proyecto1 {
    // ---- IO ----
    private final ManejoArchivos manejo = new ManejoArchivos();

    // ---- TDA ----
    private Grafo grafo;

    // ---- Visual ----
    private Graph grafoVisual;
    private SwingViewer viewer;
    private ViewPanel viewPanel;

    // ---- UI ----
    private final JFrame frame = new JFrame("Red Social");
    private final JButton btnCargar = new JButton("Cargar Archivo");
    private final JButton btnGuardar = new JButton("Actualizar Repositorio");
    private final JButton btnMostrar = new JButton("Mostrar Grafo");
    private final JButton btnAgregarUsuario = new JButton("Agregar Usuario");
    private final JButton btnAgregarRelacion = new JButton("Agregar Relación");
    private final JButton btnEliminarUsuario = new JButton("Eliminar Usuario");
    private final JButton btnCfc = new JButton("Mostrar CFC (Kosaraju)");
    private final JButton btnFit = new JButton("Ajustar Vista");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Proyecto1().iniciarUI());
    }

    private void iniciarUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 750);
        frame.setLayout(new BorderLayout());

        // Panel superior con botones
        JPanel top = new JPanel(new GridLayout(2, 4, 5, 5));
        top.add(btnCargar);
        top.add(btnGuardar);
        top.add(btnMostrar);
        top.add(btnFit);
        top.add(btnAgregarUsuario);
        top.add(btnAgregarRelacion);
        top.add(btnEliminarUsuario);
        top.add(btnCfc);
        frame.add(top, BorderLayout.NORTH);

        // Deshabilitar todo menos "Cargar" al inicio
        setButtonsEnabled(false);

        prepararViewerEmbebido(); // crea el panel de GraphStream
        frame.setVisible(true);

        // --- Acciones de los botones ---

        btnCargar.addActionListener(e -> {
            grafo = manejo.subir_archivo();   // mantiene selectedArchivo adentro de 'manejo'
            if (grafo == null) {
                JOptionPane.showMessageDialog(frame, "No se cargó ningún archivo.");
                return;
            }
            renderizarGrafo();
            setButtonsEnabled(true);          // ahora sí habilitamos todo
        });

        btnGuardar.addActionListener(e -> {
            if (grafo == null) {
                JOptionPane.showMessageDialog(frame, "No hay grafo cargado.");
                return;
            }
            manejo.actualizar_archivo(grafo); // SOBRESCRIBE el archivo abierto
            JOptionPane.showMessageDialog(frame, "Repositorio actualizado.");
        });

        btnMostrar.addActionListener(e -> {
            if (grafo == null) {
                JOptionPane.showMessageDialog(frame, "Primero carga un archivo.");
                return;
            }
            renderizarGrafo();
        });

        btnAgregarUsuario.addActionListener(e -> {
            if (grafo == null) {
                JOptionPane.showMessageDialog(frame, "Primero carga un archivo.");
                return;
            }
            String nombre = JOptionPane.showInputDialog(frame, "Nombre del nuevo usuario:");
            if (nombre != null && !nombre.isBlank()) {
                agregarUsuario(nombre.trim());
            }
        });

        btnAgregarRelacion.addActionListener(e -> {
            if (grafo == null) {
                JOptionPane.showMessageDialog(frame, "Primero carga un archivo.");
                return;
            }
            String origen = JOptionPane.showInputDialog(frame, "Usuario ORIGEN:");
            String destino = JOptionPane.showInputDialog(frame, "Usuario DESTINO:");
            if (origen != null && destino != null && !origen.isBlank() && !destino.isBlank()) {
                agregarArista(origen.trim(), destino.trim());
            }
        });

        btnEliminarUsuario.addActionListener(e -> {
            if (grafo == null) {
                JOptionPane.showMessageDialog(frame, "Primero carga un archivo.");
                return;
            }
            String nombre = JOptionPane.showInputDialog(frame, "Usuario a eliminar:");
            if (nombre == null || nombre.isBlank()) return;

            NodoGrafo n = grafo.Buscar(nombre.trim());
            if (n == null) {
                JOptionPane.showMessageDialog(frame, "Usuario no encontrado.");
                return;
            }
            grafo.Eliminar(n);
            if (grafoVisual.getNode(nombre.trim()) != null) {
                grafoVisual.removeNode(nombre.trim());
            }
        });

        btnCfc.addActionListener(e -> {
            if (grafo == null) {
                JOptionPane.showMessageDialog(frame, "Primero carga un archivo.");
                return;
            }
            // PENDIENTE: Ejecuta algoritmo de Kosaraju
            DFS dfs = new DFS(grafo);
            dfs.PrimerRecorrido(grafo.pFirst);
            JOptionPane.showMessageDialog(frame, "CFC calculados (implementar visualización).");
        });

        btnFit.addActionListener(e -> viewer.getDefaultView().getCamera().resetView());
    }

    private void setButtonsEnabled(boolean loaded) {
        btnGuardar.setEnabled(loaded);
        btnMostrar.setEnabled(loaded);
        btnAgregarUsuario.setEnabled(loaded);
        btnAgregarRelacion.setEnabled(loaded);
        btnEliminarUsuario.setEnabled(loaded);
        btnCfc.setEnabled(loaded);
        btnFit.setEnabled(loaded);
        // btnCargar siempre habilitado
        btnCargar.setEnabled(true);
    }

    // --- Configurar GraphStream embebido ---
    private void prepararViewerEmbebido() {
        System.setProperty("org.graphstream.ui", "swing");
        grafoVisual = new SingleGraph("Red Social");
        grafoVisual.setAttribute("ui.quality");
        grafoVisual.setAttribute("ui.antialias");

        viewer = new SwingViewer(grafoVisual, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();

        viewPanel = (ViewPanel) viewer.addDefaultView(false);
        frame.add(viewPanel, BorderLayout.CENTER);
    }

    // --- Renderiza el grafo completo desde el TDA ---
    private void renderizarGrafo() {
        grafoVisual.clear();

        for (NodoGrafo n = grafo.pFirst; n != null; n = n.pnext) {
            if (grafoVisual.getNode(n.usuario) == null) {
                grafoVisual.addNode(n.usuario).setAttribute("ui.label", n.usuario);
            }
        }

        for (NodoGrafo n = grafo.pFirst; n != null; n = n.pnext) {
            for (Arista a = n.minilista.primero; a != null; a = a.siguiente) {
                String id = n.usuario + "->" + a.destino;
                if (grafoVisual.getEdge(id) == null) {
                    grafoVisual.addEdge(id, n.usuario, a.destino, true);
                }
            }
        }

        viewPanel.revalidate();
        viewPanel.repaint();
    }

    // --- Actualiza ambos (TDA y GraphStream) ---
    private void agregarUsuario(String usuario) {
        if (!grafo.existe_nodo(usuario)) grafo.insertar(usuario);
        if (grafoVisual.getNode(usuario) == null)
            grafoVisual.addNode(usuario).setAttribute("ui.label", usuario);
    }

    private void agregarArista(String origen, String destino) {
        if (!grafo.existe_nodo(origen))  grafo.insertar(origen);
        if (!grafo.existe_nodo(destino)) grafo.insertar(destino);

        NodoGrafo o = grafo.Buscar(origen);
        if (o != null) o.minilista.insertar_nueva(destino);

        if (grafoVisual.getNode(origen) == null)
            grafoVisual.addNode(origen).setAttribute("ui.label", origen);
        if (grafoVisual.getNode(destino) == null)
            grafoVisual.addNode(destino).setAttribute("ui.label", destino);

        String id = origen + "->" + destino;
        if (grafoVisual.getEdge(id) == null)
            grafoVisual.addEdge(id, origen, destino, true);
    }
}
