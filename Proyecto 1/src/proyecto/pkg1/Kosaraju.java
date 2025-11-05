/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

/**
 *
 * @author USER
 */
// La clase Kosaraju permite guardar los componentes fuertemente conectados, funciona como una lista de componentes, a su vez, ella va a generar estos componentes
public class Kosaraju {
    public Componente firstcomp;
    public Componente lastcomp;
    public Grafo grafo;
    public Grafo transpuesto;
    public Pila pila;
    public int tamaño;
    
    
    //Para que esto funcione se debe trasponer el grafo de antemano
    public Kosaraju(Grafo pgrafo){
        firstcomp = null;
        lastcomp = null;
        pila = new Pila();
        grafo = pgrafo;
        transpuesto = grafo.transponer();
        tamaño = 0;
    }
    
    public void insertar_componente(Componente componente){
        if (firstcomp == null) {
            firstcomp = componente;
            lastcomp = componente;
        } else {
            lastcomp.next = componente;
            lastcomp = componente;
        }
    }
    
    public void generar_pila(){
        NodoGrafo Nodo = grafo.pFirst;
        grafo.establecer_falso();
        while(Nodo != null){
            if (Nodo.visitado){
                Nodo = Nodo.pnext;
            } else {
                DFS dfs = new DFS(grafo, pila);
                dfs.PrimerRecorrido(Nodo);
                Nodo = Nodo.pnext;
            }
        }
    }
    
    public void generar_componentes(){
        transpuesto.establecer_falso();
        while (!pila.esVacio()) {
            String nombre = pila.Cima.usuario;
            pila.Desapilar();
            NodoGrafo nodo = transpuesto.Buscar(nombre);
            if (nodo != null && !nodo.visitado){
                Componente componente = new Componente();
                DFS dfs = new DFS(transpuesto, pila);
                dfs.segundoRecorrido(nodo, componente);
                insertar_componente(componente);
            }
        }
    }
    
    
    
    
    
    
    
    
    //public void fuertemente_conectados(){         
    //    while (!(pila.esVacio())){
    //        NodoGrafo pNodo = transpuesto.Buscar(pila.Cima.usuario);
    //        Componente pComponente = new Componente();
    //        DFS dfs = new DFS(transpuesto, pila);
    //        dfs.SegundoRecorrido(pNodo, pComponente, pila);
    //        if (tamaño == 0){
    //            firstcomp = pComponente;
    //            lastcomp = pComponente; 
    //        } else {
    //            lastcomp.next = pComponente;
    //            lastcomp = pComponente;
    //        }
     //       Arista pArista = pComponente.primero;
    //        while (pArista != null){
    //            pila.eliminar(pArista.destino);
    //        }
     //       tamaño ++;
       // }
    //}
}
