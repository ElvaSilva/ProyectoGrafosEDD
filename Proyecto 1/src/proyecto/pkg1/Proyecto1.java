package proyecto.pkg1;

import javax.swing.*;
import java.awt.*;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;

public class Proyecto1 {
    // ---- IO ----
    private final ManejoArchivos manejo = new ManejoArchivos();

    // ---- TDA ----
    private Grafo grafo;         // estado actual (editable)
    private Grafo grafoBase;     // snapshot para "Reiniciar"

    // ---- Visual ----
    private Graph grafoVisual;
    private SwingViewer viewer;
    private ViewPanel viewPanel;

    // ---- UI ----
    private final JFrame frame = new JFrame("Red Social");
    private final JButton btnCargar = new JButton("Cargar Archivo");
    private final JButton btnGuardar = new JButton("Actualizar Repositorio");
    private final JButton btnReiniciar = new JButton("Reiniciar Grafo");
    private final JButton btnAgregarUsuario = new JButton("Agregar Usuario");
    private final JButton btnAgregarRelacion = new JButton("Agregar Relación");
    private final JButton btnEliminarUsuario = new JButton("Eliminar Usuario");
    private final JButton btnCfc = new JButton("Mostrar CFC (Kosaraju)");
    private final JButton btnFit = new JButton("Ajustar Vista");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Proyecto1().iniciarUI());
    }

    // Ejecuta Kosaraju sobre TDA y colorea nodos/aristas en GraphStream
    private void visualizarCFC() {
        if (grafo == null) return;

        grafo.establecer_falso();
        DFS dfs1 = new DFS(grafo);
        for (NodoGrafo n = grafo.pFirst; n != null; n = n.pnext)
            if (!n.visitado) dfs1.PrimerRecorrido(n);

        Kosaraju kosa = new Kosaraju(dfs1.pila, grafo);
        kosa.fuertemente_conectados();

        java.util.Map<String, Integer> compIndex = new java.util.HashMap<>();
        int idx = 0;
        StringBuilder mensaje = new StringBuilder("Componentes fuertemente conexos encontrados:\n\n");

        for (Componente c = kosa.firstcomp; c != null; c = c.next, idx++) {
            mensaje.append("CFC ").append(idx + 1).append(": ");
            java.util.List<String> miembros = new java.util.ArrayList<>();
            for (Arista a = c.primero; a != null; a = a.siguiente) {
                miembros.add(a.destino);
                compIndex.put(a.destino, idx);
            }
            mensaje.append(String.join(", ", miembros)).append("\n");
        }

        // colorear nodos
        for (NodoGrafo n = grafo.pFirst; n != null; n = n.pnext) {
            Node vn = grafoVisual.getNode(n.usuario);
            if (vn == null) continue;
            Integer c = compIndex.get(n.usuario);
            if (c == null) vn.removeAttribute("ui.class");
            else vn.setAttribute("ui.class", "c" + (c % 12));
        }

        // colorear aristas
        for (Edge e : grafoVisual.edges().toList()) {
            String u = e.getSourceNode().getId(), v = e.getTargetNode().getId();
            Integer cu = compIndex.get(u), cv = compIndex.get(v);
            if (cu != null && cv != null && cu.equals(cv))
                e.setAttribute("ui.class", "intra");
            else
                e.setAttribute("ui.class", "crossEdge");
        }

        viewer.disableAutoLayout();
        viewer.enableAutoLayout();
        viewPanel.revalidate();
        viewPanel.repaint();

        JOptionPane.showMessageDialog(frame, mensaje.toString());
    }

    private void iniciarUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 750);
        frame.setLayout(new BorderLayout());

        // Panel superior con botones
        JPanel top = new JPanel(new GridLayout(2, 4, 5, 5));
        top.add(btnCargar);
        top.add(btnCfc);
        top.add(btnGuardar);
        top.add(btnFit);
        top.add(btnAgregarUsuario);
        top.add(btnAgregarRelacion);
        top.add(btnEliminarUsuario);
        top.add(btnReiniciar);
        frame.add(top, BorderLayout.NORTH);

        // Deshabilitar todo menos "Cargar" al inicio
        setButtonsEnabled(false);

        prepararViewerEmbebido(); // crea el panel de GraphStream
        frame.setVisible(true);

        // --- Acciones de los botones ---

        btnCargar.addActionListener(e -> {
            grafo = manejo.subir_archivo();   // mantiene selectedArchivo adentro de 'manejo'
            if (grafo == null) return;

            // snapshot base para Reiniciar
            grafoBase = grafo.clonarProfundo();

            reiniciarViewerVisual(); // limpia viewer y reaplica estilos
            renderizarGrafo();
            setButtonsEnabled(true); // ahora sí habilitamos todo
        });

        btnGuardar.addActionListener(e -> {
            if (grafo == null) return;
            manejo.actualizar_archivo(grafo); // SOBRESCRIBE el archivo abierto
            // tras guardar, actualizamos el snapshot base
            grafoBase = grafo.clonarProfundo();
            JOptionPane.showMessageDialog(frame, "Repositorio actualizado.");
        });

        btnReiniciar.addActionListener(e -> reiniciarGrafo());

        btnAgregarUsuario.addActionListener(e -> {
            if (grafo == null) return;
            String nombre = JOptionPane.showInputDialog(frame, "Nombre del nuevo usuario:");
            if (nombre != null && !nombre.isBlank()) {
                agregarUsuario(nombre.trim());
            }
        });

        btnAgregarRelacion.addActionListener(e -> {
            if (grafo == null) return;
            String origen = JOptionPane.showInputDialog(frame, "Usuario ORIGEN:");
            String destino = JOptionPane.showInputDialog(frame, "Usuario DESTINO:");
            if (origen != null && destino != null && !origen.isBlank() && !destino.isBlank()) {
                agregarArista(origen.trim(), destino.trim());
            }
        });

        btnEliminarUsuario.addActionListener(e -> {
            if (grafo == null) return;
            String nombre = JOptionPane.showInputDialog(frame, "Usuario a eliminar:");
            if (nombre == null || nombre.isBlank()) return;
            nombre = nombre.trim();

            if (grafo.Buscar(nombre) == null) {
                JOptionPane.showMessageDialog(frame, "Usuario no encontrado.");
                return;
            }

            grafo.eliminarUsuarioCompleto(nombre);

            if (grafoVisual.getNode(nombre) != null) {
                grafoVisual.removeNode(nombre); // GraphStream quita aristas adyacentes automáticamente
            }

            renderizarGrafo(); // refresca vista
        });

        btnCfc.addActionListener(e -> {
            if (grafo == null) return;
            try {
                visualizarCFC();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error al calcular/visualizar CFC: " + ex.getMessage());
            }
        });

        btnFit.addActionListener(e -> viewer.getDefaultView().getCamera().resetView());
    }

    private void setButtonsEnabled(boolean loaded) {
        btnGuardar.setEnabled(loaded);
        btnReiniciar.setEnabled(loaded);
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
        aplicarEstilosGraficos(grafoVisual);

        viewer = new SwingViewer(grafoVisual, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();

        viewPanel = (ViewPanel) viewer.addDefaultView(false);
        frame.add(viewPanel, BorderLayout.CENTER);
    }

    // --- Reinicia por completo la capa visual (mantiene el JFrame y el ViewPanel) ---
    private void reiniciarViewerVisual() {
        grafoVisual.clear();                 // borra nodos/aristas/atributos
        aplicarEstilosGraficos(grafoVisual); // re-aplica stylesheet y flags gráficos
    }

    // --- Aplica stylesheet y flags de calidad ---
    private void aplicarEstilosGraficos(Graph g) {
        g.setAttribute("ui.stylesheet", CSS);
        g.setAttribute("ui.quality");
        g.setAttribute("ui.antialias");
    }

    // --- Botón "Reiniciar Grafo": restaura el TDA desde el snapshot y reconstruye la vista ---
    private void reiniciarGrafo() {
        if (grafoBase == null) {
            JOptionPane.showMessageDialog(frame, "No hay un estado base para reiniciar. Carga o guarda primero.");
            return;
        }
        grafo = grafoBase.clonarProfundo();  // restaura TDA
        reiniciarViewerVisual();             // limpia y reestiliza la capa visual
        renderizarGrafo();                   // vuelve a dibujar todo
        viewer.getDefaultView().getCamera().resetView();
    }

    // Renderiza el grafo completo desde el TDA
    private void renderizarGrafo() {
        if (grafo == null) return;

        // Asegura que existan todos los nodos
        for (NodoGrafo n = grafo.pFirst; n != null; n = n.pnext) {
            if (grafoVisual.getNode(n.usuario) == null) {
                grafoVisual.addNode(n.usuario).setAttribute("ui.label", n.usuario);
            }
        }

        // Asegura que existan todas las aristas dirigidas (solo si el destino existe)
        for (NodoGrafo n = grafo.pFirst; n != null; n = n.pnext) {
            for (Arista a = n.minilista.primero; a != null; a = a.siguiente) {
                if (!grafo.existe_nodo(a.destino)) continue;  // evita aristas a nodos eliminados
                String id = n.usuario + "->" + a.destino;
                if (grafoVisual.getEdge(id) == null) {
                    grafoVisual.addEdge(id, n.usuario, a.destino, true);
                }
            }
        }

        // Quita clases previas (por si venimos de una vista CFC)
        grafoVisual.nodes().forEach((Node n) -> n.removeAttribute("ui.class"));
        grafoVisual.edges().forEach((Edge e) -> e.removeAttribute("ui.class"));

        // Refresca vista
        viewPanel.revalidate();
        viewPanel.repaint();

        // Reinicia layout para recolocar los nodos
        viewer.disableAutoLayout();
        viewer.enableAutoLayout();
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
        if (o != null && !o.minilista.Buscar(destino)) {  // evita duplicados en TDA
            o.minilista.insertar_nueva(destino);
        }

        if (grafoVisual.getNode(origen) == null)
            grafoVisual.addNode(origen).setAttribute("ui.label", origen);
        if (grafoVisual.getNode(destino) == null)
            grafoVisual.addNode(destino).setAttribute("ui.label", destino);

        String id = origen + "->" + destino;
        if (grafoVisual.getEdge(id) == null)
            grafoVisual.addEdge(id, origen, destino, true);
    }

    private static final String CSS = """
        graph { padding: 60px; fill-color: #ffffff; }

        node {
            shape: circle;
            size: 22px;
            fill-color: #3a86ff;
            stroke-mode: plain;
            stroke-color: #1e3a8a;
            stroke-width: 2px;
            text-alignment: above;
            text-offset: 0px, -25px;
            text-size: 16;
            text-color: #222;
            text-style: bold;
            text-background-mode: plain;
            text-background-color: #ffffff;
            text-padding: 4px, 2px;
        }

        node.marked    { fill-color: #ff006e; stroke-color: #c9184a; size: 26px; text-color: #ff006e; }
        node.visited   { fill-color: #fb5607; }
        node.component { fill-color: #8338ec; }

        edge {
            shape: cubic-curve;
            arrow-shape: arrow;
            arrow-size: 12px, 8px;
            fill-color: rgba(100,100,100,0.6);
            size: 2px;
        }

        edge.highlighted { fill-color: #ffbe0b; size: 3px; }

        node.c0  { fill-color: #3a86ff; }
        node.c1  { fill-color: #ff006e; }
        node.c2  { fill-color: #8338ec; }
        node.c3  { fill-color: #ffbe0b; }
        node.c4  { fill-color: #fb5607; }
        node.c5  { fill-color: #2ec4b6; }
        node.c6  { fill-color: #8ecae6; }
        node.c7  { fill-color: #219ebc; }
        node.c8  { fill-color: #023047; }
        node.c9  { fill-color: #606c38; }
        node.c10 { fill-color: #009688; }
        node.c11 { fill-color: #ff7f50; }

        edge.intra     { fill-color: #222222; size: 3px; }
        edge.crossEdge { fill-color: rgba(120,120,120,0.35); size: 2px; }
    """;
}
