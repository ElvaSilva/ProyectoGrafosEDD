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
    public Kosaraju(Pila Ppila, Grafo pgrafo){
        firstcomp = null;
        lastcomp = null;
        pila = Ppila;
        grafo = pgrafo;
        transpuesto = grafo.transponer();
        tamaño = 0;
    }
    
    
    public void fuertemente_conectados(){         
        while (!(pila.esVacio())){
            NodoGrafo pNodo = transpuesto.Buscar(pila.Cima.usuario);
            Componente pComponente = new Componente();
            DFS dfs = new DFS(transpuesto);
            dfs.SegundoRecorrido(pNodo, pComponente, pila);
            if (tamaño == 0){
                firstcomp = pComponente;
                lastcomp = pComponente; 
            } else {
                lastcomp.next = pComponente;
                lastcomp = pComponente;
            }
            Arista pArista = pComponente.primero;
            while (pArista != null){
                pila.eliminar(pArista.destino);
            }
            tamaño ++;
        }
    }
}
