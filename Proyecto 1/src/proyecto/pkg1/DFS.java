/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

/**
 *
 * @author USER
 */
public class DFS {
    public Pila pila;
    public Grafo grafo;
    
    public DFS(Grafo listagrafo){
        pila = new Pila();
        grafo = listagrafo;
        
    }
    
    // Para que este código funcione al momento de implmentar la lógica, pNodo se debe poner con el primer Nodo del Grafo
    public void PrimerRecorrido(NodoGrafo pNodo){
        Arista pAux = pNodo.minilista.primero;
        pNodo.visitado = true;
        while (pAux != null) {
            NodoGrafo pNodoAux = grafo.Buscar(pAux.destino);
            if (!(pNodoAux.visitado)) {
                PrimerRecorrido(pNodoAux);
            }
            pAux = pAux.siguiente;
        }
        pila.Apilar(pNodo.usuario);
    }
    
    // kosaraju va a ser la pila que tenga como parámetro kosaraju y pNodo debe ser inicializada con el primer nodo que aparezca en la pila
    public void SegundoRecorrido(NodoGrafo pNodo, Componente componente, Pila kosaraju){
        Arista pAux = pNodo.minilista.primero;
        pNodo.visitado = true;
        componente.insertar(pNodo.usuario);
        while (pAux != null) {
            NodoGrafo pNodoAux = grafo.Buscar(pAux.destino);
            if (!(pNodoAux.visitado)) {
                SegundoRecorrido(pNodoAux, componente, kosaraju);
            }
            pAux = pAux.siguiente;
            
        }
    }
}
