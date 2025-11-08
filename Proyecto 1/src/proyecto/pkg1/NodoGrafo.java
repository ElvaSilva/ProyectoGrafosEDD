/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

/**
 * Clase que representa un nodo dentro del grafo dirigido.
 * 
 * Cada nodo contiene:
 * <ul>
 *   <li>Un identificador de usuario (String)</li>
 *   <li>Una lista de adyacencia con las relaciones salientes</li>
 *   <li>Una referencia al siguiente nodo (para recorrer la lista enlazada del grafo)</li>
 *   <li>Un marcador booleano usado en recorridos DFS y Kosaraju</li>
 * </ul>
 * 
 */
public class NodoGrafo {
    public String usuario;
    public ListaAdyacencia minilista;
    public NodoGrafo pnext;
    public boolean visitado;
    
    public NodoGrafo(String info){
        usuario = info;
        minilista = new ListaAdyacencia();
        pnext = null;
        visitado = false;
    }
}
