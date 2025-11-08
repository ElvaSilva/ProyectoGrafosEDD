/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

/**
 * Clase que representa una pila (LIFO) utilizada en los recorridos DFS y en el algoritmo de Kosaraju.
 * Cada elemento de la pila es un {@link NodoGrafo}.
 * Esta clase permite apilar y desapilar nodos del grafo de forma secuencial.
 * 
 */
public class Pila {
    public NodoGrafo Cima;
    
    public Pila(){
        Cima = null;
    }
    
    public boolean esVacio(){
        return Cima == null;
    }
    
    public void Apilar(String info){
        NodoGrafo pNodo = new NodoGrafo(info);
        pNodo.pnext =  Cima;
        Cima = pNodo;
    }
    
    public void Desapilar(){
        if (Cima != null){
            NodoGrafo pTemp = Cima;
            Cima = pTemp.pnext;
        }
    }    
}
