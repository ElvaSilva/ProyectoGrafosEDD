/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

/**
 *
 * @author remo
 */

/**
 * Interfaz gráfica con JFrame y GraphStream
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;

/**
 * Clase que implementa la interfaz gráfica de la red social.
 * 
 * Permite al usuario:
 * <ul>
 *   <li>Cargar un archivo de texto con usuarios y sus relaciones.</li>
 *   <li>Agregar, eliminar o conectar usuarios dentro del grafo.</li>
 *   <li>Visualizar el grafo usando GraphStream.</li>
 *   <li>Aplicar el algoritmo de Kosaraju para mostrar los componentes fuertemente conexos.</li>
 *   <li>Guardar los cambios realizados en el archivo original.</li>
 * </ul>
 */
public class Interfaz {
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

    // ---- Estado ----
    private boolean cambiosPendientes = false;
    
    public Interfaz() {
        prepararVentana();
        prepararViewerEmbebido();
        setButtonsEnabled(false);
        wireEventos();

        // ======= Auto-cargar usuarios.txt al iniciar (si existe) =======
        try {
            File archivoInicial = new File("usuarios.txt");
            if (archivoInicial.exists() && archivoInicial.isFile()) {
                Grafo grafoInicial = ManejoArchivos.cargarDesdeArchivo(archivoInicial);
                if (grafoInicial != null) {
                    grafo = grafoInicial;
                    grafoBase = grafo.clonarProfundo();
                    cambiosPendientes = false;

                    reiniciarViewerVisual();
                    renderizarGrafo();
                    setButtonsEnabled(true);
                }
            }
        } catch (Throwable t) {
            // Si falla la autocarga, se ignora el error
        }

        // Aviso si cierran con cambios pendientes
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!confirmarGuardarSiHayCambios()) {
                    // cancelar cierre
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                } else {
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            }
        });

        // Mensaje de ayuda al iniciar
        JOptionPane.showMessageDialog(frame,
            """
            Red Social

            Instrucciones de uso:
            1. Presione "Cargar Archivo" para abrir un grafo desde un archivo de texto.
            2. Use "Agregar Usuario" para añadir nuevos nodos (debe incluir el símbolo '@' antes del nombre, por ejemplo: @usuario).
            3. Use "Agregar Relación" para crear conexiones dirigidas entre usuarios.
            4. "Eliminar Usuario" quita el nodo y todas sus relaciones.
            5. "Mostrar CFC (Kosaraju)" identifica los Componentes Fuertemente Conexos.
            6. "Actualizar Repositorio" guarda los cambios al archivo original.
            7. "Reiniciar Grafo" restaura el estado base (último guardado).
            8. "Ajustar Vista" centra el grafo en pantalla.
            """,
            "Instrucciones de Uso",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void mostrar() {
        frame.setVisible(true);
    }

    /* ===================== Confirmación de guardado ===================== */

    /**
     * Si hay cambios en memoria, pregunta al usuario:
     *   - Guardar y continuar
     *   - Continuar sin guardar
     *   - Cancelar
     * Devuelve true si se puede continuar con la acción; false si se cancela.
     */
    private boolean confirmarGuardarSiHayCambios() {
        if (grafo == null || !cambiosPendientes) return true;

        Object[] opciones = {"Guardar y continuar", "Continuar sin guardar", "Cancelar"};
        int eleccion = JOptionPane.showOptionDialog(
            frame,
            "Tienes cambios sin guardar. ¿Deseas guardarlos antes de continuar?",
            "Cambios sin guardar",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            opciones,
            opciones[0]
        );

        if (eleccion == JOptionPane.CLOSED_OPTION || eleccion == 2) {
            // Cancelar
            return false;
        }
        if (eleccion == 0) {
            // Guardar
            manejo.actualizar_archivo(grafo);
            grafoBase = grafo.clonarProfundo();
            cambiosPendientes = false;
        }
        // YES o NO => continuar
        return true;
    }

    /* ===================== UI ===================== */

    /**
     * Configura el diseño de la ventana principal y sus botones.
     */
    private void prepararVentana() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 750);
        frame.setLayout(new BorderLayout());

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
    }

    /**
     * Activa o desactiva botones dependiendo del estado de carga del grafo.
     * @param loaded true si hay un grafo cargado, false en caso contrario.
     */
    private void setButtonsEnabled(boolean loaded) {
        btnGuardar.setEnabled(loaded);
        btnReiniciar.setEnabled(loaded);
        btnAgregarUsuario.setEnabled(loaded);
        btnAgregarRelacion.setEnabled(loaded);
        btnEliminarUsuario.setEnabled(loaded);
        btnCfc.setEnabled(loaded);
        btnFit.setEnabled(loaded);
        btnCargar.setEnabled(true); // siempre habilitado
    }

     /**
     * Define los eventos asociados a cada botón de la interfaz.
     */
    private void wireEventos() {
        // ======= Confirmar antes de cargar otro archivo =======
        btnCargar.addActionListener(e -> {
            if (!confirmarGuardarSiHayCambios()) return;

            Grafo grafoNuevo = manejo.subir_archivo();
            if (grafoNuevo == null) return;

            grafo = grafoNuevo;
            grafoBase = grafo.clonarProfundo(); // snapshot base
            cambiosPendientes = false;

            reiniciarViewerVisual();
            renderizarGrafo();
            setButtonsEnabled(true);
        });

        // Guardar
        btnGuardar.addActionListener(e -> {
            if (grafo == null) return;
            manejo.actualizar_archivo(grafo);
            grafoBase = grafo.clonarProfundo(); // actualizar snapshot
            cambiosPendientes = false;
            JOptionPane.showMessageDialog(frame, "Repositorio actualizado.");
        });

        btnReiniciar.addActionListener(e -> reiniciarGrafo());

        btnAgregarUsuario.addActionListener(e -> {
            if (grafo == null) return;
            String nombreUsuario = JOptionPane.showInputDialog(frame, "Nombre del nuevo usuario (ej: @usuario):");
            if (nombreUsuario != null) {
                nombreUsuario = nombreUsuario.trim();
                if (nombreUsuario.isBlank()) return;

                // Recomendación según ayuda: nombre debe empezar por '@'
                if (!nombreUsuario.startsWith("@")) {
                    int respuesta = JOptionPane.showConfirmDialog(
                        frame,
                        "El nombre no comienza con '@'. ¿Deseas continuar de todas formas?",
                        "Convención de nombres",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    );
                    if (respuesta != JOptionPane.YES_OPTION) return;
                }

                agregarUsuario(nombreUsuario);
            }
        });

        btnAgregarRelacion.addActionListener(e -> {
            if (grafo == null) return;
            String usuarioOrigen = JOptionPane.showInputDialog(frame, "Usuario ORIGEN (ej: @a):");
            String usuarioDestino = JOptionPane.showInputDialog(frame, "Usuario DESTINO (ej: @b):");
            if (usuarioOrigen != null && usuarioDestino != null) {
                usuarioOrigen = usuarioOrigen.trim();
                usuarioDestino = usuarioDestino.trim();
                if (!usuarioOrigen.isBlank() && !usuarioDestino.isBlank()) {
                    agregarArista(usuarioOrigen, usuarioDestino);
                }
            }
        });

        btnEliminarUsuario.addActionListener(e -> {
            if (grafo == null) return;
            String usuarioAEliminar = JOptionPane.showInputDialog(frame, "Usuario a eliminar:");
            if (usuarioAEliminar == null || usuarioAEliminar.isBlank()) return;
            usuarioAEliminar = usuarioAEliminar.trim();

            if (grafo.Buscar(usuarioAEliminar) == null) {
                JOptionPane.showMessageDialog(frame, "Usuario no encontrado.");
                return;
            }

            grafo.eliminarUsuarioCompleto(usuarioAEliminar);
            cambiosPendientes = true;

            if (grafoVisual.getNode(usuarioAEliminar) != null) {
                grafoVisual.removeNode(usuarioAEliminar); // GraphStream elimina aristas adyacentes
            }

            renderizarGrafo();
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

    /* ===================== GraphStream ===================== */

    /**
     * Configura el visor de GraphStream embebido dentro de la interfaz.
     */
    private void prepararViewerEmbebido() {
        System.setProperty("org.graphstream.ui", "swing");
        grafoVisual = new SingleGraph("Red Social");
        aplicarEstilosGraficos(grafoVisual);

        viewer = new SwingViewer(grafoVisual, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();

        viewPanel = (ViewPanel) viewer.addDefaultView(false);
        frame.add(viewPanel, BorderLayout.CENTER);
    }

    private void reiniciarViewerVisual() {
        grafoVisual.clear();
        aplicarEstilosGraficos(grafoVisual);
    }

    private void aplicarEstilosGraficos(Graph g) {
        g.setAttribute("ui.stylesheet", CSS);
        g.setAttribute("ui.quality");
        g.setAttribute("ui.antialias");
    }

    /**
     * Dibuja el grafo en pantalla según los datos cargados en memoria.
     */
    private void renderizarGrafo() {
        if (grafo == null) return;

        // Nodos
        for (NodoGrafo nodo = grafo.pFirst; nodo != null; nodo = nodo.pnext) {
            if (grafoVisual.getNode(nodo.usuario) == null) {
                grafoVisual.addNode(nodo.usuario).setAttribute("ui.label", nodo.usuario);
            }
        }

        // Aristas dirigidas (solo si el destino existe)
        for (NodoGrafo nodo = grafo.pFirst; nodo != null; nodo = nodo.pnext) {
            for (Arista arista = nodo.minilista.primero; arista != null; arista = arista.siguiente) {
                if (!grafo.existe_nodo(arista.destino)) continue;
                String idArista = nodo.usuario + "->" + arista.destino;
                if (grafoVisual.getEdge(idArista) == null) {
                    grafoVisual.addEdge(idArista, nodo.usuario, arista.destino, true);
                }
            }
        }

        // limpiar clases de estilos previos
        grafoVisual.nodes().forEach((Node nodoVisual) -> nodoVisual.removeAttribute("ui.class"));
        grafoVisual.edges().forEach((Edge aristaVisual) -> aristaVisual.removeAttribute("ui.class"));

        viewPanel.revalidate();
        viewPanel.repaint();
        viewer.disableAutoLayout();
        viewer.enableAutoLayout();
    }

    /**
     * Restaura el grafo al último estado guardado.
     */
    private void reiniciarGrafo() {
        if (grafoBase == null) {
            JOptionPane.showMessageDialog(frame, "No hay un estado base para reiniciar. Carga o guarda primero.");
            return;
        }
        grafo = grafoBase.clonarProfundo();
        cambiosPendientes = false;
        reiniciarViewerVisual();
        renderizarGrafo();
        viewer.getDefaultView().getCamera().resetView();
    }

    /* ===================== Operaciones TDA + Vista ===================== */

    private void agregarUsuario(String usuario) {
        if (!grafo.existe_nodo(usuario)) {
            grafo.insertar(usuario);
            cambiosPendientes = true;
        }
        if (grafoVisual.getNode(usuario) == null)
            grafoVisual.addNode(usuario).setAttribute("ui.label", usuario);
    }

    private void agregarArista(String origen, String destino) {
        if (!grafo.existe_nodo(origen))  { grafo.insertar(origen); cambiosPendientes = true; }
        if (!grafo.existe_nodo(destino)) { grafo.insertar(destino); cambiosPendientes = true; }

        NodoGrafo nodoOrigen = grafo.Buscar(origen);
        if (nodoOrigen != null && !nodoOrigen.minilista.Buscar(destino)) { // evita duplicados en TDA
            nodoOrigen.minilista.insertar_nueva(destino);
            cambiosPendientes = true;
        }

        if (grafoVisual.getNode(origen) == null)
            grafoVisual.addNode(origen).setAttribute("ui.label", origen);
        if (grafoVisual.getNode(destino) == null)
            grafoVisual.addNode(destino).setAttribute("ui.label", destino);

        String idArista = origen + "->" + destino;
        if (grafoVisual.getEdge(idArista) == null)
            grafoVisual.addEdge(idArista, origen, destino, true);
    }

    /* ===================== CFC (Kosaraju) ===================== */

    /**
     * Aplica el algoritmo de Kosaraju para mostrar los componentes fuertemente conexos.
     * Colorea los nodos y muestra un mensaje con los resultados.
     */
    private void visualizarCFC() {
        if (grafo == null) {
            JOptionPane.showMessageDialog(frame, "No hay grafo cargado.");
            return;
        }

        // Si no hay nodos, no hay nada que calcular
        if (grafo.pFirst == null) {
            JOptionPane.showMessageDialog(frame, "El grafo está vacío. No hay CFC para mostrar.");
            return;
        }

        grafo.establecer_falso();
        DFS dfsPrimero = new DFS(grafo);
        for (NodoGrafo nodo = grafo.pFirst; nodo != null; nodo = nodo.pnext)
            if (!nodo.visitado) dfsPrimero.PrimerRecorrido(nodo);

        Kosaraju kosaraju = new Kosaraju(dfsPrimero.pila, grafo);
        kosaraju.fuertemente_conectados();

        if (kosaraju.firstcomp == null) {
            JOptionPane.showMessageDialog(frame, "No se encontraron componentes fuertemente conexos.");
            return;
        }

        DiccionarioStringInt indiceComponentePorUsuario = new DiccionarioStringInt();
        int indiceComponente = 0;
        int cantidadCfcNoTriviales = 0; // tamaño > 1
        StringBuilder mensaje = new StringBuilder("Componentes fuertemente conexos encontrados:\n\n");

        for (Componente componente = kosaraju.firstcomp; componente != null; componente = componente.next, indiceComponente++) {
            // Recorremos miembros del componente y contamos tamaño
            int tam = 0;
            ListaCadena miembros = new ListaCadena();
            for (Arista arista = componente.primero; arista != null; arista = arista.siguiente) {
                miembros.agregar(arista.destino);
                indiceComponentePorUsuario.put(arista.destino, indiceComponente);
                tam++;
            }

            // Imprimir siempre el listado de CFC para transparencia
            mensaje.append("CFC ").append(indiceComponente + 1).append(" (tamaño ").append(tam).append("): ");
            mensaje.append(miembros.join(", ")).append("\n");

            if (tam > 1) cantidadCfcNoTriviales++;
        }

        // Colorear nodos por componente
        for (NodoGrafo nodo = grafo.pFirst; nodo != null; nodo = nodo.pnext) {
            Node nodoVisual = grafoVisual.getNode(nodo.usuario);
            if (nodoVisual == null) continue;
            Integer comp = indiceComponentePorUsuario.get(nodo.usuario);
            if (comp == null) nodoVisual.removeAttribute("ui.class");
            else nodoVisual.setAttribute("ui.class", "c" + (comp.intValue() % 12));
        }

        // Colorear aristas según si permanecen dentro del mismo componente
        for (int i = 0, total = grafoVisual.getEdgeCount(); i < total; i++) {
            Edge aristaVisual = grafoVisual.getEdge(i);
            String origen = aristaVisual.getSourceNode().getId();
            String destino = aristaVisual.getTargetNode().getId();
            Integer compOrigen = indiceComponentePorUsuario.get(origen);
            Integer compDestino = indiceComponentePorUsuario.get(destino);
            if (compOrigen != null && compDestino != null && compOrigen.intValue() == compDestino.intValue())
                aristaVisual.setAttribute("ui.class", "intra");
            else
                aristaVisual.setAttribute("ui.class", "crossEdge");
        }

        viewer.disableAutoLayout();
        viewer.enableAutoLayout();
        viewPanel.revalidate();
        viewPanel.repaint();

        // Si no hubo CFC no triviales, avisar explícitamente
        if (cantidadCfcNoTriviales == 0) {
            JOptionPane.showMessageDialog(
                frame,
                "No se encontraron CFC no triviales (tamaño > 1). Cada nodo es su propio componente.",
                "Sin CFC no triviales",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(frame, mensaje.toString());
        }
    }

    /* ===================== Estilos ===================== */

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
