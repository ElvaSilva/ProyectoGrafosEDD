/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author elva
 */
public class ManejoArchivos {
    File selectedArchivo;
    
    public ManejoArchivos(){
        this.selectedArchivo = null;
    }
    
    /** Se va a ofrecer una ventana al usuario para que eliga el archivo de texto
     * que va a montar y basandose en este, se va a inicializar el grafo y se
     * va a retornar este mismo.
     * @return Grafo inicializado
     */
    public Grafo subir_archivo(){
        try{
            JFileChooser file = new JFileChooser();
            file.showOpenDialog(null);
            File archivo = file.getSelectedFile();
            this.selectedArchivo = archivo;
            Grafo cosa = new Grafo();
            if(archivo != null){
                int aux = 0;
                FileReader archivo_2 = new FileReader(archivo);
                BufferedReader lee = new BufferedReader(archivo_2);
                String currentLine;
                while((currentLine = lee.readLine())!= null){
                    if(currentLine.equalsIgnoreCase("usuarios")){
                        aux = 1;
                    }
                    if(currentLine.equalsIgnoreCase("relaciones")){
                        aux = 2;
                    }
                    if((aux == 1) && (!currentLine.equalsIgnoreCase("usuarios"))){
                        cosa.insertar(currentLine);
                        if(cosa.esVacio()){
                            return null;
                        }
                    }
                    if((aux == 2) && (!currentLine.equalsIgnoreCase("relaciones"))){
                        String[] conexion = currentLine.split("[,// ]");
                        NodoGrafo auxN = new NodoGrafo("");
                        auxN = cosa.Buscar(conexion[0]);
                        auxN.minilista.insertar_nueva(conexion[2]);
                    }
                }
            }
            return cosa;
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Ocurrió un error");
            return null;
        }
    }
    
    /** Se va a recibir un grafo que luego se va a recorrer junto a las conexiones
     * de cada nodo para actualizar el archivo de texto.
     * @param aux grafo modificado
     */
    public void actualizar_archivo(Grafo aux){
        try{
            if(this.selectedArchivo != null){
                FileWriter archivo_2 = new FileWriter(this.selectedArchivo);
                BufferedWriter escribe = new BufferedWriter(archivo_2);
                escribe.write("usuarios");
                escribe.newLine();
                escribe.write(aux.mostrar());
                escribe.write("relaciones");
                escribe.newLine();
                escribe.write(aux.mostrarRelaciones());
                escribe.flush();
            }
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Ocurrió un error");
        }
    }
    
    /**
     * Cargar desde archivo sin JFileChooser (para auto-carga en Interfaz).
     */
    public static Grafo cargarDesdeArchivo(File archivo) throws IOException {
        if (archivo == null || !archivo.exists() || !archivo.isFile()) return null;

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {

            Grafo grafo = new Grafo();
            boolean leyendoUsuarios = false;
            boolean leyendoRelaciones = false;

            String linea;
            while ((linea = lector.readLine()) != null) {
                String texto = linea.trim();
                if (texto.isEmpty()) continue;

                String lower = texto.toLowerCase();
                if (lower.equals("usuarios"))   { leyendoUsuarios = true;  leyendoRelaciones = false; continue; }
                if (lower.equals("relaciones")) { leyendoUsuarios = false; leyendoRelaciones = true;  continue; }

                if (leyendoUsuarios) {
                    // Cada línea debe ser un @usuario
                    if (!texto.startsWith("@")) continue; // ignora basura
                    if (!grafo.existe_nodo(texto)) grafo.insertar(texto);
                } else if (leyendoRelaciones) {
                    // Formato: @a, @b
                    String[] partes = texto.split(",");
                    if (partes.length < 2) continue;
                    String origen = partes[0].trim();
                    String destino = partes[1].trim();
                    if (!origen.startsWith("@") || !destino.startsWith("@")) continue;

                    if (!grafo.existe_nodo(origen))  grafo.insertar(origen);
                    if (!grafo.existe_nodo(destino)) grafo.insertar(destino);

                    NodoGrafo nodoOrigen = grafo.Buscar(origen);
                    if (nodoOrigen != null && !nodoOrigen.minilista.Buscar(destino)) {
                        nodoOrigen.minilista.insertar_nueva(destino);
                    }
                } else {
                    // Antes de ver encabezados, si aparece algo tipo @usuario, considéralo usuario
                    if (texto.startsWith("@") && !texto.contains(",")) {
                        leyendoUsuarios = true;
                        if (!grafo.existe_nodo(texto)) grafo.insertar(texto);
                    }
                }
            }
            return grafo;
        }
    }
}

