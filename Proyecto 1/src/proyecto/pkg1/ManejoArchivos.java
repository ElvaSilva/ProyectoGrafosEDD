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
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author elva
 */
public class ManejoArchivos {
    File selectedArchivo;
    
    public ManejoArchivos(){
        this.selectedArchivo = null;
    }
    
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
}

