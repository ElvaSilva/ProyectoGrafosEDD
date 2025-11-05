/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyecto.pkg1;

/**
 *
 * @author USER
 */
public class Proyecto1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Grafo grafo = new Grafo();
        grafo.insertar("A");
        grafo.insertar("B");
        grafo.insertar("C");
        grafo.insertar("D");
        grafo.insertar("E");
        grafo.insertar("F");
        grafo.Buscar("A").minilista.insertar_nueva("B");
        grafo.Buscar("A").minilista.insertar_nueva("C");
        grafo.Buscar("A").minilista.insertar_nueva("E");
        grafo.Buscar("B").minilista.insertar_nueva("F");
        grafo.Buscar("C").minilista.insertar_nueva("D");
        grafo.Buscar("C").minilista.insertar_nueva("A");
        grafo.Buscar("D").minilista.insertar_nueva("B");
        grafo.Buscar("E").minilista.insertar_nueva("F");
        grafo.Buscar("E").minilista.insertar_nueva("C");
        grafo.Buscar("E").minilista.insertar_nueva("B");
        grafo.Buscar("E").minilista.insertar_nueva("D");
        Grafo transpuesto = grafo.transponer();
        System.out.print(transpuesto);
    }
    
}
