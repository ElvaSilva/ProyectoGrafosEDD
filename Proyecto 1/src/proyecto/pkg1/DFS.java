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
    
    /**
     * 
     * @param pNodo debe ser el primer nodo del grafo
     */
    public void PrimerRecorrido(NodoGrafo pNodo){
        Arista pAux = pNodo.minilista.primero;
        pNodo.visitado = true;
        while (pAux != null) {
            NodoGrafo pNodoAux = grafo.Buscar(pAux.destino);
            if (pNodoAux != null && !pNodoAux.visitado) {
                PrimerRecorrido(pNodoAux);
            }
            pAux = pAux.siguiente;
        }
        pila.Apilar(pNodo.usuario);
    }
    
    /**
     * 
     * @param pNodo primer nodo que aparezca en la pila
     * @param componente
     * @param kosaraju pila que se tenga como par&aacute;metro kosaraju
     */
    public void segundoRecorrido(NodoGrafo pNodo, Componente componente){
        pNodo.visitado = true;
        componente.insertar(pNodo.usuario);
        Arista pAux = pNodo.minilista.primero;
        while (pAux != null){
            NodoGrafo pNodoAux = grafo.Buscar(pAux.destino);
            if (!(pNodoAux.visitado)) {
                segundoRecorrido(pNodoAux, componente);
            }
            pAux = pAux.siguiente;
        }
    }
    
}
