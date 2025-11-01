/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

/**
 *
 * @author USER
 */
public class ListaGrafo {
    public NodoGrafo pFirst;
    public NodoGrafo pLast;
    public int tamaño;
    
    public ListaGrafo(){
        pFirst = null;
        pLast = null;
        tamaño = 0;
    }
    
    public boolean esVacio(){
        return pFirst == null;
    }
    
    public void insertar(String elem){
        NodoGrafo Nodo = new NodoGrafo(elem);
        if (esVacio()){
            pFirst = Nodo;
            pLast = Nodo;
        } else {
            if (tamaño == 1){
                pLast = Nodo;
                pFirst.pnext = pLast;
            } else {
                pLast.pnext = Nodo;
                pLast = Nodo;
            }
        }
        tamaño ++;       
    }
    
    public NodoGrafo Buscar(String elem){
        if (esVacio() != true) {
            NodoGrafo pNodo = pFirst;
            while (pNodo != null && !pNodo.usuario.equals(elem)){
                pNodo = pNodo.pnext;
            }
            return pNodo;
        }
        return null;
    }
    
    public void Eliminar(NodoGrafo pNodo){
        NodoGrafo pAux = pFirst;
        if (pNodo != null) {
            if (tamaño == 1) {
                pFirst = null;
                pLast = null;
            } else {
                if (pNodo == pFirst){
                    pFirst = pNodo.pnext;
                    pNodo.pnext = null;
                } else {
                    while (pAux.pnext != pNodo) {
                        pAux = pAux.pnext;
                    }
                    pAux.pnext = pAux.pnext.pnext;
                    if (pNodo == pLast) {
                        pLast = pAux;
                    }
                }
            }
        tamaño --;    
        }
    }
}
